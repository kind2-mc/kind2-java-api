/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Kind2 labels used in the json output.
 */
public class Labels
{
  private Labels()
  {
  }

  /**
   * The maximum number of unrolling steps used by the bounded model checking engine.
   */
  public static final String bmcMax = "bmcMax";
  /**
   * The type of the Kind 2 output object, which determines how the rest of it is read.
   */
  public static final String objectType = "objectType";
  /**
   * The top-level component of an analysis.
   */
  public static final String top = "top";
  /**
   * The components that an analysis represents by their contracts rather than their definitions.
   */
  public static final String abstractField = "abstract";
  /**
   * The components that an analysis represents by their definitions rather than their contracts.
   */
  public static final String concrete = "concrete";
  /**
   * The assumptions an analysis relies on.
   */
  public static final String assumptions = "assumptions";
  /**
   * The name of a component, property, stream, or model element.
   */
  public static final String name = "name";
  /**
   * The component in which a property is defined.
   */
  public static final String scope = "scope";
  /**
   * The input file a declaration or property originates from.
   */
  public static final String file = "file";
  /**
   * The line number of a declaration or property.
   */
  public static final String line = "line";
  /**
   * The kind of an AST declaration, such as a node, function, or contract.
   */
  public static final String kind = "kind";
  /**
   * The column number of a declaration or property.
   */
  public static final String column = "column";
  /**
   * Whether a node or function is imported rather than defined.
   */
  public static final String imported = "imported";
  /**
   * The line at which a declaration starts.
   */
  public static final String startLine = "startLine";
  /**
   * The column at which a declaration starts.
   */
  public static final String startColumn = "startColumn";
  /**
   * The line at which a declaration ends.
   */
  public static final String endLine = "endLine";
  /**
   * The column at which a declaration ends.
   */
  public static final String endColumn = "endColumn";
  /**
   * The answer Kind 2 reached for a property.
   */
  public static final String answer = "answer";
  /**
   * The value of an answer, a log message, or a stream at a given instant.
   */
  public static final String value = "value";
  /**
   * The engine that produced an answer, or the origin of a log message.
   */
  public static final String source = "source";
  /**
   * Whether a property is a candidate invariant rather than a stated property.
   */
  public static final String isCandidate = "isCandidate";
  /**
   * The timeout of an analysis, or whether a runtime measurement hit the timeout.
   */
  public static final String timeout = "timeout";
  /**
   * The counterexample trace for a falsified property.
   */
  public static final String counterExample = "counterExample";
  /**
   * The deadlocking trace reported for an unrealizable contract.
   */
  public static final String deadlockingTrace = "deadlockingTrace";
  /**
   * The context an analysis was performed in.
   */
  public static final String context = "context";
  /**
   * The witness trace reported for a realizable contract.
   */
  public static final String exampleTrace = "witness";
  /**
   * The type of a block in a counterexample or witness trace.
   */
  public static final String blockType = "blockType";
  /**
   * The streams belonging to a block of a trace.
   */
  public static final String streams = "streams";
  /**
   * The type of a stream.
   */
  public static final String type = "type";
  /**
   * The structured type information of a stream, used for array and subrange types.
   */
  public static final String typeInfo = "typeInfo";
  /**
   * The element type of an array type, or the underlying type of a subrange type.
   */
  public static final String baseType = "baseType";
  /**
   * The class of a stream, which is one of input, output, or local.
   */
  public static final String classField = "class";
  /**
   * The values a stream takes at each instant of a trace.
   */
  public static final String instantValues = "instantValues";
  /**
   * The nested blocks of a trace, one per called subcomponent.
   */
  public static final String subNodes = "subnodes";
  /**
   * The upper bound of a subrange type.
   */
  public static final String max = "max";
  /**
   * The lower bound of a subrange type.
   */
  public static final String min = "min";
  /**
   * The stream class denoting an input of a component.
   */
  public static final String input = "input";
  /**
   * The stream class denoting an output of a component.
   */
  public static final String output = "output";
  /**
   * The stream class denoting a local variable of a component.
   */
  public static final String local = "local";
  /**
   * The number of steps a property was shown to hold for without being proven.
   */
  public static final String trueFor = "trueFor";
  /**
   * The number of steps at which a property was proven or falsified.
   */
  public static final String k = "k";
  /**
   * The severity level of a log message.
   */
  public static final String level = "level";
  /**
   * The size of an array type.
   */
  public static final String size = "size";
  /**
   * The numerator of a real value expressed as a fraction.
   */
  public static final String numerator = "num";
  /**
   * The denominator of a real value expressed as a fraction.
   */
  public static final String denominator = "den";
  /**
   * The model element set category containing the elements that must be in every explanation.
   */
  public static final String must = "must";
  /**
   * The model element set category complementing the must set.
   */
  public static final String mustComplement = "must complement";
  /**
   * The model element set category holding an inductive validity core.
   */
  public static final String ivc = "ivc";
  /**
   * The model element set category complementing the inductive validity core.
   */
  public static final String ivcComplement = "ivc complement";
  /**
   * The realizability result reported for a contract.
   */
  public static final String result = "result";
  /**
   * The runtime Kind 2 spent reaching an answer.
   */
  public static final String runtime = "runtime";
  /**
   * The result value reported when a contract is realizable.
   */
  public static final String realizable = "realizable";
  /**
   * The result value reported when a contract is unrealizable.
   */
  public static final String unrealizable = "unrealizable";
  /**
   * The unit a runtime measurement is expressed in.
   */
  public static final String unit = "unit";
  /**
   * The nodes a model element set is grouped by.
   */
  public static final String nodes = "nodes";
  /**
   * The model elements belonging to a node.
   */
  public static final String elements = "elements";
  /**
   * The category of a model element.
   */
  public static final String category = "category";
  /**
   * The model element category denoting an equation.
   */
  public static final String equation = "Equation";
  /**
   * The model element category denoting a call to another component.
   */
  public static final String nodeCall = "NodeCall";
  /**
   * Whether compositional analysis is enabled.
   */
  public static final String compositional = "compositional";
  /**
   * Whether modular analysis is enabled.
   */
  public static final String modular = "modular";
  /**
   * The engines enabled for the run.
   */
  public static final String enabled = "enabled";
}
