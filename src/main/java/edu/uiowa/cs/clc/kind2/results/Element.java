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

/**
 * A model element that Kind 2 reports as part of a model element set.
 */
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

  /**
   * Constructs a model element from one Kind 2 element object.
   *
   * @param kind2Node the node this element belongs to
   * @param jsonElement the Kind 2 json object describing the element
   */
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

  /**
   * Returns the Kind2 json output for this object.
   *
   * @return the Kind2 json output for this object
   */
  public String getJson()
  {
    return json;
  }

  /**
   * Returns the raw Kind 2 json element for this object.
   *
   * @return the raw Kind 2 json element for this object
   */
  public JsonElement getJsonElement()
  {
    return jsonElement;
  }

  /**
   * Returns the category of this model element.
   *
   * @return the category of this model element
   */
  public String getCategory()
  {
    return category;
  }

  /**
   * Returns the Kind 2 identifier for this model element.
   *
   * @return the Kind 2 identifier for this model element
   */
  public String getJsonName()
  {
    return jsonName;
  }

  /**
   * Returns the associated line in the input file.
   *
   * @return the associated line in the input file
   */
  public long getLine()
  {
    return line;
  }

  /**
   * Returns the associated column in the input file.
   *
   * @return the associated column in the input file
   */
  public long getColumn()
  {
    return column;
  }

  /**
   * Returns the node this model element belongs to.
   *
   * @return the node this model element belongs to
   */
  public Node getKind2Node()
  {
    return kind2Node;
  }

  /**
   * Returns the property associated with this model element.
   *
   * @return the property associated with this model element
   */
  public Property getKind2Property()
  {
    return kind2Property;
  }

  /**
   * Returns the name of this model element.
   *
   * @return the name of this model element
   */
  public String getName()
  {
    return Result.getOpeningSymbols() +  name + Result.getClosingSymbols();
  }

  /**
   * Returns the qualified name of this model element.
   *
   * @return the qualified name of this model element
   */
  public String getQualifiedName()
  {
    return qualifiedName;
  }
}
