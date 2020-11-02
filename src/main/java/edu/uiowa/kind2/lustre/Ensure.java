/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

/*
 * This class represents a mode ensure
*/
class Ensure {
  final String name; // Nullable
  final Expr expr;

  /**
   * @param name name of the ensure
   * @param expr the expression the mode ensures
   */
  Ensure(String name, Expr expr) {
    this.name = name;
    this.expr = expr;
  }
}
