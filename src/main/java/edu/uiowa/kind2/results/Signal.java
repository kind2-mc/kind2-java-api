/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.lustre.values.Value;

/**
 * A signal is a trace of values for a specific variable
 * 
 * @param <T>
 *            Type of value contained in the signal
 */
public final class Signal<T extends Value> implements Comparable<Signal<T>> {
	private final String name;
	private final Map<Integer, T> values = new HashMap<>();

	public Signal(String name) {
		this.name = name;
	}

	/**
	 * Name of the signal
	 */
	public String getName() {
		return name;
	}

	public void putValue(int step, T value) {
		values.put(step, value);
	}

	/**
	 * Get the value of the signal on a specific step
	 * 
	 * @param step
	 *            Step to query the value at
	 * @return Value at the specified step or <code>null</code> if the signal
	 *         does not have a value on that step
	 */
	public T getValue(int step) {
		return values.get(step);
	}

	/**
	 * Get a time step indexed map containing all values for the signal
	 */
	public Map<Integer, T> getValues() {
		return Collections.unmodifiableMap(values);
	}

	/**
	 * Downcast the signal to a specific signal type
	 */
	public <S extends T> Signal<S> cast(Class<S> klass) {
		Signal<S> castSignal = new Signal<>(name);
		for (Integer step : values.keySet()) {
			Value value = values.get(step);
			if (klass.isInstance(value)) {
				castSignal.putValue(step, klass.cast(value));
			} else {
				throw new Kind2Exception(
						"Cannot cast " + value.getClass().getSimpleName() + " to " + klass.getSimpleName());
			}
		}
		return castSignal;
	}

	public Signal<T> rename(String newName) {
		Signal<T> copy = new Signal<>(newName);
		copy.values.putAll(values);
		return copy;
	}

	@Override
	public int compareTo(Signal<T> other) {
		return name.compareTo(other.name);
	}
}
