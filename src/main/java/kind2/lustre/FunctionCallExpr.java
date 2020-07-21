package kind2.lustre;

import java.util.Arrays;
import java.util.List;

import kind2.Assert;
import kind2.util.Util;

public class FunctionCallExpr extends Expr {
	public final String function;
	public final List<Expr> args;

	public FunctionCallExpr(Location loc, String function, List<Expr> args) {
		super(loc);
		Assert.isNotNull(function);
		this.function = function;
		this.args = Util.safeList(args);
	}

	public FunctionCallExpr(String node, List<Expr> args) {
		this(Location.NULL, node, args);
	}

	public FunctionCallExpr(String node, Expr... args) {
		this(Location.NULL, node, Arrays.asList(args));
	}
}