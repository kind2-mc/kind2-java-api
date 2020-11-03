/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.List;

import edu.uiowa.kind2.util.Util;

class EnumType implements Type {
  final List<String> values;

  EnumType(List<String> values) {
    this.values = Util.safeList(values);
  }
}
