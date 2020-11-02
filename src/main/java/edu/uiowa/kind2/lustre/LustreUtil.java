/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import static edu.uiowa.kind2.lustre.NamedType.BOOL;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class LustreUtil {

  /* Binary Expressions */

  public static Expr and(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.AND, right);
  }

  public static Expr or(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.OR, right);
  }

  public static Expr implies(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.IMPLIES, right);
  }

  public static Expr xor(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.XOR, right);
  }

  public static Expr arrow(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.ARROW, right);
  }

  public static Expr less(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.LESS, right);
  }

  public static Expr lessEqual(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.LESSEQUAL, right);
  }

  public static Expr greater(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.GREATER, right);
  }

  public static Expr greaterEqual(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.GREATEREQUAL, right);
  }

  public static Expr equal(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.EQUAL, right);
  }

  public static Expr notEqual(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.NOTEQUAL, right);
  }

  public static Expr plus(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.PLUS, right);
  }

  public static Expr minus(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.MINUS, right);
  }

  public static Expr multiply(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.MULTIPLY, right);
  }

  public static Expr mod(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.MODULUS, right);
  }

  public static Expr intDivide(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.INT_DIVIDE, right);
  }

  public static Expr divide(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.DIVIDE, right);
  }

  /* Unary Expressions */

  public static Expr negative(Expr expr) {
    return new UnaryExpr(UnaryOp.NEGATIVE, expr);
  }

  public static Expr not(Expr expr) {
    return new UnaryExpr(UnaryOp.NOT, expr);
  }

  public static Expr pre(Expr expr) {
    return new UnaryExpr(UnaryOp.PRE, expr);
  }

  public static Expr optimizeNot(Expr expr) {
    if (expr instanceof UnaryExpr) {
      UnaryExpr ue = (UnaryExpr) expr;
      if (ue.op == UnaryOp.NOT) {
        return ue.expr;
      }
    }
    return new UnaryExpr(UnaryOp.NOT, expr);
  }

  /* IdExpr Expressions */

  public static IdExpr id(String id) {
    return new IdExpr(id);
  }

  public static IdExpr id(VarDecl vd) {
    return new IdExpr(vd.id);
  }

  /* Literal Expressions */

  public static RealExpr real(BigInteger bi) {
    return new RealExpr(new BigDecimal(bi));
  }

  public static RealExpr real(String str) {
    return new RealExpr(new BigDecimal(str));
  }

  public static RealExpr real(int i) {
    return new RealExpr(new BigDecimal(i));
  }

  public static IntExpr integer(int i) {
    return new IntExpr(i);
  }

  public static ModeRefExpr modeRef(String... path) {
    return new ModeRefExpr(path);
  }

  public static Expr functionCall(IdExpr name, List<Expr> args) {
    return new NodeCallExpr(name.id, args);
  }

  public static Expr functionCall(IdExpr name, Expr... args) {
    return new NodeCallExpr(name.id, Arrays.asList(args));
  }

  public static Expr nodeCall(IdExpr name, List<Expr> args) {
    return new NodeCallExpr(name.id, args);
  }

  public static Expr nodeCall(IdExpr name, Expr... args) {
    return new NodeCallExpr(name.id, Arrays.asList(args));
  }

  public static Expr TRUE = new BoolExpr(true);
  public static Expr FALSE = new BoolExpr(false);

  /* Cast Expressions */

  public static Expr castInt(Expr expr) {
    return new CastExpr(NamedType.INT, expr);
  }

  public static Expr castReal(Expr expr) {
    return new CastExpr(NamedType.REAL, expr);
  }

  /* Miscellaneous Expressions */

  public static Expr and(List<Expr> conjuncts) {
    return conjuncts.stream().reduce((acc, e) -> and(acc, e)).orElse(TRUE);
  }

  public static Expr and(Expr... e) {
    return and(Arrays.asList(e));
  }

  public static Expr or(List<Expr> disjuncts) {
    return disjuncts.stream().reduce((acc, e) -> or(acc, e)).orElse(FALSE);
  }

  public static Expr or(Expr... e) {
    return or(Arrays.asList(e));
  }

  public static Expr xor(List<Expr> disjuncts) {
    return disjuncts.stream().reduce((acc, e) -> xor(acc, e)).orElse(FALSE);
  }

  public static Expr xor(Expr... e) {
    return xor(Arrays.asList(e));
  }

  public static Expr chainRelation(BiFunction<Expr, Expr, Expr> f, Expr... e) {
    List<Expr> conjuncts = new ArrayList<>();
    for (int i = 0; i < e.length - 1; i++) {
      conjuncts.add(f.apply(e[i], e[i + 1]));
    }
    return and(conjuncts);
  }

  public static Expr lessEqual(Expr... e) {
    return chainRelation(LustreUtil::lessEqual, e);
  }

  public static Expr less(Expr... e) {
    return chainRelation(LustreUtil::less, e);
  }

  public static Expr greaterEqual(Expr... e) {
    return chainRelation(LustreUtil::greaterEqual, e);
  }

  public static Expr greater(Expr... e) {
    return chainRelation(LustreUtil::greater, e);
  }

  public static Expr equal(Expr... e) {
    return chainRelation(LustreUtil::equal, e);
  }

  public static Expr ite(Expr cond, Expr thenExpr, Expr elseExpr) {
    return new IfThenElseExpr(cond, thenExpr, elseExpr);
  }

  public static Expr typeConstraint(String id, Type type) {
    if (type instanceof SubrangeIntType) {
      return subrangeConstraint(id, (SubrangeIntType) type);
    } else if (type instanceof EnumType) {
      return enumConstraint(id, (EnumType) type);
    } else {
      return null;
    }
  }

  public static Expr subrangeConstraint(String id, SubrangeIntType subrange) {
    return boundConstraint(id, new IntExpr(subrange.low), new IntExpr(subrange.high));
  }

  public static Expr enumConstraint(String id, EnumType et) {
    return boundConstraint(id, new IntExpr(0), new IntExpr(et.values.size() - 1));
  }

  private static Expr boundConstraint(String id, Expr low, Expr high) {
    return and(lessEqual(low, id(id)), lessEqual(id(id), high));
  }

  /* Decls */

  public static VarDecl varDecl(String name, Type type) {
    return new VarDecl(name, type);
  }

  public static Equation eq(IdExpr id, Expr expr) {
    return new Equation(id, expr);
  }

  /* Nodes */

  public static Component historically() {
    return historically("historically");
  }

  public static Component historically(String name) {
    NodeBuilder historically = new NodeBuilder(name);

    IdExpr signal = historically.createVarInput("signal", BOOL);
    IdExpr holds = historically.createVarOutput("holds", BOOL);

    // historically: holds = signal and (true -> pre holds);
    historically.addEquation(holds, and(signal, arrow(TRUE, pre(holds))));

    return historically.build();
  }

  public static Component once() {
    return once("once");
  }

  public static Component once(String name) {
    NodeBuilder once = new NodeBuilder(name);

    IdExpr signal = once.createVarInput("signal", BOOL);
    IdExpr holds = once.createVarOutput("holds", BOOL);

    // once: holds = signal or (false -> pre holds);
    once.addEquation(holds, or(signal, arrow(FALSE, pre(holds))));

    return once.build();
  }

  public static Component since() {
    return since("since");
  }

  public static Component since(String name) {
    NodeBuilder since = new NodeBuilder(name);

    IdExpr a = since.createVarInput("a", BOOL);
    IdExpr b = since.createVarInput("b", BOOL);

    IdExpr holds = since.createVarOutput("holds", BOOL);

    // since: holds = b or (a and (false -> pre holds))
    since.addEquation(holds, or(b, and(a, arrow(FALSE, pre(holds)))));

    return since.build();
  }

  public static Component triggers() {
    return triggers("triggers");
  }

  public static Component triggers(String name) {
    NodeBuilder triggers = new NodeBuilder(name);

    IdExpr a = triggers.createVarInput("a", BOOL);
    IdExpr b = triggers.createVarInput("b", BOOL);

    IdExpr holds = triggers.createVarOutput("holds", BOOL);

    // triggers: holds = b and (a or (true -> pre holds))
    triggers.addEquation(holds, and(b, or(a, arrow(TRUE, pre(holds)))));

    return triggers.build();
  }
}
