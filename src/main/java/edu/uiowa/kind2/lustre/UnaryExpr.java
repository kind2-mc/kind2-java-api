/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class UnaryExpr extends Expr {
  public final UnaryOp op;
  public final Expr expr;

  public UnaryExpr(Location location, UnaryOp op, Expr expr) {
    super(location);
    Assert.isNotNull(op);
    Assert.isNotNull(expr);
    this.op = op;
    this.expr = expr;
  }

  public UnaryExpr(UnaryOp op, Expr expr) {
    this(Location.NULL, op, expr);
  }
}
