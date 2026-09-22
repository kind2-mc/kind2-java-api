/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uiowa.cs.clc.kind2.Kind2Exception;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The value of kind2 rational number.
 *
 * <p>Not every one of them is a number. Kind 2 reports an infinity and an
 * undefined value as a fraction over a zero denominator, which is how a
 * division by zero in the model reaches the counterexample: {@code 1/0} and
 * {@code -1/0} for the infinities, {@code 0/0} for a value it could not
 * determine. {@link #getKind} says which of the four a value is, and
 * {@link #getValue} is the number only for a finite one.
 */
public class RealValue extends Value
{
  /**
   * What a rational reported by Kind 2 turned out to be.
   */
  public enum Kind
  {
    /**
     * A number, which {@link RealValue#getValue} returns.
     */
    FINITE,
    /**
     * Positive infinity, which Kind 2 writes as {@code 1/0}.
     */
    POSITIVE_INFINITY,
    /**
     * Negative infinity, which Kind 2 writes as {@code -1/0}.
     */
    NEGATIVE_INFINITY,
    /**
     * A value Kind 2 could not determine, which it writes as {@code 0/0}.
     */
    UNDEFINED
  }

  /**
   * the value of the rational number, or null when it is not a number.
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
  /**
   * Which of the four kinds of rational this is.
   */
  private final Kind kind;

  /**
   * Constructs a real value from one Kind 2 stream value.
   *
   * @param kind2StepValue the step this value belongs to
   * @param kind2Type the type of the value
   * @param jsonElement the Kind 2 json element holding the value
   */
  public RealValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    if (jsonElement.isJsonObject())
    {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      numerator = new BigInteger(jsonObject.get(Labels.numerator).getAsString());
      denominator = new BigInteger(jsonObject.get(Labels.denominator).getAsString());
      kind = kindOf(numerator, denominator);
      // Dividing is only possible, and only means anything, for a finite one
      value = kind == Kind.FINITE
          ? new BigDecimal(numerator).divide(new BigDecimal(denominator),
              Result.getRealPrecision(), Result.getRealRoundingMode())
          : null;
    }
    else
    {
      value = jsonElement.getAsBigDecimal();
      numerator = value.unscaledValue();
      denominator = BigInteger.TEN.pow(value.scale());
      kind = Kind.FINITE;
    }
  }

  /**
   * The kind a fraction denotes. A zero denominator carries an infinity whose
   * sign is the sign of the numerator, or, with a zero numerator, a value
   * Kind 2 could not determine.
   */
  private static Kind kindOf(BigInteger numerator, BigInteger denominator)
  {
    if (denominator.signum() != 0)
    {
      return Kind.FINITE;
    }
    if (numerator.signum() > 0)
    {
      return Kind.POSITIVE_INFINITY;
    }
    if (numerator.signum() < 0)
    {
      return Kind.NEGATIVE_INFINITY;
    }
    return Kind.UNDEFINED;
  }

  /**
   * Returns which of the four kinds of rational this value is.
   *
   * @return the kind of this value
   */
  public Kind getKind()
  {
    return kind;
  }

  /**
   * Returns whether this value is a number, and so whether
   * {@link #getValue} returns one.
   *
   * @return {@code true} if this value is finite
   */
  public boolean isFinite()
  {
    return kind == Kind.FINITE;
  }

  /**
   * Returns the value of the rational number.
   *
   * @return the value of the rational number.
   * @throws Kind2Exception if this value is not a number, which
   *         {@link #isFinite} tells apart beforehand
   */
  public BigDecimal getValue()
  {
    if (value == null)
    {
      throw new Kind2Exception(
          "This value is " + kind + " rather than a number, so it has no "
              + "decimal value. Kind 2 reports it as " + this
              + ". Use isFinite() to tell the two apart, or getDoubleValue().");
    }
    return value;
  }

  /**
   * Returns this value as a {@code double}, which, unlike a
   * {@link BigDecimal}, can carry an infinity and a value Kind 2 could not
   * determine. A finite one is rounded to the nearest {@code double}.
   *
   * @return this value as a {@code double}
   */
  public double getDoubleValue()
  {
    switch (kind)
    {
      case POSITIVE_INFINITY:
        return Double.POSITIVE_INFINITY;
      case NEGATIVE_INFINITY:
        return Double.NEGATIVE_INFINITY;
      case UNDEFINED:
        return Double.NaN;
      default:
        return value.doubleValue();
    }
  }

  /**
   * Returns the numerator of this value expressed as a fraction.
   *
   * @return the numerator of this value
   */
  public BigInteger getNumerator()
  {
    return numerator;
  }

  /**
   * Returns the denominator of this value expressed as a fraction, which is
   * zero for every value that is not a number.
   *
   * @return the denominator of this value
   */
  public BigInteger getDenominator()
  {
    return denominator;
  }

  @Override
  public String toString()
  {
    // What Kind 2 itself prints for these, in its plain text and its XML
    return value == null ? numerator + "/" + denominator : value.toString();
  }
}
