/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import edu.uiowa.cs.clc.kind2.Assert;

public class IdExpr extends Expr {
  final String id;

  IdExpr(String id) {
    Assert.isNotNull(id);
    this.id = id;
  }
}
