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

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class stores the results of kind2 analysis for a given node.
 */
public class Kind2Analysis
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  /**
   * Name of the current top-level component.
   */
  private final String nodeName;
  /**
   * Names of the subcomponents whose contract is used in the analysis.
   */
  private final List<String> abstractNodes;
  /**
   * Names of the subcomponents whose implementation is used in the analysis.
   */
  private final List<String> concreteNodes;
  /**
   * Array of pairs (name of subcomponent, number of considered invariants).
   */
  private final List<Pair<String, String>> assumptions;
  /**
   * names of the subcomponents of the current node
   */
  private final List<String> subNodes;
  /**
   * a map between json property name and kind2 attempt to prove this property in the current analysis.
   * A map is used because the same property name can appear on the json output with different k values.
   * An example is ControlSpec[l117c12].R1: Until the access code is first set, the door cannot be unlocked[1]
   * in file files/S1.json
   */
  private final Map<String, List<Kind2Property>> propertiesMap;
  /**
   * is the current analysis comes from an exhaustiveness check of the state space covered by the modes of a contract.
   */
  private boolean isModeAnalysis = false;
  /**
   * The associated node result for this analysis.
   */
  private Kind2NodeResult nodeResult = null;
  /**
   * The post analysis performed after the current analysis.
   */
  private Kind2PostAnalysis postAnalysis;

  public Kind2Analysis(JsonElement jsonElement)
  {
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

    this.nodeName = jsonElement.getAsJsonObject().get(Kind2Labels.top).getAsString();
    this.abstractNodes = new ArrayList<>();
    JsonArray abstractArray = jsonElement.getAsJsonObject().get(Kind2Labels.abstractField).getAsJsonArray();
    for (JsonElement node : abstractArray)
    {
      abstractNodes.add(node.getAsString());
    }
    this.concreteNodes = new ArrayList<>();
    JsonArray concreteArray = jsonElement.getAsJsonObject().get(Kind2Labels.concrete).getAsJsonArray();
    for (JsonElement node : concreteArray)
    {
      concreteNodes.add(node.getAsString());
    }

    subNodes = new ArrayList<>(abstractNodes);
    subNodes.addAll(concreteNodes);

    assumptions = new ArrayList<>();

    JsonArray assumptionInvariants = jsonElement.getAsJsonObject().get(Kind2Labels.assumptions).getAsJsonArray();
    for (JsonElement invariant : assumptionInvariants)
    {
      JsonArray invariantArray = invariant.getAsJsonArray();
      String nodeName = invariantArray.get(0).getAsString();
      String number = invariantArray.get(1).getAsString();
      assumptions.add(new Pair<>(nodeName, number));
    }

    this.propertiesMap = new HashMap<>();
  }

  /**
   * Add the passed property to the list of properties in the current analysis.
   * If the property has type one mode active, then the current analysis is for one mode active.
   * @param property
   */
  public void addProperty(Kind2Property property)
  {
    // add the property
    if (propertiesMap.containsKey(property.getJsonName()))
    {
      propertiesMap.get(property.getJsonName()).add(property);
    }
    else
    {
      List<Kind2Property> list = new ArrayList<>();
      list.add(property);
      propertiesMap.put(property.getJsonName(), list);
    }
    if (property.getSource() == Kind2PropertyType.oneModeActive)
    {
      isModeAnalysis = true;
    }
  }

  /**
   * @return Kind2 json output for this object
   */
  public String getJson()
  {
    return json;
  }

  /**
   * @return name of the current top-level component.
   */
  public String getNodeName()
  {
    return Kind2Result.getOpeningSymbols() + nodeName + Kind2Result.getClosingSymbols();
  }

  /**
   * @return names of the subcomponents whose contract is used in the analysis.
   */
  public List<String> getAbstractNodes()
  {
    return abstractNodes;
  }

  /**
   * @return Names of the subcomponents whose implementation is used in the analysis.
   */
  public List<String> getConcreteNodes()
  {
    return concreteNodes;
  }

  /**
   * @return the properties of the current analysis. Since the same property can appear many times,
   * this method returns only the output of the last property attempted by kind2.
   */
  public List<Kind2Property> getProperties()
  {
    List<Kind2Property> lastProperties = new ArrayList<>();
    for (Map.Entry<String, List<Kind2Property>> entry : propertiesMap.entrySet())
    {
      // add the last property object
      Kind2Property lastProperty = entry.getValue().get(entry.getValue().size() - 1);
      lastProperties.add(lastProperty);
    }
    return lastProperties;
  }

  /**
   * This method filters the properties based on the type of the answer.
   * @param answer can be valid, falsifiable, or unknown.
   * @return the properties with the specified answer in the current analysis.
   */
  private List<Kind2Property> filterProperties(Kind2Answer answer)
  {
    return getProperties().stream().filter(p -> p.getAnswer() == answer).collect(Collectors.toList());
  }

  /**
   * @return the falsified properties in the current analysis.
   */
  public List<Kind2Property> getFalsifiedProperties()
  {
    return filterProperties(Kind2Answer.falsifiable);
  }
  /**
   * @return the unknown properties in the current analysis.
   */
  public List<Kind2Property> getUnknownProperties()
  {
    return filterProperties(Kind2Answer.unknown);
  }

  /**
   * @return names of the subcomponents of the current node
   */
  public List<String> getSubNodes()
  {
    return subNodes;
  }

  /**
   * @return is the current analysis comes from an exhaustiveness check of the state space covered by the modes of a
   * contract.
   */
  public boolean isModeAnalysis()
  {
    return isModeAnalysis;
  }

  /**
   * associate the current analysis with the given node result.
   * This method should only be called internally, and must not be public.
   * @param nodeResult
   */
  void setNodeResult(Kind2NodeResult nodeResult)
  {
    this.nodeResult = nodeResult;
  }

  /**
   * @return the associated node result for this analysis.
   */
  public Kind2NodeResult getNodeResult()
  {
    return nodeResult;
  }

  /**
   * @return The associated kind2 result for this analysis.
   */
  public Kind2Result getKind2Result()
  {
    if (nodeResult == null)
    {
      return null;
    }
    else
    {
      return nodeResult.getKind2Result();
    }
  }

  /**
   * @return the valid properties in the current analysis.
   */
  public List<Kind2Property> getValidProperties()
  {
    return filterProperties(Kind2Answer.valid);
  }

  /**
   * @return a map between json property name and kind2 attempt to prove this property in the current analysis.
   * A map is used because the same property name can appear on the json output with different k values.
   * An example is ControlSpec[l117c12].R1: Until the access code is first set, the door cannot be unlocked[1]
   * in file files/S1.json
   */
  public Map<String, List<Kind2Property>> getPropertiesMap()
  {
    return propertiesMap;
  }

  /**
   * @param jsonName kind2 name for this property (which includes the line number and column number).
   * @return the output of the last property attempted by kind2 in this analysis with the given name.
   */
  public Optional<Kind2Property> getProperty(String jsonName)
  {
    return getProperties()
        .stream().filter(p -> p.getJsonName().equals(jsonName)).findFirst();
  }

  /**
   * @return The post analysis performed after the current analysis.
   */
  public Kind2PostAnalysis getPostAnalysis()
  {
    return postAnalysis;
  }

  /**
   * Associate the current analysis with the given post analysis.
   * This method should only be used internally, and must not be public.
   * @param postAnalysis
   */
  void setPostAnalysis(Kind2PostAnalysis postAnalysis)
  {
    if (this.postAnalysis == null)
    {
      this.postAnalysis = postAnalysis;
    }
    else
    {
      throw new RuntimeException(String.format("Post Analysis is already set for '%1$s'.", nodeName));
    }
  }
}
