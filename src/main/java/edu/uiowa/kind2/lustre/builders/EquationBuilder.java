package edu.uiowa.kind2.lustre.builders;

import java.util.ArrayList;
import java.util.List;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.lustre.Equation;
import edu.uiowa.kind2.lustre.Expr;
import edu.uiowa.kind2.lustre.IdExpr;
import edu.uiowa.kind2.lustre.Location;
import edu.uiowa.kind2.lustre.VarDecl;

public class EquationBuilder {
	private List<IdExpr> lhs = new ArrayList<>();
	private Expr expr;

	public EquationBuilder() {
	}

	public EquationBuilder(Equation eq) {
		lhs.addAll(eq.lhs);
		expr = eq.expr;
	}

	public EquationBuilder addLhs(IdExpr ide) {
		this.lhs.add(ide);
		return this;
	}

	public EquationBuilder addLhs(String id) {
		this.lhs.add(new IdExpr(id));
		return this;
	}

	public EquationBuilder addLhs(VarDecl varDecl) {
		this.lhs.add(new IdExpr(varDecl.id));
		return this;
	}

	public EquationBuilder clearLhs() {
		this.lhs.clear();
		return this;
	}

	public EquationBuilder setExpr(Expr expr) {
		this.expr = expr;
		return this;
	}

	public Equation build() {
		if (lhs.isEmpty()) {
			throw new Kind2Exception("left-hand side is empty");
		}
		if (expr == null) {
			throw new Kind2Exception("right-hand side is empty");
		}
		return new Equation(Location.NULL, lhs, expr);
	}
}