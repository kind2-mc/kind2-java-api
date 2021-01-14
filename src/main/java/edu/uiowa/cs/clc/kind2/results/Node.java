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

import java.util.ArrayList;
import java.util.List;

/**
 * This class groups model elements belonging to the same node in a model element set
 */
public class Node
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;

  private final JsonElement jsonElement;
  /**
   * Name of the node
   */
  private final String name;
  /**
   * Model element set to which this class belongs to
   */
  private final ModelElementSet modelElementSet;
  /**
   * List of model elements which belongs to this node
   */
  private final List<Element> elements;

  public Node(ModelElementSet modelElementSet, JsonElement jsonElement)
  {
    this.modelElementSet = modelElementSet;
    this.jsonElement = jsonElement;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    name = jsonObject.get(Labels.name).getAsString();

    elements = new ArrayList<>();
    for (JsonElement element : jsonObject.get(Labels.elements).getAsJsonArray())
    {
      Element kind2Element = new Element(this, element);
      elements.add(kind2Element);
    }
  }

  /**
   * Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  /**
   * @return the name of the name
   */
  public String getName()
  {
    return Result.getOpeningSymbols() +  name + Result.getClosingSymbols();
  }

  /**
   * @return the model element set to which this class belongs to
   */
  public ModelElementSet getModelElementSet()
  {
    return modelElementSet;
  }

  /**
   * @return list of model elements which belongs to this node
   */
  public List<Element> getElements()
  {
    return elements;
  }
}
