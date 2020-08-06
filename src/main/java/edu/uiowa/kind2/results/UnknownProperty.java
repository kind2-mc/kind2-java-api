/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

/**
 * An unknown property
 */
public final class UnknownProperty extends Property {
	private final int trueFor;
	private Counterexample cex;

	public UnknownProperty(String name, int trueFor, Counterexample cex, double runtime) {
		super(name, runtime);
		this.trueFor = trueFor;
		this.cex = cex;
	}

	/**
	 * How many steps the property was true for in the base step
	 */
	public int getTrueFor() {
		return trueFor;
	}

	/**
	 * Inductive counterexample for the property, only available if
	 * Kind2Api.setInductiveCounterexamples()
	 */
	public Counterexample getInductiveCounterexample() {
		return cex;
	}

	@Override
	public void discardDetails() {
		cex = null;
	}
}
