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

/**
 * A Kind2 options object describes the options used by kind2 process.
 */
public class Options
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  /**
   * List of Kind 2 module names that are enabled
   */
  private final List<String> enabledModules;
  /**
   * The wallclock timeout used for all the analyses.
   */
  private final double timeout;
  /**
   * Maximal number of iterations for BMC and K-induction.
   */
  private final int bmcMax;
  /**
   * Whether compositional analysis is enabled or not.
   */
  private final boolean compositional;
  /**
   * Whether modular analysis is enabled or not.
   */
  private final boolean modular;

  public Options(JsonElement jsonElement)
  {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    timeout = jsonObject.get(Labels.timeout).getAsDouble();
    bmcMax = jsonObject.get(Labels.bmcMax).getAsInt();
    compositional = jsonObject.get(Labels.compositional).getAsBoolean();
    modular = jsonObject.get(Labels.modular).getAsBoolean();

    JsonArray modules = jsonObject.get(Labels.enabled).getAsJsonArray();
    enabledModules = new ArrayList<>();
    for (JsonElement module : modules)
    {
      enabledModules.add(module.getAsString());
    }
  }

  @Override
  public String toString()
  {
    return json;
  }

  /**
   * @return Kind2 json output for this object
   */
  public String getJson()
  {
    return json;
  }

  /**
   * @return list of Kind 2 module names that are enabled
   */
  public List<String> getEnabledModules()
  {
    return enabledModules;
  }

  /**
   * @return the wallclock timeout used for all the analyses.
   */
  public double getTimeout()
  {
    return timeout;
  }

  /**
   * @return maximal number of iterations for BMC and K-induction.
   */
  public int getBmcMax()
  {
    return bmcMax;
  }

  /**
   * @return whether compositional analysis is enabled or not.
   */
  public boolean isCompositional()
  {
    return compositional;
  }

  /**
   * @return whether modular analysis is enabled or not.
   */
  public boolean isModular()
  {
    return modular;
  }
}
