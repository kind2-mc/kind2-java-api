package kind2;

public class Assert {
	public static void isNotNull(Object o) {
		if (o == null) {
			throw new Kind2Exception("Object unexpectedly null");
		}
	}

	public static void isTrue(boolean b) {
		if (!b) {
			throw new Kind2Exception("Assertion failed");
		}
	}

	public static void isFalse(boolean b) {
		if (b) {
			throw new Kind2Exception("Assertion failed");
		}
	}
}
