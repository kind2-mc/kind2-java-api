/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.Map;
import java.util.SortedMap;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

public class RecordExpr extends Expr {
  public final String id;
  public final SortedMap<String, Expr> fields;

  public RecordExpr(Location loc, String id, Map<String, Expr> fields) {
    super(loc);
    Assert.isNotNull(id);
    Assert.isNotNull(fields);
    Assert.isTrue(fields.size() > 0);
    this.id = id;
    this.fields = Util.safeStringSortedMap(fields);
  }

  public RecordExpr(String id, Map<String, Expr> fields) {
    this(Location.NULL, id, fields);
  }
}
