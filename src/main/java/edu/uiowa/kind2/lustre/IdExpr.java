/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class IdExpr extends Expr {
  public final String id;

  public IdExpr(Location location, String id) {
    super(location);
    Assert.isNotNull(id);
    this.id = id;
  }

  public IdExpr(String id) {
    this(Location.NULL, id);
  }
}
