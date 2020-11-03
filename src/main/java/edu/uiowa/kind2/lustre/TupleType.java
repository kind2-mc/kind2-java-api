/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.util.Util;

class TupleType implements Type {
  final List<Type> types;

  TupleType(List<? extends Type> types) {
    if (types != null && types.size() < 1) {
      throw new IllegalArgumentException("Cannot construct empty tuple type");
    }
    this.types = Util.safeList(types);
  }
}
