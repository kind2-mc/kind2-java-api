package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

/**
 * This class represents a contract node. A contract node is very similar to a
 * traditional Lustre node. The two differences are that
 * <ul>
 * <li>it starts with {@code contract} instead of {@code node} and</li>
 * <li>its body can only mention contract items.</li>
 * </ul>
 * To use a contract node one needs to import it through an inline contract.
 */
public class Contract extends Ast {
	public final String id;
	public final List<VarDecl> inputs;
	public final List<VarDecl> outputs;
	public final ContractBody contractBody;

	/**
	 * Constructor
	 *
	 * @param location     location of contract in a Lustre file
	 * @param id           name of contract
	 * @param inputs       inputs to this contract
	 * @param outputs      outputs of this contract
	 * @param contractBody body of this contract
	 */
	public Contract(Location location, String id, List<VarDecl> inputs, List<VarDecl> outputs,
			ContractBody contractBody) {
		super(location);
		Assert.isNotNull(id);
		this.id = id;
		this.inputs = Util.safeList(inputs);
		this.outputs = Util.safeList(outputs);
		Assert.isNotNull(contractBody);
		this.contractBody = contractBody;
	}

	/**
	 * Constructor
	 *
	 * @param id           name of this contract
	 * @param inputs       inputs to this contract
	 * @param outputs      outputs of this contract
	 * @param contractBody body of this contract
	 */
	public Contract(String id, List<VarDecl> inputs, List<VarDecl> outputs, ContractBody contractBody) {
		this(Location.NULL, id, inputs, outputs, contractBody);
	}
}
