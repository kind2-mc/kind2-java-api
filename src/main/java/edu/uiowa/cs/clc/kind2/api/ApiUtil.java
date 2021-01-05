/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

import static java.util.stream.Collectors.joining;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.uiowa.cs.clc.kind2.Kind2Exception;
import edu.uiowa.cs.clc.kind2.util.Util;

public class ApiUtil {
  public static File writeLustreFile(String program) {
    return writeTempFile("kind2-api-", ".lus", program);
  }

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

  public static String readAll(InputStream inputStream) throws IOException {
    StringBuilder result = new StringBuilder();
    BufferedInputStream buffered = new BufferedInputStream(inputStream);
    int i;
    while ((i = buffered.read()) != -1) {
      result.append((char) i);
    }
    return result.toString();
  }

  public static String getQuotedCommand(List<String> pieces) {
    return pieces.stream().map(p -> p.contains(" ") ? "\"" + p + "\"" : p).collect(joining(" "));
  }
}
