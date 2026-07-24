/*
 * Copyright (c) 2023, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

/**
 * The solvers Kind 2 can use for quantifier elimination.
 */
public enum QESolverOption {
  /**
   * The cvc5 solver.
   */
  CVC5,
  /**
   * The Z3 solver.
   */
  Z3;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}

