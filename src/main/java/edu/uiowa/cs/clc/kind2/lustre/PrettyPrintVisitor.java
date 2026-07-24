/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import edu.uiowa.cs.clc.kind2.Kind2Exception;

/**
 * Renders a Lustre AST back into Lustre source text.
 * <p>
 * Each {@code visit} method appends the syntax for one kind of AST node to an
 * internal buffer; call {@link #toString()} to retrieve the accumulated text.
 */
public class PrettyPrintVisitor {
  private StringBuilder sb = new StringBuilder();
  private String main;

  /**
   * Constructs a visitor with an empty output buffer.
   */
  public PrettyPrintVisitor() {
  }

  public String toString() {
    return sb.toString();
  }

  void write(Object o) {
    sb.append(o);
  }

  private static final String separator = System.getProperty("line.separator");

  private void newline() {
    write(separator);
  }

  /**
   * Append the Lustre syntax for the given AST node to this visitor's output.
   *
   * @param a the AST node to print
   * @throws edu.uiowa.cs.clc.kind2.Kind2Exception if the node is of an unknown kind
   */
  public void ast(Ast a) {
    if (a instanceof Type) {
      writeType((Type) a);
    } else if (a instanceof Expr) {
      expr((Expr) a);
    } else if (a instanceof Contract) {
      visit((Contract) a);
    } else if (a instanceof ContractBody) {
      visit((ContractBody) a);
    } else if (a instanceof ContractItem) {
      item((ContractItem) a);
    } else if (a instanceof Equation) {
      visit((Equation) a);
    } else if (a instanceof Expr) {
      expr((Expr) a);
    } else if (a instanceof Component) {
      visit((Component) a);
    } else if (a instanceof ImportedComponent) {
      visit((ImportedComponent) a);
    } else if (a instanceof Parameter) {
      visit((Parameter) a);
    } else if (a instanceof Program) {
      visit((Program) a);
    } else if (a instanceof Property) {
      visit((Property) a);
    } else if (a instanceof TypeDef) {
      visit((TypeDef) a);
    } else if (a instanceof VarDecl) {
      visit((VarDecl) a);
    } else {
      throw new Kind2Exception("Unknown AST construct!");
    }
  }

  /**
   * Append the Lustre syntax for the given parameter to this visitor's output.
   *
   * @param param the parameter to print
   */
  public void visit(Parameter param) {
    if (param.isConst) {
      write("const ");
    }
    write(param.id);
    write(" : ");
    write(param.type);
  }

  /**
   * Append the Lustre syntax for the given program to this visitor's output.
   *
   * @param program the program to print
   */
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

    for (Component function : program.functions) {
      if (function.contractBody == null) {
        write("function ");
        visit(function);
        newline();
        newline();
      }
    }

    for (Component node : program.nodes) {
      if (node.contractBody == null) {
        write("node ");
        visit(node);
        newline();
        newline();
      }
    }

    if (!program.importedFunctions.isEmpty()) {
      for (ImportedComponent importedFunction : program.importedFunctions) {
        write("function imported ");
        visit(importedFunction);
        newline();
      }
      newline();
    }

    if (!program.importedNodes.isEmpty()) {
      for (ImportedComponent importedNode : program.importedNodes) {
        write("node imported ");
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

    for (Component function : program.functions) {
      if (function.contractBody != null) {
        write("function ");
        visit(function);
        newline();
        newline();
      }
    }

    for (Component node : program.nodes) {
      if (node.contractBody != null) {
        write("node ");
        visit(node);
        newline();
        newline();
      }
    }
  }

  /**
   * Append the Lustre syntax for the given property to this visitor's output.
   *
   * @param property the property to print
   */
  public void visit(Property property) {
    write(" --%PROPERTY ");
    if (property.name != null) {
      write("\"");
      write(property.name);
      write("\" ");
    }
    expr(property.expr);
    write(";");
  }

  /**
   * Append the Lustre syntax for the given type definition to this visitor's output.
   *
   * @param typeDef the type definition to print
   */
  public void visit(TypeDef typeDef) {
    write("type ");
    write(typeDef.id);
    if (typeDef.type != null) {
      write(" = ");
      writeType(typeDef.type);
    }
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
    } else if (type instanceof TupleType) {
      TupleType tupleType = (TupleType) type;
      Iterator<Type> iterator = tupleType.types.iterator();
      write('[');
      while (iterator.hasNext()) {
        write(iterator.next());
        if (iterator.hasNext()) {
          write(", ");
        }
      }
      write(']');
    } else {
      write(type);
    }
  }

