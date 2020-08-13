/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.api.results.Result;
import edu.uiowa.kind2.api.LogLevel;
import edu.uiowa.kind2.api.results.PropertyResult;
import edu.uiowa.kind2.lustre.NamedType;
import edu.uiowa.kind2.lustre.Type;
import edu.uiowa.kind2.lustre.VarDecl;
import edu.uiowa.kind2.lustre.values.Value;
import edu.uiowa.kind2.results.Counterexample;
import edu.uiowa.kind2.results.FunctionTable;
import edu.uiowa.kind2.results.InconsistentProperty;
import edu.uiowa.kind2.results.InvalidProperty;
import edu.uiowa.kind2.results.Property;
import edu.uiowa.kind2.results.Signal;
import edu.uiowa.kind2.results.UnknownProperty;
import edu.uiowa.kind2.results.ValidProperty;
import edu.uiowa.kind2.util.Util;

public class XmlParseThread extends Thread {
  private final InputStream xmlStream;
  private final HashMap<LogLevel, List<String>> logs;
  private final Result result;
  private final DocumentBuilderFactory factory;
  private volatile Throwable throwable;
  private Map<String, List<PropertyResult>> analysisToProps = new HashMap<>();

  public XmlParseThread(InputStream xmlStream, Result result) {
    super("Xml Parse");
    this.xmlStream = xmlStream;
    this.logs = new HashMap<>();
    this.result = result;
    this.factory = DocumentBuilderFactory.newInstance();
  }

  @Override
  public void run() {
    /*
     * XML parsers buffer their input which conflicts with the way we are streaming data from the
     * XML file as it is written. This results in data in the XML not being acted upon until more
     * content is written to the XML file which causes the buffer to fill. Instead, we read the XML
     * file ourselves and give relevant pieces of it to the parser as they are ready.
     *
     * The downside is we assume the <Property ...> and </Property> tags are on their own lines.
     */

    try (LineInputStream lines = new LineInputStream(xmlStream)) {
      StringBuilder buffer = null;
      String line;
      String analysis = null;
      while ((line = lines.readLine()) != null) {
        boolean beginProperty = line.contains("<Property ");
        boolean endProperty = line.contains("</Property>");
        boolean beginProgress = line.contains("<Progress ");
        boolean endProgress = line.contains("</Progress>");
        boolean beginAnalysis = line.contains("<AnalysisStart");
        boolean endAnalysis = line.contains("<AnalysisStop");
        boolean beginLog = line.contains("<Log");
        boolean endLog = line.contains("</Log>");

        if (beginProgress && endProgress) {
          // Kind 2 progress format uses a single line
          parseProgressXml(line, analysis);
        } else if (beginProperty) {
          buffer = new StringBuilder();
          buffer.append(line);
          if (endProperty) {
            parsePropertyXML(buffer.toString(), analysis);
            buffer = null;
          }
        } else if (endProperty) {
          buffer.append(line);
          parsePropertyXML(buffer.toString(), analysis);
          buffer = null;
        } else if (beginAnalysis) {
          analysis = parseAnalysisXml(line);
        } else if (endAnalysis) {
          analysis = null;
        } else if (beginLog && endLog) {
          parseLogXml(line);
        } else if (buffer != null) {
          buffer.append(line);
        }
      }
    } catch (Throwable t) {
      throwable = t;
    }
  }

  private String parseAnalysisXml(String line) {
    Element progressElement = parseXml(line);
    String analysis = progressElement.getAttribute("top");
    analysisToProps.putIfAbsent(analysis, new ArrayList<>());
    return analysis;
  }

  private void parseLogXml(String line) {
    Element logElement = parseXml(line);
    String logClass = logElement.getAttribute("class").toUpperCase();
    String logText = logElement.getTextContent();
    logs.putIfAbsent(LogLevel.valueOf(logClass), new ArrayList<>());
    logs.get(LogLevel.valueOf(logClass)).add(logText);
  }

