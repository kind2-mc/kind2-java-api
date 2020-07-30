package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

/**
 * This class represents a function. Kind 2 supports the {@code function}
 * keyword which is used just like the {@code node} one but has slightly
 * different semantics. Like the name suggests, the output(s) of a function
 * should be a <i>non-temporal</i> combination of its inputs. That is, a
 * function cannot use the {@code ->}, {@code pre}, {@code merge}, {@code when},
 * {@code condact}, or {@code activate} operators. A function is also not
 * allowed to call a node, only other functions. In Lustre terms, functions are
 * stateless.
 * <p>
 * In Kind 2, these restrictions extend to the contract attached to the
 * function, if any. Note that besides the ones mentioned here, no additional
 * restrictions are enforced on functions compared to nodes.
 */
public class Function extends Ast {
	public final String id;
	public final List<VarDecl> inputs;
	public final List<VarDecl> outputs;
	public final ContractBody contractBody; // Nullable
	public final List<VarDecl> locals;
	public final List<Equation> equations;
	public final List<String> properties;
	public final List<Expr> assertions;

	/**
	 * Constructor
	 *
	 * @param location     location of imported function in a Lustre file
	 * @param id           name of this function
	 * @param inputs       inputs to this function
	 * @param outputs      outputs of this function
	 * @param contractBody an inline contract
	 * @param locals       local variables of this function
	 * @param equations    equations relating inputs and locals to other locals and
	 *                     outputs
	 * @param assertions   assumptions made on this function
	 * @param properties   properties to check on this function
	 */
	public Function(Location location, String id, List<VarDecl> inputs, List<VarDecl> outputs,
			ContractBody contractBody, List<VarDecl> locals, List<Equation> equations, List<Expr> assertions,
			List<String> properties) {
		super(location);
		Assert.isNotNull(id);
		this.id = id;
		this.inputs = Util.safeList(inputs);
		this.outputs = Util.safeList(outputs);
		this.contractBody = contractBody;
		this.locals = Util.safeList(locals);
		this.equations = Util.safeList(equations);
		this.assertions = Util.safeList(assertions);
		this.properties = Util.safeList(properties);
	}

	/**
	 * Constructor
	 *
	 * @param id           name of this function
	 * @param inputs       inputs to this function
	 * @param outputs      outputs of this function
	 * @param contractBody an inline contract
	 * @param locals       local variables of this function
	 * @param equations    equations relating inputs and locals to other locals and
	 *                     outputs
	 * @param assertions   assumptions made on this function
	 * @param properties   properties to check on this function
	 */
	public Function(String id, List<VarDecl> inputs, ContractBody contractBody, List<VarDecl> outputs,
			List<VarDecl> locals, List<Equation> equations, List<Expr> assertions, List<String> properties) {
		this(Location.NULL, id, inputs, outputs, contractBody, locals, equations, assertions, properties);
	}
}
