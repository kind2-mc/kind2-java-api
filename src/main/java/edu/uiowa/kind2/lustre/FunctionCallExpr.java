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

public class FunctionCallExpr extends Expr {
  public final String function;
  public final List<Expr> args;

  public FunctionCallExpr(Location loc, String function, List<Expr> args) {
    super(loc);
    Assert.isNotNull(function);
    this.function = function;
    this.args = Util.safeList(args);
  }

  public FunctionCallExpr(String node, List<Expr> args) {
    this(Location.NULL, node, args);
  }

  public FunctionCallExpr(String node, Expr... args) {
    this(Location.NULL, node, Arrays.asList(args));
  }
}