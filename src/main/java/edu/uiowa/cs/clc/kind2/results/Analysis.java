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
public class Analysis
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
  private final Map<String, List<Property>> propertiesMap;
  /**
   * realizability check in the current analysis.
   */
  private boolean realizabilityCheck = false;
  /**
   * Deadlocking trace of current analysis
   */
  private String deadlock = null;
  /**
   * Context of current analysis
   */
  private String context = "";
  /**
   * is the current analysis comes from an exhaustiveness check of the state space covered by the modes of a contract.
   */
  private boolean isModeAnalysis = false;
  /**
   * The associated node result for this analysis.
   */
  private NodeResult nodeResult = null;
  /**
   * The post analysis performed after the current analysis.
   */
  private PostAnalysis postAnalysis;

  public Analysis(JsonElement jsonElement)
  {
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

    this.nodeName = jsonElement.getAsJsonObject().get(Labels.top).getAsString();
    try {
      this.context = jsonElement.getAsJsonObject().get(Labels.context).getAsString();
    } catch (Exception e) {
    }
    this.abstractNodes = new ArrayList<>();
    try {
    JsonArray abstractArray = jsonElement.getAsJsonObject().get(Labels.abstractField).getAsJsonArray();
      for (JsonElement node : abstractArray)
      {
        abstractNodes.add(node.getAsString());
      }
    } catch (Exception e) {
    }

    this.concreteNodes = new ArrayList<>();
    try {
      JsonArray concreteArray = jsonElement.getAsJsonObject().get(Labels.concrete).getAsJsonArray();
      for (JsonElement node : concreteArray)
      {
        concreteNodes.add(node.getAsString());
      }
    } catch (Exception e) {
    }

    subNodes = new ArrayList<>(abstractNodes);
    subNodes.addAll(concreteNodes);

    assumptions = new ArrayList<>();

    try {
    JsonArray assumptionInvariants = jsonElement.getAsJsonObject().get(Labels.assumptions).getAsJsonArray();
      for (JsonElement invariant : assumptionInvariants)
      {
        JsonArray invariantArray = invariant.getAsJsonArray();
        String nodeName = invariantArray.get(0).getAsString();
        String number = invariantArray.get(1).getAsString();
        assumptions.add(new Pair<>(nodeName, number));
      }
    } catch (Exception e) {
    }
    this.propertiesMap = new HashMap<>();
  }

  /**
   * Add the passed property to the list of properties in the current analysis.
   * If the property has type one mode active, then the current analysis is for one mode active.
   * @param property
   */
  public void addProperty(Property property)
  {
    // add the property
    if (propertiesMap.containsKey(property.getJsonName()))
    {
      propertiesMap.get(property.getJsonName()).add(property);
    }
    else
    {
      List<Property> list = new ArrayList<>();
      list.add(property);
      propertiesMap.put(property.getJsonName(), list);
    }
    if (property.getSource() == PropertyType.oneModeActive)
    {
      isModeAnalysis = true;
    }
  }

  public void setRealizabilityCheck(boolean val) 
  {
    this.realizabilityCheck = val;
  }

  public Boolean getRealizabilityCheck() 
  {
    return this.realizabilityCheck;
  }

  public void setDeadlock(String val) 
  {
    this.deadlock = val;
  }

  public String getDeadlock() 
  {
    return this.deadlock;
  }

  public void setContext(String val) 
  {
    this.context = val;
  }

  public String getContext() 
  {
    return this.context;
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
    return Result.getOpeningSymbols() + nodeName + Result.getClosingSymbols();
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
  public List<Property> getProperties()
  {
    List<Property> lastProperties = new ArrayList<>();
    for (Map.Entry<String, List<Property>> entry : propertiesMap.entrySet())
    {
      // add the last property object
      Property lastProperty = entry.getValue().get(entry.getValue().size() - 1);
      lastProperties.add(lastProperty);
    }
    return lastProperties;
  }

  /**
   * This method filters the properties based on the type of the answer.
   * @param answer can be valid, falsifiable, unknown, reachable, or unreachable.
   * @return the properties with the specified answer in the current analysis.
   */
  private List<Property> filterProperties(Answer answer)
  {
    return getProperties().stream().filter(p -> p.getAnswer() == answer).collect(Collectors.toList());
  }

  /**
   * @return the falsified properties in the current analysis.
   */
  public List<Property> getFalsifiedProperties()
  {
    return filterProperties(Answer.falsifiable);
  }
  /**
   * @return the unknown properties in the current analysis.
   */
  public List<Property> getUnknownProperties()
  {
    return filterProperties(Answer.unknown);
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
  void setNodeResult(NodeResult nodeResult)
  {
    this.nodeResult = nodeResult;
  }

  /**
   * @return the associated node result for this analysis.
   */
  public NodeResult getNodeResult()
  {
    return nodeResult;
  }

  /**
   * @return The associated kind2 result for this analysis.
   */
  public Result getKind2Result()
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
  public List<Property> getValidProperties()
  {
    return filterProperties(Answer.valid);
  }

  /**
   * @return the reachable properties in the current analysis.
   */
  public List<Property> getReachableProperties()
  {
    return filterProperties(Answer.reachable);
  }

  /**
   * @return the unreachable properties in the current analysis.
   */
  public List<Property> getUnreachableProperties()
  {
    return filterProperties(Answer.unreachable);
  }

  /**
   * @return a map between json property name and kind2 attempt to prove this property in the current analysis.
   * A map is used because the same property name can appear on the json output with different k values.
   * An example is ControlSpec[l117c12].R1: Until the access code is first set, the door cannot be unlocked[1]
   * in file files/S1.json
   */
  public Map<String, List<Property>> getPropertiesMap()
  {
    return propertiesMap;
  }

  /**
   * @param jsonName kind2 name for this property (which includes the line number and column number).
   * @return the output of the last property attempted by kind2 in this analysis with the given name.
   */
  public Optional<Property> getProperty(String jsonName)
  {
    return getProperties()
        .stream().filter(p -> p.getJsonName().equals(jsonName)).findFirst();
  }

  /**
   * @return The post analysis performed after the current analysis.
   */
  public PostAnalysis getPostAnalysis()
  {
    return postAnalysis;
  }

  /**
   * Associate the current analysis with the given post analysis.
   * This method should only be used internally, and must not be public.
   * @param postAnalysis
   */
  void setPostAnalysis(PostAnalysis postAnalysis)
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
