/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.uiowa.cs.clc.kind2.Kind2Exception;
import edu.uiowa.cs.clc.kind2.util.Util;

/**
 * Writes Kind 2 debug output to a temporary file.
 * <p>
 * A logger built with the no-argument constructor discards everything written to it.
 */
public class DebugLogger {
  private final PrintWriter debug;

  /**
   * Constructs a logger that discards all output.
   */
  public DebugLogger() {
    debug = null;
  }

  /**
   * Constructs a logger writing to a new temporary file.
   *
   * @param prefix the prefix of the temporary file's name
   * @throws edu.uiowa.cs.clc.kind2.Kind2Exception if the file cannot be created
   */
  public DebugLogger(String prefix) {
    try {
      File debugFile = File.createTempFile(prefix, ".txt");
      debug = new PrintWriter(new FileWriter(debugFile), true);
    } catch (IOException e) {
      throw new Kind2Exception("Unable to create temporary debug file", e);
    }
  }

  /**
   * Writes an empty line.
   */
  public void println() {
    if (debug != null) {
      debug.println();
    }
  }

  /**
   * Writes a line of text.
   *
   * @param text the text to write
   */
  public void println(String text) {
    if (debug != null) {
      debug.println(text);
    }
  }

  /**
   * Writes a line of text followed by the contents of a file.
   *
   * @param text the text to write
   * @param file the file whose contents to append
   */
  public void println(String text, File file) {
    if (debug != null) {
      try {
        debug.println(text + ": " + file.getCanonicalPath());
      } catch (IOException e) {
        debug.println(text + ": " + file.getAbsolutePath());
      }
    }
  }

  /**
   * Saves the given contents to a temporary file for later inspection.
   *
   * @param prefix the prefix of the file's name
   * @param suffix the suffix of the file's name
   * @param contents the text to write
   * @return the file written, or null if debugging is off
   */
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

  /**
   * Deletes a file unless debugging is enabled, in which case it is kept.
   *
   * @param file the file to delete
   */
  public void deleteIfUnneeded(File file) {
    if (debug == null && file != null && file.exists()) {
      file.delete();
    }
  }
}
