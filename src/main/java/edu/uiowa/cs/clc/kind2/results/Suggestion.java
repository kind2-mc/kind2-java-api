/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * The class specifies the action that needs to be done in order to verify the current component.
 * It stores explanations which determines why the suggestion is proposed.
 * If N is the current component, and M is possibly a subcomponent of N, then the suggestion is one of the following:
 * - noActionRequired: no action required because all components of the system satisfy their contracts, and no
 *   component of the system was refined.
 * - strengthenSubComponentContract: fix Ms contract because N is correct after refinement, but M's contract
 *   is too weak to prove N's contract, but M's definition is strong enough.
 * - completeSpecificationOrRemoveComponent: Either complete specification of N's contract, or remove
 *   component M, because component N satisfies its current contract and one or more assumptions of M are
 *   not satisfied by N.
 * - makeWeakerOrFixDefinition: either make assumption A weaker, or fix N's definition to satisfy A, because
 *   component N doesn't satisfy its contract after refinement, and assumption A of M is not satisfied by N.
 * - makeAssumptionStrongerOrFixDefinition: Either make N's assumptions stronger, or fix N's definition to
 *   satisfy N's guarantees, because component N doesn't satisfy its contract after refinement, and
 *   either N has no subcomponents, or all its subcomponents satisfy their contract.
 * - fixSubComponentIssues: fix reported issues for N's subcomponents, because component N doesn't satisfy its
 *   contract after refinement, and One or more subcomponents of N don't satisfy their contract.
 * - fixOneModeActive: define all modes of component N, because kind2 found a state that is not covered by any
 *   of the modes in N's contract.
 * - increaseTimeout: increase the timeout for kind2, because it fails to prove or disprove one of the properties
 *   with the previous timeout.
 */
public class Suggestion
{
  /**
   * The type of the current suggestion.
   */
  private SuggestionType suggestionType;

  /**
   * Explanation justifies the current suggestion.
   */
  private List<String> explanations = new ArrayList<>();

  /**
   * A friendly format for the current suggestion that combines both explanations and suggestion.
   */
  private String label;

  /**
   * The associated node result.
   */
  private final NodeResult nodeResult;

  /**
   * A private constructor. This class should not have a public constructor.
   * Use the static methods in this class to create a new instance.
   * @param nodeResult
   * @param suggestionType
   */
  private Suggestion(NodeResult nodeResult, SuggestionType suggestionType)
  {
    this.nodeResult = nodeResult;
    this.suggestionType = suggestionType;
  }

  /**
   * @return the associated kind2 result.
   */
  private Result getKind2Result()
  {
    return nodeResult.getKind2Result();
  }

  /**
   * @return The type of the current suggestion.
   */
  public SuggestionType getSuggestionType()
  {
    return suggestionType;
  }

  @Override
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();

    for (String explanation : explanations)
    {
      stringBuilder.append(explanation + "\n");
    }

    stringBuilder.append("\nSuggestion:\n" + label + "\n");

