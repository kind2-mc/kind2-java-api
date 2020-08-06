/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2;

public class Assert {
	public static void isNotNull(Object o) {
		if (o == null) {
			throw new Kind2Exception("Object unexpectedly null");
		}
	}

	public static void isTrue(boolean b) {
		if (!b) {
			throw new Kind2Exception("Assertion failed");
		}
	}

	public static void isFalse(boolean b) {
		if (b) {
			throw new Kind2Exception("Assertion failed");
		}
	}
}
