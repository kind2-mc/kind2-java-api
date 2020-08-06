/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre.visitors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.lustre.Assume;
import edu.uiowa.kind2.lustre.Constant;
import edu.uiowa.kind2.lustre.Contract;
import edu.uiowa.kind2.lustre.ContractImport;
import edu.uiowa.kind2.lustre.Guarantee;
import edu.uiowa.kind2.lustre.IdExpr;
import edu.uiowa.kind2.lustre.ImportedFunction;
import edu.uiowa.kind2.lustre.ImportedNode;
import edu.uiowa.kind2.lustre.Function;
import edu.uiowa.kind2.lustre.LustreUtil;
import edu.uiowa.kind2.lustre.Mode;
import edu.uiowa.kind2.lustre.ModeRefExpr;
import edu.uiowa.kind2.lustre.NamedType;
import edu.uiowa.kind2.lustre.VarDef;
import edu.uiowa.kind2.lustre.builders.ContractBodyBuilder;
import edu.uiowa.kind2.lustre.builders.FunctionBuilder;

class PrettyPrintVisitorTest {
	String removeWhiteSpace(String s) {
		return s.codePoints().filter(c -> !Character.isWhitespace(c))
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}

	@Test
	void assumeTest() {
		String expected = "assume true;";

		Assertions.assertThrows(Kind2Exception.class, () -> new Assume(null));

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new Assume(LustreUtil.TRUE));

