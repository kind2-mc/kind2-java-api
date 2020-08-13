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
 * A signal value
 *
 * @see BooleanValue
 * @see IntegerValue
 * @see RealValue
 */
public abstract class Value {
  public abstract Value applyBinaryOp(BinaryOp op, Value right);

  public abstract Value applyUnaryOp(UnaryOp op);
}
