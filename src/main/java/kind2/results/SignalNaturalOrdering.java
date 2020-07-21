package kind2.results;

import java.util.Comparator;

import kind2.util.StringNaturalOrdering;

public class SignalNaturalOrdering implements Comparator<Signal<?>> {
	@Override
	public int compare(Signal<?> a, Signal<?> b) {
		return new StringNaturalOrdering().compare(a.getName(), b.getName());
	}
}
