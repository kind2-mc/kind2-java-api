/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * The severity levels Kind 2 log messages can carry.
 */
public enum LogLevel
{
  /**
   * Disables all messages.
   */
  off("off"), // -1: off disables all messages
  /**
   * The most severe level, used for unrecoverable errors.
   */
  fatal("fatal"), // 0: most severe
  /**
   * Errors that prevent Kind 2 from producing a result.
   */
  error("error"), // 1
  /**
   * Conditions that may indicate a problem.
   */
  warn("warn"), // 2
  /**
   * Notable events that are not problems.
   */
  note("note"), // 3
  /**
   * Informational progress messages.
   */
  info("info"), // 4
  /**
   * Messages intended for debugging Kind 2.
   */
  debug("debug"), // 5
  /**
   * The least severe level, used for detailed tracing.
   */
  trace("trace"); // 6: least severe

  private final String value;

  LogLevel(String value)
  {
    this.value = value;
  }

  /**
   * Returns the log level denoted by the given Kind 2 level name.
   *
   * @param level the Kind 2 level name
   * @return the corresponding level
   * @throws UnsupportedOperationException if the name is not recognised
   */
  public static LogLevel getLevel(String level)
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

  @Override
  public String toString()
  {
    return this.value;
  }
}
