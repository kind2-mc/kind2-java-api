package kind2.lustre.visitors;

import static java.util.stream.Collectors.joining;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import kind2.JKindException;
import kind2.lustre.ArrayAccessExpr;
import kind2.lustre.ArrayExpr;
import kind2.lustre.ArrayType;
import kind2.lustre.ArrayUpdateExpr;
import kind2.lustre.Assume;
import kind2.lustre.Ast;
import kind2.lustre.BinaryExpr;
import kind2.lustre.BoolExpr;
import kind2.lustre.CastExpr;
import kind2.lustre.CondactExpr;
import kind2.lustre.Constant;
import kind2.lustre.Contract;
import kind2.lustre.ContractBody;
import kind2.lustre.ContractImport;
import kind2.lustre.ContractItem;
import kind2.lustre.EnumType;
import kind2.lustre.Equation;
import kind2.lustre.Expr;
import kind2.lustre.Function;
import kind2.lustre.FunctionCallExpr;
import kind2.lustre.Guarantee;
import kind2.lustre.IdExpr;
import kind2.lustre.IfThenElseExpr;
import kind2.lustre.ImportedFunction;
import kind2.lustre.ImportedNode;
import kind2.lustre.IntExpr;
import kind2.lustre.Kind2Function;
import kind2.lustre.Mode;
import kind2.lustre.ModeRefExpr;
import kind2.lustre.NamedType;
import kind2.lustre.Node;
import kind2.lustre.NodeCallExpr;
import kind2.lustre.Program;
import kind2.lustre.RealExpr;
import kind2.lustre.RecordAccessExpr;
import kind2.lustre.RecordExpr;
import kind2.lustre.RecordType;
import kind2.lustre.RecordUpdateExpr;
import kind2.lustre.TupleExpr;
import kind2.lustre.Type;
import kind2.lustre.TypeDef;
import kind2.lustre.UnaryExpr;
import kind2.lustre.UnaryOp;
import kind2.lustre.VarDecl;
import kind2.lustre.VarDef;

public class PrettyPrintVisitor {
	private StringBuilder sb = new StringBuilder();
	private String main;

	public String toString() {
		return sb.toString();
	}

	protected void write(Object o) {
		sb.append(o);
	}

	private static final String separator = System.getProperty("line.separator");

	private void newline() {
		write(separator);
	}

	public void ast(Ast a) {
		if (a instanceof Contract) {
			visit((Contract) a);
		} else if (a instanceof ContractBody) {
			visit((ContractBody) a);
		} else if (a instanceof ContractItem) {
			item((ContractItem) a);
		} else if (a instanceof Equation) {
			visit((Equation) a);
		} else if (a instanceof Expr) {
			expr((Expr) a);
		} else if (a instanceof Function) {
			visit((Function) a);
		} else if (a instanceof ImportedFunction) {
			visit((ImportedFunction) a);
		} else if (a instanceof ImportedNode) {
			visit((ImportedNode) a);
		} else if (a instanceof Kind2Function) {
			visit((Kind2Function) a);
		} else if (a instanceof Node) {
			visit((Node) a);
		} else if (a instanceof Program) {
			visit((Program) a);
		} else if (a instanceof TypeDef) {
			visit((TypeDef) a);
		} else if (a instanceof VarDecl) {
			visit((VarDecl) a);
		} else {
			throw new JKindException("Unkown AST construct!");
		}
	}

	public void visit(Program program) {
		main = program.main;

		if (!program.types.isEmpty()) {
			for (TypeDef typeDef : program.types) {
				visit(typeDef);
				newline();
			}
			newline();
		}

		if (!program.constants.isEmpty()) {
			for (Constant constant : program.constants) {
				visit(constant);
				newline();
			}
			newline();
		}

		if (!program.functions.isEmpty()) {
			for (Function function : program.functions) {
				visit(function);
				newline();
			}
			newline();
		}

		if (!program.importedFunctions.isEmpty()) {
			for (ImportedFunction importedFunction : program.importedFunctions) {
				visit(importedFunction);
				newline();
			}
			newline();
		}

		if (!program.importedNodes.isEmpty()) {
			for (ImportedNode importedNode : program.importedNodes) {
				visit(importedNode);
				newline();
			}
			newline();
		}

		for (Contract contract : program.contracts) {
			visit(contract);
			newline();
			newline();
		}

		for (Kind2Function kind2Function : program.kind2Functions) {
			visit(kind2Function);
			newline();
			newline();
		}

		Iterator<Node> iterator = program.nodes.iterator();
		while (iterator.hasNext()) {
			visit(iterator.next());
			newline();
			if (iterator.hasNext()) {
				newline();
			}
		}

	}

	public void visit(TypeDef typeDef) {
		write("type ");
		write(typeDef.id);
		write(" = ");
		writeType(typeDef.type);
		write(";");

	}

