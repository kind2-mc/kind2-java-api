/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

class NamedType implements Type {
  final String name;

  NamedType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
