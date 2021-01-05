/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class Kind2Stream
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;
  /**
   * the name of the variable for this stream.
   */
  private final String name;
  /**
   * The type of the variable:  bool, int, uint8, uint16, uint32, uint64, int8, int16, int32, int64. real,
   * subrange, enum, or array.
   */
  private final Kind2Type kind2Type;
  /**
   * the variable class: input, output, or local.
   */
  private final String streamClass;
  /**
   * The sequence of values for this stream.
   */
  private final List<Kind2StepValue> stepValues;
  /**
   * The associated component for this stream.
   */
  private final Kind2SubNode kind2SubNode;

  public Kind2Stream(Kind2SubNode kind2SubNode, JsonElement jsonElement)
  {
    this.kind2SubNode = kind2SubNode;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    name = jsonElement.getAsJsonObject().get(Kind2Labels.name).getAsString();
    String typeString = jsonElement.getAsJsonObject().get(Kind2Labels.type).getAsString();
    kind2Type = Kind2Type.getType(typeString);
    streamClass = jsonElement.getAsJsonObject().get(Kind2Labels.classField).getAsString();

    this.stepValues = new ArrayList<>();

    JsonArray streamValues = jsonElement.getAsJsonObject().get(Kind2Labels.instantValues).getAsJsonArray();

    for (JsonElement element : streamValues)
    {
      Kind2StepValue stepValue = new Kind2StepValue(this, element);
      stepValues.add(stepValue);
    }
  }

  /**
    * @return the associated kind2 result for this stream.
   */
  public Kind2Result getKind2Result()
  {
    return kind2SubNode.getKind2Result();
  }

  /**
   * @return the name of the variable for this stream.
   */
  public String getName()
  {
    return Kind2Result.getOpeningSymbols() + name + Kind2Result.getClosingSymbols();
  }

  /**
   * @return The type of the variable:  bool, int, uint8, uint16, uint32, uint64, int8, int16, int32, int64. real,
   * subrange, enum, or array.
   */
  public Kind2Type getKind2Type()
  {
    return kind2Type;
  }

  /**
   * @return the variable class: input, output, or local.
   */
  public String getStreamClass()
  {
    return streamClass;
  }

  /**
   * @return The sequence of values for this stream.
   */
  public List<Kind2StepValue> getStepValues()
  {
    return stepValues;
  }

  /**
   * @return Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  @Override
  public String toString()
  {
    return getName();
  }
}
