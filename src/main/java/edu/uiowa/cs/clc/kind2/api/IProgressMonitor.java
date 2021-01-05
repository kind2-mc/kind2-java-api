/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

/**
 * The {@code IProgressMonitor} interface is implemented by objects that monitor the progress of an
 * activity; the methods in this interface are invoked by code that performs the activity.
 */
public interface IProgressMonitor {
  /**
   * Returns whether cancellation of current operation has been requested. Long-running operations
   * should poll to see if cancellation has been requested.
   *
   * @return true if cancellation has been requested, and false otherwise
   */
  public boolean isCanceled();

  /**
   * Notifies that the work is done; that is, either the main task is completed or the user canceled
   * it.
   */
  public void done();
}
