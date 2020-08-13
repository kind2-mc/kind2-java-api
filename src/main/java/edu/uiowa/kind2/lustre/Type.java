/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.lustre;

import edu.uiowa.kind2.Assert;

public abstract class Type {
  public final Location location;

  protected Type(Location location) {
    Assert.isNotNull(location);
    this.location = location;
  }
}
