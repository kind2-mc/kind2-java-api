/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.ArrayList;
import java.util.List;

public class ImportedComponentBuilder {
  private String id;
  private List<Parameter> inputs = new ArrayList<>();
  private List<Parameter> outputs = new ArrayList<>();
  private ContractBody contractBody = null;

  public ImportedComponentBuilder(String id) {
    this.id = id;
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

  public void setContractBody(ContractBodyBuilder contractBodyBuilder) {
    this.contractBody = contractBodyBuilder.build();
  }

  ImportedComponent build() {
    return new ImportedComponent(id, inputs, outputs, contractBody);
  }
}
