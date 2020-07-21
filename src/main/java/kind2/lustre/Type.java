package kind2.lustre;

import kind2.Assert;

public abstract class Type {
	public final Location location;

	protected Type(Location location) {
		Assert.isNotNull(location);
		this.location = location;
	}
}
