/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

import com.google.gson.JsonElement;

import java.math.BigInteger;

/**
 * Kind2 value for int, uint8, uint16, uint32, uint64, int8, int16, int32, int64.
 */
public class Kind2IntValue extends Kind2Value
{
  private final BigInteger value;

  public Kind2IntValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    value = new BigInteger(jsonElement.getAsString());
  }

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
