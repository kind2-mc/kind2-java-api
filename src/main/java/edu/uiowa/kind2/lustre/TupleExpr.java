/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.util.Util;

public class TupleExpr extends Expr {
	public final List<Expr> elements;

	public TupleExpr(Location loc, List<? extends Expr> elements) {
		super(loc);
		if (elements != null && elements.size() == 1) {
			throw new IllegalArgumentException("Cannot construct singleton tuple");
		}
		this.elements = Util.safeList(elements);
	}

	public TupleExpr(List<? extends Expr> elements) {
		this(Location.NULL, elements);
	}

	public static Expr compress(List<? extends Expr> exprs) {
		if (exprs.size() == 1) {
			return exprs.get(0);
		}
		return new TupleExpr(exprs);
	}
}
