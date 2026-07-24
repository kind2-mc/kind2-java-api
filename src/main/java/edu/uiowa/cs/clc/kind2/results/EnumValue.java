/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.JsonElement;

/**
 * The value of kind2 enum.
 */
public class EnumValue extends Value
{
  private final String name;

  /**
   * Constructs a enumeration value from one Kind 2 stream value.
   *
   * @param kind2StepValue the step this value belongs to
   * @param kind2Type the type of the value
   * @param jsonElement the Kind 2 json element holding the value
   */
  public EnumValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    name = jsonElement.getAsString().trim();
  }

  /**
   * Returns the name of the enumeration constant.
   *
   * @return the name of the enumeration constant
   */
  public String getName()
  {
    return Result.getOpeningSymbols() + name + Result.getClosingSymbols();
  }

  @Override
  public String toString()
  {
    return name;
  }
}
