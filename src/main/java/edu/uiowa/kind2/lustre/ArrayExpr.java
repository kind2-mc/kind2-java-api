/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.util.Util;

class ArrayExpr extends Expr {
  final List<Expr> elements;

  ArrayExpr(List<? extends Expr> elements) {
    this.elements = Util.safeList(elements);
  }
}
