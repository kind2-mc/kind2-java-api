/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

public enum SolverOption {
  BITWUZLA, CVC5, MATHSAT, YICES, YICES2, Z3;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
