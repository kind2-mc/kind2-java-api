/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class Constant extends ContractItem {
  final String id;
  final Type type;
  final Expr expr;

  Constant(String id, Type type, Expr expr) {
    Assert.isNotNull(id);
    // 'type' can be null
    Assert.isNotNull(expr);
    this.id = id;
    this.type = type;
    this.expr = expr;
  }
}
