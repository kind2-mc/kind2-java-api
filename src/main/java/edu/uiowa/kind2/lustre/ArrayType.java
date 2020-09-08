/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * The type of an array variable.
 */
public class ArrayType extends Type {
  public final Type base;
  public final int size;

  /**
   * Constructor
   *
   * @param location location of contract in a Lustre file
   * @param base     type of the array
   * @param size     size of the array
   */
  public ArrayType(Location location, Type base, int size) {
    super(location);
    Assert.isNotNull(base);
    Assert.isTrue(size > 0);
    this.base = base;
    this.size = size;
  }

  /**
   * Constructor
   *
   * @param base type of the array
   * @param size size of the array
   */
  public ArrayType(Type base, int size) {
    this(Location.NULL, base, size);
  }

  @Override
  public String toString() {
    return base + "^" + size;
  }
}
