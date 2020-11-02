/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

class EnumType extends Type {
  final String id;
  final List<String> values;

  EnumType(String id, List<String> values) {
    Assert.isNotNull(id);
    this.id = id;
    this.values = Util.safeList(values);
  }

  String getValue(int i) {
    return values.get(i);
  }

  @Override
  public String toString() {
    return id;
  }
}
