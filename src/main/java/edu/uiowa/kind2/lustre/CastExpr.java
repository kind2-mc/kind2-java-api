/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class CastExpr extends Expr {
  public final Type type;
  public final Expr expr;

  public CastExpr(Location location, Type type, Expr expr) {
    super(location);
    Assert.isNotNull(type);
    Assert.isNotNull(expr);
    this.type = type;
    this.expr = expr;
  }

  public CastExpr(Type type, Expr expr) {
    this(Location.NULL, type, expr);
  }
}
