package edu.uiowa.kind2.lustre;

/**
 * This abstract class represents the items (i.e., statements) that can appear
 * in a contract body.
 */
public abstract class ContractItem extends Ast {
	/**
	 * Constructor
	 *
	 * @param location location of contract item in a Lustre file
	 */
	public ContractItem(Location location) {
		super(location);
	}
}
