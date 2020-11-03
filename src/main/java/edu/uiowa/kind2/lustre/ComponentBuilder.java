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

public class ComponentBuilder {
  private String id;
  private List<Parameter> inputs = new ArrayList<>();
  private List<Parameter> outputs = new ArrayList<>();
  private ContractBody contractBody = null;
  private List<Constant> localConsts = new ArrayList<>();
  private List<VarDecl> localVars = new ArrayList<>();
  private List<Equation> equations = new ArrayList<>();
  private List<Expr> assertions = new ArrayList<>();
  private List<Property> properties = new ArrayList<>();

  public ComponentBuilder(String id) {
    this.id = id;
  }

  public ComponentBuilder setId(String id) {
    this.id = id;
    return this;
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

  public IdExpr createLocalConst(String name, Type type) {
    this.localConsts.add(new Constant(name, type, null));
    return new IdExpr(name);
  }

  public IdExpr createLocalConst(String name, Expr expr) {
    this.localConsts.add(new Constant(name, null, expr));
    return new IdExpr(name);
  }

  public IdExpr createLocalConst(String name, Type type, Expr expr) {
    this.localConsts.add(new Constant(name, type, expr));
    return new IdExpr(name);
  }

  public IdExpr createLocalVar(String name, Type type) {
    this.localVars.add(new VarDecl(name, type));
    return new IdExpr(name);
  }

  public ComponentBuilder addEquation(IdExpr var, Expr expr) {
    this.equations.add(new Equation(var, expr));
    return this;
  }

  public ComponentBuilder addEquation(List<IdExpr> vars, Expr expr) {
    this.equations.add(new Equation(vars, expr));
    return this;
  }

  public ComponentBuilder addProperty(String name, Expr expr) {
    this.properties.add(new Property(name, expr));
    return this;
  }

  public ComponentBuilder addProperty(Expr expr) {
    this.properties.add(new Property(null, expr));
    return this;
  }

  public ComponentBuilder addAssertion(Expr assertion) {
    this.assertions.add(assertion);
    return this;
  }

  public ComponentBuilder setContractBody(ContractBodyBuilder contractBodyBuilder) {
    this.contractBody = contractBodyBuilder.build();
    return this;
  }

  Component build() {
    return new Component(id, inputs, outputs, contractBody, localConsts, localVars, equations,
        assertions, properties);
  }
}
