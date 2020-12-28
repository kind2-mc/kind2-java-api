/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

/**
 * Kind2 output log.
 */
public class Log
{
  /**
   * A level that gives a rough guide of the importance of the message.
   * Can be off, fatal, error, warn, note, info, debug, or trace.
   */
  private final LogLevel level;
  /**
   * The name of the Kind 2 module which wrote the log.
   */
  private final String source;
  /**
   * The log message.
   */
  private final String value;
  /**
   * Associated kind2Result object
   */
  private final Result kind2Result;
  /**
   * The original kind2 output for this object in pretty json format.
   */
  private final String prettyJson;
  /**
   * The original kind2 output for this object in json format
   */
  private final String json;
  /**
   * Associated line in the input file, if any.
   */
  private final String line;
  /**
   * Associated column in the input file, if any.
   */
  private final String column;
  /**
   * isHidden determines whether the current log is printed.
   */
  private boolean isHidden;

  public Log(Result kind2Result, JsonElement jsonElement)
  {
    this.kind2Result = kind2Result;
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    json = new GsonBuilder().create().toJson(jsonElement);
    this.level = LogLevel.getLevel(jsonObject.get(Labels.level).getAsString());
    this.source = jsonObject.get(Labels.source).getAsString();
    this.value = jsonObject.get(Labels.value).getAsString();
    this.line = jsonObject.get(Labels.line) == null ? null :
        jsonObject.get(Labels.line).getAsString();
    this.column = jsonObject.get(Labels.column) == null ? null :
        jsonObject.get(Labels.column).getAsString();
    hideSpecialLogs();
  }

  /**
   * determines whether the current log should be printed.
   */
  private void hideSpecialLogs()
  {
    this.isHidden = value.equals("Wallclock timeout.");
  }

  /**
   * @return A level that gives a rough guide of the importance of the message.
   * Can be off, fatal, error, warn, note, info, debug, or trace.
   */
  public LogLevel getLevel()
  {
    return level;
  }

  /**
   * @return The name of the Kind 2 module which wrote the log.
   */
  public String getSource()
  {
    return source;
  }

  /**
   * @return The log message.
   */
  public String getValue()
  {
    return value;
  }

  /**
   * @return the associated kind2 result for this log.
   */
  public Result getKind2Result()
  {
    return kind2Result;
  }

  /**
   * @return The original kind2 output for this object in pretty json format.
   */
  public String getJson()
  {
    return prettyJson;
  }

  /**
   * @return the associated line in the input file, if any.
   */
  public String getLine()
  {
    return line;
  }

  /**
   * @return the associated column in the input file, if any.
   */
  public String getColumn()
  {
    return column;
  }

  @Override
  public String toString()
  {
    return json;
  }

  /**
   * @return  a boolean that determines whether the current log is printed.
   */
  public boolean isHidden()
  {
    return isHidden;
  }
}
