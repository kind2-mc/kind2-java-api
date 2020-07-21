package kind2.lustre;

import kind2.Assert;

public class Constant extends ContractItem {
	public final String id;
	public final Type type;
	public final Expr expr;

	public Constant(Location location, String id, Type type, Expr expr) {
		super(location);
		Assert.isNotNull(id);
		// 'type' can be null
		Assert.isNotNull(expr);
		this.id = id;
		this.type = type;
		this.expr = expr;
	}

	public Constant(String id, Type type, Expr expr) {
		this(Location.NULL, id, type, expr);
	}
}