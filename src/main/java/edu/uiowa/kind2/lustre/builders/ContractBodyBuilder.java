/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uiowa.kind2.lustre.Assume;
import edu.uiowa.kind2.lustre.Constant;
import edu.uiowa.kind2.lustre.ContractBody;
import edu.uiowa.kind2.lustre.ContractImport;
import edu.uiowa.kind2.lustre.ContractItem;
import edu.uiowa.kind2.lustre.Expr;
import edu.uiowa.kind2.lustre.Guarantee;
import edu.uiowa.kind2.lustre.IdExpr;
import edu.uiowa.kind2.lustre.Location;
import edu.uiowa.kind2.lustre.Mode;
import edu.uiowa.kind2.lustre.Type;
import edu.uiowa.kind2.lustre.VarDef;

/**
 * This class provides helper functions for constructing a contract body.
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
   * add a ghost constant
   *
   * @param constant the ghost constant to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addConstant(Constant constant) {
    this.items.add(constant);
    return this;
  }

  /**
   * add ghost constants
   *
   * @param constants a collection of ghost constants to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addConstants(Collection<Constant> constants) {
    this.items.addAll(constants);
    return this;
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
   * add a ghost variable definition
   *
   * @param varDef the ghost variable definition to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addVarDef(VarDef varDef) {
    this.items.add(varDef);
    return this;
  }

  /**
   * add ghost variable definitions
   *
   * @param varDefs a collection of ghost variable definitions to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addVarDefs(Collection<VarDef> varDefs) {
    this.items.addAll(varDefs);
    return this;
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
  public ContractBodyBuilder addAssumption(Expr expr) {
    this.items.add(new Assume(expr));
    return this;
  }

  /**
   * add an assumption
   *
   * @param assumption assumption to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addAssumption(Assume assumption) {
    this.items.add(assumption);
    return this;
  }

  /**
   * add assumptions
   *
   * @param assumptions a collection of assumptions to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addAssumptions(Collection<Assume> assumptions) {
    this.items.addAll(assumptions);
    return this;
  }

  /**
   * add a guarantee
   *
   * @param expr constraint expressing the behavior of a node
   * @return this contract body builder
   */
  public ContractBodyBuilder addGuarantee(Expr expr) {
    this.items.add(new Guarantee(expr));
    return this;
  }

  /**
   * add a guarantee
   *
   * @param guarantee guarantee to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addGuarantee(Guarantee guarantee) {
    this.items.add(guarantee);
    return this;
  }

  /**
   * add guarantees
   *
   * @param guarantees a collection of guarantees to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addGuarantees(Collection<Guarantee> guarantees) {
    this.items.addAll(guarantees);
    return this;
  }

  /**
   * add a mode
   *
   * @param id      name of this mode
   * @param require a list of requirements for this mode
   * @param ensure  a list of constraints that express behavior in this mode
   * @return this contract body builder
   */
  public ContractBodyBuilder addMode(String id, List<Expr> require, List<Expr> ensure) {
    this.items.add(new Mode(id, require, ensure));
    return this;
  }

  /**
   * add a mode
   *
   * @param mode mode to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addMode(Mode mode) {
    this.items.add(mode);
    return this;
  }

  /**
   * add modes
   *
   * @param modes a collection of modes to add
   * @return this contract body builder
   */
  public ContractBodyBuilder addModes(Collection<Mode> modes) {
    this.items.addAll(modes);
    return this;
  }

  /**
   * import a contract
   *
   * @param id      name of contract to import
   * @param inputs  inputs to the contract
   * @param outputs outputs of the contract
   * @return this contract body builder
   */
  public ContractBodyBuilder importContract(String name, List<Expr> inputs, List<IdExpr> outputs) {
    this.items.add(new ContractImport(name, inputs, outputs));
    return this;
  }

  /**
   * import a contract
   *
   * @param contract contract to import
   * @return this contract body builder
   */
  public ContractBodyBuilder addImport(ContractImport contract) {
    this.items.add(contract);
    return this;
  }

  /**
   * import contracts
   *
   * @param contracts a collection of contracts to import
   * @return this contract body builder
   */
  public ContractBodyBuilder addImports(Collection<ContractImport> contracts) {
    this.items.addAll(contracts);
    return this;
  }

  /**
   * construct a contract body
   *
   * @return constructed contract body
   */
  public ContractBody build() {
    return new ContractBody(Location.NULL, items);
  }
}
