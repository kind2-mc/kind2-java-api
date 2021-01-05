/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.Arrays;
import java.util.List;

import edu.uiowa.cs.clc.kind2.Assert;

/**
 * This class represents a mode reference. A mode reference is a Lustre expression of type
 * {@code bool} just like any other Boolean expression. It can appear under a {@code pre}, be used
 * in a node call or a contract import, etc. It is only legal <b>after</b> the mode item itself.
 * That is, no forward/self-references are allowed.
 */
class ModeRefExpr extends Expr {
  final List<String> path;

  /**
   * Constructor
   *
   * @param path path to the mode
   */
  ModeRefExpr(String... path) {
    Assert.isFalse(path.length == 0);
    for (String id : path) {
      Assert.isNotNull(id);
    }
    this.path = Arrays.asList(path);
  }

}
