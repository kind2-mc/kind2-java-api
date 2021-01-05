/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.math.BigDecimal;

import edu.uiowa.cs.clc.kind2.Assert;

class RealExpr extends Expr {
  final BigDecimal value;

  RealExpr(BigDecimal value) {
    Assert.isNotNull(value);
    this.value = value;
  }
}
