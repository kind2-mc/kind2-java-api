package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

/**
 * This class represents a ghost variable definition. A ghost variable is a
 * stream that is local to the contract. That is, it is not accessible from the
 * body of the node specified. Ghost variables are defined with the {@code var}
 * keyword.
 */
public class VarDef extends ContractItem {
	public final VarDecl varDecl;
	public final Expr expr;

	/**
	 * Constructor
	 *
	 * @param location location of ghost variable definition in a Lustre file
	 * @param varDecl  ghost variable's declaration
	 * @param expr     expression specifying stream of values assigned to ghost
	 *                 variable
	 */
	public VarDef(Location location, VarDecl varDecl, Expr expr) {
		super(location);
		this.varDecl = varDecl;
		Assert.isNotNull(expr);
		this.expr = expr;
	}

	/**
	 * Constructor
	 *
	 * @param location location of ghost variable definition in a Lustre file
	 * @param id       name of ghost variable
	 * @param type     type of ghost variable
	 * @param expr     expression specifying stream of values assigned to ghost
	 *                 variable
	 */
	public VarDef(Location location, String id, Type type, Expr expr) {
		this(location, new VarDecl(id, type), expr);
	}

	/**
	 * Constructor
	 *
	 * @param id   name of ghost variable
	 * @param type type of ghost variable
	 * @param expr expression specifying stream of values assigned to ghost variable
	 */
	public VarDef(String id, Type type, Expr expr) {
		this(Location.NULL, id, type, expr);
	}
}
