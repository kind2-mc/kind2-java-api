/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.Arrays;
import java.util.List;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a mode reference. A mode reference is a Lustre expression of type
 * {@code bool} just like any other Boolean expression. It can appear under a {@code pre}, be used
 * in a node call or a contract import, etc. It is only legal <b>after</b> the mode item itself.
 * That is, no forward/self-references are allowed.
 */
public class ModeRefExpr extends Expr {
  public final List<String> path;

  /**
   * Constructor
   *
   * @param location location of mode reference expression in a Lustre file
   * @param path     path to the mode
   */
  public ModeRefExpr(Location location, String... path) {
    super(location);
    Assert.isFalse(path.length == 0);
    for (String id : path) {
      Assert.isNotNull(id);
    }
    this.path = Arrays.asList(path);
  }

  /**
   * Constructor
   *
   * @param path path to the mode
   */
  public ModeRefExpr(String... path) {
    this(Location.NULL, path);
  }
}
