/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.JsonElement;

/**
 * This class stores the boolean value of kind2 bool type.
 */
public class Kind2BoolValue extends Kind2Value
{
  private final boolean value;

  public Kind2BoolValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    value = Boolean.parseBoolean(jsonElement.getAsString());
  }

  public boolean getValue()
  {
    return value;
  }

  @Override
  public String toString()
  {
    return Boolean.toString(value);
  }
}
