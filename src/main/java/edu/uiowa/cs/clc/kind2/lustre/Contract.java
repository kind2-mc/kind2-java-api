/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.List;

import edu.uiowa.cs.clc.kind2.Assert;
import edu.uiowa.cs.clc.kind2.util.Util;

/**
 * This class represents a contract node
 */
class Contract extends Ast {
  final String id;
  final List<Parameter> inputs;
  final List<Parameter> outputs;
  final ContractBody contractBody;

  /**
   * Constructor
   *
   * @param id           name of contract
   * @param inputs       inputs to this contract
   * @param outputs      outputs of this contract
   * @param contractBody body of this contract
   */
  Contract(String id, List<Parameter> inputs, List<Parameter> outputs,
      ContractBody contractBody) {
    Assert.isNotNull(id);
    this.id = id;
    this.inputs = Util.safeList(inputs);
    this.outputs = Util.safeList(outputs);
    Assert.isNotNull(contractBody);
    this.contractBody = contractBody;
  }
}
