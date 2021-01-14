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

/**
 * An abstract class for all kind2 values.
 */
abstract public class Value
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  private final StepValue kind2StepValue;
  private final Type kind2Type;

  Value(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement)
  {
    this.kind2StepValue = kind2StepValue;
    this.kind2Type = kind2Type;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
  }

  public Type getKind2Type()
  {
    return kind2Type;
  }

  public StepValue getKind2StepValue()
  {
    return kind2StepValue;
  }

  public Result getKind2Result()
  {
    return kind2StepValue.getKind2Result();
  }

  public static Value getKind2Value(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement)
  {
    if (kind2Type instanceof Int)
    {
      return new IntValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof Bool)
    {
      return new BoolValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof Real)
    {
      return new RealValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof SubRange)
    {
      return new SubRangeValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof Array)
    {
      List<Value> values = new ArrayList<>();
      Type elementType = ((Array) kind2Type).getElementType();
      JsonArray arrayValue = jsonElement.getAsJsonArray();
      for (JsonElement element : arrayValue)
      {
        Value kind2Value = Value.getKind2Value(kind2StepValue, elementType, element);
        values.add(kind2Value);
      }
      return new ArrayValue(kind2StepValue, kind2Type, jsonElement, values);
    }

    if (kind2Type instanceof Enum)
    {
      return new EnumValue(kind2StepValue, kind2Type, jsonElement);
    }

    throw new UnsupportedOperationException(kind2Type.toString());
  }

  public String getJson()
  {
    return json;
  }

  @Override
  abstract public String toString();
}
