/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

/**
 * The SMT solvers Kind 2 can use as its main solver.
 */
public enum SolverOption {
  /**
   * The Bitwuzla SMT solver.
   */
  BITWUZLA,
  /**
   * The cvc5 SMT solver.
   */
  CVC5,
  /**
   * The MathSAT SMT solver.
   */
  MATHSAT,
  /**
   * The SMTInterpol SMT solver.
   */
  SMTINTERPOL,
  /**
   * The Yices 1 SMT solver.
   */
  YICES,
  /**
   * The Yices 2 SMT solver.
   */
  YICES2,
  /**
   * The Z3 SMT solver.
   */
  Z3;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
