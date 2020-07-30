package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class IfThenElseExpr extends Expr {
	public final Expr cond;
	public final Expr thenExpr;
	public final Expr elseExpr;

	public IfThenElseExpr(Location location, Expr cond, Expr thenExpr, Expr elseExpr) {
		super(location);
		Assert.isNotNull(cond);
		Assert.isNotNull(thenExpr);
		Assert.isNotNull(elseExpr);
		this.cond = cond;
		this.thenExpr = thenExpr;
		this.elseExpr = elseExpr;
	}

	public IfThenElseExpr(Expr cond, Expr thenExpr, Expr elseExpr) {
		this(Location.NULL, cond, thenExpr, elseExpr);
	}
}
