/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PostAnalysis in Kind 2
 *
 * TODO: Convert this class to an abstract class, and add customized classes
 * for each post-analysis
 */
public class PostAnalysis
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;
  /**
   * Name of the Kind 2 post-analysis
   */
  private final String name;
  /**
   * Model elements computed in the post-analysis
   */
  private final List<ModelElementSet> modelElements;

  /**
   * The associated kind2 analysis.
   */
  private final Analysis analysis;

  public PostAnalysis(Analysis analysis, JsonElement jsonElement)
  {
    this.analysis = analysis;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

    this.name = jsonElement.getAsJsonObject().get(Labels.name).getAsString();
    modelElements = new ArrayList<>();
  }

  public void addModelElementSet(ModelElementSet modelElementSet)
  {
    modelElements.add(modelElementSet);
  }

  /**
   * @return Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  /**
   * @return the name of the Kind 2 post-analysis
   */
  public String getName()
  {
    return Result.getOpeningSymbols() + name + Result.getClosingSymbols();
  }

  /**
   * @return the associated kind2 analysis.
   */
  public Analysis getAnalysis()
  {
    return analysis;
  }

  /**
   * @return the model elements computed in the post-analysis
   */
  public List<ModelElementSet> getModelElements()
  {
    return modelElements;
  }
}
