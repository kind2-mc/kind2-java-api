/*
 * Copyright (c) 2012-2013, Rockwell Collins
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
 * This class represents a contract node. A contract node is very similar to a traditional Lustre
 * node. The two differences are that
 * <ul>
 * <li>it starts with {@code contract} instead of {@code node} and</li>
 * <li>its body can only mention contract items.</li>
 * </ul>
 * To use a contract node one needs to import it through an inline contract.
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
