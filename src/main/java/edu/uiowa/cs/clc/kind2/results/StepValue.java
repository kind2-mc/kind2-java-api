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

  /**
   * Constructs one instant of a stream from its Kind 2 json value.
   *
   * @param stream the stream this value belongs to
   * @param jsonElement the Kind 2 json element holding the instant and its value
   */
  public StepValue(Stream stream, JsonElement jsonElement)
  {
    this.stream = stream;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    time = jsonElement.getAsJsonArray().get(0).getAsInt();
    kind2Value = Value.getKind2Value(this, getKind2Type(), jsonElement.getAsJsonArray().get(1));
  }

  /**
   * Returns the type of the current value.
   *
   * @return the type of the current value
   */
  public Type getKind2Type()
  {
    return stream.getKind2Type();
  }

  /**
   * Returns the Kind2 json output for this object.
   *
   * @return Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  /**
   * Returns the value at the specified time.
   *
   * @return The value at the specified time.
   */
  public Value getKind2Value()
  {
    return kind2Value;
  }

  /**
   * Returns the step index in the stream sequence.
   *
   * @return the step index in the stream sequence.
   */
  public int getTime()
  {
    return time;
  }

  /**
   * Returns the associated stream for this object.
   *
   * @return the associated stream for this object.
   */
  public Stream getStream()
  {
    return stream;
  }

  /**
   * Returns the associated kind2 result for this object.
   *
   * @return the associated kind2 result for this object.
   */
  public Result getKind2Result()
  {
    return stream.getKind2Result();
  }

  /**
   * Returns the value of this object. To get the original json value, use {@link
   * StepValue#getJson()}.
   *
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
