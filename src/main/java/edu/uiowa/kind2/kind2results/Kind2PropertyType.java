/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.kind2results;

/**
 * Enum for property types.
 */
public enum Kind2PropertyType
{
  assumption("Assumption"),
  guarantee("Guarantee"),
  ensure("Ensure"),
  annotation("PropAnnot"),
  oneModeActive("OneModeActive");

  private final String value;

  Kind2PropertyType(String value)
  {
    this.value = value;
  }

  public static Kind2PropertyType getPropertyType(String propertyType)
  {
    switch (propertyType)
    {
      case "Assumption":
      case "assumption":
        return assumption;
      case "Guarantee":
      case "guarantee":
        return guarantee;
      case "Ensure":
      case "ensure":
        return ensure;
      case "PropAnnot":
      case "propAnnot":
        return annotation;
      case "OneModeActive":
      case "oneModeActive":
        return oneModeActive;
      default:
        throw new UnsupportedOperationException("Property type " + propertyType + " is not defined");
    }
  }

  @Override
  public String toString()
  {
    return this.value;
  }
}
