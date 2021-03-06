/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import edu.uiowa.cs.clc.kind2.Assert;

class VarDecl extends Ast {
  final String id;
  final Type type;

  VarDecl(String id, Type type) {
    Assert.isNotNull(id);
    Assert.isNotNull(type);
    this.id = id;
    this.type = type;
  }
}
