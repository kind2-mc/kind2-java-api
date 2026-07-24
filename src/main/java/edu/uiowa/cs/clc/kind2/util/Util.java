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

/**
 * Assorted collection and formatting helpers.
 */
public class Util {
  private Util() {
  }


  /**
   * Copies a collection into an unmodifiable list, treating null as empty.
   *
   * @param <T> the element type
   * @param original the collection to copy, may be null
   * @return an unmodifiable list holding the same elements
   */
  public static <T> List<T> safeList(Collection<? extends T> original) {
    if (original == null) {
      return Collections.emptyList();
    } else {
      return Collections.unmodifiableList(new ArrayList<>(original));
    }
  }

  /**
   * Copies a map into an unmodifiable sorted map, treating null as empty.
   *
   * @param <T> the value type
   * @param original the map to copy, may be null
   * @return an unmodifiable map sorted by key in natural order
   */
  public static <T> SortedMap<String, T> safeStringSortedMap(Map<String, T> original) {
    TreeMap<String, T> map = new TreeMap<>(new StringNaturalOrdering());
    if (original != null) {
      map.putAll(original);
    }
    return Collections.unmodifiableSortedMap(map);
  }

  /**
   * Writes text to a file, replacing any existing contents.
   *
   * @param content the text to write
   * @param file the file to write to
   * @throws java.io.IOException if the file cannot be written
   */
  public static void writeToFile(String content, File file) throws IOException {
    try (Writer writer = new BufferedWriter(new FileWriter(file))) {
      writer.append(content);
    }
  }

  /**
   * Removes trailing fractional zeros, and a trailing decimal point, from a decimal string.
   *
   * @param str the decimal string to trim
   * @return the trimmed string
   */
  public static String removeTrailingZeros(String str) {
    if (!str.contains(".")) {
      return str;
    }

    return str.replaceFirst("\\.?0*$", "");
  }

  /**
   * In SMT solvers, integer division behaves differently than in Java. In particular, for -5 div 3
   * java says '-1' and SMT solvers say '-2'
   *
   * @param a the dividend
   * @param b the divisor
   * @return the quotient of {@code a} divided by {@code b}, using SMT division semantics
   */
  public static BigInteger smtDivide(BigInteger a, BigInteger b) {
    return a.subtract(a.mod(b)).divide(b);
  }
}
