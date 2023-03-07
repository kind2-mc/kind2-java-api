/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * The source of the answer, and the result value of the check.
 * The result can be valid, falsifiable, or unknown
 */
public enum Answer
{
  valid("valid"),
  falsifiable("falsifiable"),
  unknown("unknown"),
  reachable("reachable"),
  unreachable("unreachable");

  private final String value;

  Answer(String value)
  {
    this.value = value;
  }

  public static Answer getAnswer(String answer)
  {
    switch (answer)
    {
      case "valid":
        return valid;
      case "falsifiable":
        return falsifiable;
      case "unknown":
        return unknown;
      case "reachable":
        return reachable;
      case "unreachable":
        return unreachable;
      default:
        throw new UnsupportedOperationException("Answer " + answer + " is not defined");
    }
  }

  @Override
  public String toString()
  {
    return this.value;
  }
}