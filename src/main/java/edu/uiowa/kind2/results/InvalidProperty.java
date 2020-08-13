/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

import java.util.Collections;
import java.util.List;

import edu.uiowa.kind2.util.Util;

/**
 * An invalid property
 */
public final class InvalidProperty extends Property {
  private final String source;
  private Counterexample cex;
  private List<String> conflicts;

  // public InvalidProperty(String name, String source, Counterexample cex, List<String> conflicts,
  // double runtime) {
  // super(name, runtime);
  // this.source = source;
  // this.conflicts = Util.safeList(conflicts);
  // this.cex = cex;
  // }

  public InvalidProperty(String name, String source, Counterexample cex, List<String> conflicts,
      double runtime) {
    super(name, runtime);
    this.source = source;
    this.conflicts = Util.safeList(conflicts);
    this.cex = cex;
  }

  /**
   * Name of the engine used to find the counterexample (bmc, pdr, ...)
   */
  public String getSource() {
    return source;
  }

  /**
   * Counterexample for the property
   */
  public Counterexample getCounterexample() {
    return cex;
  }

  /**
   * Conflicts (used in realizability analysis)
   */
  public List<String> getConflicts() {
    return conflicts;
  }

  @Override
  public void discardDetails() {
    cex = null;
    conflicts = Collections.emptyList();
  }
}
