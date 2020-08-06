/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a contract assumption. An assumption over a node
 * {@code n} is a constraint one must respect in order to use {@code n} legally.
 * It cannot mention the outputs of {@code n} in the current state, but
 * referring to outputs under a {@code pre} is fine.
 */
public class Assume extends ContractItem {
	public final Expr expr;

	/**
	 * Constructor
	 *
	 * @param location location of assumption in a Lustre file
	 * @param expr     an expression representing a constraint
	 */
	public Assume(Location location, Expr expr) {
		super(location);
		Assert.isNotNull(expr);
		this.expr = expr;
	}

	/**
	 * Constructor
	 *
	 * @param expr an expression representing a constraint
	 */
	public Assume(Expr expr) {
		this(Location.NULL, expr);
	}
}
