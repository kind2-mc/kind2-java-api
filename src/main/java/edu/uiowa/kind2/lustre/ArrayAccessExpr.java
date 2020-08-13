/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class ArrayAccessExpr extends Expr {
  public final Expr array;
  public final Expr index;

  public ArrayAccessExpr(Location location, Expr array, Expr index) {
    super(location);
    Assert.isNotNull(array);
    Assert.isNotNull(index);
    this.array = array;
    this.index = index;
  }

  public ArrayAccessExpr(Expr array, Expr index) {
    this(Location.NULL, array, index);
  }

  public ArrayAccessExpr(Expr array, int index) {
    this(array, new IntExpr(index));
  }
}
