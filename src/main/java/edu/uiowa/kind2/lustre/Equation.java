/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.Collections;
import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

public class Equation extends Ast {
	public final List<IdExpr> lhs;
	public final Expr expr;

	public Equation(Location location, List<IdExpr> lhs, Expr expr) {
		super(location);
		Assert.isNotNull(expr);
		this.lhs = Util.safeList(lhs);
		this.expr = expr;
	}

	public Equation(Location location, IdExpr id, Expr expr) {
		super(location);
		this.lhs = Collections.singletonList(id);
		this.expr = expr;
	}

	public Equation(List<IdExpr> lhs, Expr expr) {
		this(Location.NULL, lhs, expr);
	}

	public Equation(IdExpr id, Expr expr) {
		this(Location.NULL, id, expr);
	}
}