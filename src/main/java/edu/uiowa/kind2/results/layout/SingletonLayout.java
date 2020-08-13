/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results.layout;

import java.util.Collections;
import java.util.List;

/**
 * A layout which assigns everything to one single category
 */
public class SingletonLayout implements Layout {
  private final String name;

  /**
   * @param name Name of the single category
   */
  public SingletonLayout(String name) {
    this.name = name;
  }

  public SingletonLayout() {
    this("Signals");
  }

  @Override
  public List<String> getCategories() {
    return Collections.singletonList(name);
  }

  @Override
  public String getCategory(String signal) {
    return name;
  }
}
