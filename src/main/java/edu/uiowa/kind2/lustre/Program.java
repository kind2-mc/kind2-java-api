/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.Arrays;
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
  public final List<TypeDef> types;
  public final List<Constant> constants;
  public final List<ImportedFunction> importedFunctions;
  public final List<ImportedNode> importedNodes;
  public final List<Contract> contracts;
  public final List<Function> functions;
  public final List<Node> nodes;
  public final String main; // Nullable

  /**
   * Constructor
   *
   * @param location          location of assumption in a Lustre file
   * @param types             a collection of type definitions
   * @param constants         a collection of constant definitions
   * @param importedFunctions a collections of Kind 2 imported functions
   * @param importedNodes     a collections of Kind 2 imported nodes
   * @param contracts         a collections of Kind 2 contracts
   * @param functions         a collections of functions
   * @param nodes             a collections of nodes
   * @param main              id of the main node/function
   */
  public Program(Location location, List<TypeDef> types, List<Constant> constants,
      List<ImportedFunction> importedFunctions, List<ImportedNode> importedNodes,
      List<Contract> contracts, List<Function> functions, List<Node> nodes, String main) {
    super(location);
    this.types = Util.safeList(types);
    this.constants = Util.safeList(constants);
    this.importedFunctions = Util.safeList(importedFunctions);
    this.importedNodes = Util.safeList(importedNodes);
    this.contracts = Util.safeList(contracts);
    this.functions = Util.safeList(functions);
    this.nodes = Util.safeList(nodes);
    this.main = main;
  }

  /**
   * Constructor
   *
   * @param nodes an array of nodes
   */
  public Program(Node... nodes) {
    this(Location.NULL, null, null, null, null, null, null, Arrays.asList(nodes), null);
  }
}
