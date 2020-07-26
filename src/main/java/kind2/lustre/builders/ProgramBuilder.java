package kind2.lustre.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kind2.lustre.Constant;
import kind2.lustre.Contract;
import kind2.lustre.ImportedFunction;
import kind2.lustre.ImportedNode;
import kind2.lustre.Function;
import kind2.lustre.Location;
import kind2.lustre.Node;
import kind2.lustre.Program;
import kind2.lustre.TypeDef;

public class ProgramBuilder {
	private List<TypeDef> types = new ArrayList<>();
	private List<Constant> constants = new ArrayList<>();
	private List<ImportedFunction> importedFunctions = new ArrayList<>();
	private List<ImportedNode> importedNodes = new ArrayList<>();
	private List<Contract> contracts = new ArrayList<>();
	private List<Function> functions = new ArrayList<>();
	private List<Node> nodes = new ArrayList<>();
	private String main;

	public ProgramBuilder() {
	}

	public ProgramBuilder(Program program) {
		this.types = new ArrayList<>(program.types);
		this.constants = new ArrayList<>(program.constants);
		this.importedFunctions = new ArrayList<>(program.importedFunctions);
		this.importedNodes = new ArrayList<>(program.importedNodes);
		this.contracts = new ArrayList<>(program.contracts);
		this.functions = new ArrayList<>(program.functions);
		this.nodes = new ArrayList<>(program.nodes);
		this.main = program.main;
	}

	public ProgramBuilder addType(TypeDef type) {
		this.types.add(type);
		return this;
	}

	public ProgramBuilder addTypes(Collection<TypeDef> types) {
		this.types.addAll(types);
		return this;
	}

	public ProgramBuilder clearTypes() {
		this.types.clear();
		return this;
	}

	public ProgramBuilder addConstant(Constant constant) {
		this.constants.add(constant);
		return this;
	}

	public ProgramBuilder addConstants(Collection<Constant> constants) {
		this.constants.addAll(constants);
		return this;
	}

	public ProgramBuilder clearConstants() {
		this.constants.clear();
		return this;
	}

	public ProgramBuilder addImportedFunction(ImportedFunction importedFunction) {
		this.importedFunctions.add(importedFunction);
		return this;
	}

	public ProgramBuilder addImportedFunctions(Collection<ImportedFunction> importedFunctions) {
		this.importedFunctions.addAll(importedFunctions);
		return this;
	}

	public ProgramBuilder clearImportedFunctions() {
		this.importedFunctions.clear();
		return this;
	}

	public ProgramBuilder addImportedNode(ImportedNode importedNode) {
		this.importedNodes.add(importedNode);
		return this;
	}

	public ProgramBuilder addImportedNodes(Collection<ImportedNode> importedNodes) {
		this.importedNodes.addAll(importedNodes);
		return this;
	}

	public ProgramBuilder clearImportedNodes() {
		this.importedNodes.clear();
		return this;
	}

	public ProgramBuilder addContract(Contract contract) {
		this.contracts.add(contract);
		return this;
	}

	public ProgramBuilder addContracts(Collection<Contract> contracts) {
		this.contracts.addAll(contracts);
		return this;
	}

	public ProgramBuilder clearContracts() {
		this.contracts.clear();
		return this;
	}

	public ProgramBuilder addFunction(Function function) {
		this.functions.add(function);
		return this;
	}

	public ProgramBuilder addFunctions(Collection<Function> functions) {
		this.functions.addAll(functions);
		return this;
	}

	public ProgramBuilder clearFunctions() {
		this.functions.clear();
		return this;
	}

	public ProgramBuilder addNode(Node node) {
		this.nodes.add(node);
		return this;
	}

	public ProgramBuilder addNodes(Collection<Node> nodes) {
		this.nodes.addAll(nodes);
		return this;
	}

	public ProgramBuilder clearNodes() {
		this.nodes.clear();
		return this;
	}

	public ProgramBuilder setMain(String main) {
		this.main = main;
		return this;
	}

	public Program build() {
		return new Program(Location.NULL, types, constants, importedFunctions, importedNodes, contracts,
				functions, nodes, main);
	}
}
