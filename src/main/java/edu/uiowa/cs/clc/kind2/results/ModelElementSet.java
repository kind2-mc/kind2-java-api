/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ModelElementSet
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;

  private final JsonElement jsonElement;
  /**
   * Class of the core.
   * Can be "must", "must complement", "ivc", "ivc complement", "mcs", or
   * "mcs complement".
   */
  private final String classField;
  /**
   * Number of model elements in the core
   */
  private final int size;
  /**
   * Time unit of the runtime value
   */
  private final String runtimeUnit;
  /**
   * Runtime of the computation
   */
  private final double runtimeValue;
  /**
   * List of nodes with at least one model element in the core
   */
  private final List<Node> nodes;

  /**
   * The associated  post analysis object. 
   */
  private PostAnalysis postAnalysis;

  public ModelElementSet(PostAnalysis analysis, JsonElement jsonElement)
  {
    this.postAnalysis = analysis;
    this.jsonElement = jsonElement;
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    classField = jsonObject.get(Labels.classField).getAsString();
    size = jsonObject.get(Labels.size).getAsInt();
    JsonObject runtime = jsonObject.get(Labels.runtime).getAsJsonObject();
    runtimeUnit = runtime.get(Labels.unit).getAsString();
    runtimeValue = runtime.get(Labels.value).getAsDouble();
    nodes = new ArrayList<>();
    JsonArray nodeElements = jsonObject.get(Labels.nodes).getAsJsonArray();
    for (JsonElement element : nodeElements)
    {
      Node kind2Node = new Node(this, element);
      this.nodes.add(kind2Node);
    }
  }

  /**
   * @return
   *    Kind2 json output for this object
   */
  public String getJson()
  {
    return json;
  }

  public JsonElement getJsonElement()
  {
    return jsonElement;
  }

  /**
   * @return the class of the core
   */
  public String getClassField()
  {
    return classField;
  }

  /**
   * @return the size of the core
   */
  public int getSize()
  {
    return size;
  }

  /**
   * @return the time unit of runtime value
   */
  public String getRuntimeUnit()
  {
    return runtimeUnit;
  }

  /**
   * @return the runtime value
   */
  public double getRuntimeValue()
  {
    return runtimeValue;
  }

  /**
   * @return the list of nodes with at least one model element in the core
   */
  public List<Node> getNodes()
  {
    return nodes;
  }

  /**
   * @return the associated post analysis.
   */
  public PostAnalysis getPostAnalysis()
  {
    return postAnalysis;
  }
}
