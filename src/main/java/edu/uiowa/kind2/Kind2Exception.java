/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2;

/**
 * An exception generated from Kind 2 or the Kind 2 API
 */
public class Kind2Exception extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public Kind2Exception(String message) {
    super(message);
  }

  public Kind2Exception(String message, Throwable t) {
    super(message, t);
  }
}