	private void writeType(Type type) {
		if (type instanceof RecordType) {
			RecordType recordType = (RecordType) type;
			write("struct {");
			Iterator<Entry<String, Type>> iterator = recordType.fields.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Type> entry = iterator.next();
				write(entry.getKey());
				write(" : ");
				write(entry.getValue());
				if (iterator.hasNext()) {
					write("; ");
				}
			}
			write("}");
		} else if (type instanceof EnumType) {
			EnumType enumType = (EnumType) type;
			write("enum {");
			Iterator<String> iterator = enumType.values.iterator();
			while (iterator.hasNext()) {
				write(iterator.next());
				if (iterator.hasNext()) {
					write(", ");
				}
			}
			write("}");
		} else {
			write(type);
		}
	}

	public void visit(Constant constant) {
		write("const ");
		write(constant.id);
		write(" = ");
		expr(constant.expr);
		write(";");

	}

	public void visit(Contract contract) {
		write("contract ");
		write(contract.id);
		write("(");
		newline();
		varDecls(contract.inputs);
		newline();
		write(") returns (");
		newline();
		varDecls(contract.outputs);
		newline();
		write(");");
		newline();
		write("let");
		newline();
		visit(contract.contractBody);
		write("tel;");

	}

	public void visit(ContractBody contractBody) {
		for (ContractItem item : contractBody.items) {
			write("  ");
			item(item);
			newline();
		}
	}

	protected void item(ContractItem i) {
		if (i instanceof Assume) {
			visit((Assume) i);
		} else if (i instanceof Constant) {
			visit((Constant) i);
		} else if (i instanceof ContractImport) {
			visit((ContractImport) i);
		} else if (i instanceof Guarantee) {
			visit((Guarantee) i);
		} else if (i instanceof Mode) {
			visit((Mode) i);
		} else if (i instanceof VarDef) {
			visit((VarDef) i);
		} else {
			throw new JKindException("Unkown contract item!");
		}
	}

	public void visit(ContractImport contractImport) {
		write("import ");
		write(contractImport.id);
		write("(");

		Iterator<Expr> inputIt = contractImport.inputs.iterator();

		while (inputIt.hasNext()) {
			expr(inputIt.next());
			if (inputIt.hasNext()) {
				write(", ");
			}
		}

		write(") returns (");

		Iterator<IdExpr> outputIt = contractImport.outputs.iterator();
		while (outputIt.hasNext()) {
			expr(outputIt.next());
			if (outputIt.hasNext()) {
				write(", ");
			}
		}

		write(");");

	}

	public void visit(Function function) {
		write("function ");
		write(function.id);
		write("(");
		newline();
		varDecls(function.inputs);
		newline();
		write(") returns (");
		newline();
		varDecls(function.outputs);
		newline();
		write(");");
		newline();

	}

	public void visit(Node node) {
		write("node ");
		write(node.id);
		write("(");
		newline();
		varDecls(node.inputs);
		newline();
		write(") returns (");
		newline();
		varDecls(node.outputs);
		newline();
		write(");");
		newline();

		if (node.contractBody != null) {
			write("(*@contract");
			newline();
			visit(node.contractBody);
			write("*)");
			newline();
		}

		if (!node.locals.isEmpty()) {
			write("var");
			newline();
			varDecls(node.locals);
			write(";");
			newline();
		}
		write("let");
		newline();

		if (node.id.equals(main)) {
			write("  --%MAIN;");
			newline();
		}

		for (Equation equation : node.equations) {
			write("  ");
			visit(equation);
			newline();
			newline();
		}

		for (Expr assertion : node.assertions) {
			assertion(assertion);
			newline();
		}

		if (!node.properties.isEmpty()) {
			for (String property : node.properties) {
				property(property);
				newline();
			}
		}

		if (node.realizabilityInputs != null) {
			write("  --%REALIZABLE ");
			write(node.realizabilityInputs.stream().collect(joining(", ")));
			write(";");
			newline();
			newline();
		}

		if (!node.ivc.isEmpty()) {
			write("  --%IVC ");
			write(node.ivc.stream().collect(joining(", ")));
			write(";");
			newline();
			newline();
		}

		write("tel;");

	}

	private void varDecls(List<VarDecl> varDecls) {
		Iterator<VarDecl> iterator = varDecls.iterator();
		while (iterator.hasNext()) {
			write("  ");
			visit(iterator.next());
			if (iterator.hasNext()) {
				write(";");
				newline();
			}
		}
	}

	public void visit(VarDecl varDecl) {
		Type type = varDecl.type;
		if (type instanceof ArrayType) {
			StringBuilder sb = new StringBuilder("");
			while (type instanceof ArrayType) {
				ArrayType arrayType = (ArrayType) type;
				StringBuilder thisStr = new StringBuilder("^" + arrayType.size);
				thisStr.append(sb);
				sb = thisStr;
				type = arrayType.base;
			}
			write(varDecl.id);
			write(" : ");
			write(type);
			write(sb);
		} else {
			write(varDecl.id);
			write(" : ");
			write(varDecl.type);
		}

	}

	public void visit(Equation equation) {
		if (equation.lhs.isEmpty()) {
			write("()");
		} else {
			Iterator<IdExpr> iterator = equation.lhs.iterator();
			while (iterator.hasNext()) {
				write(iterator.next().id);
				if (iterator.hasNext()) {
					write(", ");
				}
			}
		}

		write(" = ");
		expr(equation.expr);
		write(";");

	}

	private void assertion(Expr assertion) {
		write("  assert ");
		expr(assertion);
		write(";");
		newline();
	}

	protected void property(String s) {
		write("  --%PROPERTY ");
		write(s);
		write(";");
	}

	protected void expr(Expr e) {
		if (e instanceof ArrayAccessExpr) {
			visit((ArrayAccessExpr) e);
		} else if (e instanceof ArrayExpr) {
			visit((ArrayExpr) e);
		} else if (e instanceof ArrayUpdateExpr) {
			visit((ArrayUpdateExpr) e);
		} else if (e instanceof BinaryExpr) {
			visit((BinaryExpr) e);
		} else if (e instanceof BoolExpr) {
			visit((BoolExpr) e);
		} else if (e instanceof CastExpr) {
			visit((CastExpr) e);
		} else if (e instanceof CondactExpr) {
			visit((CondactExpr) e);
		} else if (e instanceof FunctionCallExpr) {
			visit((FunctionCallExpr) e);
		} else if (e instanceof IdExpr) {
			visit((IdExpr) e);
		} else if (e instanceof IfThenElseExpr) {
			visit((IfThenElseExpr) e);
		} else if (e instanceof IntExpr) {
			visit((IntExpr) e);
		} else if (e instanceof ModeRefExpr) {
			visit((ModeRefExpr) e);
		} else if (e instanceof NodeCallExpr) {
			visit((NodeCallExpr) e);
		} else if (e instanceof RealExpr) {
			visit((RealExpr) e);
		} else if (e instanceof RecordAccessExpr) {
			visit((RecordAccessExpr) e);
		} else if (e instanceof RecordExpr) {
			visit((RecordExpr) e);
		} else if (e instanceof RecordUpdateExpr) {
			visit((RecordUpdateExpr) e);
		} else if (e instanceof TupleExpr) {
			visit((TupleExpr) e);
		} else if (e instanceof UnaryExpr) {
			visit((UnaryExpr) e);
		} else {
			throw new JKindException("Unkown expression kind!");
		}
	}

	public void visit(ArrayAccessExpr e) {
		expr(e.array);
		write("[");
		expr(e.index);
		write("]");

	}

	public void visit(ArrayExpr e) {
		Iterator<Expr> iterator = e.elements.iterator();
		write("[");
		expr(iterator.next());
		while (iterator.hasNext()) {
			write(", ");
			expr(iterator.next());
		}
		write("]");

	}

	public void visit(ArrayUpdateExpr e) {
		expr(e.array);
		write("[");
		expr(e.index);
		write(" := ");
		expr(e.value);
		write("]");

	}

	public void visit(Assume assumption) {
		write("assume ");
		expr(assumption.expr);
		write(";");

	}

	public void visit(BinaryExpr e) {
		write("(");
		expr(e.left);
		write(" ");
		write(e.op);
		write(" ");
		expr(e.right);
		write(")");

	}

	public void visit(BoolExpr e) {
		write(Boolean.toString(e.value));

	}

	public void visit(CastExpr e) {
		write(getCastFunction(e.type));
		write("(");
		expr(e.expr);
		write(")");

	}

	private String getCastFunction(Type type) {
		if (type == NamedType.REAL) {
			return "real";
		} else if (type == NamedType.INT) {
			return "floor";
		} else {
			throw new IllegalArgumentException("Unable to cast to type: " + type);
		}
	}

	public void visit(CondactExpr e) {
		write("condact(");
		expr(e.clock);
		write(", ");
		expr(e.call);
		for (Expr arg : e.args) {
			write(", ");
			expr(arg);
		}
		write(")");

	}

	public void visit(FunctionCallExpr e) {
		write(e.function);
		write("(");
		Iterator<Expr> iterator = e.args.iterator();
		while (iterator.hasNext()) {
			expr(iterator.next());
			if (iterator.hasNext()) {
				write(", ");
			}
		}
		write(")");

	}

	public void visit(Guarantee guarantee) {
		write("guarantee ");
		expr(guarantee.expr);
		write(";");

	}

	public void visit(IdExpr e) {
		write(e.id);

	}

	public void visit(IfThenElseExpr e) {
		write("(if ");
		expr(e.cond);
		write(" then ");
		expr(e.thenExpr);
		write(" else ");
		expr(e.elseExpr);
		write(")");

	}

	public void visit(ImportedFunction importedFunction) {
		write("function imported ");
		write(importedFunction.id);
		write("(");
		newline();
		varDecls(importedFunction.inputs);
		newline();
		write(") returns (");
		newline();
		varDecls(importedFunction.outputs);
		newline();
		write(");");
		newline();

		if (importedFunction.contractBody != null) {
			write("(*@contract");
			newline();
			visit(importedFunction.contractBody);
			write("*)");
		}

	}

	public void visit(ImportedNode importedNode) {
		write("node imported ");
		write(importedNode.id);
		write("(");
		newline();
		varDecls(importedNode.inputs);
		newline();
		write(") returns (");
		newline();
		varDecls(importedNode.outputs);
		newline();
		write(");");
		newline();

		if (importedNode.contractBody != null) {
			write("(*@contract");
			newline();
			visit(importedNode.contractBody);
			write("*)");
		}

	}

	public void visit(IntExpr e) {
		write(e.value);

	}

	public void visit(Kind2Function kind2Function) {
		write("function ");
		write(kind2Function.id);
		write("(");
		newline();
		varDecls(kind2Function.inputs);
		newline();
		write(") returns (");
		newline();
		varDecls(kind2Function.outputs);
		newline();
		write(");");
		newline();

		if (kind2Function.contractBody != null) {
			write("(*@contract");
			newline();
			visit(kind2Function.contractBody);
			write("*)");
			newline();
		}

		if (!kind2Function.locals.isEmpty()) {
			write("var");
			newline();
			varDecls(kind2Function.locals);
			write(";");
			newline();
		}
		write("let");
		newline();

		if (kind2Function.id.equals(main)) {
			write("  --%MAIN;");
			newline();
		}

		for (Equation equation : kind2Function.equations) {
			write("  ");
			visit(equation);
			newline();
		}

		if (!kind2Function.assertions.isEmpty()) {
			newline();
			for (Expr assertion : kind2Function.assertions) {
				assertion(assertion);
				newline();
			}
		}

		if (!kind2Function.properties.isEmpty()) {
			newline();
			for (String property : kind2Function.properties) {
				property(property);
				newline();
			}
		}

		write("tel;");

	}

	public void visit(Mode mode) {
		write("mode ");
		write(mode.id);
		write(" (");
		newline();
		for (Expr e : mode.require) {
			write("    require ");
			expr(e);
			write(";");
			newline();
		}
		for (Expr e : mode.ensure) {
			write("    ensure  ");
			expr(e);
			write(";");
			newline();
		}
		write("  );");

	}

	public void visit(ModeRefExpr e) {
		for (String s : e.path) {
			write("::");
			write(s);
		}

	}

	public void visit(NodeCallExpr e) {
		write(e.node);
		write("(");
		Iterator<Expr> iterator = e.args.iterator();
		while (iterator.hasNext()) {
			expr(iterator.next());
			if (iterator.hasNext()) {
				write(", ");
			}
		}
		write(")");

	}

	public void visit(RealExpr e) {
		String str = e.value.toPlainString();
		write(str);
		if (!str.contains(".")) {
			write(".0");
		}

	}

	public void visit(RecordAccessExpr e) {
		expr(e.record);
		write(".");
		write(e.field);

	}

	public void visit(RecordExpr e) {
		write(e.id);
		write(" {");
		Iterator<Entry<String, Expr>> iterator = e.fields.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Expr> entry = iterator.next();
			write(entry.getKey());
			write(" = ");
			expr(entry.getValue());
			if (iterator.hasNext()) {
				write("; ");
			}
		}
		write("}");

	}

	public void visit(RecordUpdateExpr e) {
		expr(e.record);
		write("{");
		write(e.field);
		write(" := ");
		expr(e.value);
		write("}");

	}

	public void visit(TupleExpr e) {
		if (e.elements.isEmpty()) {
			write("()");

		}

		Iterator<Expr> iterator = e.elements.iterator();
		write("(");
		expr(iterator.next());
		while (iterator.hasNext()) {
			write(", ");
			expr(iterator.next());
		}
		write(")");

	}

	public void visit(UnaryExpr e) {
		write("(");
		write(e.op);
		if (e.op != UnaryOp.NEGATIVE) {
			write(" ");
		}
		expr(e.expr);
		write(")");

	}

	public void visit(VarDef varDef) {
		write("var ");
		visit(varDef.varDecl);
		write(" = ");
		expr(varDef.expr);
		write(";");

	}
}
