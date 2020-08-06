/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

/**
 * An inconsistent property
 */
public final class InconsistentProperty extends Property {
	private final String source;
	private final int k;

	public InconsistentProperty(String name, String source, int k, double runtime) {
		super(name, runtime);
		this.source = source;
		this.k = k;
	}

	/**
	 * Name of the engine used to prove the property inconsistent
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Number of steps until the property is inconsistent
	 */
	public int getK() {
		return k;
	}

	@Override
	public void discardDetails() {
	}
}
