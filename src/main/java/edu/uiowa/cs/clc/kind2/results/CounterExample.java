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
 * Counterexample to the property satisfaction (only available when answer is falsifiable).
 * It describes a sequence of values for each stream, and automaton,
 * that leads the system to the violation of the property.
 * It also gives the list of contract modes that are active at each step, if any.
 */
public class CounterExample
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;
  /**
   * This field stores the input, output, and local streams for the top node in this counter example.
   */
  private final SubNode topNode;

  /**
   * the associated property for this counter example.
   */
  private final Property property;

  public CounterExample(Property property, JsonElement jsonElement)
  {
    this.property = property;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    topNode = new SubNode(this, jsonElement);
  }

  /**
   * @return the streams for the top node in this counter example.
   */
  public SubNode getTopNode()
  {
    return topNode;
  }

  /**
   * @return the associated kind2 result for this counter example.
   */
  public Result getKind2Result()
  {
    return property.getKind2Result();
  }

  @Override
  public String toString()
  {
    int maxNameLength = topNode.getMaxNameLength();
    int maxValueLength = topNode.getMaxValueLength();
    return "Counterexample:" + topNode.print(maxNameLength, maxValueLength);
  }

  /**
   * @return Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  /**
   * @return the associated property for this counter example.
   */
  public Property getProperty()
  {
    return property;
  }
}
