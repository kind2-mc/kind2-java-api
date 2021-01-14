/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * This class a value in a stream.
 */
public class StepValue
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;

  /**
   * The step index in the stream sequence.
   */
  private final int time;
  /**
   * The value at the specified time.
   */
  private final Value kind2Value;
  /**
   * the associated stream.
   */
  private final Stream stream;

  public StepValue(Stream stream, JsonElement jsonElement)
  {
    this.stream = stream;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    time = jsonElement.getAsJsonArray().get(0).getAsInt();
    kind2Value = Value.getKind2Value(this, getKind2Type(), jsonElement.getAsJsonArray().get(1));
  }

  /**
   * @return the type of the current value
   */
  public Type getKind2Type()
  {
    return stream.getKind2Type();
  }

  /**
   * @return Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  /**
   * @return The value at the specified time.
   */
  public Value getKind2Value()
  {
    return kind2Value;
  }

  /**
   * @return the step index in the stream sequence.
   */
  public int getTime()
  {
    return time;
  }

  /**
   * @return the associated stream for this object.
   */
  public Stream getStream()
  {
    return stream;
  }

  /**
   * @return the associated kind2 result for this object.
   */
  public Result getKind2Result()
  {
    return stream.getKind2Result();
  }

  /**
   * @return the value of this object.
   * To get the original json value, use {@link StepValue#getJson()}
   */
  public String print()
  {
    return kind2Value.toString();
  }

  @Override
  public String toString()
  {
    return kind2Value.toString();
  }
}
