/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

/**
 * This class represents a contract mode.
 */
class Mode extends ContractItem {
  final String id;
  final List<Require> require;
  final List<Ensure> ensure;

  /**
   * Constructor
   *
   * @param id      name of this mode
   * @param require a list of requirements for this mode
   * @param ensure  a list of constraints that express behavior in this mode
   */
  Mode(String id, List<Require> require, List<Ensure> ensure) {
    Assert.isNotNull(id);
    this.id = id;
    this.require = Util.safeList(require);
    this.ensure = Util.safeList(ensure);
  }
}
