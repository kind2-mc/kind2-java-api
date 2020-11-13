/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a contract guarantee.
 */
class Guarantee extends ContractItem {
  final boolean weak;
  final String name; // Nullable
  final Expr expr;

  /**
   * Constructor
   *
   * @param weak whether this is a weakly guarantee or not
   * @param name name of the guarantee
   * @param expr constraint expressing the behavior of a node
   */
  Guarantee(boolean weak, String name, Expr expr) {
    this.weak = weak;
    this.name = name;
    Assert.isNotNull(expr);
    this.expr = expr;
  }
}
