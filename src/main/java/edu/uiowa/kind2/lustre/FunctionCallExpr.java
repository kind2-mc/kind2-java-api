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

class FunctionCallExpr extends Expr {
  final String function;
  final List<Expr> args;

  FunctionCallExpr(String function, List<Expr> args) {
    Assert.isNotNull(function);
    this.function = function;
    this.args = Util.safeList(args);
  }

  FunctionCallExpr(String node, Expr... args) {
    this(node, Arrays.asList(args));
  }
}
