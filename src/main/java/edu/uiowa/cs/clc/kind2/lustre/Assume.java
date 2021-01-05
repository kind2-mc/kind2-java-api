/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import edu.uiowa.cs.clc.kind2.Assert;

/**
 * This class represents a contract assumption.
 */
class Assume extends ContractItem {
  final boolean weak;
  final String name; // Nullable
  final Expr expr;

  /**
   * Constructor
   *
   * @param weak whether this is a weakly assume or not
   * @param name name of the assumption
   * @param expr an expression representing a constraint
   */
  Assume(boolean weak, String name, Expr expr) {
    this.weak = weak;
    this.name = name;
    Assert.isNotNull(expr);
    this.expr = expr;
  }
}
