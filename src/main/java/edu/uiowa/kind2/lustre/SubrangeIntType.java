/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.math.BigInteger;

import edu.uiowa.kind2.Assert;

class SubrangeIntType extends Type {
  final BigInteger low;
  final BigInteger high;

  SubrangeIntType(BigInteger low, BigInteger high) {
    Assert.isNotNull(low);
    Assert.isNotNull(high);
    Assert.isTrue(low.compareTo(high) <= 0);
    this.low = low;
    this.high = high;
  }

  @Override
  public String toString() {
    return "subrange [" + low + ", " + high + "] of int";
  }
}
