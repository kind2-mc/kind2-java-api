/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.math.BigInteger;

import edu.uiowa.kind2.Assert;

public class IntExpr extends Expr {
  public final BigInteger value;

  public IntExpr(Location location, BigInteger value) {
    super(location);
    Assert.isNotNull(value);
    this.value = value;
  }

  public IntExpr(BigInteger value) {
    this(Location.NULL, value);
  }

  public IntExpr(int value) {
    this(Location.NULL, BigInteger.valueOf(value));
  }
}