		assertEquals(visitor.toString(), expected);
	}

	@Test
	void guaranteeTest() {
		String expected = "guarantee true;";

		Assertions.assertThrows(Kind2Exception.class, () -> new Guarantee(null));

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new Guarantee(LustreUtil.TRUE));

		assertEquals(visitor.toString(), expected);
	}

	@Test
	void modeRefExprTest() {
		String expected = "::a::b::c";

		Assertions.assertThrows(Kind2Exception.class, ModeRefExpr::new);
		Assertions.assertThrows(Kind2Exception.class, () -> new ModeRefExpr((String) null));

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(LustreUtil.modeRef("a", "b", "c"));

		assertEquals(visitor.toString(), expected);
	}

	@Test
	void constantTest() {
		String expected = "const c = true;";

		Assertions.assertThrows(Kind2Exception.class, () -> new Constant(null, null, LustreUtil.TRUE));
		Assertions.assertThrows(Kind2Exception.class, () -> new Constant("c", null, null));

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new Constant("c", null, LustreUtil.TRUE));

		assertEquals(visitor.toString(), expected);
	}

	@Test
	void varDefTest() {
		String expected = "var x : bool = true;";

		Assertions.assertThrows(Kind2Exception.class, () -> new VarDef(null, NamedType.BOOL, LustreUtil.TRUE));
		Assertions.assertThrows(Kind2Exception.class, () -> new VarDef("x", null, LustreUtil.TRUE));
		Assertions.assertThrows(Kind2Exception.class, () -> new VarDef("x", NamedType.BOOL, null));

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new VarDef("x", NamedType.BOOL, LustreUtil.TRUE));

		assertEquals(visitor.toString(), expected);
	}

	@Test
	void contractImportTest() {
		Assertions.assertThrows(Kind2Exception.class, () -> new ContractImport(null, null, null));

		String expected = "import Spec() returns ();";

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new ContractImport("Spec", null, null));

		assertEquals(visitor.toString(), expected);

		expected = "import Spec(x) returns (y);";

		visitor = new PrettyPrintVisitor();
		visitor.visit(new ContractImport("Spec", Collections.singletonList(LustreUtil.id("x")),
				Collections.singletonList(LustreUtil.id("y"))));

		assertEquals(visitor.toString(), expected);
	}

	@Test
	void modeTest() {
		String expected = "mode m1 ();";

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new Mode("m1", null, null));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

		expected = "mode m2 (require true;);";

		visitor = new PrettyPrintVisitor();
		visitor.visit(new Mode("m2", Collections.singletonList(LustreUtil.TRUE), null));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

		expected = "mode m3 (ensure true;);";

		visitor = new PrettyPrintVisitor();
		visitor.visit(new Mode("m3", null, Collections.singletonList(LustreUtil.TRUE)));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

		expected = "mode m4 (require true; ensure true;);";

		visitor = new PrettyPrintVisitor();
		visitor.visit(
				new Mode("m4", Collections.singletonList(LustreUtil.TRUE), Collections.singletonList(LustreUtil.TRUE)));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
	}

	@Test
	void contractBodyTest() {
		// raw strings require Java 12+
		String expected = "guarantee true; assume true; import Spec() returns ();"
				+ "mode m(); const c = true; var x : bool = true;";

		ContractBodyBuilder c = new ContractBodyBuilder();

		Assertions.assertThrows(Kind2Exception.class, c::build);

		c.addGuarantee(LustreUtil.TRUE);
		c.addAssumption(LustreUtil.TRUE);
		c.importContract("Spec", null, null);
		c.addMode("m", null, null);
		c.createConstant("c", null, LustreUtil.TRUE);
		c.createVarDef("x", NamedType.BOOL, LustreUtil.TRUE);

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(c.build());

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
	}

	@Test
	void contractTest() {
		// raw strings require Java 12+
		String expected = "contract c() returns (); let guarantee true; tel;";

		ContractBodyBuilder c = new ContractBodyBuilder();
		c.addGuarantee(LustreUtil.TRUE);

		Assertions.assertThrows(Kind2Exception.class, () -> new Contract(null, null, null, c.build()));

		Assertions.assertThrows(Kind2Exception.class, () -> new Contract("c", null, null, null));

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new Contract("c", null, null, c.build()));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
	}

	@Test
	void importedFunctionTest() {
		Assertions.assertThrows(Kind2Exception.class, () -> new ImportedFunction(null, null, null, null));

		String expected = "function imported f() returns ();";

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new ImportedFunction("f", null, null, null));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

		// raw strings require Java 12+
		expected = "function imported f() returns (); (*@contract guarantee true; *)";

		ContractBodyBuilder c = new ContractBodyBuilder();
		c.addGuarantee(LustreUtil.TRUE);

		visitor = new PrettyPrintVisitor();
		visitor.visit(new ImportedFunction("f", null, null, c.build()));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
	}

	@Test
	void importedNodeTest() {
		Assertions.assertThrows(Kind2Exception.class, () -> new ImportedNode(null, null, null, null));

		String expected = "node imported n() returns ();";

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(new ImportedNode("n", null, null, null));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

		// raw strings require Java 12+
		expected = "node imported n() returns (); (*@contract guarantee true; *)";

		ContractBodyBuilder c = new ContractBodyBuilder();
		c.addGuarantee(LustreUtil.TRUE);

		visitor = new PrettyPrintVisitor();
		visitor.visit(new ImportedNode("n", null, null, c.build()));

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
	}

	@Test
	void kind2FunctionTest() {
		Assertions.assertThrows(Kind2Exception.class,
				() -> new Function(null, null, null, null, null, null, null, null));

		FunctionBuilder f = new FunctionBuilder("even");

		IdExpr N = f.createInput("N", NamedType.INT);
		IdExpr B = f.createOutput("B", NamedType.BOOL);
		f.addEquation(B, LustreUtil.equal(LustreUtil.mod(N, LustreUtil.integer(2)), LustreUtil.integer(0)));

		String expected = "function even(N : int) returns (B : bool); let B = ((N mod 2) = 0); tel;";

		PrettyPrintVisitor visitor = new PrettyPrintVisitor();
		visitor.visit(f.build());

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));

		// raw strings require Java 12+
		expected = "function even(N : int) returns (B : bool); (*@contract guarantee true; *)"
				+ "let B = ((N mod 2) = 0); tel;";

		ContractBodyBuilder c = new ContractBodyBuilder();
		c.addGuarantee(LustreUtil.TRUE);

		f.setContractBody(c.build());

		visitor = new PrettyPrintVisitor();
		visitor.visit(f.build());

		assertEquals(removeWhiteSpace(visitor.toString()), removeWhiteSpace(expected));
	}
}
