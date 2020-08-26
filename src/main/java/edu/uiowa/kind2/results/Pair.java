/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

/**
 * A pair class
 * @param <K> the java type of the first element.
 * @param <V> the java type of the second element.
 */
final class Pair<K, V>
{
  private final K key;
  private final V value;

  public Pair(K key, V value)
  {
    this.key = key;
    this.value = value;
  }

  public K getKey()
  {
    return key;
  }

  public V getValue()
  {
    return value;
  }
}