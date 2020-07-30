package kind2.lustre;

import kind2.Assert;

public class NamedType extends Type {
	public final String name;

	public NamedType(Location location, String name) {
		super(location);
		Assert.isNotNull(name);
		Assert.isFalse(name.equals(BOOL.toString()));
		Assert.isFalse(name.equals(INT.toString()));
		Assert.isFalse(name.equals(REAL.toString()));

		Assert.isFalse(name.equals(INT8.toString()));
		Assert.isFalse(name.equals(INT16.toString()));
		Assert.isFalse(name.equals(INT32.toString()));
		Assert.isFalse(name.equals(INT64.toString()));

		Assert.isFalse(name.equals(UINT8.toString()));
		Assert.isFalse(name.equals(UINT16.toString()));
		Assert.isFalse(name.equals(UINT32.toString()));
		Assert.isFalse(name.equals(UINT64.toString()));

		this.name = name;
	}

	public NamedType(String name) {
		this(Location.NULL, name);
	}

	/*
	 * Private constructor for built-in types
	 */
	private NamedType(String name, @SuppressWarnings("unused") Object unused) {
		super(Location.NULL);
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static final NamedType BOOL = new NamedType("bool", null);
	public static final NamedType INT = new NamedType("int", null);
	public static final NamedType REAL = new NamedType("real", null);

	public static final NamedType INT8 = new NamedType("int8", null);
	public static final NamedType INT16 = new NamedType("int16", null);
	public static final NamedType INT32 = new NamedType("int32", null);
	public static final NamedType INT64 = new NamedType("int64", null);

	public static final NamedType UINT8 = new NamedType("uint8", null);
	public static final NamedType UINT16 = new NamedType("uint16", null);
	public static final NamedType UINT32 = new NamedType("uint32", null);
	public static final NamedType UINT64 = new NamedType("uint64", null);

	public static NamedType get(String name) {
		switch (name) {
			case "int":
				return NamedType.INT;
			case "real":
				return NamedType.REAL;
			case "bool":
				return NamedType.BOOL;
			case "int8":
				return NamedType.INT8;
			case "int16":
				return NamedType.INT16;
			case "int32":
				return NamedType.INT32;
			case "int64":
				return NamedType.INT64;
			case "uint8":
				return NamedType.UINT8;
			case "uint16":
				return NamedType.UINT16;
			case "uint32":
				return NamedType.UINT32;
			case "uint64":
				return NamedType.UINT64;
			default:
				return new NamedType(name);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof NamedType)) {
			return false;
		}
		NamedType other = (NamedType) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
