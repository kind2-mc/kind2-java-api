/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.ArrayList;
import java.util.List;

public class ContractBuilder extends ContractBodyBuilder {
  private String id;
  private List<Parameter> inputs;
  private List<Parameter> outputs;


  public ContractBuilder(String id) {
    this.id = id;
    inputs = new ArrayList<>();
    outputs = new ArrayList<>();
  }

  public IdExpr createConstInput(String name, Type type) {
    this.inputs.add(new Parameter(name, type, true));
    return new IdExpr(name);
  }

  public IdExpr createVarInput(String name, Type type) {
    this.inputs.add(new Parameter(name, type));
    return new IdExpr(name);
  }

  public IdExpr createVarOutput(String name, Type type) {
    this.outputs.add(new Parameter(name, type));
    return new IdExpr(name);
  }

  /**
   * construct a contract
   *
   * @return constructed contract
   */
  Contract buildContract() {
    return new Contract(id, inputs, outputs, build());
  }
}
