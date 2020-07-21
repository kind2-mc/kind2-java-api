package kind2.lustre;

import java.util.Arrays;
import java.util.List;

import kind2.util.Util;

public class Program extends Ast {
	public final List<TypeDef> types;
	public final List<Constant> constants;
	public final List<Function> functions;
	public final List<ImportedFunction> importedFunctions;
	public final List<ImportedNode> importedNodes;
	public final List<Contract> contracts;
	public final List<Kind2Function> kind2Functions;
	public final List<Node> nodes;
	public final String main;

	public Program(Location location, List<TypeDef> types, List<Constant> constants,
			List<Function> functions, List<ImportedFunction> importedFunctions,
			List<ImportedNode> importedNodes, List<Contract> contracts, 
			List<Kind2Function> kind2Functions, List<Node> nodes, String main) {
		super(location);
		this.types = Util.safeList(types);
		this.constants = Util.safeList(constants);
		this.functions = Util.safeList(functions);
		this.importedFunctions = Util.safeList(importedFunctions);
		this.importedNodes = Util.safeList(importedNodes);
		this.contracts = Util.safeList(contracts);
		this.kind2Functions = Util.safeList(kind2Functions);
		this.nodes = Util.safeList(nodes);
		if (main == null && nodes != null && nodes.size() > 0) {
			this.main = nodes.get(nodes.size() - 1).id;
		} else {
			this.main = main;
		}
	}

	public Program(Node... nodes) {
		this(Location.NULL, null, null, null, null, null, null, null, Arrays.asList(nodes), null);
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
