/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.math.BigDecimal;

import edu.uiowa.kind2.Assert;

public class RealExpr extends Expr {
	public final BigDecimal value;

	public RealExpr(Location location, BigDecimal value) {
		super(location);
		Assert.isNotNull(value);
		this.value = value;
	}

	public RealExpr(BigDecimal value) {
		this(Location.NULL, value);
	}
}
