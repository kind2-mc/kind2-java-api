/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.util.Util;

class ListExpr extends Expr {
  final List<Expr> list;

  ListExpr(List<? extends Expr> list) {
    this.list = Util.safeList(list);
  }
}
