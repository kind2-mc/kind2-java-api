package edu.uiowa.kind2.api;

public enum LogLevel {
  OFF, FATAL, ERROR, WARN, NOTE, INFO, DEBUG, TRACE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
