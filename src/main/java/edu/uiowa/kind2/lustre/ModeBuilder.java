/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.ArrayList;
import java.util.List;
import edu.uiowa.kind2.Assert;

/**
 * This class provides helper functions for constructing a mode. A mode {@code (R,E)} is a set of
 * requires {@code R} and a set of ensures {@code E}. Requires have the same restrictions as
 * assumptions: they cannot mention outputs of the node they specify in the current state. Ensures,
 * like guarantees, have no restriction.
 */
public class ModeBuilder {
  private String id;
  private List<Require> require;
  private List<Ensure> ensure;

  /**
   * Constructor
   *
   * @param id name of the the contract mode
   */
  public ModeBuilder(String id) {
    Assert.isNotNull(id);
    this.id = id;
    this.require = new ArrayList<>();
    this.ensure = new ArrayList<>();
  }

  /**
   * add a mode require
   *
   * @param expr an expression representing a constraint
   */
  public void require(Expr expr) {
    this.require.add(new Require(null, expr));
  }

  /**
   * add a mode require
   *
   * @param name name of the mode require
   * @param expr an expression representing a constraint
   */
  public void require(String name, Expr expr) {
    this.require.add(new Require(name, expr));
  }

  /**
   * add a mode ensure
   *
   * @param expr an expression representing a constraint
   */
  public void ensure(Expr expr) {
    this.ensure.add(new Ensure(null, expr));
  }

  /**
   * add a mode ensure
   *
   * @param name name of the mode ensure
   * @param expr an expression representing a constraint
   */
  public void ensure(String name, Expr expr) {
    this.ensure.add(new Ensure(name, expr));
  }

  /**
   * construct a mode
   *
   * @return constructed mode
   */
  Mode build() {
    return new Mode(id, require, ensure);
  }
}
