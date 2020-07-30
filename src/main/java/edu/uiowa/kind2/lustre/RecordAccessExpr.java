package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public class RecordAccessExpr extends Expr {
	public final Expr record;
	public final String field;

	public RecordAccessExpr(Location location, Expr record, String field) {
		super(location);
		Assert.isNotNull(record);
		Assert.isNotNull(field);
		this.record = record;
		this.field = field;
	}

	public RecordAccessExpr(Expr record, String field) {
		this(Location.NULL, record, field);
	}
}