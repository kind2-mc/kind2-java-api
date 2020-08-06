/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a contract guarantee. Unlike assumptions, guarantees do
 * not have any restrictions on the streams they can mention. They typically
 * mention the outputs in the current state since they express the behavior of
 * the node they specified under the assumptions of this node.
 */
public class Guarantee extends ContractItem {
	public final Expr expr;

	/**
	 * Constructor
	 *
	 * @param location location of guarantee in a Lustre file
	 * @param expr     constraint expressing the behavior of a node
	 */
	public Guarantee(Location location, Expr expr) {
		super(location);
		Assert.isNotNull(expr);
		this.expr = expr;
	}

	/**
	 * Constructor
	 *
	 * @param expr constraint expressing the behavior of a node
	 */
	public Guarantee(Expr expr) {
		this(Location.NULL, expr);
	}
}
