/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api;

public enum SolverOption {
	BOOLECTOR, CVC4, YICES, YICES2, Z3;

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
