/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

class RecordAccessExpr extends Expr {
  final Expr record;
  final String field;

  RecordAccessExpr(Expr record, String field) {
    Assert.isNotNull(record);
    Assert.isNotNull(field);
    this.record = record;
    this.field = field;
  }
}
