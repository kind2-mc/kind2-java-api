/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

/**
 * This class represents an node/function input/output parameter.
 */
class Parameter extends Ast {
  final String id;
  final Type type;
  final boolean isConst;

  /**
   * Constructor
   *
   * @param id      name of the parameter
   * @param type    type of the parameter
   * @param isConst whether or not the parameter is constant
   */
  Parameter(String id, Type type, boolean isConst) {
    this.id = id;
    this.type = type;
    this.isConst = isConst;
  }

  /**
   * Constructor
   *
   * @param id   name of the parameter
   * @param type type of the parameter
   */
  Parameter(String id, Type type) {
    this(id, type, false);
  }
}
