package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.util.Util;

public class ArrayExpr extends Expr {
	public final List<Expr> elements;

	public ArrayExpr(Location loc, List<Expr> elements) {
		super(loc);
		this.elements = Util.safeList(elements);
	}

	public ArrayExpr(List<Expr> elements) {
		this(Location.NULL, elements);
	}

}
