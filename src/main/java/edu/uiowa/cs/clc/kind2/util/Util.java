/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Util {

  public static <T> List<T> safeList(Collection<? extends T> original) {
    if (original == null) {
      return Collections.emptyList();
    } else {
      return Collections.unmodifiableList(new ArrayList<>(original));
    }
  }

  public static <T> SortedMap<String, T> safeStringSortedMap(Map<String, T> original) {
    TreeMap<String, T> map = new TreeMap<>(new StringNaturalOrdering());
    if (original != null) {
      map.putAll(original);
    }
    return Collections.unmodifiableSortedMap(map);
  }

  public static void writeToFile(String content, File file) throws IOException {
    try (Writer writer = new BufferedWriter(new FileWriter(file))) {
      writer.append(content);
    }
  }

  public static String removeTrailingZeros(String str) {
    if (!str.contains(".")) {
      return str;
    }

    return str.replaceFirst("\\.?0*$", "");
  }

  /**
   * In SMT solvers, integer division behaves differently than in Java. In particular, for -5 div 3
   * java says '-1' and SMT solvers say '-2'
   */
  public static BigInteger smtDivide(BigInteger a, BigInteger b) {
    return a.subtract(a.mod(b)).divide(b);
  }
}
