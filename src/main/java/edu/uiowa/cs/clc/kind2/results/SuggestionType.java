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
public enum SuggestionType
{
  /**
   * No action is required: every component satisfies its contract and none was refined.
   */
  noActionRequired, // suggestion 1
  /**
   * Strengthen a subcomponent's contract, which is too weak to prove the current component even though its definition is strong enough.
   */
  strengthenSubComponentContract, // suggestion 2
  /**
   * Either complete the current component's contract or remove the subcomponent whose assumptions it fails to satisfy.
   */
  completeSpecificationOrRemoveComponent, // suggestion 3
  /**
   * Either weaken a subcomponent's assumption or fix the current component's definition to satisfy it.
   */
  makeWeakerOrFixDefinition, // suggestion 4
  /**
   * Either strengthen the current component's assumptions or fix its definition to satisfy its guarantees.
   */
  makeAssumptionStrongerOrFixDefinition, // suggestion 5
  /**
   * Fix the issues reported for the current component's subcomponents.
   */
  fixSubComponentIssues, // suggestion 6
  /**
   * Define all modes of the current component, since Kind 2 found a state no mode covers.
   */
  fixOneModeActive, // suggestion 7
  /**
   * Increase the timeout, since Kind 2 could not decide one of the properties in time.
   */
  increaseTimeout // for unknown properties
}
