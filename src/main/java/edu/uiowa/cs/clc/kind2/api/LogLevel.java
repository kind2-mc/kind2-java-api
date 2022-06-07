/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

public enum LogLevel {
  OFF, FATAL, ERROR, WARN, NOTE, INFO, DEBUG, TRACE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  public String getOption() {
    switch (this) {
      case OFF:
        return "-qq";
      case FATAL:
        return "-q";
      case ERROR:
        return "-s";
      case WARN:
      case NOTE:
        return "";
      case INFO:
        return "-v";
      case DEBUG:
        return "-vv";
      case TRACE:
        return "-vvv";
      default:
        throw new IllegalArgumentException("Error: Unknown log level.");
    }
  }
}
