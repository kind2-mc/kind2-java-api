/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

enum BinaryOp {
  PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"), INT_DIVIDE("div"), MODULUS("mod"), EQUAL(
      "="), NOTEQUAL("<>"), GREATER(">"), LESS("<"), GREATEREQUAL(
          ">="), LESSEQUAL("<="), OR("or"), AND("and"), XOR("xor"), IMPLIES("=>"), ARROW("->"), CARTESIAN("^");

  private String str;

  private BinaryOp(String str) {
    this.str = str;
  }

  @Override
  public String toString() {
    return str;
  }

  static BinaryOp fromString(String string) {
    for (BinaryOp op : BinaryOp.values()) {
      if (op.toString().equals(string)) {
        return op;
      }
    }
    throw new IllegalArgumentException("Unknown binary operator: " + string);
  }
}
