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
import edu.uiowa.cs.clc.kind2.Assert;

/**
 * This class provides helper functions for constructing a lustre program.
 */
public class ProgramBuilder {
  private List<TypeDef> types = new ArrayList<>();
  private List<Constant> constants = new ArrayList<>();
  private List<ImportedComponent> importedFunctions = new ArrayList<>();
  private List<ImportedComponent> importedNodes = new ArrayList<>();
  private List<Contract> contracts = new ArrayList<>();
  private List<Component> functions = new ArrayList<>();
  private List<Component> nodes = new ArrayList<>();
  private String main;

  /**
   * Constructor
   */
  public ProgramBuilder() {
  }

  /**
   * define a type
   *
   * @param name name of the type to define
   * @param type the type to define
   * @return the defined type
   */
  public Type defineType(String name, Type type) {
    Assert.isNotNull(name);
    Assert.isNotNull(type);
    this.types.add(new TypeDef(name, type));
    return new NamedType(name);
  }

  /**
   * define a global symbolic constant
   *
   * @param name name of the global constant
   * @param type type of the global constant
   * @return an id-expr for the global constant
   */
  public IdExpr createConst(String name, Type type) {
    Assert.isNotNull(name);
    Assert.isNotNull(type);
    this.constants.add(new Constant(name, type, null));
    return new IdExpr(name);
  }

  /**
   * define a global constant
   *
   * @param name name of the global constant
   * @param expr definition of the global constant
   * @return an id-expr for the global constant
   */
  public IdExpr createConst(String name, Expr expr) {
    Assert.isNotNull(name);
    Assert.isNotNull(expr);
    this.constants.add(new Constant(name, null, expr));
    return new IdExpr(name);
  }

  /**
   * define a global constant
   *
   * @param name name of the global constant
   * @param type type of the global constant
   * @param expr definition of the global constant
   * @return an id-expr for the global constant
   */
  public IdExpr createConst(String name, Type type, Expr expr) {
    Assert.isNotNull(name);
    Assert.isTrue(type != null || expr != null);
    this.constants.add(new Constant(name, type, expr));
    return new IdExpr(name);
  }

  /**
   * add a contract
   *
   * @param contractBuilder builder for the contract to add
   */
  public void addContract(ContractBuilder contractBuilder) {
    this.contracts.add(contractBuilder.buildContract());
  }

  /**
   * import a function
   *
   * @param importedComponentBuilder a builder for the function to import
   */
  public void importFunction(ImportedComponentBuilder importedComponentBuilder) {
    this.importedFunctions.add(importedComponentBuilder.build());
  }

  /**
   * add a function
   *
   * @param componentBuilder a builder for the function to add
   */
  public void addFunction(ComponentBuilder componentBuilder) {
    this.functions.add(componentBuilder.build());
  }

  /**
   * import a node
   *
   * @param importedComponentBuilder a builder for the node to import
   */
  public void importNode(ImportedComponentBuilder importedComponentBuilder) {
    this.importedNodes.add(importedComponentBuilder.build());
  }

  /**
   * add a node
   *
   * @param componentBuilder a builder for the node to add
   */
  public void addNode(ComponentBuilder componentBuilder) {
    this.nodes.add(componentBuilder.build());
  }

  /**
   * set the main node/function
   *
   * @param main name of the main node/function
   */
  public void setMain(String main) {
    this.main = main;
  }

  /**
   * construct a lustre program
   *
   * @return constructed lustre program
   */
  public Program build() {
    return new Program(types, constants, importedFunctions, importedNodes, contracts, functions,
        nodes, main);
  }
}
