/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class UnaryExpr extends Expr {
  final UnaryOp op;
  final Expr expr;

  UnaryExpr(UnaryOp op, Expr expr) {
    Assert.isNotNull(op);
    Assert.isNotNull(expr);
    this.op = op;
    this.expr = expr;
  }
}
