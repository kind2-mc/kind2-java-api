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
   * PropAnnot if it comes from the check of a property annotation,
   * NonVacuityCheck if it comes from a non-vacuity check.
   */
  private final PropertyType source;
   /**
   * True iff the property is a candidate property
   */
  private final Boolean isCandidate;
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


  /**
   * Constructs a property from one Kind 2 property object.
   *
   * @param analysis the analysis this property was checked in
   * @param jsonElement the Kind 2 json object describing the property
   */
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
    if (jsonObject.has(Labels.isCandidate)) {
      isCandidate = jsonObject.get(Labels.isCandidate).getAsBoolean();
    } else {
      isCandidate = null;
    }
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

  /**
   * Returns the raw Kind 2 json element for this object.
   *
   * @return the raw Kind 2 json element for this object
   */
  public JsonElement getJsonElement()
  {
    return jsonElement;
  }

  /**
   * Returns the Kind2 json output for this object.
   *
   * @return the Kind2 json output for this object
   */
  public String getJson()
  {
    return json;
  }

  /**
   * Returns the Kind 2 unique identifier for this property.
   *
   * @return the Kind 2 unique identifier for this property
   */
  public String getJsonName()
  {
    return jsonName;
  }

  /**
   * Returns the identifier for this property, without line or column numbers.
   *
   * @return the identifier for this property, without line or column numbers
   */
  public String getName()
  {
    return Result.getOpeningSymbols() + name + Result.getClosingSymbols();
  }

  /**
   * Returns the associated kind2 result.
   *
   * @return the associated kind2 result
   */
  public Result getKind2Result()
  {
    return analysis.getKind2Result();
  }

  /**
   * Returns the name of the component where the property was analyzed.
   *
   * @return the name of the component where the property was analyzed
   */
  public String getScope()
  {
    return scope;
  }

  /**
   * Returns the input file this property comes from, if any.
   *
   * @return the input file this property comes from, if any
   */
  public String getFile() {
    return file;
  }

  /**
   * Returns the associated line in the input file, if any.
   *
   * @return the associated line in the input file, if any
   */
  public String getLine()
  {
    return line;
  }

  /**
   * Returns the associated column in the input file, if any.
   *
   * @return the associated column in the input file, if any
   */
  public String getColumn()
  {
    return column;
  }

  /**
   * Returns the result value of the check.
   *
   * @return the result value of the check
   */
  public Answer getAnswer()
  {
    return answer;
  }

  /**
   * Returns the origin of the property.
   *
   * @return the origin of the property
   */
  public PropertyType getSource()
  {
    return source;
  }

  /**
   * Returns {@code true} if the property is a candidate property.
   *
   * @return {@code true} if the property is a candidate property
   */
  public Boolean getIsCandidate()
  {
    return isCandidate;
  }

  /**
   * Returns the counterexample to the property satisfaction, only available when the answer is falsifiable.
   *
   * @return the counterexample to the property satisfaction, only available when the answer is falsifiable
   */
  public CounterExample getCounterExample()
  {
    return counterExample;
  }

  /**
   * Returns the example trace for the property, only available when the answer is reachable.
   *
   * @return the example trace for the property, only available when the answer is reachable
   */
  public CounterExample getExampleTrace()
  {
    return exampleTrace;
  }

  /**
   * Returns the largest value of k for which the property was proved true, if any.
   *
   * @return the largest value of k for which the property was proved true, if any
   */
  public String getTrueFor()
  {
    return trueFor;
  }

  /**
   * Returns the value of k in a k-inductive proof, if any.
   *
   * @return the value of k in a k-inductive proof, if any
   */
  public Integer getKInductionStep()
  {
    return kInductionStep;
  }

  /**
   * Returns the qualified identifier for this property, without line or column numbers.
   *
   * @return the qualified identifier for this property, without line or column numbers
   */
  public String getQualifiedName()
  {
    return qualifiedName;
  }

  /**
   * Returns the analysis this property was checked in.
   *
   * @return the analysis this property was checked in
   */
  public Analysis getAnalysis()
  {
    return analysis;
  }
}
