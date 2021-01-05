/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

/*
 * This class represents a mode requirement
*/
class Require {
  final String name; // Nullable
  final Expr expr;

  /**
   * @param name name of the requirement
   * @param expr the requirement to check
   */
  Require(String name, Expr expr) {
    this.name = name;
    this.expr = expr;
  }
}
