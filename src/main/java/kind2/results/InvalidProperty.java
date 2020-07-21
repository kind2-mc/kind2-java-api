package kind2.results;

import java.util.Collections;
import java.util.List;

import kind2.util.Util;

/**
 * An invalid property
 */
public final class InvalidProperty extends Property {
	private final String source;
	private Counterexample cex;
	private List<String> conflicts;
	private String report;

//	public InvalidProperty(String name, String source, Counterexample cex, List<String> conflicts,
//			double runtime) {
//		super(name, runtime);
//		this.source = source;
//		this.conflicts = Util.safeList(conflicts);
//		this.cex = cex;
//	}

	public InvalidProperty(String name, String source, Counterexample cex, List<String> conflicts, double runtime, String report) {
		super(name, runtime);
		this.source = source;
		this.conflicts = Util.safeList(conflicts);
		this.cex = cex;
		this.report = report;
	}

	/**
	 * Name of the engine used to find the counterexample (bmc, pdr, ...)
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Counterexample for the property
	 */
	public Counterexample getCounterexample() {
		return cex;
	}

	/**
	 * Conflicts (used in realizability analysis)
	 */
	public List<String> getConflicts() {
		return conflicts;
	}

	/**
	 * Report (used only by Sally)
	 */
	public String getReport() {
		return report;
	}

	@Override
	public void discardDetails() {
		cex = null;
		conflicts = Collections.emptyList();
	}
}