  /**
   * Append the Lustre syntax for the given constant declaration to this visitor's output.
   *
   * @param constant the constant declaration to print
   */
  public void visit(Constant constant) {
    write("const ");
    write(constant.id);
    if (constant.type != null) {
      write(" : ");
      writeType(constant.type);
    }
    if (constant.expr != null) {
      write(" = ");
      expr(constant.expr);
    }
    write(";");
  }

  /**
   * Append the Lustre syntax for the given contract to this visitor's output.
   *
   * @param contract the contract to print
   */
  public void visit(Contract contract) {
    write("contract ");
    write(contract.id);
    write(" (");
    newline();
    params(contract.inputs);
    newline();
    write(") returns (");
    newline();
    params(contract.outputs);
    newline();
    write(");");
    newline();
    write("let");
    newline();
    visit(contract.contractBody);
    write("tel");
  }

  /**
   * Append the Lustre syntax for the given contract body to this visitor's output.
   *
   * @param contractBody the contract body to print
   */
  public void visit(ContractBody contractBody) {
    for (ContractItem item : contractBody.items) {
      write("  ");
      item(item);
      newline();
    }
  }

  void item(ContractItem i) {
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
      throw new Kind2Exception("Unknown contract item!");
    }
  }

  /**
   * Append the Lustre syntax for the given contract import to this visitor's output.
   *
   * @param contractImport the contract import to print
   */
  public void visit(ContractImport contractImport) {
    write("import ");
    write(contractImport.id);
    write(" (");

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

  /**
   * Append the Lustre syntax for the given imported component to this visitor's output.
   *
   * @param importedComponent the imported component to print
   */
  public void visit(ImportedComponent importedComponent) {
    write(importedComponent.id);
    write(" (");
    newline();
    params(importedComponent.inputs);
    newline();
    write(") returns (");
    newline();
    params(importedComponent.outputs);
    newline();
    write(");");
    newline();

    if (importedComponent.contractBody != null) {
      write("(*@contract");
      newline();
      visit(importedComponent.contractBody);
      write("*)");
      newline();
    }
  }

  /**
   * Append the Lustre syntax for the given component to this visitor's output.
   *
   * @param component the component to print
   */
  public void visit(Component component) {
    visit((ImportedComponent) component);

    if (!component.localVars.isEmpty()) {
      write("var");
      newline();
      varDecls(component.localVars);
      write(";");
      newline();
    }
    write("let");
    newline();

    if (component.id.equals(main)) {
      write("  --%MAIN;");
      newline();
    }

    for (Equation equation : component.equations) {
      write("  ");
      visit(equation);
      newline();
    }

    for (Expr assertion : component.assertions) {
      assertion(assertion);
      newline();
    }

    for (Property property : component.properties) {
      visit(property);
      newline();
    }

    write("tel");
  }

  private void params(List<Parameter> params) {
    Iterator<Parameter> iterator = params.iterator();
    while (iterator.hasNext()) {
      write("  ");
      visit(iterator.next());
      if (iterator.hasNext()) {
        write(";");
        newline();
      }
    }
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

  /**
   * Append the Lustre syntax for the given variable declaration to this visitor's output.
   *
   * @param varDecl the variable declaration to print
   */
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

  /**
   * Append the Lustre syntax for the given equation to this visitor's output.
   *
   * @param equation the equation to print
   */
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
  }

  void property(String s) {
    write("  --%PROPERTY ");
    write(s);
    write(";");
  }

  void expr(Expr e) {
    if (e instanceof ArrayAccessExpr) {
      visit((ArrayAccessExpr) e);
    } else if (e instanceof ArrayExpr) {
      visit((ArrayExpr) e);
    } else if (e instanceof BinaryExpr) {
      visit((BinaryExpr) e);
    } else if (e instanceof BoolExpr) {
      visit((BoolExpr) e);
    } else if (e instanceof CastExpr) {
      visit((CastExpr) e);
    } else if (e instanceof CondactExpr) {
      visit((CondactExpr) e);
    } else if (e instanceof IdExpr) {
      visit((IdExpr) e);
    } else if (e instanceof IfThenElseExpr) {
      visit((IfThenElseExpr) e);
    } else if (e instanceof IntExpr) {
      visit((IntExpr) e);
    } else if (e instanceof ListExpr) {
      visit((ListExpr) e);
    } else if (e instanceof ModeRefExpr) {
      visit((ModeRefExpr) e);
    } else if (e instanceof ComponentCallExpr) {
      visit((ComponentCallExpr) e);
    } else if (e instanceof RealExpr) {
      visit((RealExpr) e);
    } else if (e instanceof RecordAccessExpr) {
      visit((RecordAccessExpr) e);
    } else if (e instanceof RecordExpr) {
      visit((RecordExpr) e);
    } else if (e instanceof TupleExpr) {
      visit((TupleExpr) e);
    } else if (e instanceof UnaryExpr) {
      visit((UnaryExpr) e);
    } else {
      throw new Kind2Exception("Unknown expression kind!");
    }
  }

  /**
   * Append the Lustre syntax for the given array access expression to this visitor's output.
   *
   * @param e the array access expression to print
   */
  public void visit(ArrayAccessExpr e) {
    write("(");
    expr(e.array);
    write(")");
    write("[");
    expr(e.index);
    write("]");
  }

  /**
   * Append the Lustre syntax for the given array expression to this visitor's output.
   *
   * @param e the array expression to print
   */
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

  /**
   * Append the Lustre syntax for the given assumption to this visitor's output.
   *
   * @param assumption the assumption to print
   */
  public void visit(Assume assumption) {
    if (assumption.weak) {
      write("weakly ");
    }
    write("assume ");
    if (assumption.name != null) {
      write("\"");
      write(assumption.name);
      write("\" ");
    }
    expr(assumption.expr);
    write(";");
  }

  /**
   * Append the Lustre syntax for the given binary expression to this visitor's output.
   *
   * @param e the binary expression to print
   */
  public void visit(BinaryExpr e) {
    write("(");
    expr(e.left);
    write(")");
    write(" ");
    write(e.op);
    write(" ");
    write("(");
    expr(e.right);
    write(")");
  }

  /**
   * Append the Lustre syntax for the given boolean literal to this visitor's output.
   *
   * @param e the boolean literal to print
   */
  public void visit(BoolExpr e) {
    write(Boolean.toString(e.value));
  }

  /**
   * Append the Lustre syntax for the given cast expression to this visitor's output.
   *
   * @param e the cast expression to print
   */
  public void visit(CastExpr e) {
    write(getCastFunction(e.type));
    write(" (");
    expr(e.expr);
    write(")");
  }

  private String getCastFunction(Type type) {
    if (type instanceof NamedType) {
      return ((NamedType) type).name;
    } else {
      throw new IllegalArgumentException("Unable to cast to type: " + type);
    }
  }

  /**
   * Append the Lustre syntax for the given condact expression to this visitor's output.
   *
   * @param e the condact expression to print
   */
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

  /**
   * Append the Lustre syntax for the given guarantee to this visitor's output.
   *
   * @param guarantee the guarantee to print
   */
  public void visit(Guarantee guarantee) {
    if (guarantee.weak) {
      write("weakly ");
    }
    write("guarantee ");
    if (guarantee.name != null) {
      write("\"");
      write(guarantee.name);
      write("\" ");
    }
    expr(guarantee.expr);
    write(";");
  }

  /**
   * Append the Lustre syntax for the given require clause to this visitor's output.
   *
   * @param require the require clause to print
   */
  public void visit(Require require) {
    write("    require ");
    if (require.name != null) {
      write("\"");
      write(require.name);
      write("\" ");
    }
    expr(require.expr);
    write(";");
  }

  /**
   * Append the Lustre syntax for the given ensure clause to this visitor's output.
   *
   * @param ensure the ensure clause to print
   */
  public void visit(Ensure ensure) {
    write("    ensure ");
    if (ensure.name != null) {
      write("\"");
      write(ensure.name);
      write("\" ");
    }
    expr(ensure.expr);
    write(";");
  }

  /**
   * Append the Lustre syntax for the given identifier expression to this visitor's output.
   *
   * @param e the identifier expression to print
   */
  public void visit(IdExpr e) {
    write(e.id);
  }

  /**
   * Append the Lustre syntax for the given if-then-else expression to this visitor's output.
   *
   * @param e the if-then-else expression to print
   */
  public void visit(IfThenElseExpr e) {
    write("if ");
    write("(");
    expr(e.cond);
    write(")");
    write(" then ");
    write("(");
    expr(e.thenExpr);
    write(")");
    write(" else ");
    write("(");
    expr(e.elseExpr);
    write(")");
  }

  /**
   * Append the Lustre syntax for the given integer literal to this visitor's output.
   *
   * @param e the integer literal to print
   */
  public void visit(IntExpr e) {
    write(e.value);
  }

  /**
   * Append the Lustre syntax for the given list expression to this visitor's output.
   *
   * @param e the list expression to print
   */
  public void visit(ListExpr e) {
    write('(');
    Iterator<Expr> it = e.list.iterator();
    if (it.hasNext()) {
      expr(it.next());
    }
    while (it.hasNext()) {
      write(", ");
      expr(it.next());
    }
    write(')');
  }

  /**
   * Append the Lustre syntax for the given mode to this visitor's output.
   *
   * @param mode the mode to print
   */
  public void visit(Mode mode) {
    write("mode ");
    write(mode.id);
    write(" (");
    newline();
    for (Require require : mode.require) {
      visit(require);
      newline();
    }
    for (Ensure ensure : mode.ensure) {
      visit(ensure);
      newline();
    }
    write("  );");
  }

  /**
   * Append the Lustre syntax for the given mode reference expression to this visitor's output.
   *
   * @param e the mode reference expression to print
   */
  public void visit(ModeRefExpr e) {
    for (String s : e.path) {
      write("::");
      write(s);
    }
  }

  /**
   * Append the Lustre syntax for the given component call expression to this visitor's output.
   *
   * @param e the component call expression to print
   */
  public void visit(ComponentCallExpr e) {
    write(e.node);
    write("(");
    Iterator<Expr> iterator = e.args.iterator();
    if (iterator.hasNext()) {
      expr(iterator.next());
    }
    while (iterator.hasNext()) {
      write(", ");
      expr(iterator.next());
    }
    write(")");
  }

  /**
   * Append the Lustre syntax for the given real literal to this visitor's output.
   *
   * @param e the real literal to print
   */
  public void visit(RealExpr e) {
    String str = e.value.toPlainString();
    write(str);
    if (!str.contains(".")) {
      write(".0");
    }
  }

  /**
   * Append the Lustre syntax for the given record access expression to this visitor's output.
   *
   * @param e the record access expression to print
   */
  public void visit(RecordAccessExpr e) {
    write("(");
    expr(e.record);
    write(")");
    write(".");
    write(e.field);
  }

  /**
   * Append the Lustre syntax for the given record expression to this visitor's output.
   *
   * @param e the record expression to print
   */
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

  /**
   * Append the Lustre syntax for the given tuple expression to this visitor's output.
   *
   * @param e the tuple expression to print
   */
  public void visit(TupleExpr e) {
    Iterator<Expr> iterator = e.elements.iterator();
    write("[");
    expr(iterator.next());
    while (iterator.hasNext()) {
      write(", ");
      expr(iterator.next());
    }
    write("]");
  }

  /**
   * Append the Lustre syntax for the given unary expression to this visitor's output.
   *
   * @param e the unary expression to print
   */
  public void visit(UnaryExpr e) {
    write(e.op);
    if (e.op != UnaryOp.NEGATIVE) {
      write(" ");
    }
    write("(");
    expr(e.expr);
    write(")");
  }

  /**
   * Append the Lustre syntax for the given variable definition to this visitor's output.
   *
   * @param varDef the variable definition to print
   */
  public void visit(VarDef varDef) {
    write("var ");
    visit(varDef.varDecl);
    write(" = ");
    expr(varDef.expr);
    write(";");
  }
}
