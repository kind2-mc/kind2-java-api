/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

class TypeDef extends Ast {
  final String id;
  final Type type; // Nullable

  TypeDef(String id, Type type) {
    this.id = id;
    this.type = type;
  }
}
