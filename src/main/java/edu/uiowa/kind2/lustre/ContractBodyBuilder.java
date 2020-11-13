/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides helper functions for constructing a contract body which can be embedded in a
 * node's/function's inline contract or an external contract node. The body is composed of items,
 * each of which define
 * <ul>
 * <li>a ghost variable / constant,</li>
 * <li>an assumption,</li>
 * <li>a guarantee,</li>
 * <li>a mode, or</li>
 * <li>an import of a contract node.</li>
 * </ul>
 */
public class ContractBodyBuilder {
  private final List<ContractItem> items;

  /**
   * Constructor
   */
  public ContractBodyBuilder() {
    this.items = new ArrayList<>();
  }

  /**
   * Constructor
   *
   * @param contractBody contract body to clone
   */
  public ContractBodyBuilder(ContractBody contractBody) {
    this.items = new ArrayList<>(contractBody.items);
  }

  /**
   * create a ghost constant
   *
   * @param name name of ghost constant
   * @param type type of ghost constant
   * @return an expression referring to created ghost constant
   */
  public IdExpr createConstant(String name, Type type) {
    this.items.add(new Constant(name, type, null));
    return new IdExpr(name);
  }

  /**
   * create a ghost constant
   *
   * @param name name of ghost constant
   * @param expr expression specifying value assigned to ghost constant
   * @return an expression referring to created ghost constant
   */
  public IdExpr createConstant(String name, Expr expr) {
    this.items.add(new Constant(name, null, expr));
    return new IdExpr(name);
  }

  /**
   * create a ghost constant
   *
   * @param name name of ghost constant
   * @param type type of ghost constant
   * @param expr expression specifying value assigned to ghost constant
   * @return an expression referring to created ghost constant
   */
  public IdExpr createConstant(String name, Type type, Expr expr) {
    this.items.add(new Constant(name, type, expr));
    return new IdExpr(name);
  }

  /**
   * create a ghost variable
   *
   * @param name name of ghost variable
   * @param type type of ghost variable
   * @param expr expression specifying stream of values assigned to ghost variable
   * @return an expression referring to created ghost variable
   */
  public IdExpr createVarDef(String name, Type type, Expr expr) {
    this.items.add(new VarDef(name, type, expr));
    return new IdExpr(name);
  }

  /**
   * add an assumption
   *
   * @param expr an expression representing a constraint
   * @return this contract body builder
   */
  public void addAssumption(Expr expr) {
    this.items.add(new Assume(null, expr));
  }

  /**
   * add an assumption
   *
   * @param name name of the assumption
   * @param expr an expression representing a constraint
   * @return this contract body builder
   */
  public void addAssumption(String name, Expr expr) {
    this.items.add(new Assume(name, expr));
  }

  /**
   * add a guarantee
   *
   * @param expr constraint expressing the behavior of a node
   * @return this contract body builder
   */
  public void addGuarantee(Expr expr) {
    this.items.add(new Guarantee(null, expr));
  }

  /**
   * add a guarantee
   *
   * @param name name of the guarantee
   * @param expr constraint expressing the behavior of a node
   * @return this contract body builder
   */
  public void addGuarantee(String name, Expr expr) {
    this.items.add(new Guarantee(name, expr));
  }

  /**
   * add a mode
   *
   * @param modeBuilder a mode builder
   */
  public void addMode(ModeBuilder modeBuilder) {
    this.items.add(modeBuilder.build());
  }

  /**
   * import a contract
   *
   * @param id      name of contract to import
   * @param inputs  inputs to the contract
   * @param outputs outputs of the contract
   * @return this contract body builder
   */
  public void importContract(String name, List<Expr> inputs, List<IdExpr> outputs) {
    this.items.add(new ContractImport(name, inputs, outputs));
  }

  /**
   * construct a contract body
   *
   * @return constructed contract body
   */
  ContractBody build() {
    return new ContractBody(items);
  }
}
