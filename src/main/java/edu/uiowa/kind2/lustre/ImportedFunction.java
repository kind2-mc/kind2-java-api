/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

/**
 * This class represents an {@code imported} function. An imported function does
 * not have a body {@code (let ... tel)}. In a Lustre compiler, this is usually
 * used to encode a C function or more generally a call to an external library.
 * <p>
 * In Kind 2, this means that the function is always abstract in the contract
 * sense. It can never be refined, and is always abstracted by its contract. If
 * none is given, then the implicit (rather weak) contract
 * {@code (*@contract assume true; guarantee true; *)} is used.
 * <p>
 * In a modular analysis, imported functions will not be analyzed, although if
 * their contract has modes they will be checked for exhaustiveness,
 * consistently with the usual Kind 2 contract workflow.
 */
public class ImportedFunction extends Ast {
	public final String id;
	public final List<VarDecl> inputs;
	public final List<VarDecl> outputs;
	public final ContractBody contractBody; // Nullable

	/**
	 * Constructor
	 *
	 * @param location     location of imported function in a Lustre file
	 * @param id           name of this function
	 * @param inputs       inputs to this function
	 * @param outputs      outputs of this function
	 * @param contractBody an inline contract
	 */
	public ImportedFunction(Location location, String id, List<VarDecl> inputs, List<VarDecl> outputs,
			ContractBody contractBody) {
		super(location);
		Assert.isNotNull(id);
		this.id = id;
		this.inputs = Util.safeList(inputs);
		this.outputs = Util.safeList(outputs);
		this.contractBody = contractBody;
	}

	/**
	 * Constructor
	 *
	 * @param id           name of this function
	 * @param inputs       inputs to this function
	 * @param outputs      outputs of this function
	 * @param contractBody an inline contract
	 */
	public ImportedFunction(String id, List<VarDecl> inputs, List<VarDecl> outputs, ContractBody contractBody) {
		this(Location.NULL, id, inputs, outputs, contractBody);
	}
}