  private Element parseXml(String xml) {
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(xml)));
      return doc.getDocumentElement();
    } catch (Exception e) {
      throw new Kind2Exception("Error parsing: " + xml, e);
    }
  }

  private void parseProgressXml(String progressXml, String analysis) {
    Element progressElement = parseXml(progressXml);
    String source = progressElement.getAttribute("source");
    if ("bmc".equals(source)) {
      int k = Integer.parseInt(progressElement.getTextContent());
      for (PropertyResult pr : analysisToProps.get(analysis)) {
        pr.setBaseProgress(k);
      }
    }
  }

  public void parsePropertyXML(String propertyXml, String analysis) {
    Property prop = getProperty(parseXml(propertyXml));
    String propName = prop.getName();
    PropertyResult pr = getOrAddProperty(analysis, propName);
    if (pr != null) {
      pr.setProperty(prop);
      if (analysis != null) {
        analysisToProps.get(analysis).add(pr);
      }
    }
  }

  private PropertyResult getOrAddProperty(String analysis, String propName) {
    PropertyResult pr = result.getPropertyResult(propName);
    if (pr == null && analysis != null) {
      propName = analysis + propName;
      pr = result.getPropertyResult(propName);
    }
    if (pr == null) {
      pr = result.addProperty(propName);
    }
    return pr;
  }

  private Property getProperty(Element propertyElement) {
    String name = propertyElement.getAttribute("name");
    double runtime = getRuntime(getElement(propertyElement, "Runtime"));
    int trueFor = getTrueFor(getElement(propertyElement, "TrueFor"));
    int k = getK(getElement(propertyElement, "K"));
    String answer = getAnswer(getElement(propertyElement, "Answer"));
    String source = getSource(getElement(propertyElement, "Answer"));
    int numOfIVCs = getNumOfIVCs(getElement(propertyElement, "NumberOfIVCs"));
    boolean mivcTimedOut = getTimedOutInfo(getElement(propertyElement, "TimedoutLoop"));
    List<String> invariants = getStringList(getElements(propertyElement, "Invariant"));
    List<String> ivc = getStringList(getElements(propertyElement, "Ivc"));
    Set<List<String>> invarantSets = new HashSet<List<String>>();
    Set<List<String>> ivcSets = new HashSet<List<String>>();
    List<String> conflicts = getConflicts(getElement(propertyElement, "Conflicts"));
    Counterexample cex = getCounterexample(getElement(propertyElement, getCounterexampleTag()), k);

    if (numOfIVCs == 0) {
      List<String> curInvariants = getStringList(getElements(propertyElement, "Invariant"));
      List<String> curIvc = getStringList(getElements(propertyElement, "Ivc"));
      invarantSets.add(curInvariants);
      ivcSets.add(curIvc);
    } else {
      for (int i = 0; i < numOfIVCs; i++) {
        Element ivcSetElem = (Element) propertyElement.getElementsByTagName("IvcSet").item(i);
        List<String> curInvariants = getStringList(getElements(ivcSetElem, "Invariant"));
        List<String> curIvc = getStringList(getElements(ivcSetElem, "Ivc"));
        invarantSets.add(curInvariants);
        ivcSets.add(curIvc);
      }
    }

    switch (answer) {
      case "valid":
        return new ValidProperty(name, source, k, runtime, invariants, ivc, invarantSets, ivcSets,
            mivcTimedOut);

      case "falsifiable":
        return new InvalidProperty(name, source, cex, conflicts, runtime);

      case "unknown":
        return new UnknownProperty(name, trueFor, cex, runtime);

      case "inconsistent":
        return new InconsistentProperty(name, source, k, runtime);

      default:
        throw new Kind2Exception("Unknown property answer in XML file: " + answer);
    }
  }

  private double getRuntime(Node runtimeNode) {
    if (runtimeNode == null) {
      return 0;
    }
    return Double.parseDouble(runtimeNode.getTextContent());
  }

  private int getTrueFor(Node trueForNode) {
    if (trueForNode == null) {
      return 0;
    }
    return Integer.parseInt(trueForNode.getTextContent());
  }

  private int getK(Node kNode) {
    if (kNode == null) {
      return 0;
    }
    int k = Integer.parseInt(kNode.getTextContent());

    return k + 1;
  }

  private int getNumOfIVCs(Node numOfIVCNode) {
    if (numOfIVCNode == null) {
      return 0;
    }
    int num = Integer.parseInt(numOfIVCNode.getTextContent());
    return num;
  }

  private boolean getTimedOutInfo(Node timedOutLoopNode) {
    if (timedOutLoopNode == null) {
      return false;
    }
    String timedOutInfo = timedOutLoopNode.getTextContent();
    return timedOutInfo.equals("yes");
  }

  private String getAnswer(Node answerNode) {
    return answerNode.getTextContent();
  }

  private String getSource(Element answerNode) {
    return answerNode.getAttribute("source");
  }

  private List<String> getStringList(List<Element> elements) {
    List<String> result = new ArrayList<>();
    for (Element e : elements) {
      result.add(e.getTextContent());
    }
    return result;
  }

  private List<String> getConflicts(Element conflictsElement) {
    if (conflictsElement == null) {
      return Collections.emptyList();
    }

    List<String> conflicts = new ArrayList<>();
    for (Element conflictElement : getElements(conflictsElement, "Conflict")) {
      conflicts.add(conflictElement.getTextContent());
    }
    return conflicts;
  }

  private Counterexample getCounterexample(Element cexElement, Integer k) {
    if (cexElement == null) {
      return null;
    }

    Counterexample cex = new Counterexample(k);
    for (Element signalElement : getElements(cexElement, getSignalTag())) {
      cex.addSignal(getSignal(signalElement));
    }
    for (Element functionElement : getElements(cexElement, "Function")) {
      cex.addFunctionTable(getFunction(functionElement));
    }
    return cex;
  }

  protected String getCounterexampleTag() {
    return "CounterExample";
  }

  protected String getSignalTag() {
    return "Stream";
  }

  private Signal<Value> getSignal(Element signalElement) {
    String name = signalElement.getAttribute("name");
    String type = signalElement.getAttribute("type");
    if (type.contains("subrange ")) {
      type = "int";
    }

    Signal<Value> signal = new Signal<>(name);
    for (Element valueElement : getElements(signalElement, "Value")) {
      int time = Integer.parseInt(valueElement.getAttribute(getTimeAttribute()));
      signal.putValue(time, getValue(valueElement, type));
    }
    return signal;
  }

  protected String getTimeAttribute() {
    return "instant";
  }

  private Value getValue(Element valueElement, String type) {
    if (type.startsWith("array of")) {
      type = type.replaceAll("array of ", "");
      return Util.parseArrayValue(type, getElement(valueElement, "Array"));
    }

    return Util.parseValue(type, valueElement.getTextContent());
  }

  private FunctionTable getFunction(Element functionElement) {
    String name = functionElement.getAttribute("name");
    List<VarDecl> inputs = new ArrayList<>();
    for (Element inputElement : getElements(functionElement, "Input")) {
      inputs.add(getVarDecl(inputElement));
    }
    VarDecl output = getVarDecl(getElement(functionElement, "Output"));
    FunctionTable table = new FunctionTable(name, inputs, output);

    for (Element fvElement : getElements(functionElement, "FunctionValue")) {
      List<Value> inputValues = new ArrayList<>();
      List<Element> ivElements = getElements(fvElement, "InputValue");
      for (int i = 0; i < inputs.size(); i++) {
        inputValues.add(Util.parseValue(inputs.get(i).type, ivElements.get(i).getTextContent()));
      }
      Value outputValue =
          Util.parseValue(output.type, getElement(fvElement, "OutputValue").getTextContent());
      table.addRow(inputValues, outputValue);
    }

    return table;
  }

  private VarDecl getVarDecl(Element element) {
    String name = element.getAttribute("name");
    Type type = NamedType.get(element.getAttribute("type"));
    return new VarDecl(name, type);
  }

  private Element getElement(Element element, String name) {
    return (Element) element.getElementsByTagName(name).item(0);
  }

  private List<Element> getElements(Element element, String name) {
    List<Element> elements = new ArrayList<>();
    NodeList nodeList = element.getElementsByTagName(name);
    for (int i = 0; i < nodeList.getLength(); i++) {
      elements.add((Element) nodeList.item(i));
    }
    return elements;
  }

  public List<String> getLogs(LogLevel logLevel) {
    return logs.get(logLevel);
  }

  public Throwable getThrowable() {
    return throwable;
  }
}
