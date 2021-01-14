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
public class ArrayValue extends Value
{
  private final List<Value> values;

  public ArrayValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement, List<Value> values)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    this.values = values;
  }

  public List<Value> getValues()
  {
    return values;
  }

  @Override
  public String toString()
  {
    return values.toString();
  }
}
