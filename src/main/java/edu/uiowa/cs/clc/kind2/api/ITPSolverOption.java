/*
 * Copyright (c) 2023, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

/**
 * The solvers Kind 2 can use to compute interpolants.
 */
public enum ITPSolverOption {
  /**
   * The cvc5 solver using quantifier elimination.
   */
  CVC5QE,
  /**
   * The MathSAT solver.
   */
  MATHSAT,
  /**
   * The OpenSMT solver.
   */
  OPENSMT,
  /**
   * The SMTInterpol solver.
   */
  SMTINTERPOL,
  /**
   * The Z3 solver using quantifier elimination.
   */
  Z3QE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}

