/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

public class Location {
	public final int line;
	public final int charPositionInLine;

	public Location(int line, int charPositionInLine) {
		this.line = line;
		this.charPositionInLine = charPositionInLine;
	}

	@Override
	public String toString() {
		return line + ":" + charPositionInLine;
	}

	public static final Location NULL = new Location(0, 0);
}
