/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A utility class for constructing Lustre expressions.
 */
public class ExprUtil {

  /* Binary Expressions */

  /**
   * Construct an and expression
   * <p>
   * Lustre: {@code <left> and <right>}
   *
   * @param left  left side of the and expression
   * @param right right side of the and expression
   * @return the and expression
   */
  public static Expr and(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.AND, right);
  }

  /**
   * Construct an or expression
   * <p>
   * Lustre: {@code <left> or <right>}
   *
   * @param left  left side of the or expression
   * @param right right side of the or expression
   * @return the or expression
   */
  public static Expr or(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.OR, right);
  }

  /**
   * Construct an imply expression
   * <p>
   * Lustre: {@code <left> => <right>}
   *
   * @param left  left side of the imply expression
   * @param right right side of the imply expression
   * @return the imply expression
   */
  public static Expr implies(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.IMPLIES, right);
  }

  /**
   * Construct an xor expression
   * <p>
   * Lustre: {@code <left> xor <right>}
   *
   * @param left  left side of the xor expression
   * @param right right side of the xor expression
   * @return the xor expression
   */
  public static Expr xor(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.XOR, right);
  }

  /**
   * Construct an arrow expression
   * <p>
   * Lustre: {@code <left> -> <right>}
   *
   * @param left  left side of the arrow expression
   * @param right right side of the arrow expression
   * @return the arrow expression
   */
  public static Expr arrow(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.ARROW, right);
  }

  /**
   * Construct a less-than expression
   * <p>
   * Lustre: {@code <left> < <right>}
   *
   * @param left  left side of the less-than expression
   * @param right right side of the less-than expression
   * @return the less-than expression
   */
  public static Expr less(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.LESS, right);
  }

  /**
   * Construct a less-than-or-equal expression
   * <p>
   * Lustre: {@code <left> <= <right>}
   *
   * @param left  left side of the less-than-or-equal expression
   * @param right right side of the less-than-or-equal expression
   * @return the less-than-or-equal expression
   */
  public static Expr lessEqual(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.LESSEQUAL, right);
  }

  /**
   * Construct a greater-than expression
   * <p>
   * Lustre: {@code <left> > <right>}
   *
   * @param left  left side of the greater-than expression
   * @param right right side of the greater-than expression
   * @return the greater-than expression
   */
  public static Expr greater(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.GREATER, right);
  }

  /**
   * Construct a greater-than-or-equal expression
   * <p>
   * Lustre: {@code <left> >= <right>}
   *
   * @param left  left side of the greater-than-or-equal expression
   * @param right right side of the greater-than-or-equal expression
   * @return the greater-than-or-equal expression
   */
  public static Expr greaterEqual(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.GREATEREQUAL, right);
  }

  /**
   * Construct an equal expression
   * <p>
   * Lustre: {@code <left> = <right>}
   *
   * @param left  left side of the equal expression
   * @param right right side of the equal expression
   * @return the equal expression
   */
  public static Expr equal(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.EQUAL, right);
  }

  /**
   * Construct a not-equal expression
   * <p>
   * Lustre: {@code <left> <> <right>}
   *
   * @param left  left side of the not-equal expression
   * @param right right side of the not-equal expression
   * @return the not-equal expression
   */
  public static Expr notEqual(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.NOTEQUAL, right);
  }

  /**
   * Construct a plus expression
   * <p>
   * Lustre: {@code <left> + <right>}
   *
   * @param left  left side of the plus expression
   * @param right right side of the plus expression
   * @return the plus expression
   */
  public static Expr plus(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.PLUS, right);
  }

  /**
   * Construct a minus expression
   * <p>
   * Lustre: {@code <left> - <right>}
   *
   * @param left  left side of the minus expression
   * @param right right side of the minus expression
   * @return the minus expression
   */
  public static Expr minus(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.MINUS, right);
  }

  /**
   * Construct a multiply expression
   * <p>
   * Lustre: {@code <left> * <right>}
   *
   * @param left  left side of the multiply expression
   * @param right right side of the multiply expression
   * @return the multiply expression
   */
  public static Expr multiply(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.MULTIPLY, right);
  }

  /**
   * Construct a mod expression
   * <p>
   * Lustre: {@code <left> mod <right>}
   *
   * @param left  left side of the mod expression
   * @param right right side of the mod expression
   * @return the mod expression
   */
  public static Expr mod(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.MODULUS, right);
  }

  /**
   * Construct an integer-divide expression
   * <p>
   * Lustre: {@code <left> div <right>}
   *
   * @param left  left side of the integer-divide expression
   * @param right right side of the integer-divide expression
   * @return the integer-divide expression
   */
  public static Expr intDivide(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.INT_DIVIDE, right);
  }

  /**
   * Construct a divide expression
   * <p>
   * Lustre: {@code <left> / <right>}
   *
   * @param left  left side of the divide expression
   * @param right right side of the divide expression
   * @return the divide expression
   */
  public static Expr divide(Expr left, Expr right) {
    return new BinaryExpr(left, BinaryOp.DIVIDE, right);
  }

  /* Unary Expressions */

  /**
   * Construct a negative expression
   * <p>
   * Lustre: {@code -<expr>}
   *
   * @param expr expression to get the negative value of
   * @return the negative expression
   */
  public static Expr negative(Expr expr) {
    return new UnaryExpr(UnaryOp.NEGATIVE, expr);
  }

  /**
   * Construct a not expression
   * <p>
   * Lustre: {@code not <expr>}
   *
   * @param expr expression to negate
   * @return the not expression
   */
  public static Expr not(Expr expr) {
    return new UnaryExpr(UnaryOp.NOT, expr);
  }

  /**
   * Construct a pre expression
   * <p>
   * Lustre: {@code pre <expr>}
   *
   * @param expr expression to get the previous value of
   * @return the pre expression
   */
  public static Expr pre(Expr expr) {
    return new UnaryExpr(UnaryOp.PRE, expr);
  }

  /* IdExpr Expressions */

  /**
   * Construct an id expression
   * <p>
   * Lustre: {@code <expr>}
   *
   * @param id identifier for component, variable, or constant
   * @return the id expression
   */
  public static IdExpr id(String id) {
    return new IdExpr(id);
  }

  /* Literal Expressions */

  /**
   * Construct a real value expression
   * <p>
   * Lustre: {@code <str>}
   *
   * @param str a floating point number
   * @return the real value expression
   */
  public static Expr real(String str) {
    return new RealExpr(new BigDecimal(str));
  }

  /**
   * Construct an integer value expression
   * <p>
   * Lustre: {@code <str>}
   *
   * @param str an integer number
   * @return the integer value expression
   */
  public static Expr integer(String str) {
    return new IntExpr(new BigInteger(str));
  }

  /**
   * Construct an integer value expression
   * <p>
   * Lustre: {@code <i>}
   *
   * @param value an integer number
   * @return the integer value expression
   */
  public static Expr integer(long value) {
    return new IntExpr(BigInteger.valueOf(value));
  }

  /**
   * Construct a mode-reference expression
   * <p>
   * Lustre: {@code <path>}
   *
   * @param path path to mode
   * @return the mode-reference expression
   */
  public static Expr modeRef(String... path) {
    return new ModeRefExpr(path);
  }

  /* component calls */

  /**
   * Construct a function-call expression
   * <p>
   * Lustre: {@code <name>(<args[0]>, <args[1]>, ...)}
   *
   * @param name name of the function
   * @param args a list of argument expressions
   * @return the function-call expression
   */
  public static Expr functionCall(IdExpr name, List<Expr> args) {
    return new ComponentCallExpr(name.id, args);
  }

  /**
   * Construct a function-call expression
   * <p>
   * Lustre: {@code <name>(<args[0]>, <args[1]>, ...)}
   *
   * @param name name of the function
   * @param args an array of argument expressions
   * @return the function-call expression
   */
  public static Expr functionCall(IdExpr name, Expr... args) {
    return new ComponentCallExpr(name.id, Arrays.asList(args));
  }

  /**
   * Construct a node-call expression
   * <p>
   * Lustre: {@code <name>(<args[0]>, <args[1]>, ...)}
   *
   * @param name name of the node
   * @param args a list of argument expressions
   * @return the node-call expression
   */
  public static Expr nodeCall(IdExpr name, List<Expr> args) {
    return new ComponentCallExpr(name.id, args);
  }

  /**
   * Construct a node-call expression
   * <p>
   * Lustre: {@code <name>(<args[0]>, <args[1]>, ...)}
   *
   * @param name name of the node
   * @param args an array of argument expressions
   * @return the node-call expression
   */
  public static Expr nodeCall(IdExpr name, Expr... args) {
    return new ComponentCallExpr(name.id, Arrays.asList(args));
  }

  public static Expr TRUE = new BoolExpr(true);
  public static Expr FALSE = new BoolExpr(false);

  /* Cast Expressions */

  /**
   * Construct an integer-cast expression
   * <p>
   * Lustre: {@code int <expr>}
   *
   * @param expr expression to cast to integer
   * @return the integer-cast expression
   */
  public static Expr castInt(Expr expr) {
    return new CastExpr(TypeUtil.INT, expr);
  }

  /**
   * Construct an real-cast expression
   * <p>
   * Lustre: {@code real <expr>}
   *
   * @param expr expression to cast to real
   * @return the real-cast expression
   */
  public static Expr castReal(Expr expr) {
    return new CastExpr(TypeUtil.REAL, expr);
  }

  /* Miscellaneous Expressions */

  /**
   * Construct an and expression
   * <p>
   * Lustre: {@code <conjuncts[0]> and <conjuncts[1]> and ...}
   *
   * @param conjuncts a list of conjuncts
   * @return the and expression
   */
  public static Expr and(List<Expr> conjuncts) {
    return conjuncts.stream().reduce((acc, e) -> and(acc, e)).orElse(TRUE);
  }

  /**
   * Construct an and expression
   * <p>
   * Lustre: {@code <e[0]> and <e[1]> and ...}
   *
   * @param e an array of conjuncts
   * @return the and expression
   */
  public static Expr and(Expr... e) {
    return and(Arrays.asList(e));
  }

  /**
   * Construct an or expression
   * <p>
   * Lustre: {@code <disjuncts[0]> or <disjuncts[1]> or ...}
   *
   * @param disjuncts a list of disjuncts
   * @return the or expression
   */
  public static Expr or(List<Expr> disjuncts) {
    return disjuncts.stream().reduce((acc, e) -> or(acc, e)).orElse(FALSE);
  }

  /**
   * Construct an or expression
   * <p>
   * Lustre: {@code <e[0]> or <e[1]> or ...}
   *
   * @param e an array of disjuncts
   * @return the or expression
   */
  public static Expr or(Expr... e) {
    return or(Arrays.asList(e));
  }

  /**
   * Construct an xor expression
   * <p>
   * Lustre: {@code <disjuncts[0]> xor <disjuncts[1]> xor ...}
   *
   * @param disjuncts a list of disjuncts
   * @return the xor expression
   */
  public static Expr xor(List<Expr> disjuncts) {
    return disjuncts.stream().reduce((acc, e) -> xor(acc, e)).orElse(FALSE);
  }

  /**
   * Construct an xor expression
   * <p>
   * Lustre: {@code <e[0]> xor <e[1]> xor ...}
   *
   * @param e an array of disjuncts
   * @return the xor expression
   */
  public static Expr xor(Expr... e) {
    return xor(Arrays.asList(e));
  }

  /**
   * Construct an if-then-else expression
   * <p>
   * Lustre: {@code if <cond> then <thenExpr> else <elseExpr>}
   *
   * @param cond     condition part of the if-then-else expression
   * @param thenExpr then part of the if-then-else expression
   * @param elseExpr else part of the if-then-else expression
   * @return the if-then-else expression
   */
  public static Expr ite(Expr cond, Expr thenExpr, Expr elseExpr) {
    return new IfThenElseExpr(cond, thenExpr, elseExpr);
  }

  /**
   * Construct a list expression
   * <p>
   * Lustre: {@code (<list[0]>, <list[1]>, ...)}
   *
   * @param list a list of sub-expressions
   * @return the list expressions
   */
  public static Expr list(List<? extends Expr> list) {
    return new ListExpr(list);
  }

  /**
   * Construct a record-literal expression
   * <p>
   * Lustre: {@code id} {{@code <fields[0].key> = <fields[0].value>, ...}}
   *
   * @param id     record type name
   * @param fields a map from fields to values
   * @return the record-literal expression
   */
  public static Expr recordLiteral(String id, Map<String, Expr> fields) {
    return new RecordExpr(id, fields);
  }

  /**
   * Construct an array expression
   * <p>
   * Lustre: {@code [<elements[0]>, <elements[1]>, ...}
   *
   * @param elements a list of sub-expression elements
   * @return the array expression
   */
  public static Expr array(List<? extends Expr> elements) {
    return new ArrayExpr(elements);
  }

  /**
   * Construct a record-access expression
   * <p>
   * Lustre: {@code <record>.<field>}
   *
   * @param record record of the field to access
   * @param field field to access
   * @return the record-access expression
   */
  public static Expr recordAccess(Expr record, String field) {
    return new RecordAccessExpr(record, field);
  }
}
