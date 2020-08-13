/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class IfThenElseExpr extends Expr {
  public final Expr cond;
  public final Expr thenExpr;
  public final Expr elseExpr;

  public IfThenElseExpr(Location location, Expr cond, Expr thenExpr, Expr elseExpr) {
    super(location);
    Assert.isNotNull(cond);
    Assert.isNotNull(thenExpr);
    Assert.isNotNull(elseExpr);
    this.cond = cond;
    this.thenExpr = thenExpr;
    this.elseExpr = elseExpr;
  }

  public IfThenElseExpr(Expr cond, Expr thenExpr, Expr elseExpr) {
    this(Location.NULL, cond, thenExpr, elseExpr);
  }
}
