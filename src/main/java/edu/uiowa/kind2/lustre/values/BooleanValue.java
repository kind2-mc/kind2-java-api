/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre.values;

import edu.uiowa.kind2.lustre.BinaryOp;
import edu.uiowa.kind2.lustre.UnaryOp;

/**
 * A boolean signal value
 */
public class BooleanValue extends Value {
  public final boolean value;

  private BooleanValue(boolean value) {
    this.value = value;
  }

  public static final BooleanValue TRUE = new BooleanValue(true);
  public static final BooleanValue FALSE = new BooleanValue(false);

  public static BooleanValue fromBoolean(boolean value) {
    return value ? TRUE : FALSE;
  }

  @Override
  public Value applyBinaryOp(BinaryOp op, Value right) {

    if (right instanceof UnknownValue) {
      switch (op) {
        case OR:
          return value ? TRUE : UnknownValue.UNKNOWN;

        case AND:
          return !value ? FALSE : UnknownValue.UNKNOWN;

        case IMPLIES:
          return !value ? TRUE : UnknownValue.UNKNOWN;

        default:
          return UnknownValue.UNKNOWN;
      }
    }

    if (!(right instanceof BooleanValue)) {
      return null;
    }
    boolean other = ((BooleanValue) right).value;

    switch (op) {
      case EQUAL:
        return fromBoolean(value == other);
      case NOTEQUAL:
        return fromBoolean(value != other);
      case OR:
        return fromBoolean(value || other);
      case AND:
        return fromBoolean(value && other);
      case XOR:
        return fromBoolean(value != other);
      case IMPLIES:
        return fromBoolean(!value || other);
      default:
        return null;
    }
  }

  @Override
  public Value applyUnaryOp(UnaryOp op) {
    switch (op) {
      case NOT:
        return fromBoolean(!value);
      default:
        return null;
    }
  }

  @Override
  public String toString() {
    return Boolean.toString(value);
  }

  @Override
  public int hashCode() {
    return Boolean.valueOf(value).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BooleanValue) {
      BooleanValue other = (BooleanValue) obj;
      return (value == other.value);
    }
    return false;
  }
}
