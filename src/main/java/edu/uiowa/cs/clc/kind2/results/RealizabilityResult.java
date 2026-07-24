/*
 * Copyright (c) 2024, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Enum for realizability results.
 */
public enum RealizabilityResult
{
  /**
   * The contract admits an implementation.
   */
  realizable("realizable"),
  /**
   * The contract admits no implementation.
   */
  unrealizable("unrealizable");

  private final String value;

  RealizabilityResult(String value)
  {
    this.value = value;
  }

  /**
   * Returns the realizability result denoted by the given Kind 2 result name.
   *
   * @param realizabilityResult the Kind 2 result name
   * @return the corresponding result
   * @throws UnsupportedOperationException if the name is not recognised
   */
  public static RealizabilityResult getRealizabilityResult(String realizabilityResult)
  {
    switch (realizabilityResult)
    {
      case "Realizable":
      case "realizable":
        return realizable;
      case "Unrealizable":
      case "unrealizable":
        return unrealizable;
      default:
        throw new UnsupportedOperationException("Realizability result " + realizabilityResult + " is not defined");
    }
  }

  @Override
  public String toString()
  {
    return this.value;
  }
}