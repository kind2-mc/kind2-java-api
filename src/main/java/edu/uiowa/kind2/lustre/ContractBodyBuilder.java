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
   * Create a ghost constant. A ghost constant is a stream that is local to the contract. That is,
   * it is not accessible from the body of the node specified.
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
   * Create a ghost constant. A ghost constant is a stream that is local to the contract. That is,
   * it is not accessible from the body of the node specified.
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
   * Create a ghost constant. A ghost constant is a stream that is local to the contract. That is,
   * it is not accessible from the body of the node specified.
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
   * Create a ghost variable. A ghost variable is a stream that is local to the contract. That is,
   * it is not accessible from the body of the node specified.
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
   * Add an assumption. An assumption over a node {@code n} is a constraint one must respect in
   * order to use {@code n} legally. It cannot mention the outputs of {@code n} in the current
   * state, but referring to outputs under a {@code pre} is fine.
   *
   * @param expr an expression representing a constraint
   * @return this contract body builder
   */
  public void assume(Expr expr) {
    this.items.add(new Assume(false, null, expr));
  }

  /**
   * Add an assumption. An assumption over a node {@code n} is a constraint one must respect in
   * order to use {@code n} legally. It cannot mention the outputs of {@code n} in the current
   * state, but referring to outputs under a {@code pre} is fine.
   *
   * @param name name of the assumption
   * @param expr an expression representing a constraint
   * @return this contract body builder
   */
  public void assume(String name, Expr expr) {
    this.items.add(new Assume(false, name, expr));
  }

  /**
   * Add a a weakly assume. An assumption over a node {@code n} is a constraint one must respect in
   * order to use {@code n} legally. It cannot mention the outputs of {@code n} in the current
   * state, but referring to outputs under a {@code pre} is fine. Use this function if you are
   * interested in computing an IVC among a subset of the assumptions.
   *
   * @param expr an expression representing a constraint
   * @return this contract body builder
   */
  public void weaklyAssume(Expr expr) {
    this.items.add(new Assume(true, null, expr));
  }

  /**
   * Add a weakly assume. An assumption over a node {@code n} is a constraint one must respect in
   * order to use {@code n} legally. It cannot mention the outputs of {@code n} in the current
   * state, but referring to outputs under a {@code pre} is fine. Use this function if you are
   * interested in computing an IVC among a subset of the assumptions.
   *
   * @param name name of the assumption
   * @param expr an expression representing a constraint
   * @return this contract body builder
   */
  public void weaklyAssume(String name, Expr expr) {
    this.items.add(new Assume(true, name, expr));
  }

  /**
   * Add a guarantee. Unlike assumptions, guarantees do not have any restrictions on the streams
   * they can mention. They typically mention the outputs in the current state since they express
   * the behavior of the node they specified under the assumptions of this node.
   *
   * @param expr constraint expressing the behavior of a node
   * @return this contract body builder
   */
  public void guarantee(Expr expr) {
    this.items.add(new Guarantee(false, null, expr));
  }

  /**
   * Add a guarantee. Unlike assumptions, guarantees do not have any restrictions on the streams
   * they can mention. They typically mention the outputs in the current state since they express
   * the behavior of the node they specified under the assumptions of this node.
   *
   * @param name name of the guarantee
   * @param expr constraint expressing the behavior of a node
   * @return this contract body builder
   */
  public void guarantee(String name, Expr expr) {
    this.items.add(new Guarantee(false, name, expr));
  }

  /**
   * Add a guarantee. Unlike assumptions, guarantees do not have any restrictions on the streams
   * they can mention. They typically mention the outputs in the current state since they express
   * the behavior of the node they specified under the assumptions of this node. Use this function
   * if you are interested in computing an IVC among a subset of the guarantees.
   *
   * @param expr constraint expressing the behavior of a node
   * @return this contract body builder
   */
  public void weaklyGuarantee(Expr expr) {
    this.items.add(new Guarantee(true, null, expr));
  }

  /**
   * Add a weakly guarantee. Unlike assumptions, guarantees do not have any restrictions on the
   * streams they can mention. They typically mention the outputs in the current state since they
   * express the behavior of the node they specified under the assumptions of this node. Use this
   * function if you are interested in computing an IVC among a subset of the guarantees.
   *
   * @param name name of the guarantee
   * @param expr constraint expressing the behavior of a node
   * @return this contract body builder
   */
  public void weaklyGuarantee(String name, Expr expr) {
    this.items.add(new Guarantee(true, name, expr));
  }

  /**
   * Add a mode
   *
   * @param modeBuilder a mode builder
   */
  public void addMode(ModeBuilder modeBuilder) {
    this.items.add(modeBuilder.build());
  }

  /**
   * Import a contract. A contract import merges the current contract with the one imported. That
   * is, if the current contract is {@code (A,G,M)} and we import {@code (A',G',M')}, the resulting
   * contract is {@code (A U A', G U G', M U M')} where U is set union.
   * <p>
   * When importing a contract, it is necessary to specify how the instantiation of the contract is
   * performed. This defines a mapping from the input (output) formal parameters to the actual ones
   * of the import.
   * <p>
   * When importing contract {@code c} in the contract of node {@code n}, it is <b>illegal</b> to
   * mention an output of {@code n} in the actual input parameters of the import of {@code c}.
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
   * Construct a contract body.
   *
   * @return constructed contract body
   */
  ContractBody build() {
    return new ContractBody(items);
  }
}
