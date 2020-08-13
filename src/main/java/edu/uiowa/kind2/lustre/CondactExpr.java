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

public class CondactExpr extends Expr {
  public final Expr clock;
  public final NodeCallExpr call;
  public final List<Expr> args;

  public CondactExpr(Location loc, Expr clock, NodeCallExpr call, List<Expr> args) {
    super(loc);
    Assert.isNotNull(clock);
    Assert.isNotNull(call);
    this.clock = clock;
    this.call = call;
    this.args = Util.safeList(args);
  }

  public CondactExpr(Expr clock, NodeCallExpr call, List<Expr> args) {
    this(Location.NULL, clock, call, args);
  }

  public CondactExpr(Expr clock, NodeCallExpr call, Expr... args) {
    this(Location.NULL, clock, call, Arrays.asList(args));
  }
}
