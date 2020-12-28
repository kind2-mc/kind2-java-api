/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

public enum Kind2LogLevel
{
  off,
  fatal,
  error,
  warn,
  note,
  info,
  debug,
  trace;

  public static Kind2LogLevel getLevel(String level)
  {
    switch (level)
    {
      case "warn":
        return warn;
      case "fatal":
        return fatal;
      case "error":
        return error;
      case "info":
        return info;
      case "note":
        return note;
      case "off":
        return off;
      case "debug":
        return debug;
      case "trace":
        return trace;

      default:
        throw new UnsupportedOperationException("Log " + level + " is not defined");
    }
  }
}
