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

  /**
   * Constructor
   *
   * @param id name of the component
   */
  public ComponentBuilder(String id) {
    this.id = id;
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
   * define a symbolic local constant
   *
   * @param name name of the local constant
   * @param type type of the local constant
   * @return an id-expr for the local constant
   */
  public IdExpr createLocalConst(String name, Type type) {
    this.localConsts.add(new Constant(name, type, null));
    return new IdExpr(name);
  }

  /**
   * define a local constant
   *
   * @param name name of the local constant
   * @param expr definition of the local constant
   * @return an id-expr for the local constant
   */
  public IdExpr createLocalConst(String name, Expr expr) {
    this.localConsts.add(new Constant(name, null, expr));
    return new IdExpr(name);
  }

  /**
   * define a local constant
   *
   * @param name name of the local constant
   * @param type type of the local constant
   * @param expr definition of the local constant
   * @return an id-expr for the local constant
   */
  public IdExpr createLocalConst(String name, Type type, Expr expr) {
    this.localConsts.add(new Constant(name, type, expr));
    return new IdExpr(name);
  }

  /**
   * declare a local variable
   *
   * @param name name of the local variable
   * @param type type of the local variable
   * @return an id-expr for the local variable
   */
  public IdExpr createLocalVar(String name, Type type) {
    this.localVars.add(new VarDecl(name, type));
    return new IdExpr(name);
  }

  /**
   * add an equation
   *
   * @param var  lhs of the equation
   * @param expr rhs of the equation
   */
  public void addEquation(IdExpr var, Expr expr) {
    this.equations.add(new Equation(var, expr));
  }

  /**
   * add an equation
   *
   * @param vars lhs of the equation
   * @param expr rhs of the equation
   */
  public void addEquation(List<IdExpr> vars, Expr expr) {
    this.equations.add(new Equation(vars, expr));
  }

  /**
   * add a property for Kind2 to check
   *
   * @param name name of the property
   * @param expr the property to check
   */
  public void addProperty(String name, Expr expr) {
    this.properties.add(new Property(name, expr));
  }

  /**
   * add a property for Kind2 to check
   *
   * @param expr the property to check
   */
  public void addProperty(Expr expr) {
    this.properties.add(new Property(null, expr));
  }

  /**
   * add an assertion
   *
   * @param expr the assertion to add
   */
  public void addAssertion(Expr assertion) {
    this.assertions.add(assertion);
  }

  /**
   * set the contract body for the component
   *
   * @param contractBodyBuilder a builder for the contract body
   */
  public void setContractBody(ContractBodyBuilder contractBodyBuilder) {
    this.contractBody = contractBodyBuilder.build();
  }

  /**
   * construct a component from this builder
   *
   * @return constructed component
   */
  Component build() {
    return new Component(id, inputs, outputs, contractBody, localConsts, localVars, equations,
        assertions, properties);
  }
}
