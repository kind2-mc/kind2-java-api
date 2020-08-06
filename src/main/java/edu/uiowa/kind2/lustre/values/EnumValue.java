/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre.values;

import edu.uiowa.kind2.lustre.BinaryOp;
import edu.uiowa.kind2.lustre.UnaryOp;
import edu.uiowa.kind2.util.StringNaturalOrdering;

/**
 * An enumerated value (only used during counter-example output)
 */
public class EnumValue extends Value implements Comparable<EnumValue> {
	public final String value;

	public EnumValue(String value) {
		this.value = value;
	}

	@Override
	public Value applyBinaryOp(BinaryOp op, Value right) {

		if (right instanceof UnknownValue) {
			return UnknownValue.UNKNOWN;
		}

		if (!(right instanceof EnumValue)) {
			return null;
		}

		EnumValue other = (EnumValue) right;

		switch (op) {
		case EQUAL:
			return BooleanValue.fromBoolean(equals(other));

		case NOTEQUAL:
			return BooleanValue.fromBoolean(!equals(other));

		default:
			return null;
		}
	}

	@Override
	public Value applyUnaryOp(UnaryOp op) {
		return null;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EnumValue) {
			EnumValue other = (EnumValue) obj;
			return value.equals(other.value);
		}
		return false;
	}

	@Override
	public int compareTo(EnumValue other) {
		return new StringNaturalOrdering().compare(value, other.value);
	}
}
