/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;
import edu.uiowa.cs.clc.kind2.Kind2Exception;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for all kind2 types.
 */
abstract public class Type
{
  /**
   * The name of this type.
   */
  public final String name;

  /**
   * Constructs a type with the given name.
   *
   * @param name the name of the type
   */
  public Type(String name)
  {
    this.name = name;
  }

  /**
   * Returns the type denoted by the given Kind 2 type name.
   *
   * @param type the Kind 2 type name
   * @return the corresponding type
   */
  public static Type getType(String type)
  {
    return getType(type, null);
  }
  /**
   * Builds the datatype described by the given type information. The element
   * type of an array is named without any information of its own, so a
   * datatype reached that way is left with its constructors unknown rather
   * than rejected.
   */
  private static Type makeDatatype(JsonElement typeInfo)
  {
    if (typeInfo == null)
    {
      return new Datatype("datatype", new ArrayList<>());
    }
    JsonObject info = typeInfo.getAsJsonObject();
    String name = info.has(Labels.name) ? info.get(Labels.name).getAsString() : "datatype";
    List<String> constructors = new ArrayList<>();
    if (info.has(Labels.constructors))
    {
      for (JsonElement constructor : info.get(Labels.constructors).getAsJsonArray())
      {
        constructors.add(constructor.getAsString());
      }
    }
    return new Datatype(name, constructors);
  }

  private static Type makeNestedArray(String baseType, int numDims){
    if (numDims == 0){
      return getType(baseType);
    } else {
      return new Array(makeNestedArray(baseType, numDims-1));
    }
  }
  

  /**
   * Returns the type denoted by the given Kind 2 type name and structured type information.
   *
   * @param typeString the Kind 2 type name
   * @param typeInfo the structured type information, used for array and subrange types
   * @return the corresponding type
   */
  public static Type getType(String typeString, JsonElement typeInfo)
  {
    switch (typeString)
    {
      case "bool":
        return new Bool();
      case "int":
      case "uint8":
      case "uint16":
      case "uint32":
      case "uint64":
      case "int8":
      case "int16":
      case "int32":
      case "int64":
      case "subrange":
        return new Int();
      case "real":
        return new Real();
      case "array":
        if (typeInfo == null) throw new Kind2Exception("Array with no type info found");
        String baseType =  typeInfo.getAsJsonObject().get(Labels.baseType).getAsString();
        int numIndicies = typeInfo.getAsJsonObject().get("sizes").getAsJsonArray().size();
        return makeNestedArray(baseType, numIndicies);
      case "datatype":
        return makeDatatype(typeInfo);
      default:
      {
        if (typeString.matches("subrange \\[.*?\\] of int"))
        {
          String [] range = typeString.replaceAll("subrange \\[", "")
                                .replaceAll("\\] of int", "").split(",");
          int min = Integer.parseInt(range[0]);
          int max = Integer.parseInt(range[0]);
          return new SubRange(min, max);
        }

        if (typeString.startsWith("array of"))
        {
          String elementTypeName = typeString.replaceFirst("array of", "").trim();
          Type elementType = getType(elementTypeName);
          return new Array(elementType);
        }

        // the type is enum
        return new Enum(typeString);
      }
    }
  }

  @Override
  public String toString()
  {
    return name;
  }
}
