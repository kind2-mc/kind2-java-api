package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class BoolExpr extends Expr {
	public final boolean value;

	public BoolExpr(Location location, boolean value) {
		super(location);
		Assert.isNotNull(value);
		this.value = value;
	}

	public BoolExpr(boolean value) {
		this(Location.NULL, value);
	}
}
