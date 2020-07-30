package edu.uiowa.kind2.lustre.values;

import edu.uiowa.kind2.lustre.BinaryOp;
import edu.uiowa.kind2.lustre.UnaryOp;

/**
 * A signal value
 * @see BooleanValue
 * @see IntegerValue
 * @see RealValue
 */
public abstract class Value {
	public abstract Value applyBinaryOp(BinaryOp op, Value right);

	public abstract Value applyUnaryOp(UnaryOp op);
}
