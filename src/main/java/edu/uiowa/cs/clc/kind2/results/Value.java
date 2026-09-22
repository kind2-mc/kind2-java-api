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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import edu.uiowa.cs.clc.kind2.Kind2Exception;
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

    if (kind2Type instanceof Datatype)
    {
      // Kind 2 releases up to 3.0.0 wrote a constructor taking no fields as a
      // bare name rather than as a constructor with an empty field list, and
      // gson read that name as a string. Read it as the constructor it names,
      // so that the output of those releases is still understood.
      if (jsonElement.isJsonPrimitive())
      {
        return new DatatypeValue(kind2StepValue, kind2Type, jsonElement,
            jsonElement.getAsString().trim(), new ArrayList<>());
      }

      JsonObject object = jsonElement.getAsJsonObject();
      String constructor = object.get(Labels.constructor).getAsString();
      List<Value> args = new ArrayList<>();
      if (object.has(Labels.args))
      {
        for (JsonElement arg : object.get(Labels.args).getAsJsonArray())
        {
          args.add(getKind2Value(kind2StepValue, getFieldType(arg), arg));
        }
      }
      return new DatatypeValue(kind2StepValue, kind2Type, jsonElement, constructor, args);
    }

    if (kind2Type instanceof Enum)
    {
      return new EnumValue(kind2StepValue, kind2Type, jsonElement);
    }

    throw new UnsupportedOperationException(kind2Type.toString());
  }

  /**
   * Returns the type to read a datatype constructor's field as. Kind 2 does
   * not name the types of a constructor's fields in its output, so the type
   * is taken from the shape of the json: an object naming a constructor is
   * another datatype value, an object with a numerator and a denominator is a
   * real, and a primitive is whichever of a Boolean, a number or a name it
   * holds. A name covers a constructor of an enumeration and a value of an
   * uninterpreted sort alike, neither of which carries anything else.
   *
   * @param jsonElement the Kind 2 json element holding the field
   * @return the type to read the field as
   */
  private static Type getFieldType(JsonElement jsonElement)
  {
    if (jsonElement.isJsonArray())
    {
      JsonArray array = jsonElement.getAsJsonArray();
      // An empty array has no element to take a type from. Nothing reads the
      // element type of one, since it has no elements to read.
      return new Array(array.size() == 0 ? new Bool() : getFieldType(array.get(0)));
    }

    if (jsonElement.isJsonObject())
    {
      JsonObject object = jsonElement.getAsJsonObject();
      if (object.has(Labels.constructor))
      {
        return new Datatype("datatype", new ArrayList<>());
      }
      if (object.has(Labels.numerator) && object.has(Labels.denominator))
      {
        return new Real();
      }
      throw new Kind2Exception("Unrecognized datatype field: " + jsonElement);
    }

    if (jsonElement.isJsonPrimitive())
    {
      JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
      if (primitive.isBoolean())
      {
        return new Bool();
      }
      if (primitive.isNumber())
      {
        return primitive.getAsString().matches("-?\\d+") ? new Int() : new Real();
      }
      return new Enum("enum");
    }

    throw new Kind2Exception("Unrecognized datatype field: " + jsonElement);
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
