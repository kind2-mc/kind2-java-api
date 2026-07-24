/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Kind2 type for arrays.
 */
public class Array extends Type
{
  private final Type elementType;

  /**
   * Constructs an array type with the given element type.
   *
   * @param elementType the type of the array's elements
   */
  public Array(Type elementType)
  {
    super("array of " + elementType.toString());
    this.elementType = elementType;
  }

  /**
   * Returns the type of the array's elements.
   *
   * @return the type of the array's elements
   */
  public Type getElementType()
  {
    return elementType;
  }
}
