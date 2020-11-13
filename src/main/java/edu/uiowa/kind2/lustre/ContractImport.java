/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

/**
 * This class represents a contract import.
 */
class ContractImport extends ContractItem {
  final String id;
  final List<Expr> inputs;
  final List<IdExpr> outputs;

  /**
   * Constructor
   *
   * @param id      name of contract to import
   * @param inputs  inputs to the contract
   * @param outputs outputs of the contract
   */
  ContractImport(String id, List<Expr> inputs, List<IdExpr> outputs) {
    Assert.isNotNull(id);
    this.id = id;
    this.inputs = Util.safeList(inputs);
    this.outputs = Util.safeList(outputs);
  }
}
