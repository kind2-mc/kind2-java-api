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
  realizable("Realizable"),
  unrealizable("Unrealizable"),
  none("None");

  private final String value;

  RealizabilityResult(String value)
  {
    this.value = value;
  }

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
      case "None":
      case "none":
        return none;
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
