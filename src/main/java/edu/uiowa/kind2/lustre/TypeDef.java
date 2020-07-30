package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class TypeDef extends Ast {
	public final String id;
	public final Type type;

	public TypeDef(Location location, String id, Type type) {
		super(location);
		Assert.isNotNull(id);
		Assert.isNotNull(type);
		this.id = id;
		this.type = type;
	}

	public TypeDef(String id, Type type) {
		this(Location.NULL, id, type);
	}
}