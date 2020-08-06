/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

public class Node extends Ast {
	public final String id;
	public final List<VarDecl> inputs;
	public final List<VarDecl> outputs;
	public final List<VarDecl> locals;
	public final List<Equation> equations;
	public final List<String> properties;
	public final List<Expr> assertions;
	public final ContractBody contractBody; // Nullable

	public Node(Location location, String id, List<VarDecl> inputs, List<VarDecl> outputs, ContractBody contractBody,
			List<VarDecl> locals, List<Equation> equations, List<Expr> assertions, List<String> properties) {
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

	public Node(String id, List<VarDecl> inputs, List<VarDecl> outputs, ContractBody contractBody, List<VarDecl> locals,
			List<Equation> equations, List<Expr> assertions, List<String> properties) {
		this(Location.NULL, id, inputs, outputs, contractBody, locals, equations, assertions, properties);
	}
}
