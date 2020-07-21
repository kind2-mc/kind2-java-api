package kind2.lustre;

import java.math.BigDecimal;

import kind2.Assert;

public class RealExpr extends Expr {
	public final BigDecimal value;

	public RealExpr(Location location, BigDecimal value) {
		super(location);
		Assert.isNotNull(value);
		this.value = value;
	}

	public RealExpr(BigDecimal value) {
		this(Location.NULL, value);
	}
}
