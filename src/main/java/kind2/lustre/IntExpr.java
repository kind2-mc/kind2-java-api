package kind2.lustre;

import java.math.BigInteger;

import kind2.Assert;

public class IntExpr extends Expr {
	public final BigInteger value;

	public IntExpr(Location location, BigInteger value) {
		super(location);
		Assert.isNotNull(value);
		this.value = value;
	}

	public IntExpr(BigInteger value) {
		this(Location.NULL, value);
	}

	public IntExpr(int value) {
		this(Location.NULL, BigInteger.valueOf(value));
	}
}
