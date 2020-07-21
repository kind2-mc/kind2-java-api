package kind2.lustre;

import kind2.Assert;

public class ArrayUpdateExpr extends Expr {
	public final Expr array;
	public final Expr index;
	public final Expr value;

	public ArrayUpdateExpr(Location location, Expr array, Expr index, Expr value) {
		super(location);
		Assert.isNotNull(array);
		Assert.isNotNull(index);
		Assert.isNotNull(value);
		this.array = array;
		this.index = index;
		this.value = value;
	}

	public ArrayUpdateExpr(Expr array, Expr index, Expr value) {
		this(Location.NULL, array, index, value);
	}
}