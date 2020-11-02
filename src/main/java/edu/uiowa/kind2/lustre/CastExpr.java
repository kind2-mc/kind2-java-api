/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class CastExpr extends Expr {
  final Type type;
  final Expr expr;

  CastExpr(Type type, Expr expr) {
    Assert.isNotNull(type);
    Assert.isNotNull(expr);
    this.type = type;
    this.expr = expr;
  }
}
