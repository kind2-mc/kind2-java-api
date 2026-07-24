/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

/**
 * The verification engines Kind 2 can run.
 * <p>
 * Modules are turned on and off with {@link Kind2Api#enable(Module)} and
 * {@link Kind2Api#disable(Module)}. Several of them normally run in parallel and
 * exchange the invariants they discover.
 */
public enum Module {
  /**
   * The IC3/PDR-style property-directed reachability engine.
   */
  IC3,
  /**
   * The IC3 engine using quantifier elimination to compute pre-images.
   */
  IC3QE,
  /**
   * The IC3 engine using implicit abstraction.
   */
  IC3IA,
  /**
   * The bounded model checking engine, which searches for counterexamples of increasing length.
   */
  BMC,
  /**
   * The k-induction engine.
   */
  IND,
  /**
   * An additional induction-based engine that runs alongside {@link #IND}.
   */
  IND2,
  /**
   * Two-state graph-based invariant generation over Boolean terms.
   */
  INVGEN,
  /**
   * One-state graph-based invariant generation over Boolean terms.
   */
  INVGENOS,
  /**
   * Two-state graph-based invariant generation over integer terms.
   */
  INVGENINT,
  /**
   * One-state graph-based invariant generation over integer terms.
   */
  INVGENINTOS,
  /**
   * Two-state graph-based invariant generation over machine integer terms.
   */
  INVGENMACH,
  /**
   * One-state graph-based invariant generation over machine integer terms.
   */
  INVGENMACHOS,
  /**
   * Two-state graph-based invariant generation over real terms.
   */
  INVGENREAL,
  /**
   * One-state graph-based invariant generation over real terms.
   */
  INVGENREALOS,
  /**
   * The C2I invariant generation engine, which learns candidate invariants.
   */
  C2I,
  /**
   * The Lustre interpreter, which evaluates a program on concrete inputs.
   */
  interpreter,
  /**
   * The engine that computes minimal cut sets.
   */
  MCS,
  /**
   * The engine that checks whether a contract is realizable.
   */
  CONTRACTCK;
}
