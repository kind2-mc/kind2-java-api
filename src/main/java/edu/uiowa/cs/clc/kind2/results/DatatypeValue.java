/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The value of a kind2 algebraic datatype: a constructor applied to its
 * fields, which is a constructor name alone when the constructor takes none.
 */
public class DatatypeValue extends Value
{
  private final String constructor;
  private final List<Value> args;

  /**
   * Constructs a datatype value from one Kind 2 stream value.
   *
   * @param kind2StepValue the step this value belongs to
   * @param kind2Type the type of the value
   * @param jsonElement the Kind 2 json element holding the value
   * @param constructor the name of the constructor this value was built with
   * @param args the fields the constructor was applied to
   */
  public DatatypeValue(StepValue kind2StepValue, Type kind2Type, JsonElement jsonElement,
                       String constructor, List<Value> args)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    this.constructor = constructor;
    this.args = args;
  }

  /**
   * Returns the name of the constructor this value was built with.
   *
   * @return the name of the constructor this value was built with
   */
  public String getConstructor()
  {
    return constructor;
  }

  /**
   * Returns the fields the constructor was applied to, which is empty for a
   * constructor that takes none.
   *
   * @return the fields the constructor was applied to
   */
  public List<Value> getArgs()
  {
    return Collections.unmodifiableList(args);
  }

  @Override
  public String toString()
  {
    if (args.isEmpty())
    {
      return constructor;
    }
    return constructor + "("
        + args.stream().map(Value::toString).collect(Collectors.joining(", ")) + ")";
  }
}
