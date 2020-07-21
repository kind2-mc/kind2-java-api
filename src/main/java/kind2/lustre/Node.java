package kind2.lustre;

import java.util.List;

import kind2.Assert;
import kind2.util.Util;

public class Node extends Ast {
	public final String id;
	public final List<VarDecl> inputs;
	public final List<VarDecl> outputs;
	public final List<VarDecl> locals;
	public final List<Equation> equations;
	public final List<String> properties;
	public final List<Expr> assertions;
	public final List<String> ivc;
	public final List<String> realizabilityInputs; // Nullable
	public final ContractBody contractBody; // Nullable

	public Node(Location location, String id, List<VarDecl> inputs, List<VarDecl> outputs, List<VarDecl> locals,
			List<Equation> equations, List<String> properties, List<Expr> assertions, List<String> realizabilityInputs,
			ContractBody contractBody, List<String> ivc) {
		super(location);
		Assert.isNotNull(id);
		this.id = id;
		this.inputs = Util.safeList(inputs);
		this.outputs = Util.safeList(outputs);
		this.locals = Util.safeList(locals);
		this.equations = Util.safeList(equations);
		this.properties = Util.safeList(properties);
		this.assertions = Util.safeList(assertions);
		this.ivc = Util.safeList(ivc);
		this.realizabilityInputs = Util.safeNullableList(realizabilityInputs);
		this.contractBody = contractBody;
	}

	public Node(String id, List<VarDecl> inputs, List<VarDecl> outputs, List<VarDecl> locals, List<Equation> equations,
			List<String> properties, List<Expr> assertions, List<String> realizabilityInputs, ContractBody contractBody,
			List<String> ivc) {
		this(Location.NULL, id, inputs, outputs, locals, equations, properties, assertions, realizabilityInputs,
				contractBody, ivc);
	}
}
