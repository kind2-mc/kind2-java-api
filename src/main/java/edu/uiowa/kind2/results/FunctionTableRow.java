/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

import java.util.List;

import edu.uiowa.kind2.lustre.values.BooleanValue;
import edu.uiowa.kind2.lustre.values.EnumValue;
import edu.uiowa.kind2.lustre.values.IntegerValue;
import edu.uiowa.kind2.lustre.values.RealValue;
import edu.uiowa.kind2.lustre.values.Value;
import edu.uiowa.kind2.util.Util;

public class FunctionTableRow implements Comparable<FunctionTableRow> {
	private final List<Value> inputs;
	private final Value output;

	public FunctionTableRow(List<Value> inputs, Value output) {
		this.inputs = Util.safeList(inputs);
		this.output = output;
	}

	public List<Value> getInputs() {
		return inputs;
	}

	public Value getOutput() {
		return output;
	}

	@Override
	public int hashCode() {
		return inputs.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FunctionTableRow) {
			FunctionTableRow row = (FunctionTableRow) o;
			return row.inputs.equals(inputs);
		}
		return false;
	}

	@Override
	public int compareTo(FunctionTableRow other) {
		for (int i = 0; i < inputs.size(); i++) {
			Value x = inputs.get(i);
			Value y = other.inputs.get(i);
			int r = compareValues(x, y);
			if (r != 0) {
				return r;
			}
		}

		return 0;
	}

	private int compareValues(Value x, Value y) {
		if (x instanceof BooleanValue && y instanceof BooleanValue) {
			BooleanValue xbv = (BooleanValue) x;
			BooleanValue ybv = (BooleanValue) y;
			return Boolean.compare(xbv.value, ybv.value);
		} else if (x instanceof IntegerValue && y instanceof IntegerValue) {
			IntegerValue xiv = (IntegerValue) x;
			IntegerValue yiv = (IntegerValue) y;
			return xiv.value.compareTo(yiv.value);
		} else if (x instanceof RealValue && y instanceof RealValue) {
			RealValue xrv = (RealValue) x;
			RealValue yrv = (RealValue) y;
			return xrv.value.compareTo(yrv.value);
		} else if (x instanceof EnumValue && y instanceof EnumValue) {
			EnumValue xev = (EnumValue) x;
			EnumValue yev = (EnumValue) y;
			return xev.compareTo(yev);
		} else {
			throw new IllegalArgumentException();
		}
	}
}
