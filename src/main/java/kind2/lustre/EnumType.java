package kind2.lustre;

import java.util.List;

import kind2.Assert;
import kind2.util.Util;

public class EnumType extends Type {
	public final String id;
	public final List<String> values;

	public EnumType(Location location, String id, List<String> values) {
		super(location);
		Assert.isNotNull(id);
		this.id = id;
		this.values = Util.safeList(values);
	}

	public EnumType(String id, List<String> values) {
		this(Location.NULL, id, values);
	}

	public String getValue(int i) {
		return values.get(i);
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EnumType) {
			EnumType et = (EnumType) obj;
			return id.equals(et.id);
		}
		return false;
	}
}