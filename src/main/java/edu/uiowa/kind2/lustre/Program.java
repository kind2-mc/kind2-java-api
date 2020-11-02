/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.util.Util;

/**
 * This class represents a lustre program. In Kind 2, a lustre program consists of:
 * <ul>
 * <li>Type definitions</li>
 * <li>Constant definitions</li>
 * <li>Imported functions</li>
 * <li>Imported nodes</li>
 * <li>Contracts</li>
 * <li>Functions</li>
 * <li>Nodes</li>
 * </ul>
 */
public class Program extends Ast {
  final List<TypeDef> types;
  final List<Constant> constants;
  final List<Component> importedFunctions;
  final List<Component> importedNodes;
  final List<Contract> contracts;
  final List<Component> functions;
  final List<Component> nodes;
  final String main; // Nullable

  /**
   * Constructor
   *
   * @param types             a collection of type definitions
   * @param constants         a collection of constant definitions
   * @param importedFunctions a collections of Kind 2 imported functions
   * @param importedNodes     a collections of Kind 2 imported nodes
   * @param contracts         a collections of Kind 2 contracts
   * @param functions         a collections of functions
   * @param nodes             a collections of nodes
   * @param main              id of the main node/function
   */
  Program(List<TypeDef> types, List<Constant> constants, List<Component> importedFunctions,
      List<Component> importedNodes, List<Contract> contracts, List<Component> functions,
      List<Component> nodes, String main) {
    this.types = Util.safeList(types);
    this.constants = Util.safeList(constants);
    this.importedFunctions = Util.safeList(importedFunctions);
    this.importedNodes = Util.safeList(importedNodes);
    this.contracts = Util.safeList(contracts);
    this.functions = Util.safeList(functions);
    this.nodes = Util.safeList(nodes);
    this.main = main;
  }
}
