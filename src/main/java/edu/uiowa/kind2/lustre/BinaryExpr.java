/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class BinaryExpr extends Expr {
	public final Expr left;
	public final BinaryOp op;
	public final Expr right;

	public BinaryExpr(Location location, Expr left, BinaryOp op, Expr right) {
		super(location);
		Assert.isNotNull(left);
		Assert.isNotNull(op);
		Assert.isNotNull(right);
		this.left = left;
		this.op = op;
		this.right = right;
	}

	public BinaryExpr(Expr left, BinaryOp op, Expr right) {
		this(Location.NULL, left, op, right);
	}
}