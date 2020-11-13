/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.uiowa.kind2.Kind2Exception;

class PrettyPrintVisitorTest {
  String removeWhiteSpace(String s) {
    return s.codePoints().filter(c -> !Character.isWhitespace(c))
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  @Test
  void assumeTest() {
    // TODO: add tests for name
    String expected = "assume true;";

    Assertions.assertThrows(Kind2Exception.class, () -> new Assume(null, null));

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(new Assume(null, ExprUtil.TRUE));

    assertEquals(visitor.toString(), expected);
  }

  @Test
  void guaranteeTest() {
    // TODO: add tests for name
    String expected = "guarantee true;";

    Assertions.assertThrows(Kind2Exception.class, () -> new Guarantee(null, null));

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(new Guarantee(null, ExprUtil.TRUE));

    assertEquals(visitor.toString(), expected);
  }

  @Test
  void modeRefExprTest() {
    String expected = "::a::b::c";

    Assertions.assertThrows(Kind2Exception.class, ModeRefExpr::new);
    Assertions.assertThrows(Kind2Exception.class, () -> new ModeRefExpr((String) null));

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.expr(ExprUtil.modeRef("a", "b", "c"));

    assertEquals(visitor.toString(), expected);
  }

  @Test
  void constantTest() {
    String expected = "const c = true;";

    Assertions.assertThrows(Kind2Exception.class, () -> new Constant(null, null, ExprUtil.TRUE));
    Assertions.assertThrows(Kind2Exception.class, () -> new Constant("c", null, null));

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(new Constant("c", null, ExprUtil.TRUE));

    assertEquals(visitor.toString(), expected);
  }

  @Test
  void varDefTest() {
    String expected = "var x : bool = true;";

    Assertions.assertThrows(Kind2Exception.class,
        () -> new VarDef(null, TypeUtil.BOOL, ExprUtil.TRUE));
    Assertions.assertThrows(Kind2Exception.class, () -> new VarDef("x", null, ExprUtil.TRUE));
    Assertions.assertThrows(Kind2Exception.class, () -> new VarDef("x", TypeUtil.BOOL, null));

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(new VarDef("x", TypeUtil.BOOL, ExprUtil.TRUE));

    assertEquals(visitor.toString(), expected);
  }

  @Test
  void contractImportTest() {
    Assertions.assertThrows(Kind2Exception.class, () -> new ContractImport(null, null, null));

    String expected = "import Spec () returns ();";

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(new ContractImport("Spec", null, null));

    assertEquals(visitor.toString(), expected);

    expected = "import Spec (x) returns (y);";

    visitor = new PrettyPrintVisitor();
    visitor.visit(new ContractImport("Spec", Collections.singletonList(ExprUtil.id("x")),
        Collections.singletonList(ExprUtil.id("y"))));

    assertEquals(visitor.toString(), expected);
  }

  @Test
  void modeTest() {
    // TODO: add tests for name
    String expected = "mode m1 ();";

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(new Mode("m1", null, null));

    assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

    expected = "mode m2 (require true;);";

    visitor = new PrettyPrintVisitor();
    visitor
        .visit(new Mode("m2", Collections.singletonList(new Require(null, ExprUtil.TRUE)), null));

    assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

    expected = "mode m3 (ensure true;);";

    visitor = new PrettyPrintVisitor();
    visitor.visit(new Mode("m3", null, Collections.singletonList(new Ensure(null, ExprUtil.TRUE))));

    assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

    expected = "mode m4 (require true; ensure true;);";

    visitor = new PrettyPrintVisitor();
    visitor.visit(new Mode("m4", Collections.singletonList(new Require(null, ExprUtil.TRUE)),
        Collections.singletonList(new Ensure(null, ExprUtil.TRUE))));

    assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
  }

  @Test
  void contractBodyTest() {
    // raw strings require Java 12+
    String expected = "guarantee true; assume true; import Spec() returns ();"
        + "mode m(); const c = true; var x : bool = true;";

    ContractBodyBuilder c = new ContractBodyBuilder();

    Assertions.assertThrows(Kind2Exception.class, c::build);

    c.addGuarantee(ExprUtil.TRUE);
    c.addAssumption(ExprUtil.TRUE);
    c.importContract("Spec", null, null);
    ModeBuilder m = new ModeBuilder("m");
    c.addMode(m);
    c.createConstant("c", null, ExprUtil.TRUE);
    c.createVarDef("x", TypeUtil.BOOL, ExprUtil.TRUE);

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(c.build());

    assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
  }

  @Test
  void contractTest() {
    // raw strings require Java 12+
    String expected = "contract c() returns (); let guarantee true; tel";

    ContractBodyBuilder c = new ContractBodyBuilder();
    c.addGuarantee(ExprUtil.TRUE);

    Assertions.assertThrows(Kind2Exception.class, () -> new Contract(null, null, null, c.build()));

    Assertions.assertThrows(Kind2Exception.class, () -> new Contract("c", null, null, null));

    PrettyPrintVisitor visitor = new PrettyPrintVisitor();
    visitor.visit(new Contract("c", null, null, c.build()));

    assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
  }
}
