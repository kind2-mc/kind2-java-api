/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Enum for the types of suggestions.
 */
public enum Kind2SuggestionType
{
  noActionRequired, // suggestion 1
  strengthenSubComponentContract, // suggestion 2
  completeSpecificationOrRemoveComponent, // suggestion 3
  makeWeakerOrFixDefinition, // suggestion 4
  makeAssumptionStrongerOrFixDefinition, // suggestion 5
  fixSubComponentIssues, // suggestion 6
  fixOneModeActive, // suggestion 7
  increaseTimeout // for unknown properties
}
