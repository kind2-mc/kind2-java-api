/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api.xml;

import java.io.IOException;
import java.io.InputStream;

import edu.uiowa.kind2.Kind2Exception;

public class LineInputStream implements AutoCloseable {
  private final InputStream source;

  public LineInputStream(InputStream source) {
    this.source = source;
  }

  public String readLine() throws IOException {
    StringBuilder buffer = new StringBuilder();

    int c;
    while ((c = source.read()) != -1) {
      buffer.append((char) c);
      if (c == '\n') {
        return buffer.toString();
      }
    }

    if (buffer.length() == 0) {
      source.close();
      return null;
    } else {
      return buffer.toString();
    }
  }

  @Override
  public void close() {
    try {
      source.close();
    } catch (IOException e) {
      throw new Kind2Exception("Error closing input stream", e);
    }
  }
}
