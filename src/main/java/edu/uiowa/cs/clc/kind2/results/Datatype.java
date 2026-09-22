/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kind2 type for algebraic datatypes.
 */
public class Datatype extends Type
{
  private final List<String> constructors;

  /**
   * Constructs a datatype with the given name and constructors.
   *
   * @param name the name of the datatype
   * @param constructors the names of the datatype's constructors
   */
  public Datatype(String name, List<String> constructors)
  {
    super(name);
    this.constructors = new ArrayList<>(constructors);
  }

  /**
   * Returns the names of this datatype's constructors.
   *
   * @return the names of this datatype's constructors
   */
  public List<String> getConstructors()
  {
    return Collections.unmodifiableList(constructors);
  }
}
