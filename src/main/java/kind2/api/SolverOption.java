package kind2.api;

public enum SolverOption {
	BOOLECTOR, CVC4, YICES, YICES2, Z3;

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
