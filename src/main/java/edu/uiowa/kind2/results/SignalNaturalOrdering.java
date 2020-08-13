/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

import java.util.Comparator;

import edu.uiowa.kind2.util.StringNaturalOrdering;

public class SignalNaturalOrdering implements Comparator<Signal<?>> {
  @Override
  public int compare(Signal<?> a, Signal<?> b) {
    return new StringNaturalOrdering().compare(a.getName(), b.getName());
  }
}
