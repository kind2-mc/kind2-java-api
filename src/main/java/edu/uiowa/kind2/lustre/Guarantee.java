/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a contract guarantee. Unlike assumptions, guarantees do not have any
 * restrictions on the streams they can mention. They typically mention the outputs in the current
 * state since they express the behavior of the node they specified under the assumptions of this
 * node.
 */
class Guarantee extends ContractItem {
  final String name;
  final Expr expr;

  /**
   * Constructor
   *
   * @param name name of the guarantee
   * @param expr constraint expressing the behavior of a node
   */
  Guarantee(String name, Expr expr) {
    this.name = name;
    Assert.isNotNull(expr);
    this.expr = expr;
  }
}
