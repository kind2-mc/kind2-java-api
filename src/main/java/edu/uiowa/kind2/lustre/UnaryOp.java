/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

public enum UnaryOp {
  NEGATIVE("-"), NOT("not"), PRE("pre");

  private String str;

  private UnaryOp(String str) {
    this.str = str;
  }

  @Override
  public String toString() {
    return str;
  }

  public static UnaryOp fromString(String string) {
    for (UnaryOp op : UnaryOp.values()) {
      if (op.toString().equals(string)) {
        return op;
      }
    }
    return null;
  }
}
