/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

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
    switch (type)
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
        return new Int();
      case "real":
        return new Real();
      default:
      {
        if (type.matches("subrange \\[.*?\\] of int"))
        {
          String [] range = type.replaceAll("subrange \\[", "")
                                .replaceAll("\\] of int", "").split(",");
          int min = Integer.parseInt(range[0]);
          int max = Integer.parseInt(range[0]);
          return new SubRange(min, max);
        }

        if (type.startsWith("array of"))
        {
          String elementTypeName = type.replaceFirst("array of", "").trim();
          Type elementType = getType(elementTypeName);
          return new Array(elementType);
        }

        // the type is enum
        return new Enum(type);
      }
    }
  }

  @Override
  public String toString()
  {
    return name;
  }
}
