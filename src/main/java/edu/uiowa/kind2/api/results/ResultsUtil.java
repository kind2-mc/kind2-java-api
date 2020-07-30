package edu.uiowa.kind2.api.results;

public class ResultsUtil {
	public static MultiStatus getMultiStatus(AnalysisResult result) {
		if (result instanceof CompositeAnalysisResult) {
			return ((CompositeAnalysisResult) result).getMultiStatus();
		} else if (result instanceof Result) {
			return ((Result) result).getMultiStatus();
		} else {
			return null;
		}
	}
}
