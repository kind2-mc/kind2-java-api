/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class TypeDef extends Ast {
  final String id;
  final Type type;

  TypeDef(String id, Type type) {
    Assert.isNotNull(id);
    Assert.isNotNull(type);
    this.id = id;
    this.type = type;
  }
}
