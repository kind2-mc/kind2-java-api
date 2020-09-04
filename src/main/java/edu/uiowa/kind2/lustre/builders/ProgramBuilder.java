/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uiowa.kind2.lustre.Constant;
import edu.uiowa.kind2.lustre.Contract;
import edu.uiowa.kind2.lustre.ImportedFunction;
import edu.uiowa.kind2.lustre.ImportedNode;
import edu.uiowa.kind2.lustre.Function;
import edu.uiowa.kind2.lustre.Location;
import edu.uiowa.kind2.lustre.Node;
import edu.uiowa.kind2.lustre.Program;
import edu.uiowa.kind2.lustre.TypeDef;

/**
 * This class provides helper functions for constructing a lustre program.
 */
public class ProgramBuilder {
  private List<TypeDef> types = new ArrayList<>();
  private List<Constant> constants = new ArrayList<>();
  private List<ImportedFunction> importedFunctions = new ArrayList<>();
  private List<ImportedNode> importedNodes = new ArrayList<>();
  private List<Contract> contracts = new ArrayList<>();
  private List<Function> functions = new ArrayList<>();
  private List<Node> nodes = new ArrayList<>();
  private String main;

  /**
   * Constructor
   */
  public ProgramBuilder() {
  }

  /**
   * Constructor
   *
   * @param program lustre program to clone
   */
  public ProgramBuilder(Program program) {
    this.types = new ArrayList<>(program.types);
    this.constants = new ArrayList<>(program.constants);
    this.importedFunctions = new ArrayList<>(program.importedFunctions);
    this.importedNodes = new ArrayList<>(program.importedNodes);
    this.contracts = new ArrayList<>(program.contracts);
    this.functions = new ArrayList<>(program.functions);
    this.nodes = new ArrayList<>(program.nodes);
    this.main = program.main;
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

  /**
   * add a constant definition
   *
   * @param constant the constant definition to add
   * @return this program builder
   */
  public ProgramBuilder addConstant(Constant constant) {
    this.constants.add(constant);
    return this;
  }

  /**
   * add constant definitions
   *
   * @param constants a collection of constant definitions to add
   * @return this program builder
   */
  public ProgramBuilder addConstants(Collection<Constant> constants) {
    this.constants.addAll(constants);
    return this;
  }

  /**
   * remove all constant definitions
   *
   * @return this program builder
   */
  public ProgramBuilder clearConstants() {
    this.constants.clear();
    return this;
  }

  /**
   * add an imported function
   *
   * @param importedFunction the imported function to add
   * @return this program builder
   */
  public ProgramBuilder addImportedFunction(ImportedFunction importedFunction) {
    this.importedFunctions.add(importedFunction);
    return this;
  }

  /**
   * add imported functions
   *
   * @param importedFunctions a collection of imported functions to add
   * @return this program builder
   */
  public ProgramBuilder addImportedFunctions(Collection<ImportedFunction> importedFunctions) {
    this.importedFunctions.addAll(importedFunctions);
    return this;
  }

  /**
   * remove all imported functions
   *
   * @return this program builder
   */
  public ProgramBuilder clearImportedFunctions() {
    this.importedFunctions.clear();
    return this;
  }

  /**
   * add an imported node
   *
   * @param importedNode the imported node to add
   * @return this program builder
   */
  public ProgramBuilder addImportedNode(ImportedNode importedNode) {
    this.importedNodes.add(importedNode);
    return this;
  }

  /**
   * add imported nodes
   *
   * @param importedNodes a collection of imported nodes to add
   * @return this program builder
   */
  public ProgramBuilder addImportedNodes(Collection<ImportedNode> importedNodes) {
    this.importedNodes.addAll(importedNodes);
    return this;
  }

  /**
   * remove all imported nodes
   *
   * @return this program builder
   */
  public ProgramBuilder clearImportedNodes() {
    this.importedNodes.clear();
    return this;
  }

  /**
   * add a contract
   *
   * @param contract the contract to add
   * @return this program builder
   */
  public ProgramBuilder addContract(Contract contract) {
    this.contracts.add(contract);
    return this;
  }

  /**
   * add contracts
   *
   * @param contract a collection of contracts to add
   * @return this program builder
   */
  public ProgramBuilder addContracts(Collection<Contract> contracts) {
    this.contracts.addAll(contracts);
    return this;
  }

  /**
   * remove all contracts
   *
   * @return this program builder
   */
  public ProgramBuilder clearContracts() {
    this.contracts.clear();
    return this;
  }

  /**
   * add a function
   *
   * @param function the function to add
   * @return this program builder
   */
  public ProgramBuilder addFunction(Function function) {
    this.functions.add(function);
    return this;
  }

  /**
   * add functions
   *
   * @param function a collection of functions to add
   * @return this program builder
   */
  public ProgramBuilder addFunctions(Collection<Function> functions) {
    this.functions.addAll(functions);
    return this;
  }

  /**
   * remove all functions
   *
   * @return this program builder
   */
  public ProgramBuilder clearFunctions() {
    this.functions.clear();
    return this;
  }

  /**
   * add a node
   *
   * @param node the node to add
   * @return this program builder
   */
  public ProgramBuilder addNode(Node node) {
    this.nodes.add(node);
    return this;
  }

  /**
   * add nodes
   *
   * @param node a collection of nodes to add
   * @return this program builder
   */
  public ProgramBuilder addNodes(Collection<Node> nodes) {
    this.nodes.addAll(nodes);
    return this;
  }

  /**
   * remove all nodes
   *
   * @return this program builder
   */
  public ProgramBuilder clearNodes() {
    this.nodes.clear();
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
    return new Program(Location.NULL, types, constants, importedFunctions, importedNodes, contracts,
        functions, nodes, main);
  }
}
