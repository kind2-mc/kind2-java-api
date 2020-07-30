package edu.uiowa.kind2.lustre;

import java.util.Arrays;
import java.util.List;

import edu.uiowa.kind2.util.Util;

public class Program extends Ast {
	public final List<TypeDef> types;
	public final List<Constant> constants;
	public final List<ImportedFunction> importedFunctions;
	public final List<ImportedNode> importedNodes;
	public final List<Contract> contracts;
	public final List<Function> functions;
	public final List<Node> nodes;
	public final String main;

	public Program(Location location, List<TypeDef> types, List<Constant> constants, List<ImportedFunction> importedFunctions,
			List<ImportedNode> importedNodes, List<Contract> contracts, 
			List<Function> functions, List<Node> nodes, String main) {
		super(location);
		this.types = Util.safeList(types);
		this.constants = Util.safeList(constants);
		this.importedFunctions = Util.safeList(importedFunctions);
		this.importedNodes = Util.safeList(importedNodes);
		this.contracts = Util.safeList(contracts);
		this.functions = Util.safeList(functions);
		this.nodes = Util.safeList(nodes);
		if (main == null && nodes != null && nodes.size() > 0) {
			this.main = nodes.get(nodes.size() - 1).id;
		} else {
			this.main = main;
		}
	}

	public Program(Node... nodes) {
		this(Location.NULL, null, null, null, null, null, null, Arrays.asList(nodes), null);
	}

	public Node getMainNode() {
		for (Node node : nodes) {
			if (node.id.equals(main)) {
				return node;
			}
		}
		return null;
	}
}
