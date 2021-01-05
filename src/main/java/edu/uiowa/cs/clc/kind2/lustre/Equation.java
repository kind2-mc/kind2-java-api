/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.Collections;
import java.util.List;

import edu.uiowa.cs.clc.kind2.Assert;
import edu.uiowa.cs.clc.kind2.util.Util;

class Equation extends Ast {
  final List<IdExpr> lhs;
  final Expr expr;

  Equation(List<IdExpr> lhs, Expr expr) {
    Assert.isNotNull(expr);
    this.lhs = Util.safeList(lhs);
    this.expr = expr;
  }

  Equation(IdExpr id, Expr expr) {
    this.lhs = Collections.singletonList(id);
    this.expr = expr;
  }
}
