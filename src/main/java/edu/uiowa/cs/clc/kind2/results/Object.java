/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Enum for the kind2 json objects
 */
public enum Object
{
  /**
   * The options Kind 2 was run with.
   */
  kind2Options("kind2Options"),
  /**
   * A log message emitted by Kind 2.
   */
  log("log"),
  /**
   * The beginning of an analysis of one component.
   */
  analysisStart("analysisStart"),
  /**
   * The result Kind 2 reached for one property.
   */
  property("property"),
  /**
   * The result of a contract realizability check.
   */
  realizabilityResult("realizabilityResult"),
  /**
   * The result of a satisfiability check.
   */
  satisfiabilityCheck("satisfiabilityCheck"),
  /**
   * The end of an analysis of one component.
   */
  analysisStop("analysisStop"),
  /**
   * The beginning of a post-analysis treatment.
   */
  postAnalysisStart("postAnalysisStart"),
  /**
   * The end of a post-analysis treatment.
   */
  postAnalysisEnd("postAnalysisEnd"),
  /**
   * A set of model elements produced by a post-analysis.
   */
  modelElementSet("modelElementSet"),
  /**
   * A progress report emitted while an engine is running.
   */
  progress("progress"),
  /**
   * Source location information for a declaration in the input program.
   */
  lsp("lsp"),
  /**
   * An enumeration of model element sets.
   */
  modelSetEnumeration("modelSetEnumeration"),
  /**
   * A report that no model element set could be computed.
   */
  noModelElementSet("noModelElementSet");
  private final String value;

  private Object(String value)
  {
    this.value = value;
  }

  /**
   * Returns the object type denoted by the given Kind 2 object type name.
   *
   * @param kind2Object the Kind 2 object type name
   * @return the corresponding object type
   * @throws UnsupportedOperationException if the name is not recognised
   */
  public static Object getKind2Object(String kind2Object)
  {
    switch (kind2Object)
    {
      case "kind2Options":
        return kind2Options;
      case "log":
        return log;
      case "analysisStart":
        return analysisStart;
      case "property":
        return property;
      case "realizabilityCheck":
        return realizabilityResult;
      case "satisfiabilityCheck":
        return satisfiabilityCheck;
      case "analysisStop":
        return analysisStop;
      case "postAnalysisStart":
        return postAnalysisStart;
      case "modelElementSet":
        return modelElementSet;
      case "postAnalysisEnd":
        return postAnalysisEnd;
      case "progress":
        return progress;
      case "lsp":
        return lsp;
      case "modelSetEnumeration":
        return modelSetEnumeration;
      case "noModelElementSet":
        return noModelElementSet;
      default:
        throw new UnsupportedOperationException("Value " + kind2Object + " is not defined");
    }
  }

  @Override
  public String toString()
  {
    return this.value;
  }
}