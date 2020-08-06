/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

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
