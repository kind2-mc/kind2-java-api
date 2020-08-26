/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

/**
 * Kind2 type for arrays.
 */
public class Kind2Array extends Kind2Type
{
  private final Kind2Type elementType;

  public Kind2Array(Kind2Type elementType)
  {
    super("array of " + elementType.toString());
    this.elementType = elementType;
  }

  public Kind2Type getElementType()
  {
    return elementType;
  }
}
