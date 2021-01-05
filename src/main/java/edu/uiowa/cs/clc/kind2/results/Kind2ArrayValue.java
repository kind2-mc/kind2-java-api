/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * This class stores the value of kind2 array type.
 */
public class Kind2ArrayValue extends Kind2Value
{
  private final List<Kind2Value> values;

  public Kind2ArrayValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement, List<Kind2Value> values)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    this.values = values;
  }

  public List<Kind2Value> getValues()
  {
    return values;
  }

  @Override
  public String toString()
  {
    return values.toString();
  }
}
