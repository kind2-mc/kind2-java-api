/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a ghost variable definition.
 */
class VarDef extends ContractItem {
  final VarDecl varDecl;
  final Expr expr;

  /**
   * Constructor
   *
   * @param varDecl ghost variable's declaration
   * @param expr    expression specifying stream of values assigned to ghost variable
   */
  VarDef(VarDecl varDecl, Expr expr) {
    this.varDecl = varDecl;
    Assert.isNotNull(expr);
    this.expr = expr;
  }

  /**
   * Constructor
   *
   * @param id   name of ghost variable
   * @param type type of ghost variable
   * @param expr expression specifying stream of values assigned to ghost variable
   */
  VarDef(String id, Type type, Expr expr) {
    this(new VarDecl(id, type), expr);
  }
}
