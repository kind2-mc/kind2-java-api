/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.kind2results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This class stores information about kind2 properties.
 */
public class Kind2Property
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
   * Unlike {@link Kind2Property#jsonName} this name may not be unique.
   */
  private final String name;
  /**
   * A qualified identifier for this property without line or column numbers ({nodeName.propertyName}).
   * Unlike {@link Kind2Property#jsonName} this name may not be unique.
   */
  private final String qualifiedName;
  /**
   * Name of the component where the property was analyzed
   */
  private final String scope;
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
  private final Kind2PropertyType source;
  /**
   * The source of the answer, and the result value of the check.
   * The result can be valid, falsifiable, or unknown
   */
  private final Kind2Answer answer;
  /**
   * Counterexample to the property satisfaction (only available when answer is falsifiable).
   * It describes a sequence of values for each stream, and automaton,
   * that leads the system to the violation of the property.
   * It also gives the list of contract modes that are active at each step, if any.
   */
  private final Kind2CounterExample counterExample;
  private final Kind2Analysis analysis;
  /**
   * The value of k in a k-inductive proof, if any.
   */
  private final Integer kInductionStep;


  public Kind2Property(Kind2Analysis analysis, JsonElement jsonElement)
  {
    this.analysis = analysis;
    this.jsonElement = jsonElement;
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    jsonName = jsonObject.get(Kind2Labels.name).getAsString();
    name = jsonName.replaceAll("\\[.*?\\]", "").replaceFirst(".*?\\.", "");
    qualifiedName = analysis.getNodeName() + "." + getName();
    scope = jsonObject.get(Kind2Labels.scope) == null ? "" :
        jsonObject.get(Kind2Labels.scope).getAsString();
    line = jsonObject.get(Kind2Labels.line).getAsString();
    column = jsonObject.get(Kind2Labels.column).getAsString();
    source = Kind2PropertyType.getPropertyType(jsonObject.get(Kind2Labels.source).getAsString());
    JsonElement answerJsonObject = jsonObject.get(Kind2Labels.answer);
    answer = Kind2Answer.getAnswer(answerJsonObject.getAsJsonObject().get(Kind2Labels.value).getAsString());
    JsonElement counterExampleElement = jsonObject.get(Kind2Labels.counterExample);
    counterExample = counterExampleElement == null ? null :
        new Kind2CounterExample(this, counterExampleElement);
    trueFor = jsonObject.get(Kind2Labels.trueFor) == null ? null :
        jsonObject.get(Kind2Labels.trueFor).getAsString();
    JsonElement k = jsonObject.get(Kind2Labels.k);
    kInductionStep = k == null ? null : k.getAsInt();
  }

  @Override
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("The answer for property '" + getQualifiedName() + "' ");

    if (Kind2Result.isPrintingLineNumbersEnabled())
    {
      stringBuilder.append("in line " + getLine() + " ");
      stringBuilder.append("column " + getColumn() + " ");
    }
    stringBuilder.append("is " + answer + ".");
    if (answer == Kind2Answer.unknown)
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
    return Kind2Result.getOpeningSymbols() + name + Kind2Result.getClosingSymbols();
  }

  public Kind2Result getKind2Result()
  {
    return analysis.getKind2Result();
  }

  public String getScope()
  {
    return scope;
  }

  public String getLine()
  {
    return line;
  }

  public String getColumn()
  {
    return column;
  }

  public Kind2Answer getAnswer()
  {
    return answer;
  }

  public Kind2PropertyType getSource()
  {
    return source;
  }

  public Kind2CounterExample getCounterExample()
  {
    return counterExample;
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

  public Kind2Analysis getAnalysis()
  {
    return analysis;
  }
}
