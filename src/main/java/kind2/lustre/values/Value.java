package kind2.lustre.values;

import kind2.lustre.BinaryOp;
import kind2.lustre.UnaryOp;

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
