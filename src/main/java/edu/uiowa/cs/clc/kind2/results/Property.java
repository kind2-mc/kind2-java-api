/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This class stores information about kind2 properties.
 */
public class Property
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  private final JsonElement jsonElement;
  /**
   * Kind2 unique identifier for this property
   */
  private final String jsonName;
  /**
   * Identifier for this property without line or column numbers.
   * Unlike {@link Property#jsonName} this name may not be unique.
   */
  private final String name;
  /**
   * A qualified identifier for this property without line or column numbers ({nodeName.propertyName}).
   * Unlike {@link Property#jsonName} this name may not be unique.
   */
  private final String qualifiedName;
  /**
   * Name of the component where the property was analyzed
   */
  private final String scope;
  private final String file;
  /**
   * Associated line in the input file, if any.
   */
  private final String line;
  /**
   * Associated column in the input file, if any.
   */
  private final String column;
  /**
   * The largest value of k for which the property was proved to be true, if any.
   */
  private final String trueFor;
  /**
   * Origin of the property. Can be Assumption if it comes from an assumption check,
   * Guarantee if it comes from the check of a guarantee,
   * Ensure if it comes from a check of a require-ensure clause in a contract mode,
   * OneModeActive if it comes from an exhaustiveness check of the state space covered by the modes of a contract,
   * and PropAnnot if it comes from the check of a property annotation
   */
  private final PropertyType source;
  /**
   * The source of the answer, and the result value of the check.
   * The result can be valid, falsifiable, unknown, reachable, or unreachable
   */
  private final Answer answer;
  /**
   * Counterexample to the property satisfaction (only available when answer is falsifiable).
   * It describes a sequence of values for each stream, and automaton,
   * that leads the system to the violation of the property.
   * It also gives the list of contract modes that are active at each step, if any.
   */
  private final CounterExample counterExample;
  /**
   * Example trace to the property satisfaction (only available when answer is reachable).
   * It describes a sequence of values for each stream, and automaton,
   * that leads the system to the violation of the property.
   * It also gives the list of contract modes that are active at each step, if any.
   */
  private final CounterExample exampleTrace;
  private final Analysis analysis;
  /**
   * The value of k in a k-inductive proof, if any.
   */
  private final Integer kInductionStep;


  public Property(Analysis analysis, JsonElement jsonElement)
  {
    this.analysis = analysis;
    this.jsonElement = jsonElement;
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    jsonName = jsonObject.get(Labels.name).getAsString();
    name = jsonName.replaceAll("\\[.*?\\]", "").replaceFirst(".*?\\.", "");
    file =
        jsonObject.get(Labels.file) == null ? null : jsonObject.get(Labels.file).getAsString();
    qualifiedName = analysis.getNodeName() + "." + getName();
    scope = jsonObject.get(Labels.scope) == null ? "" :
        jsonObject.get(Labels.scope).getAsString();
    line = jsonObject.get(Labels.line).getAsString();
    column = jsonObject.get(Labels.column).getAsString();
    source = PropertyType.getPropertyType(jsonObject.get(Labels.source).getAsString());
    JsonElement answerJsonObject = jsonObject.get(Labels.answer);
    answer = Answer.getAnswer(answerJsonObject.getAsJsonObject().get(Labels.value).getAsString());
    JsonElement counterExampleElement = jsonObject.get(Labels.counterExample);
    counterExample = counterExampleElement == null ? null :
        new CounterExample(this, counterExampleElement);
    JsonElement exampleTraceElement = jsonObject.get(Labels.exampleTrace);
    exampleTrace = exampleTraceElement == null ? null :
        new CounterExample(this, exampleTraceElement);
    trueFor = jsonObject.get(Labels.trueFor) == null ? null :
        jsonObject.get(Labels.trueFor).getAsString();
    JsonElement k = jsonObject.get(Labels.k);
    kInductionStep = k == null ? null : k.getAsInt();
  }

  @Override
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("The answer for property '" + getQualifiedName() + "' ");

    if (Result.isPrintingLineNumbersEnabled())
    {
      stringBuilder.append("in line " + getLine() + " ");
      stringBuilder.append("column " + getColumn() + " ");
    }
    stringBuilder.append("is " + answer + ".");
    if (answer == Answer.unknown)
    {
      if (trueFor != null)
      {
        stringBuilder.append(String.format(" This property is satisfied for %1s steps.", trueFor));
      }
      if (kInductionStep != null)
      {
        stringBuilder.append(String.format(" K induction step is  %1s.", kInductionStep));
      }
    }
    return stringBuilder.toString();
  }

  public JsonElement getJsonElement()
  {
    return jsonElement;
  }

  public String getJson()
  {
    return json;
  }

  public String getJsonName()
  {
    return jsonName;
  }

  public String getName()
  {
    return Result.getOpeningSymbols() + name + Result.getClosingSymbols();
  }

  public Result getKind2Result()
  {
    return analysis.getKind2Result();
  }

  public String getScope()
  {
    return scope;
  }

  public String getFile() {
    return file;
  }

  public String getLine()
  {
    return line;
  }

  public String getColumn()
  {
    return column;
  }

  public Answer getAnswer()
  {
    return answer;
  }

  public PropertyType getSource()
  {
    return source;
  }

  public CounterExample getCounterExample()
  {
    return counterExample;
  }

  public CounterExample getExampleTrace()
  {
    return exampleTrace;
  }

  public String getTrueFor()
  {
    return trueFor;
  }

  public Integer getKInductionStep()
  {
    return kInductionStep;
  }

  public String getQualifiedName()
  {
    return qualifiedName;
  }

  public Analysis getAnalysis()
  {
    return analysis;
  }
}
