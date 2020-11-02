/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.Arrays;
import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

class CondactExpr extends Expr {
  final Expr clock;
  final NodeCallExpr call;
  final List<Expr> args;

  CondactExpr(Expr clock, NodeCallExpr call, List<Expr> args) {
    Assert.isNotNull(clock);
    Assert.isNotNull(call);
    this.clock = clock;
    this.call = call;
    this.args = Util.safeList(args);
  }

  CondactExpr(Expr clock, NodeCallExpr call, Expr... args) {
    this(clock, call, Arrays.asList(args));
  }
}
