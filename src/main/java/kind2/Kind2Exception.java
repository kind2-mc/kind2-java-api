package kind2;

/**
 * An exception generated from Kind 2 or the Kind 2 API
 */
public class Kind2Exception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public Kind2Exception(String message) {
		super(message);
	}

	public Kind2Exception(String message, Throwable t) {
		super(message, t);
	}
}
