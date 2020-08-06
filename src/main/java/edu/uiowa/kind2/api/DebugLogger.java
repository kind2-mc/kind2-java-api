/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.util.Util;

public class DebugLogger {
	private final PrintWriter debug;

	public DebugLogger() {
		debug = null;
	}

	public DebugLogger(String prefix) {
		try {
			File debugFile = File.createTempFile(prefix, ".txt");
			debug = new PrintWriter(new FileWriter(debugFile), true);
		} catch (IOException e) {
			throw new Kind2Exception("Unable to create temporary debug file", e);
		}
	}

	public void println() {
		if (debug != null) {
			debug.println();
		}
	}

	public void println(String text) {
		if (debug != null) {
			debug.println(text);
		}
	}

	public void println(String text, File file) {
		if (debug != null) {
			try {
				debug.println(text + ": " + file.getCanonicalPath());
			} catch (IOException e) {
				debug.println(text + ": " + file.getAbsolutePath());
			}
		}
	}

	public File saveFile(String prefix, String suffix, String contents) {
		if (debug != null) {
			try {
				File file = File.createTempFile(prefix, suffix);
				Util.writeToFile(contents, file);
				return file;
			} catch (IOException e) {
				throw new Kind2Exception("Unable to create temporary file", e);
			}
		} else {
			return null;
		}
	}

	public void deleteIfUnneeded(File file) {
		if (debug == null && file != null && file.exists()) {
			file.delete();
		}
	}
}
