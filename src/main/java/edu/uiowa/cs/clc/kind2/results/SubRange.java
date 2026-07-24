/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Kind2 type for subranges.
 */
public class SubRange extends Type
{
  /**
   * the min value of the subrange.
   */
  private final int min;
  /**
   * the max value of the subrange.
   */
  private final int max;

  /**
   * Constructs a subrange type with the given bounds.
   *
   * @param min the lower bound
   * @param max the upper bound
   */
  public SubRange(int min, int max)
  {
    super("subrange");
    this.min = min;
    this.max = max;
  }

  /**
   * Returns the min value of the subrange.
   *
   * @return the min value of the subrange.
   */
  public int getMin()
  {
    return min;
  }

  /**
   * Returns the maximum value of the subrange.
   *
   * @return the maximum value of the subrange.
   */
  public int getMax()
  {
    return max;
  }
}
