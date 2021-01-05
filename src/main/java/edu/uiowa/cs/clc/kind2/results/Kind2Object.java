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
public enum Kind2Object
{
  kind2Options("kind2Options"),
  log("log"),
  analysisStart("analysisStart"),
  property("property"),
  analysisStop("analysisStop"),
  postAnalysisStart("postAnalysisStart"),
  postAnalysisEnd("postAnalysisEnd"),
  modelElementSet("modelElementSet"),
  progress("progress");

  private final String value;

  private Kind2Object(String value)
  {
    this.value = value;
  }

  public static Kind2Object getKind2Object(String kind2Object)
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