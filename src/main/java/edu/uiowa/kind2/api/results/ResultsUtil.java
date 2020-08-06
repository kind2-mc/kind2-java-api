/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

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
