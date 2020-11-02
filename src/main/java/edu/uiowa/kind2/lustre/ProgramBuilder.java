/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class provides helper functions for constructing a lustre program.
 */
public class ProgramBuilder {
  private List<TypeDef> types = new ArrayList<>();
  private List<Constant> constants = new ArrayList<>();
  private List<Component> importedFunctions = new ArrayList<>();
  private List<Component> importedNodes = new ArrayList<>();
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
   * add a type definition
   *
   * @param type the type definition to add
   * @return this program builder
   */
  public ProgramBuilder addType(TypeDef type) {
    this.types.add(type);
    return this;
  }

  /**
   * add type definitions
   *
   * @param types a collection of type definitions to add
   * @return this program builder
   */
  public ProgramBuilder addTypes(Collection<TypeDef> types) {
    this.types.addAll(types);
    return this;
  }

  /**
   * remove all type definitions
   *
   * @return this program builder
   */
  public ProgramBuilder clearTypes() {
    this.types.clear();
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
   * @param functionBuilder a builder for the function to add
   * @return this program builder
   */
  public ProgramBuilder importFunction(FunctionBuilder functionBuilder) {
    this.importedFunctions.add(functionBuilder.build());
    return this;
  }

  /**
   * add a function
   *
   * @param nodeBuilder a builder for the function to add
   * @return this program builder
   */
  public ProgramBuilder addFunction(FunctionBuilder functionBuilder) {
    this.functions.add(functionBuilder.build());
    return this;
  }

  /**
   * add a node
   *
   * @param nodeBuilder a builder for the node to add
   * @return this program builder
   */
  public ProgramBuilder importNode(NodeBuilder nodeBuilder) {
    this.importedNodes.add(nodeBuilder.build());
    return this;
  }

  /**
   * add a node
   *
   * @param nodeBuilder a builder for the node to add
   * @return this program builder
   */
  public ProgramBuilder addNode(NodeBuilder nodeBuilder) {
    this.nodes.add(nodeBuilder.build());
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
