/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

enum UnaryOp {
  NEGATIVE("-"), NOT("not"), PRE("pre");

  private String str;

  private UnaryOp(String str) {
    this.str = str;
  }

  @Override
  public String toString() {
    return str;
  }
}
