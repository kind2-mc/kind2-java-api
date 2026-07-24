/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Kind2 type for enums.
 */
public class Enum extends Type
{
  /**
   * Constructs an enumeration type with the given name.
   *
   * @param name the name of the enumeration type
   */
  public Enum(String name)
  {
    super(name);
  }
}
