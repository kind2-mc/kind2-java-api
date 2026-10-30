/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

import static java.util.stream.Collectors.joining;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import edu.uiowa.cs.clc.kind2.Kind2Exception;
import edu.uiowa.cs.clc.kind2.util.Util;

/**
 * Helpers for writing Kind 2 input files and reading its output.
 */
public class ApiUtil {
  private ApiUtil() {
  }

  /**
   * Writes a Lustre program to a temporary file for Kind 2 to read.
   *
   * @param program the Lustre program text
   * @return the file the program was written to
   */
  public static File writeLustreFile(String program) {
    return writeTempFile("kind2-api-", ".lus", program);
  }

  /**
   * Writes an interpreter input to a temporary file for Kind 2 to read.
   *
   * @param program the interpreter input text
   * @return the file the input was written to
   */
  public static File writeInterpreterFile(String program) {
    return writeTempFile("kind2-api-", ".json", program);
  }

  /**
   * Writes the given contents to a new temporary file.
   *
   * @param fileName the base name of the file
   * @param fileExt the file extension, may be null
   * @param contents the text to write, may be null to leave the file empty
   * @return the newly created file
   */
  public static File writeTempFile(String fileName, String fileExt, String contents) {
    File file = null;
    try {
      file = File.createTempFile(fileName, fileExt);
      if (contents != null) {
        Util.writeToFile(contents, file);
      }
      return file;
    } catch (IOException e) {
      throw new Kind2Exception("Cannot write to file: " + file, e);
    }
  }

  /**
   * Reads a stream to its end and decodes it as UTF-8.
   *
   * @param inputStream the stream to read
   * @return the full contents of the stream
   * @throws java.io.IOException if the stream cannot be read
   */
  public static String readAll(InputStream inputStream) throws IOException {
    // Decode once at the end, so that a multi-byte character is never split between two reads.
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int n;
    while ((n = inputStream.read(buffer)) != -1) {
      bytes.write(buffer, 0, n);
    }
    return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
  }

  /**
   * Renders a command line with each argument quoted.
   *
   * @param pieces the command and its arguments
   * @return the quoted command line
   */
  public static String getQuotedCommand(List<String> pieces) {
    return pieces.stream().map(p -> p.contains(" ") ? "\"" + p + "\"" : p).collect(joining(" "));
  }
}
