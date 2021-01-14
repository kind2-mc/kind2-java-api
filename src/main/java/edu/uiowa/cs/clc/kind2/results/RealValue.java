/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The value of kind2 rational number.
 */
public class RealValue extends Value
{
  /**
   * the value of the rational number.
   */
  private final BigDecimal value;
  /**
   * The numerator of the rational
   */
  private final BigInteger numerator;
  /**
   * The denominator of the rational number
   */
  private final BigInteger denominator;

  public RealValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    if (jsonElement.isJsonObject())
    {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      numerator = new BigInteger(jsonObject.get(Labels.numerator).getAsString());
      denominator = new BigInteger(jsonObject.get(Labels.denominator).getAsString());
      value = new BigDecimal(numerator).divide(new BigDecimal(denominator),
          Result.getRealPrecision(), Result.getRealRoundingMode());
    }
    else
    {
      value = jsonElement.getAsBigDecimal();
      numerator = value.unscaledValue();
      denominator = BigInteger.TEN.pow(value.scale());
    }
  }

  /**
   * @return the value of the rational number.
   */
  public BigDecimal getValue()
  {
    return value;
  }

  @Override
  public String toString()
  {
    return value.toString();
  }
}
