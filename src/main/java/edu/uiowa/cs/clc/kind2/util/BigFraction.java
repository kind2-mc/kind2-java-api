/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import edu.uiowa.cs.clc.kind2.Kind2Exception;

/**
 * An arbitrary sized fractional value
 *
 * Stored as <code>numerator</code> / <code>denominator</code> where the fraction is in reduced form
 * and <code>denominator</code> is always positive
 */
public class BigFraction implements Comparable<BigFraction> {
  /**
   * The fraction equal to zero.
   */
  public static final BigFraction ZERO = new BigFraction(BigInteger.ZERO);
  /**
   * The fraction equal to one.
   */
  public static final BigFraction ONE = new BigFraction(BigInteger.ONE);

  // The numerator and denominator are always stored in reduced form with the
  // denominator always positive
  final private BigInteger num;
  final private BigInteger denom;

  /**
   * Constructs the fraction {@code num / denom}, reduced to lowest terms.
   *
   * @param num the numerator
   * @param denom the denominator
   * @throws ArithmeticException if {@code denom} is zero
   */
  public BigFraction(BigInteger num, BigInteger denom) {
    if (num == null || denom == null) {
      throw new NullPointerException();
    }
    if (denom.equals(BigInteger.ZERO)) {
      throw new ArithmeticException("Divide by zero");
    }

    BigInteger gcd = num.gcd(denom);
    if (denom.compareTo(BigInteger.ZERO) > 0) {
      this.num = num.divide(gcd);
      this.denom = denom.divide(gcd);
    } else {
      this.num = num.negate().divide(gcd);
      this.denom = denom.negate().divide(gcd);
    }
  }

  /**
   * Constructs the fraction {@code num / 1}.
   *
   * @param num the numerator
   */
  public BigFraction(BigInteger num) {
    this(num, BigInteger.ONE);
  }

  /**
   * Converts a decimal value to an exact fraction.
   *
   * @param value the decimal value to convert
   * @return the equivalent fraction
   */
  public static BigFraction valueOf(BigDecimal value) {
    if (value.scale() >= 0) {
      return new BigFraction(value.unscaledValue(), BigInteger.valueOf(10).pow(value.scale()));
    } else {
      return new BigFraction(
          value.unscaledValue().multiply(BigInteger.valueOf(10).pow(-value.scale())));
    }
  }

  /**
   * Returns the numerator of this fraction.
   *
   * @return the numerator of this fraction
   */
  public BigInteger getNumerator() {
    return num;
  }

  /**
   * Returns the denominator of this fraction.
   *
   * @return the denominator of this fraction
   */
  public BigInteger getDenominator() {
    return denom;
  }

  /**
   * Adds another fraction to this one.
   *
   * @param val the fraction to add
   * @return the sum
   */
  public BigFraction add(BigFraction val) {
    return new BigFraction(num.multiply(val.denom).add(val.num.multiply(denom)),
        denom.multiply(val.denom));
  }

  /**
   * Adds an integer to this fraction.
   *
   * @param val the integer to add
   * @return the sum
   */
  public BigFraction add(BigInteger val) {
    return add(new BigFraction(val));
  }

  /**
   * Subtracts another fraction from this one.
   *
   * @param val the fraction to subtract
   * @return the difference
   */
  public BigFraction subtract(BigFraction val) {
    return new BigFraction(num.multiply(val.denom).subtract(val.num.multiply(denom)),
        denom.multiply(val.denom));
  }

  /**
   * Subtracts an integer from this fraction.
   *
   * @param val the integer to subtract
   * @return the difference
   */
  public BigFraction subtract(BigInteger val) {
    return subtract(new BigFraction(val));
  }

  /**
   * Multiplies this fraction by another.
   *
   * @param val the fraction to multiply by
   * @return the product
   */
  public BigFraction multiply(BigFraction val) {
    return new BigFraction(num.multiply(val.num), denom.multiply(val.denom));
  }

  /**
   * Multiplies this fraction by an integer.
   *
   * @param val the integer to multiply by
   * @return the product
   */
  public BigFraction multiply(BigInteger val) {
    return multiply(new BigFraction(val));
  }

  /**
   * Divides this fraction by another.
   *
   * @param val the fraction to divide by
   * @return the quotient
   * @throws ArithmeticException if {@code val} is zero
   */
  public BigFraction divide(BigFraction val) {
    return new BigFraction(num.multiply(val.denom), denom.multiply(val.num));
  }

  /**
   * Divides this fraction by an integer.
   *
   * @param val the integer to divide by
   * @return the quotient
   * @throws ArithmeticException if {@code val} is zero
   */
  public BigFraction divide(BigInteger val) {
    return divide(new BigFraction(val));
  }

  /**
   * Negates this fraction.
   *
   * @return this fraction with its sign flipped
   */
  public BigFraction negate() {
    return new BigFraction(num.negate(), denom);
  }

  /**
   * Returns the sign of this fraction.
   *
   * @return {@code -1}, {@code 0}, or {@code 1} as this fraction is negative, zero, or positive
   */
  public int signum() {
    return num.signum();
  }

  /**
   * Converts this fraction to a double, which may lose precision.
   *
   * @return the closest double to this fraction
   */
  public double doubleValue() {
    double result = num.doubleValue() / denom.doubleValue();
    if (Double.isFinite(result)) {
      return result;
    } else {
      BigDecimal numDec = new BigDecimal(num);
      BigDecimal denomDec = new BigDecimal(denom);
      return numDec.divide(denomDec, MathContext.DECIMAL64).doubleValue();
    }
  }

  /**
   * Rounds this fraction towards negative infinity.
   *
   * @return the largest integer that is not greater than this fraction
   */
  public BigInteger floor() {
    BigInteger divAndRem[] = num.divideAndRemainder(denom);
    if (num.signum() >= 0 || divAndRem[1].equals(BigInteger.ZERO)) {
      return divAndRem[0];
    } else {
      return divAndRem[0].subtract(BigInteger.ONE);
    }
  }

  /**
   * Converts this fraction to a decimal with the given number of fractional digits.
   *
   * @param scale the number of digits after the decimal point
   * @return this fraction as a decimal
   */
  public BigDecimal toBigDecimal(int scale) {
    BigDecimal decNum = new BigDecimal(num).setScale(scale);
    BigDecimal decDenom = new BigDecimal(denom);
    return decNum.divide(decDenom, RoundingMode.DOWN);
  }

  /**
   * Formats this fraction as a decimal with trailing zeros removed.
   *
   * @param scale the number of digits after the decimal point before truncation
   * @param suffix appended to the result when the decimal is not exact
   * @return the formatted value
   */
  public String toTruncatedDecimal(int scale, String suffix) {
    if (scale <= 0) {
      throw new Kind2Exception("Scale must be positive");
    }

    BigDecimal dec = toBigDecimal(scale);
    if (this.equals(BigFraction.valueOf(dec))) {
      return Util.removeTrailingZeros(dec.toPlainString());
    } else {
      return dec.toPlainString() + suffix;
    }
  }

  @Override
  public int compareTo(BigFraction other) {
    return num.multiply(other.denom).compareTo(other.num.multiply(denom));
  }

  @Override
  public String toString() {
    if (denom.equals(BigInteger.ONE)) {
      return num.toString();
    } else {
      return num + "/" + denom;
    }
  }

  @Override
  public int hashCode() {
    return num.hashCode() + denom.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof BigFraction)) {
      return false;
    }
    BigFraction other = (BigFraction) obj;
    return num.equals(other.num) && denom.equals(other.denom);
  }
}
