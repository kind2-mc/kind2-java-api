/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.lustre.visitors.PrettyPrintVisitor;

public abstract class Ast {
	public final Location location;

	public Ast(Location location) {
		Assert.isNotNull(location);
		this.location = location;
	}

	@Override
	public String toString() {
		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.ast(this);
		return visitor.toString();
	}
}
