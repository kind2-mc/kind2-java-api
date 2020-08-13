/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api.results;

import java.util.Collections;

import edu.uiowa.kind2.util.Util;

/**
 * This class holds the results of a run of Realizability.
 *
 * Note on renaming: This object can be configured with a {@link Renaming} which changes the names
 * of properties and signals as they arrive. In this case, all properties are added and retrieved
 * using their original names.
 *
 * @see PropertyResult
 */
public class RealizabilityResult extends Result {
  /**
   * Construct a RealizabilityResult to hold the results of a run of Realizability
   *
   * @param name Name of the results
   */
  public RealizabilityResult(String name) {
    super(name, Collections.singletonList(Util.REALIZABLE));
  }

  /**
   * Construct a RealizabilityResult to hold the results of a run of Realizability
   *
   * @param name     Name of the results
   * @param renaming Renaming to apply to apply properties
   */
  public RealizabilityResult(String name, Renaming renaming) {
    super(name, Collections.singletonList(Util.REALIZABLE), renaming);
  }

  /**
   * Get the PropertyResult for realizability
   */
  public PropertyResult getPropertyResult() {
    return getPropertyResult(Util.REALIZABLE);
  }
}
