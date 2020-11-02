/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.math.BigInteger;

import edu.uiowa.kind2.Assert;

class IntExpr extends Expr {
  final BigInteger value;

  IntExpr(BigInteger value) {
    Assert.isNotNull(value);
    this.value = value;
  }

  IntExpr(int value) {
    this(BigInteger.valueOf(value));
  }
}