    return stringBuilder.toString();
  }

  public static Suggestion increaseTimeout(NodeResult nodeResult, List<Property> unknownProperties)
  {
    Suggestion suggestion = new Suggestion(nodeResult, SuggestionType.increaseTimeout);
    suggestion.explanations.add("Unknown answer for properties:");
    for (Property property : unknownProperties)
    {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(property.toString());

      if (Result.isPrintingUnknownCounterExamplesEnabled())
      {
        List<Property> propertyList = nodeResult.getLastAnalysis()
                                                     .getPropertiesMap().get(property.getJsonName());
        if (propertyList.size() > 1)
        {
          // get the last property with a counter example
          Property lastUnprovenInductiveStep = propertyList.get(propertyList.size() - 2);
          if (lastUnprovenInductiveStep.getCounterExample() != null &&
              lastUnprovenInductiveStep.getKInductionStep() != null)
          {
            // fix this message
            String message = "\nIf the starting state of the following trace is reachable, then the property" +
                " is falsified within %1$d steps. If it is not reachable, " +
                "please add auxiliary lemmas to prove this property.\n";
            stringBuilder.append(String.format(message, lastUnprovenInductiveStep.getKInductionStep()));
            stringBuilder.append(lastUnprovenInductiveStep.getCounterExample().toString());
          }
        }
      }

      suggestion.explanations.add(stringBuilder.toString());
    }
    suggestion.label = String.format("Kind2 did not find an answer for component %1$s " +
            "within time limit = %2$s seconds. Try to increase the timeout.",
        nodeResult.getName(),
        nodeResult.getKind2Result().getTimeout());
    return suggestion;
  }

  static Suggestion noActionRequired(NodeResult nodeResult)
  {
    // suggestion 1
    Suggestion suggestion = new Suggestion(nodeResult, SuggestionType.noActionRequired);

    suggestion.label = "No action required.";

    suggestion.explanations.add("All components of the system satisfy their contracts.");

    suggestion.explanations.add("No component of the system was refined.");

    return suggestion;
  }

  public static Suggestion strengthenSubComponentContract(NodeResult nodeResult)
  {
    // suggestion 2
    Suggestion suggestion = new Suggestion(nodeResult,
        SuggestionType.strengthenSubComponentContract);

    Set<String> nodes = new HashSet<>();

    // find concrete nodes which were abstracted in a previous analysis
    List<Analysis> analyses = nodeResult.getAnalyses();
    for (int i = analyses.size() - 1; i > 0; i--)
    {
      List<String> concreteNodes = analyses.get(i).getConcreteNodes();
      for (int j = i - 1; j >= 0; j--)
      {
        List<String> abstractNodes = analyses.get(j).getAbstractNodes();
        List<String> weakNodes = concreteNodes.stream()
                                              .filter(n -> abstractNodes.contains(n))
                                              .collect(Collectors.toList());
        nodes.addAll(weakNodes);
      }
    }

    suggestion.explanations.add(String.format("Component %1$s is correct after refinement.",
        nodeResult.getName()));
    StringBuilder stringBuilder = new StringBuilder();
    for (String node : nodes)
    {
      suggestion.explanations
          .add(String.format("%1$s is a subcomponent of %2$s. " +
                  "Its contract is too weak to prove %2$s, "
                  + "but its definition is strong enough. ",
              node, nodeResult.getName()));

      stringBuilder.append(String.format("Fix the contract of  %1$s.\n", node));
    }

    suggestion.label = stringBuilder.toString();
    return suggestion;
  }

  public static Suggestion completeSpecificationOrRemoveSubNodes(NodeResult nodeResult,
                                                                      List<Property> assumptions,
                                                                      boolean isNodeContractSatisfied)
  {
    // suggestion 3
    Suggestion suggestion = new Suggestion(nodeResult,
        SuggestionType.completeSpecificationOrRemoveComponent);
    if (isNodeContractSatisfied)
    {
      suggestion.explanations.add(String.format("Component %1$s satisfies its current contract.",
          nodeResult.getName()));
    }

    Map<String, List<Property>> subComponents = new HashMap<>();

    for (Property assumption : assumptions)
    {
      String scope = assumption.getScope();
      if (subComponents.containsKey(scope))
      {
        subComponents.get(scope).add(assumption);
      }
      else
      {
        List<Property> properties = new ArrayList<>();
        properties.add(assumption);
        subComponents.put(scope, properties);
      }
    }

    for (Map.Entry<String, List<Property>> subcomponent : subComponents.entrySet())
    {
      suggestion.explanations.add(String.format("%1$s is a direct subcomponent of %2$s, "
              + "but one or more assumptions of %1$s are not satisfied by %2$s.",
          Result.getOpeningSymbols()  + subcomponent.getKey() + Result.getClosingSymbols(),
          nodeResult.getName()));

      suggestion.explanations.add("Falsified assumptions:");

      for (Property assumption : subcomponent.getValue())
      {
        suggestion.explanations.add(assumption.getQualifiedName());
        if (Result.isPrintingCounterExamplesEnabled())
        {
          suggestion.explanations.add(assumption.getCounterExample().toString());
        }
      }
    }

    suggestion.label = String.format("Either complete specification in the contract of  %1$s, " +
            "or remove components: %2$s.",
        nodeResult.getName(),
        subComponents.keySet()
                .stream().map(n -> Result.getOpeningSymbols()  + n + Result.getClosingSymbols())
                .collect(Collectors.toSet()));
    return suggestion;
  }

  public static Suggestion makeWeakerOrFixDefinition(NodeResult nodeResult, List<Property> assumptions)
  {
    // suggestion 4

    Suggestion suggestion = new Suggestion(nodeResult,
        SuggestionType.makeWeakerOrFixDefinition);

    suggestion.explanations
        .add(String.format("Component %1$s does not satisfy its contract after refinement.",
            nodeResult.getName()));


    StringBuilder stringBuilder = new StringBuilder();

    for (Property assumption : assumptions)
    {
      suggestion.explanations.add(String.format("Assumption %1$s of subcomponent %2$s is " +
              "not satisfied by %3$s.",
          assumption.getName(),
          assumption.getScope(),
          nodeResult.getName()));
      if (Result.isPrintingCounterExamplesEnabled())
      {
        suggestion.explanations.add(assumption.getCounterExample().toString());
      }
      stringBuilder.append(String.format("Either make assumption %1$s weaker, or fix the definition of %2$s " +
              "to satisfy %1$s.\n",
          assumption.getQualifiedName(),
          nodeResult.getName()));
    }

    suggestion.explanations.add("\nFalsified Properties:");
    Set<Property> falsifiedProperties = nodeResult.getLastAnalysis().getFalsifiedProperties()
                                                       .stream().filter(p -> p.getSource() != PropertyType.assumption)
                                                       .collect(Collectors.toSet());
    for (Property property : falsifiedProperties)
    {
      suggestion.explanations.add(property.toString());
      if (Result.isPrintingCounterExamplesEnabled())
      {
        suggestion.explanations.add(property.getCounterExample().toString());
      }
    }

    suggestion.label = stringBuilder.toString();
    return suggestion;
  }

  static Suggestion makeAssumptionStrongerOrFixDefinition(NodeResult nodeResult,
                                                               List<Property> falsifiedProperties)
  {
    // suggestion 5
    Suggestion suggestion = new Suggestion(nodeResult,
        SuggestionType.makeAssumptionStrongerOrFixDefinition);

    suggestion.label = String.format(
        "Either make assumptions stronger in the contract of %1$s, " +
            "or fix the definition of %1$s to satisfy its contract.",
        nodeResult.getName());

    suggestion.explanations
        .add(String.format("Component %1$s does not satisfy its contract after refinement.",
            nodeResult.getName()));

    suggestion.explanations.add("\nFalsified Properties:");

    for (Property property : falsifiedProperties)
    {
      suggestion.explanations.add(property.toString());
      if (Result.isPrintingCounterExamplesEnabled())
      {
        suggestion.explanations.add(property.getCounterExample().toString());
      }
    }
    return suggestion;
  }

  static Suggestion fixSubComponentIssues(NodeResult nodeResult,
                                               List<Property> subComponentUnprovenProperties)
  {
    // suggestion 6: Fix reported issues for N ’s subcomponents.
    Suggestion suggestion = new Suggestion(nodeResult,
        SuggestionType.fixSubComponentIssues);

    suggestion.explanations.add("\nUnproved Properties:");
    Set<String> subComponents = new HashSet<>();
    for (Property property : subComponentUnprovenProperties)
    {
      suggestion.explanations.add(property.toString());
      subComponents.add(property.getScope());
      if (Result.isPrintingCounterExamplesEnabled())
      {
        suggestion.explanations.add(property.getCounterExample().toString());
      }
    }

    for (Property property : nodeResult.getLastAnalysis().getFalsifiedProperties())
    {
      suggestion.explanations.add(property.toString());
      if (Result.isPrintingCounterExamplesEnabled())
      {
        suggestion.explanations.add(property.getCounterExample().toString());
      }
    }

    suggestion.explanations.add(String.format(
        "Component %1$s does not satisfy its contract after refinement.",
        nodeResult.getName()));

    suggestion.label = String.format(String.format("Fix reported issues for %1$s’s subcomponents: %2$s.",
        nodeResult.getName(),
        subComponents));
    return suggestion;
  }

  public static Suggestion fixOneModeActive(NodeResult nodeResult,
                                                 Analysis modeAnalysis)
  {
    // suggestion 7: Fix one mode active
    Suggestion suggestion = new Suggestion(nodeResult,
        SuggestionType.fixOneModeActive);

    suggestion.explanations.add("\nIssues:");

    for (Property property : modeAnalysis.getFalsifiedProperties())
    {
      suggestion.explanations.add(property.toString());
      if (Result.isPrintingCounterExamplesEnabled())
      {
        suggestion.explanations.add(property.getCounterExample().toString());
      }
    }

    suggestion.explanations.add(String.format(
        "The modes defined in the contract of %1$s does not cover all states.",
        nodeResult.getName()));

    suggestion.label = String.format(String.format("Fix the modes of component %1$s.",
        nodeResult.getName()));
    return suggestion;
  }
}
