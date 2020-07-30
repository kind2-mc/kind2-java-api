package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

/**
 * A mode {@code (R,E)} is a set of requires {@code R} and a set of ensures
 * {@code E}. Requires have the same restrictions as assumptions: they cannot
 * mention outputs of the node they specify in the current state. Ensures, like
 * guarantees, have no restriction.
 */
public class Mode extends ContractItem {
	public final String id;
	public final List<Expr> require;
	public final List<Expr> ensure;

	/**
	 * Constructor
	 *
	 * @param location location of mode in a Lustre file
	 * @param id       name of this mode
	 * @param require  a list of requirements for this mode
	 * @param ensure   a list of constraints that express behavior in this mode
	 */
	public Mode(Location location, String id, List<Expr> require, List<Expr> ensure) {
		super(location);
		Assert.isNotNull(id);
		this.id = id;
		this.require = Util.safeList(require);
		this.ensure = Util.safeList(ensure);
	}

	/**
	 * Constructor
	 *
	 * @param id      name of mode
	 * @param require a list of requirements for this mode
	 * @param ensure  a list of constraints that express behavior in this mode
	 */
	public Mode(String id, List<Expr> require, List<Expr> ensure) {
		this(Location.NULL, id, require, ensure);
	}
}
