/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class ArrayAccessExpr extends Expr {
  final Expr array;
  final Expr index;

  ArrayAccessExpr(Expr array, Expr index) {
    Assert.isNotNull(array);
    Assert.isNotNull(index);
    this.array = array;
    this.index = index;
  }

  ArrayAccessExpr(Expr array, int index) {
    this(array, new IntExpr(index));
  }
}
