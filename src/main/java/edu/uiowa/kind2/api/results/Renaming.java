/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api.results;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.lustre.VarDecl;
import edu.uiowa.kind2.lustre.values.EnumValue;
import edu.uiowa.kind2.lustre.values.Value;
import edu.uiowa.kind2.results.Counterexample;
import edu.uiowa.kind2.results.FunctionTable;
import edu.uiowa.kind2.results.FunctionTableRow;
import edu.uiowa.kind2.results.InconsistentProperty;
import edu.uiowa.kind2.results.InvalidProperty;
import edu.uiowa.kind2.results.Property;
import edu.uiowa.kind2.results.Signal;
import edu.uiowa.kind2.results.UnknownProperty;
import edu.uiowa.kind2.results.ValidProperty;

/**
 * A class for renaming and removing variables from analysis results
 *
 * @see MapRenaming
 */
public abstract class Renaming {
	/**
	 * Returns the new name for a given name, or null if the original name
	 * should be hidden. This method should always return the same result when
	 * given the same input.
	 *
	 * @param original
	 *            Original variable name
	 * @return the new variable name or null if variable should be hidden
	 */
	public abstract String rename(String original);

	/**
	 * Rename property and signals (if present), possibly omitting some
	 *
	 * @param property
	 *            Property to be renamed
	 * @return Renamed version of the property, or <code>null</code> if there is
	 *         no renaming for the property
	 */
	public Property rename(Property property) {
		if (property instanceof ValidProperty) {
			return rename((ValidProperty) property);
		} else if (property instanceof InvalidProperty) {
			return rename((InvalidProperty) property);
		} else if (property instanceof UnknownProperty) {
			return rename((UnknownProperty) property);
		} else if (property instanceof InconsistentProperty) {
			return rename((InconsistentProperty) property);
		} else {
			return null;
		}
	}

	/**
	 * Rename valid property and signals (if present), possibly omitting some
	 *
	 * Note: Invariants (if present) will not be renamed
	 *
	 * @param property
	 *            Property to be renamed
	 * @return Renamed version of the property, or <code>null</code> if there is
	 *         no renaming for the property
	 */
	public ValidProperty rename(ValidProperty property) {
		String name = rename(property.getName());
		if (name == null) {
			return null;
		}

		return new ValidProperty(name, property.getSource(), property.getK(), property.getRuntime(),
				property.getInvariants(), rename(this::renameIVC, property.getIvc()), property.getInvariantSets(),
				rename(this::renameIVC, property.getIvcSets()), property.getMivcTimedOut());
	}

	/**
	 * Rename invalid property and signals (if present), possibly omitting some
	 *
	 * @param property
	 *            Property to be renamed
	 * @return Renamed version of the property, or <code>null</code> if there is
	 *         no renaming for the property
	 */
	public InvalidProperty rename(InvalidProperty property) {
		String name = rename(property.getName());
		if (name == null) {
			return null;
		}

		return new InvalidProperty(name, property.getSource(), rename(property.getCounterexample()),
				rename(this::rename, property.getConflicts()), property.getRuntime());
	}

	/**
	 * Rename unknown property and signals (if present), possibly omitting some
	 *
	 * @param property
	 *            Property to be renamed
	 * @return Renamed version of the property, or <code>null</code> if there is
	 *         no renaming for the property
	 */
	public UnknownProperty rename(UnknownProperty property) {
		String name = rename(property.getName());
		if (name == null) {
			return null;
		}

		return new UnknownProperty(name, property.getTrueFor(), rename(property.getInductiveCounterexample()),
				property.getRuntime());
	}

	/**
	 * Rename inconsistent property
	 *
	 * @param property
	 *            Property to be renamed
	 * @return Renamed version of the property, or <code>null</code> if there is
	 *         no renaming for the property
	 */
	public InconsistentProperty rename(InconsistentProperty property) {
		String name = rename(property.getName());
		if (name == null) {
			return null;
		}

		return new InconsistentProperty(name, property.getSource(), property.getK(), property.getRuntime());
	}

