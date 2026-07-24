/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.JsonElement;

import java.math.BigInteger;

/**
 * Kind2 value for int, uint8, uint16, uint32, uint64, int8, int16, int32, int64.
 */
public class IntValue extends Value
{
  private final BigInteger value;

  /**
   * Constructs a integer value from one Kind 2 stream value.
   *
   * @param kind2StepValue the step this value belongs to
   * @param kind2Type the type of the value
   * @param jsonElement the Kind 2 json element holding the value
   */
  public IntValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    value = new BigInteger(jsonElement.getAsString());
  }

  /**
   * Returns the integer value.
   *
   * @return the integer value
   */
  public BigInteger getValue()
  {
    return value;
  }

  @Override
  public String toString()
  {
    return value.toString();
  }
}
