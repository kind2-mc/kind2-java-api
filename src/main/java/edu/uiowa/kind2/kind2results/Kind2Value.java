/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.kind2results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for all kind2 values.
 */
abstract public class Kind2Value
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  private final Kind2StepValue kind2StepValue;
  private final Kind2Type kind2Type;

  Kind2Value(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
  {
    this.kind2StepValue = kind2StepValue;
    this.kind2Type = kind2Type;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
  }

  public Kind2Type getKind2Type()
  {
    return kind2Type;
  }

  public Kind2StepValue getKind2StepValue()
  {
    return kind2StepValue;
  }

  public Kind2Result getKind2Result()
  {
    return kind2StepValue.getKind2Result();
  }

  public static Kind2Value getKind2Value(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
  {
    if (kind2Type instanceof Kind2Int)
    {
      return new Kind2IntValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof Kind2Bool)
    {
      return new Kind2BoolValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof Kind2Real)
    {
      return new Kind2RealValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof Kind2SubRange)
    {
      return new Kind2SubRangeValue(kind2StepValue, kind2Type, jsonElement);
    }

    if (kind2Type instanceof Kind2Array)
    {
      List<Kind2Value> values = new ArrayList<>();
      Kind2Type elementType = ((Kind2Array) kind2Type).getElementType();
      JsonArray arrayValue = jsonElement.getAsJsonArray();
      for (JsonElement element : arrayValue)
      {
        Kind2Value kind2Value = Kind2Value.getKind2Value(kind2StepValue, elementType, element);
        values.add(kind2Value);
      }
      return new Kind2ArrayValue(kind2StepValue, kind2Type, jsonElement, values);
    }

    if (kind2Type instanceof Kind2Enum)
    {
      return new Kind2EnumValue(kind2StepValue, kind2Type, jsonElement);
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
