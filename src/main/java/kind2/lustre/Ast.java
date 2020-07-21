package kind2.lustre;

import kind2.Assert;
import kind2.lustre.visitors.PrettyPrintVisitor;

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
