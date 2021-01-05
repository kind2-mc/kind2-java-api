/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import edu.uiowa.cs.clc.kind2.Assert;

/**
 * The type of an array variable.
 */
class ArrayType implements Type {
  final Type base;
  final int size;

  /**
   * Constructor
   *
   * @param base type of the array
   * @param size size of the array
   */
  ArrayType(Type base, int size) {
    Assert.isNotNull(base);
    Assert.isTrue(size > 0);
    this.base = base;
    this.size = size;
  }

  @Override
  public String toString() {
    return base + "^" + size;
  }
}
