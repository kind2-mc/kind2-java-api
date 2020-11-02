/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class IfThenElseExpr extends Expr {
  final Expr cond;
  final Expr thenExpr;
  final Expr elseExpr;

  IfThenElseExpr(Expr cond, Expr thenExpr, Expr elseExpr) {
    Assert.isNotNull(cond);
    Assert.isNotNull(thenExpr);
    Assert.isNotNull(elseExpr);
    this.cond = cond;
    this.thenExpr = thenExpr;
    this.elseExpr = elseExpr;
  }
}
