/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.uiowa.kind2.Kind2Exception;

public class PrettyPrintVisitor {
  private StringBuilder sb = new StringBuilder();
  private String main;

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
    } else if (a instanceof Component) {
      visit((Component) a);
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

  public void visit(Parameter param) {
    if (param.isConst) {
      write("const ");
    }
    write(param.id);
    write(" : ");
    write(param.type);
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

    if (!program.importedFunctions.isEmpty()) {
      for (Component importedFunction : program.importedFunctions) {
        write("function imported ");
        visit(importedFunction);
        newline();
      }
      newline();
    }

    if (!program.importedNodes.isEmpty()) {
      for (Component importedNode : program.importedNodes) {
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
      write("function ");
      visit(function);
      newline();
      newline();
    }

    Iterator<Component> iterator = program.nodes.iterator();
    while (iterator.hasNext()) {
      write("node ");
      visit(iterator.next());
      newline();
      if (iterator.hasNext()) {
        newline();
      }
    }
  }

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

  public void visit(Contract contract) {
    write("contract ");
    write(contract.id);
    write("(");
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
    write("tel;");
  }

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

  public void visit(Component component) {
    write(component.id);
    write("(");
    newline();
    params(component.inputs);
    newline();
    write(") returns (");
    newline();
    params(component.outputs);
    newline();
    write(");");
    newline();

    if (component.contractBody != null) {
      write("(*@contract");
      newline();
      visit(component.contractBody);
      write("*)");
      newline();
    }

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
      newline();
    }

    for (Expr assertion : component.assertions) {
      assertion(assertion);
      newline();
    }

    if (!component.properties.isEmpty()) {
      for (Property property : component.properties) {
        visit(property);
        newline();
      }
    }

    write("tel;");
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
    } else if (e instanceof FunctionCallExpr) {
      visit((FunctionCallExpr) e);
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
    } else if (e instanceof NodeCallExpr) {
      visit((NodeCallExpr) e);
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

  public void visit(Assume assumption) {
    write("assume ");
    if (assumption.name != null) {
      write("\"");
      write(assumption.name);
      write("\" ");
    }
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
    if (type == TypeUtil.REAL) {
      return "real";
    } else if (type == TypeUtil.INT) {
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
    if (guarantee.name != null) {
      write("\"");
      write(guarantee.name);
      write("\" ");
    }
    expr(guarantee.expr);
    write(";");
  }

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

  public void visit(IntExpr e) {
    write(e.value);
  }

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
