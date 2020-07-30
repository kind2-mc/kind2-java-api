package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class UnaryExpr extends Expr {
	public final UnaryOp op;
	public final Expr expr;

	public UnaryExpr(Location location, UnaryOp op, Expr expr) {
		super(location);
		Assert.isNotNull(op);
		Assert.isNotNull(expr);
		this.op = op;
		this.expr = expr;
	}

	public UnaryExpr(UnaryOp op, Expr expr) {
		this(Location.NULL, op, expr);
	}
}