	/**
	 * Rename signals in a counterexample, possibly omitting some
	 *
	 * @param cex
	 *            Counterexample to be renamed
	 * @return Renamed version of the counterexample
	 */
	protected Counterexample rename(Counterexample cex) {
		if (cex == null) {
			return null;
		}

		Counterexample result = new Counterexample(cex.getLength());

		for (Signal<Value> signal : cex.getSignals()) {
			Signal<Value> newSignal = rename(signal);
			if (newSignal != null) {
				result.addSignal(newSignal);
			}
		}

		for (FunctionTable table : cex.getFunctionTables()) {
			FunctionTable newTable = rename(table);
			if (newTable != null) {
				result.addFunctionTable(newTable);
			}
		}

		return result;
	}

	protected FunctionTable rename(FunctionTable table) {
		String name = rename(table.getName());
		if (name == null) {
			return null;
		}

		List<VarDecl> inputs = table.getInputs().stream().map(this::rename).collect(toList());
		if (inputs.contains(null)) {
			return null;
		}

		VarDecl output = rename(table.getOutput());
		if (output == null) {
			return null;
		}

		FunctionTable result = new FunctionTable(name, inputs, output);

		for (FunctionTableRow row : table.getRows()) {
			List<Value> inputValues = row.getInputs().stream().map(this::rename).collect(toList());
			Value outputValue = rename(row.getOutput());
			result.addRow(inputValues, outputValue);
		}

		return result;
	}

	private VarDecl rename(VarDecl vd) {
		String id = rename(vd.id);
		if (id == null) {
			return null;
		} else {
			return new VarDecl(id, vd.type);
		}
	}

	private Value rename(Value value) {
		if (value instanceof EnumValue) {
			EnumValue ev = (EnumValue) value;
			String renamedValue = rename(ev.value);
			if (renamedValue == null) {
				throw new Kind2Exception("Failed when renaming enumeration value: " + ev.value);
			}
			return new EnumValue(renamedValue);
		} else {
			return value;
		}
	}

	/**
	 * Rename signal
	 *
	 * @param <T>
	 *
	 * @param signal
	 *            The signal to be renamed
	 * @return Renamed version of the signal or <code>null</code> if there is no
	 *         renaming for it
	 */
	@SuppressWarnings("unchecked")
	private <T extends Value> Signal<T> rename(Signal<T> signal) {
		String name = rename(signal.getName());
		if (name == null) {
			return null;
		}

		Signal<T> newSignal = new Signal<>(name);
		for (Entry<Integer, T> entry : signal.getValues().entrySet()) {
			newSignal.putValue(entry.getKey(), (T) rename(entry.getValue()));
		}
		return newSignal;
	}

	/**
	 * Rename a collection of elements, possibly omitting some
	 *
	 * @param es
	 *            Strings to be renamed
	 * @return Renamed version of the conflicts
	 */
	private List<String> rename(Function<String, String> f, Collection<String> es) {
		List<String> updatedOrigList = new ArrayList<String>();
		for (String curOrigStr : es) {
			String updatedName = curOrigStr;
			if (curOrigStr.contains(".")) {
				updatedName = updatedNodeElemName(curOrigStr);
			}
			updatedOrigList.add(updatedName);
		}
		List<String> renamedList = updatedOrigList.stream().map(f).filter(e -> e != null).collect(toList());

		return renamedList;
	}

	private Set<List<String>> rename(Function<String, String> f, Set<List<String>> es) {
		Set<List<String>> set = new HashSet<List<String>>();
		for (List<String> curOrigList : es) {
			List<String> updatedOrigList = new ArrayList<String>();
			for (String curOrigStr : curOrigList) {
				String updatedName = curOrigStr;
				if (curOrigStr.contains(".")) {
					updatedName = updatedNodeElemName(curOrigStr);
				}
				updatedOrigList.add(updatedName);
			}
			List<String> renamedList = updatedOrigList.stream().map(f).filter(e -> e != null).collect(toList());
			set.add(renamedList);
		}
		return set;
	}

	private String updatedNodeElemName(String curOrigStr) {
		String updatedName;
		String nodeName = curOrigStr.substring(0, curOrigStr.lastIndexOf('.'));
		String elemName = curOrigStr.substring(curOrigStr.lastIndexOf('.'), curOrigStr.length());
		nodeName = nodeName.replaceAll("~([0-9]+)$", "");
		updatedName = nodeName + elemName;
		return updatedName;
	}

	/**
	 * Rename an IVC variable
	 *
	 * @param ivc
	 *            the string to be renamed
	 * @return Renamed version of the ivc string
	 */
	public String renameIVC(String ivc) {
		return rename(ivc);
	}
}
