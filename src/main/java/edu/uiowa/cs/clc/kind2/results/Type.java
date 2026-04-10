/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;
import edu.uiowa.cs.clc.kind2.Kind2Exception;

import com.google.gson.JsonElement;

/**
 * An abstract class for all kind2 types.
 */
abstract public class Type
{
  public final String name;

  public Type(String name)
  {
    this.name = name;
  }

  public static Type getType(String type)
  {
    return getType(type, null);
  }
  private static Type makeNestedArray(String baseType, int numDims){
    if (numDims == 0){
      return getType(baseType);
    } else {
      return new Array(makeNestedArray(baseType, numDims-1));
    }
  }
  

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
