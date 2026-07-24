/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2;

/**
 * Simple runtime assertions used to validate arguments.
 */
public class Assert {
  private Assert() {
  }

  /**
   * Asserts that the given object is not null.
   *
   * @param o the object to check
   * @throws IllegalArgumentException if {@code o} is null
   */
  public static void isNotNull(Object o) {
    if (o == null) {
      throw new Kind2Exception("Object unexpectedly null");
    }
  }

  /**
   * Asserts that the given condition holds.
   *
   * @param b the condition to check
   * @throws IllegalArgumentException if {@code b} is false
   */
  public static void isTrue(boolean b) {
    if (!b) {
      throw new Kind2Exception("Assertion failed");
    }
  }

  /**
   * Asserts that the given condition does not hold.
   *
   * @param b the condition to check
   * @throws IllegalArgumentException if {@code b} is true
   */
  public static void isFalse(boolean b) {
    if (b) {
      throw new Kind2Exception("Assertion failed");
    }
  }
}
