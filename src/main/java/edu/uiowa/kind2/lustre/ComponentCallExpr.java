/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

class ComponentCallExpr extends Expr {
  final String node;
  final List<Expr> args;

  ComponentCallExpr(String node, List<Expr> args) {
    Assert.isNotNull(node);
    this.node = node;
    this.args = Util.safeList(args);
  }
}
