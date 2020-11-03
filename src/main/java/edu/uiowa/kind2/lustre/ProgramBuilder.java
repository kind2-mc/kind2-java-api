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
   * @return this program builder
   */
  public ProgramBuilder defineType(String name, Type type) {
    this.types.add(new TypeDef(name, type));
    return this;
  }

  public ProgramBuilder createConst(String name, Type type) {
    this.constants.add(new Constant(name, type, null));
    return this;
  }

  public ProgramBuilder createConst(String name, Expr expr) {
    this.constants.add(new Constant(name, null, expr));
    return this;
  }

  public ProgramBuilder createConst(String name, Type type, Expr expr) {
    this.constants.add(new Constant(name, type, expr));
    return this;
  }

  /**
   * add a contract
   *
   * @param contract the contract to add
   * @return this program builder
   */
  public ProgramBuilder addContract(ContractBuilder contractBuilder) {
    this.contracts.add(contractBuilder.buildContract());
    return this;
  }

  /**
   * import a function
   *
   * @param importedComponentBuilder a builder for the function to import
   * @return this program builder
   */
  public ProgramBuilder importFunction(ImportedComponentBuilder importedComponentBuilder) {
    this.importedFunctions.add(importedComponentBuilder.build());
    return this;
  }

  /**
   * add a function
   *
   * @param componentBuilder a builder for the function to add
   * @return this program builder
   */
  public ProgramBuilder addFunction(ComponentBuilder componentBuilder) {
    this.functions.add(componentBuilder.build());
    return this;
  }

  /**
   * import a node
   *
   * @param importedComponentBuilder a builder for the node to import
   * @return this program builder
   */
  public ProgramBuilder importNode(ImportedComponentBuilder importedComponentBuilder) {
    this.importedNodes.add(importedComponentBuilder.build());
    return this;
  }

  /**
   * add a node
   *
   * @param componentBuilder a builder for the node to add
   * @return this program builder
   */
  public ProgramBuilder addNode(ComponentBuilder componentBuilder) {
    this.nodes.add(componentBuilder.build());
    return this;
  }

  /**
   * set the main node
   *
   * @return this program builder
   */
  public ProgramBuilder setMain(String main) {
    this.main = main;
    return this;
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
