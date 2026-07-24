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

  /**
   * Constructs an array value from one Kind 2 stream value.
   *
   * @param kind2StepValue the step this value belongs to
   * @param kind2Type the type of the value
   * @param jsonElement the Kind 2 json element holding the value
   * @param values the elements of the array
   */
  public ArrayValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement, List<Value> values)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    this.values = values;
  }

  /**
   * Returns the elements of this array.
   *
   * @return the elements of this array
   */
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
