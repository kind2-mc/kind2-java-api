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

  /**
   * Returns the type of this value.
   *
   * @return the type of this value
   */
  public Type getKind2Type()
  {
    return kind2Type;
  }

  /**
   * Returns the step this value belongs to.
   *
   * @return the step this value belongs to
   */
  public StepValue getKind2StepValue()
  {
    return kind2StepValue;
  }

  /**
   * Returns the associated kind2 result.
   *
   * @return the associated kind2 result
   */
  public Result getKind2Result()
  {
    return kind2StepValue.getKind2Result();
  }

  /**
   * Constructs the value of the appropriate subclass for the given type.
   *
   * @param kind2StepValue the step this value belongs to
   * @param kind2Type the type of the value
   * @param jsonElement the Kind 2 json element holding the value
   * @return the parsed value
   */
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

  /**
   * Returns the Kind2 json output for this object.
   *
   * @return the Kind2 json output for this object
   */
  public String getJson()
  {
    return json;
  }

  @Override
  abstract public String toString();
}
