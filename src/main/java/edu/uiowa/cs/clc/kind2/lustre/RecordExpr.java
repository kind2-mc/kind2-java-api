/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.Map;
import java.util.SortedMap;

import edu.uiowa.cs.clc.kind2.Assert;
import edu.uiowa.cs.clc.kind2.util.Util;

class RecordExpr extends Expr {
  final String id;
  final SortedMap<String, Expr> fields;

  RecordExpr(String id, Map<String, Expr> fields) {
    Assert.isNotNull(id);
    Assert.isNotNull(fields);
    Assert.isTrue(fields.size() > 0);
    this.id = id;
    this.fields = Util.safeStringSortedMap(fields);
  }
}
