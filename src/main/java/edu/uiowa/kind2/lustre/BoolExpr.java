/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class BoolExpr extends Expr {
	public final boolean value;

	public BoolExpr(Location location, boolean value) {
		super(location);
		Assert.isNotNull(value);
		this.value = value;
	}

	public BoolExpr(boolean value) {
		this(Location.NULL, value);
	}
}
