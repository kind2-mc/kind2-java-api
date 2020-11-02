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
 * This class represents a contract import. A contract import merges the current contract with the
 * one imported. That is, if the current contract is {@code (A,G,M)} and we import
 * {@code (A',G',M')}, the resulting contract is {@code (A U A', G U G', M U M')} where U is set
 * union.
 * <p>
 * When importing a contract, it is necessary to specify how the instantiation of the contract is
 * performed. This defines a mapping from the input (output) formal parameters to the actual ones of
 * the import.
 * <p>
 * When importing contract {@code c} in the contract of node {@code n}, it is <b>illegal</b> to
 * mention an output of {@code n} in the actual input parameters of the import of {@code c}.
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
