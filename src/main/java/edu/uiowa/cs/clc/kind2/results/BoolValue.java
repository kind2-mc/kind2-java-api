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
public class BoolValue extends Value
{
  private final boolean value;

  /**
   * Constructs a boolean value from one Kind 2 stream value.
   *
   * @param kind2StepValue the step this value belongs to
   * @param kind2Type the type of the value
   * @param jsonElement the Kind 2 json element holding the value
   */
  public BoolValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    value = Boolean.parseBoolean(jsonElement.getAsString());
  }

  /**
   * Returns the boolean value.
   *
   * @return the boolean value
   */
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
