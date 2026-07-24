/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

/**
 * The verbosity levels Kind 2 can run at.
 */
public enum LogLevel {
  /**
   * Disables all messages.
   */
  OFF,
  /**
   * Only fatal errors.
   */
  FATAL,
  /**
   * Errors and above.
   */
  ERROR,
  /**
   * Warnings and above.
   */
  WARN,
  /**
   * Notes and above.
   */
  NOTE,
  /**
   * Informational messages and above.
   */
  INFO,
  /**
   * Debug messages and above.
   */
  DEBUG,
  /**
   * All messages, including traces.
   */
  TRACE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  /**
   * Returns the Kind 2 command line flag selecting this level.
   *
   * @return the command line flag
   * @throws IllegalArgumentException if the level is not recognised
   */
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
