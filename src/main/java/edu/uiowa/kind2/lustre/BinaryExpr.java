/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class BinaryExpr extends Expr {
  final Expr left;
  final BinaryOp op;
  final Expr right;

  BinaryExpr(Expr left, BinaryOp op, Expr right) {
    Assert.isNotNull(left);
    Assert.isNotNull(op);
    Assert.isNotNull(right);
    this.left = left;
    this.op = op;
    this.right = right;
  }
}
