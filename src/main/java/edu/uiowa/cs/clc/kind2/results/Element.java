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

import java.util.Optional;

public class Element
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  private final JsonElement jsonElement;
  private final String category;
  private final String jsonName;
  private final String name;
  private final String qualifiedName;
  private final long line;
  private final long column;
  private final Node kind2Node;
  private final Property kind2Property;

  public Element(Node kind2Node, JsonElement jsonElement)
  {
    this.kind2Node = kind2Node;
    this.jsonElement = jsonElement;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    jsonName = jsonObject.get(Labels.name).getAsString();
    category = jsonObject.get(Labels.category).getAsString();
    if (category.equals(Labels.equation))
    {
      // equation does not correspond to a property
      kind2Property = null;
      name = jsonName;
    }
    else
    {
      // get the corresponding property
      Optional<Property> property = getKind2Analysis().getProperty(jsonName);
      this.kind2Property = property.isPresent() ? property.get() : null;
      name = jsonName.replaceAll("\\[.*?\\]", "").replaceFirst(".*?\\.", "");
    }

    qualifiedName = kind2Node.getName() + "." + Result.getOpeningSymbols() + name + Result.getClosingSymbols();
    line = jsonObject.get(Labels.line).getAsLong();
    column = jsonObject.get(Labels.column).getAsLong();
  }

  private Analysis getKind2Analysis()
  {
    return this.kind2Node.getModelElementSet().getPostAnalysis().getAnalysis();
  }

  public String getJson()
  {
    return json;
  }

  public JsonElement getJsonElement()
  {
    return jsonElement;
  }

  public String getCategory()
  {
    return category;
  }

  public String getJsonName()
  {
    return jsonName;
  }

  public long getLine()
  {
    return line;
  }

  public long getColumn()
  {
    return column;
  }

  public Node getKind2Node()
  {
    return kind2Node;
  }

  public Property getKind2Property()
  {
    return kind2Property;
  }

  public String getName()
  {
    return Result.getOpeningSymbols() +  name + Result.getClosingSymbols();
  }

  public String getQualifiedName()
  {
    return qualifiedName;
  }
}
