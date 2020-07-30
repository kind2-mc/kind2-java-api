package edu.uiowa.kind2.lustre.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uiowa.kind2.lustre.ContractBody;
import edu.uiowa.kind2.lustre.Equation;
import edu.uiowa.kind2.lustre.Expr;
import edu.uiowa.kind2.lustre.IdExpr;
import edu.uiowa.kind2.lustre.Function;
import edu.uiowa.kind2.lustre.Location;
import edu.uiowa.kind2.lustre.Type;
import edu.uiowa.kind2.lustre.VarDecl;

/**
 * This class provides helper functions for constructing a function.
 */
public class FunctionBuilder {
	private String id;
	private List<VarDecl> inputs = new ArrayList<>();
	private List<VarDecl> outputs = new ArrayList<>();
	private ContractBody contractBody = null;
	private List<VarDecl> locals = new ArrayList<>();
	private List<Equation> equations = new ArrayList<>();
	private List<String> properties = new ArrayList<>();
	private List<Expr> assertions = new ArrayList<>();

	/**
	 * Constructor
	 *
	 * @param id name of the function
	 */
	public FunctionBuilder(String id) {
		this.id = id;
	}

	/**
	 * Constructor
	 *
	 * @param function function to clone
	 */
	public FunctionBuilder(Function Function) {
		this.id = Function.id;
		this.inputs = new ArrayList<>(Function.inputs);
		this.outputs = new ArrayList<>(Function.outputs);
		this.contractBody = Function.contractBody;
		this.locals = new ArrayList<>(Function.locals);
		this.equations = new ArrayList<>(Function.equations);
		this.properties = new ArrayList<>(Function.properties);
		this.assertions = new ArrayList<>(Function.assertions);
	}

	/**
	 * set the function's name
	 *
	 * @param id name of the function
	 * @return this function builder
	 */
	public FunctionBuilder setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * add an input to the function
	 *
	 * @param input the input to add
	 * @return this function builder
	 */
	public FunctionBuilder addInput(VarDecl input) {
		this.inputs.add(input);
		return this;
	}

	/**
	 * add inputs to the function
	 *
	 * @param inputs a collection of inputs to add
	 * @return this function builder
	 */
	public FunctionBuilder addInputs(Collection<VarDecl> inputs) {
		this.inputs.addAll(inputs);
		return this;
	}

	/**
	 * create an input to the function
	 *
	 * @param name name of input to the function
	 * @param type name of input to the function
	 * @return an expression referring to created input
	 */
	public IdExpr createInput(String name, Type type) {
		this.inputs.add(new VarDecl(name, type));
		return new IdExpr(name);
	}

	/**
	 * remove all inputs to the function
	 *
	 * @return this function builder
	 */
	public FunctionBuilder clearInputs() {
		this.inputs.clear();
		return this;
	}

	/**
	 * add an output for the function
	 *
	 * @param output the output to add
	 * @return this function builder
	 */
	public FunctionBuilder addOutput(VarDecl output) {
		this.outputs.add(output);
		return this;
	}

	/**
	 * add outputs for the function
	 *
	 * @param outputs a collection of outputs to add
	 * @return this function builder
	 */
	public FunctionBuilder addOutputs(Collection<VarDecl> outputs) {
		this.outputs.addAll(outputs);
		return this;
	}

	/**
	 * create an output for the function
	 *
	 * @param name name of output to the function
	 * @param type name of output to the function
	 * @return an expression referring to created output
	 */
	public IdExpr createOutput(String name, Type type) {
		this.outputs.add(new VarDecl(name, type));
		return new IdExpr(name);
	}

	/**
	 * remove all outputs for the function
	 *
	 * @return this function builder
	 */
	public FunctionBuilder clearOutputs() {
		this.outputs.clear();
		return this;
	}

	/**
	 * associate an inline contract with the function
	 *
	 * @param contractBody body of the inline contract
	 * @return this function builder
	 */
	public FunctionBuilder setContractBody(ContractBody contractBody) {
		this.contractBody = contractBody;
		return this;
	}

	/**
	 * add a local var
	 *
	 * @param local the local var to add
	 * @return this function builder
	 */
	public FunctionBuilder addLocal(VarDecl local) {
		this.locals.add(local);
		return this;
	}

	/**
	 * add local vars
	 *
	 * @param locals a collection of local vars to add
	 * @return this function builder
	 */
	public FunctionBuilder addLocals(Collection<VarDecl> locals) {
		this.locals.addAll(locals);
		return this;
	}

	/**
	 * create a local var
	 *
	 * @param name name of local var
	 * @param type name of local var
	 * @return an expression referring to created local var
	 */
	public IdExpr createLocal(String name, Type type) {
		this.locals.add(new VarDecl(name, type));
		return new IdExpr(name);
	}

	/**
	 * remove all local vars
	 *
	 * @return this function builder
	 */
	public FunctionBuilder clearLocals() {
		this.locals.clear();
		return this;
	}

	/**
	 * add an equation
	 *
	 * @param equation the equation to add
	 * @return this function builder
	 */
	public FunctionBuilder addEquation(Equation equation) {
		this.equations.add(equation);
		return this;
	}

	/**
	 * add an equation
	 *
	 * @param var  the left-hand side to define by the equation
	 * @param expr the right-hand side of the equation
	 * @return this function builder
	 */
	public FunctionBuilder addEquation(IdExpr var, Expr expr) {
		this.equations.add(new Equation(var, expr));
		return this;
	}

	/**
	 * add equations
	 *
	 * @param equations a collection of equations to add
	 * @return this function builder
	 */
	public FunctionBuilder addEquations(Collection<Equation> equations) {
		this.equations.addAll(equations);
		return this;
	}

	/**
	 * remove all equations
	 *
	 * @return this function builder
	 */
	public FunctionBuilder clearEquations() {
		this.equations.clear();
		return this;
	}

	/**
	 * add an assertion
	 *
	 * @param assertion a boolean expression representing a constraint
	 * @return this function builder
	 */
	public FunctionBuilder addAssertion(Expr assertion) {
		this.assertions.add(assertion);
		return this;
	}

	/**
	 * add assertions
	 *
	 * @param assertions a collection of assertions to add
	 * @return this function builder
	 */
	public FunctionBuilder addAssertions(Collection<Expr> assertions) {
		this.assertions.addAll(assertions);
		return this;
	}

	/**
	 * remove all assertions
	 *
	 * @return this function builder
	 */
	public FunctionBuilder clearAssertions() {
		this.assertions.clear();
		return this;
	}

	/**
	 * add a property
	 *
	 * @param property constraint expressing the behavior of a node
	 * @return this function builder
	 */
	public FunctionBuilder addProperty(String property) {
		this.properties.add(property);
		return this;
	}

	/**
	 * add a property
	 *
	 * @param property a boolean var/constant
	 * @return this function builder
	 */
	public FunctionBuilder addProperty(IdExpr property) {
		this.properties.add(property.id);
		return this;
	}

	/**
	 * add properties
	 *
	 * @param property a collection of properties to add
	 * @return this function builder
	 */
	public FunctionBuilder addProperties(Collection<String> properties) {
		this.properties.addAll(properties);
		return this;
	}

	/**
	 * remove all properties
	 *
	 * @return this function builder
	 */
	public FunctionBuilder clearProperties() {
		this.properties.clear();
		return this;
	}

	/**
	 * construct a function
	 *
	 * @return constructed function
	 */
	public Function build() {
		return new Function(Location.NULL, id, inputs, outputs, contractBody, locals, equations, assertions,
				properties);
	}
}
