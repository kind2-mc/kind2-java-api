/*
 * Copyright (c) 2023, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

public enum ITPSolverOption {
  CVC5QE, MATHSAT, OPENSMT, SMTINTERPOL, Z3QE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}

