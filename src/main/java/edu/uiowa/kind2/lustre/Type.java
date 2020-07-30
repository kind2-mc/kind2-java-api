package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public abstract class Type {
	public final Location location;

	protected Type(Location location) {
		Assert.isNotNull(location);
		this.location = location;
	}
}
