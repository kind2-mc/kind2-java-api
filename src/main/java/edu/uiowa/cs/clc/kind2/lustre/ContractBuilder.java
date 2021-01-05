/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides helper functions for constructing a contract node. A contract node is very
 * similar to a traditional Lustre node. The two differences are that
 * <ul>
 * <li>it starts with {@code contract} instead of {@code node} and</li>
 * <li>its body can only mention contract items.</li>
 * </ul>
 * To use a contract node one needs to import it through an inline contract.
 */
public class ContractBuilder {
  private String id;
  private List<Parameter> inputs;
  private List<Parameter> outputs;
  private ContractBodyBuilder contractBodyBuilder;

  /**
   * Constructor
   *
   * @param id name of the contract
   */
  public ContractBuilder(String id) {
    this.id = id;
    inputs = new ArrayList<>();
    outputs = new ArrayList<>();
  }

  /**
   * add a constant input parameter
   *
   * @param name name of the input parameter
   * @param type type of the input parameter
   * @return an id-expr for the input parameter
   */
  public IdExpr createConstInput(String name, Type type) {
    this.inputs.add(new Parameter(name, type, true));
    return new IdExpr(name);
  }

  /**
   * add a variable input parameter
   *
   * @param name name of the input parameter
   * @param type type of the input parameter
   * @return an id-expr for the input parameter
   */
  public IdExpr createVarInput(String name, Type type) {
    this.inputs.add(new Parameter(name, type));
    return new IdExpr(name);
  }

  /**
   * add a variable output parameter
   *
   * @param name name of the output parameter
   * @param type type of the output parameter
   * @return an id-expr for the output parameter
   */
  public IdExpr createVarOutput(String name, Type type) {
    this.outputs.add(new Parameter(name, type));
    return new IdExpr(name);
  }

  /**
   * set the contract body for the contract
   *
   * @param contractBodyBuilder a builder for the contract body
   */
  public void setContractBody(ContractBodyBuilder contractBodyBuilder) {
    this.contractBodyBuilder = contractBodyBuilder;
  }

  /**
   * construct a contract
   *
   * @return constructed contract
   */
  Contract buildContract() {
    return new Contract(id, inputs, outputs, contractBodyBuilder.build());
  }
}
