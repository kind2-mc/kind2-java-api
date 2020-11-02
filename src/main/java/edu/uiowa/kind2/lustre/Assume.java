/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a contract assumption. An assumption over a node {@code n} is a constraint
 * one must respect in order to use {@code n} legally. It cannot mention the outputs of {@code n} in
 * the current state, but referring to outputs under a {@code pre} is fine.
 */
class Assume extends ContractItem {
  final String name; // Nullable
  final Expr expr;

  /**
   * Constructor
   *
   * @param name name of the assumption
   * @param expr an expression representing a constraint
   */
  Assume(String name, Expr expr) {
    this.name = name;
    Assert.isNotNull(expr);
    this.expr = expr;
  }
}
