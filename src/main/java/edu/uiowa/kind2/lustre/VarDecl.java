/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class VarDecl extends Ast {
	public final String id;
	public final Type type;

	public VarDecl(Location location, String id, Type type) {
		super(location);
		Assert.isNotNull(id);
		Assert.isNotNull(type);
		this.id = id;
		this.type = type;
	}

	public VarDecl(String id, Type type) {
		this(Location.NULL, id, type);
	}
}