/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import java.util.Map;
import java.util.SortedMap;

import edu.uiowa.kind2.Assert;
import edu.uiowa.kind2.util.Util;

public class RecordType extends Type {
  public final String id;
  public final SortedMap<String, Type> fields;

  public RecordType(Location location, String id, Map<String, Type> fields) {
    super(location);
    Assert.isNotNull(id);
    Assert.isNotNull(fields);
    Assert.isTrue(fields.size() > 0);
    this.id = id;
    this.fields = Util.safeStringSortedMap(fields);
  }

  public RecordType(String id, Map<String, Type> fields) {
    this(Location.NULL, id, fields);
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof RecordType) {
      RecordType rt = (RecordType) obj;
      return id.equals(rt.id);
    }
    return false;
  }
}
