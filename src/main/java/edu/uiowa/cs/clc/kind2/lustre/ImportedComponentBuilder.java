/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an {@code imported} function/node. An imported function/node does not have
 * a body {@code (let ... tel)}. In a Lustre compiler, this is usually used to encode a C function
 * or more generally a call to an external library.
 * <p>
 * In Kind 2, this means that the function/node is always abstract in the contract sense. It can
 * never be refined, and is always abstracted by its contract. If none is given, then the implicit
 * (rather weak) contract {@code (*@contract assume true; guarantee true; *)} is used.
 * <p>
 * In a modular analysis, imported functions/nodes will not be analyzed, although if their contract
 * has modes they will be checked for exhaustiveness, consistently with the usual Kind 2 contract
 * workflow.
 */
public class ImportedComponentBuilder {
  private String id;
  private List<Parameter> inputs;
  private List<Parameter> outputs;
  private ContractBody contractBody;

  /**
   * Constructor
   *
   * @param id name of the imported component
   */
  public ImportedComponentBuilder(String id) {
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
   * set the contract body for the imported component
   *
   * @param contractBodyBuilder a builder for the contract body
   */
  public void setContractBody(ContractBodyBuilder contractBodyBuilder) {
    this.contractBody = contractBodyBuilder.build();
  }

  /**
   * construct an imported component
   *
   * @return constructed imported component
   */
  ImportedComponent build() {
    return new ImportedComponent(id, inputs, outputs, contractBody);
  }
}
