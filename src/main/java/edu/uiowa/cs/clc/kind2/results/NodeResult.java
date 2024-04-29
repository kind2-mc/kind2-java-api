/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class stores the result of all analyses done by kind2 for a given component.
 */
public class NodeResult
{
  /**
   * the name of the component.
   */
  private final String name;

  /**
   * A list of analyses done by kind2 for the current component.
   */
  private List<Analysis> analyses = new ArrayList<>();

  /**
   * The list of node results for the subcomponents.
   */
  private Set<NodeResult> children = new HashSet<>();

  /**
   * The list of components that call this component.
   */
  private List<NodeResult> parents = new ArrayList<>();

  /**
   * determines whether the current component is analyzed. The current component may be called recursively
   * by multiple components, and we need to analyze it only once.
   */
  private boolean isAnalyzed = false;

  /**
   * determines whether the current component is printed. The current component may be called recursively
   * by multiple components, and we need to print it only once.
   */
  private boolean isVisited = true;

  /**
   * The list of suggestions for this component.
   */
  private List<Suggestion> suggestions = new ArrayList<>();

  /**
   * The associated kind2 result.
   */
  private final Result kind2Result;

  public NodeResult(Result kind2Result, String name)
  {
    this.kind2Result = kind2Result;
    this.name = name;
  }

  /**
   * Add an analysis for this component.
   * @param analysis
   */
  public void addAnalysis(Analysis analysis)
  {
    getAnalyses().add(analysis);
    analysis.setNodeResult(this);
  }

  /**
   * @return the list of suggestions for this component.
   */
  public List<Suggestion> getSuggestions()
  {
    return suggestions;
  }

  /**
   * @return the name of the component.
   */
  public String getName()
  {
    return name;
  }

  @Override
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();

    for (NodeResult child : children)
    {
      stringBuilder.append(child.toString() + "\n");
    }

    stringBuilder.append("Component: " + name + "\n");

    for (Suggestion suggestion : suggestions)
    {
      stringBuilder.append(suggestion.toString());
    }

    return stringBuilder.toString();
  }

  /**
   * @return the summary of the verification. Unlike toString() this does not print suggestions.
   */
  public String printVerificationSummary()
  {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("\nValid properties:\n");
    Set<Property> validProperties = this.getValidProperties();
    printProperties(stringBuilder, validProperties);

    stringBuilder.append("\nFalsified properties:\n");
    Set<Property> falsifiedProperties = this.getFalsifiedProperties();
    printProperties(stringBuilder, falsifiedProperties);

    stringBuilder.append("\nUnknown properties:\n");
    Set<Property> unknownProperties = this.getUnknownProperties();
    printProperties(stringBuilder, unknownProperties);

    stringBuilder.append("\nReachable properties:\n");
    Set<Property> reachableProperties = this.getReachableProperties();
    printProperties(stringBuilder, reachableProperties);

    stringBuilder.append("\nUnreachable properties:\n");
    Set<Property> unreachableProperties = this.getUnreachableProperties();
    printProperties(stringBuilder, unreachableProperties);

    boolean realizabilityResult = getLastAnalysis().getRealizabilityCheck();
    if (realizabilityResult) {
      stringBuilder.append("\nRealizability result: \nRealizable\n");
    } else {
      stringBuilder.append("\nRealizability result: \nUnrealizable\n");
    }

    return stringBuilder.toString();
  }

  /**
   * Append a friendly string for the given properties to the given string builder
   * @param stringBuilder
   * @param properties
   */
  private void printProperties(StringBuilder stringBuilder, Set<Property> properties)
  {
    if (properties.isEmpty())
    {
      stringBuilder.append("None.\n");
    }
    else
    {
      for (Property property : properties)
      {

        stringBuilder.append(property.getSource() + ": ");
        stringBuilder.append(property.getQualifiedName());

        if (Result.isPrintingLineNumbersEnabled())
        {
          stringBuilder.append(" in line " + property.getLine() + " ");
          stringBuilder.append("column " + property.getColumn() + ".");
        }
        stringBuilder.append("\n");
      }
    }
  }

  /**
   * @return a list of analyses done by kind2 for the current component.
   */
  public List<Analysis> getAnalyses()
  {
    return analyses;
  }

 /**
   * @return the list of node results for the subcomponents.
   */
  public Set<NodeResult> getChildren()
  {
    return children;
  }

  /**
   * @return the last analysis done by kind2 for this component.
   * The last analysis contains the final result.
   */
  public Analysis getLastAnalysis()
  {
    return analyses.get(analyses.size() - 1);
  }

  /**
   * Add the node result of a subcomponent. This method is used internally, and should not be public.
   * @param child
   */
  void addChild(NodeResult child)
  {
    children.add(child);
    // add this as another parent to the nodeResult
    child.parents.add(this);
  }

  /**
   * @return the list of components that call this component.
   */
  public List<NodeResult> getParents()
  {
    return parents;
  }

  /**
   * analyze kind2 output for the current node and its subnodes.
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
  public void analyze()
  {
    // analyze children first
    for (NodeResult child : children)
    {
      if (!child.isAnalyzed)
      {
        child.analyze();
      }
    }

    Analysis lastAnalysis = getLastAnalysis();
    List<Property> unknownProperties = lastAnalysis.getUnknownProperties();
    List<Property> falsifiedProperties = lastAnalysis.getFalsifiedProperties();
    List<Property> falsifiedAssumptions = falsifiedProperties
        .stream().filter(p -> p.getSource() == PropertyType.assumption)
        .collect(Collectors.toList());

    if (falsifiedProperties.isEmpty())
    {
      if (analyses.size() == 1)
      {
        if (unknownProperties.isEmpty())
        {
          // Suggestion 1: No action required.
          suggestions.add(Suggestion.noActionRequired(this));
        }
      }
      else
      {
        // mode analysis is not included in the last analysis
        Optional<Analysis> modeAnalysis = analyses
            .stream().filter(a -> a.isModeAnalysis()).findFirst();

        if (modeAnalysis.isPresent())
        {
          if (!modeAnalysis.get().getFalsifiedProperties().isEmpty())
          {
            // suggestion 7: Fix one mode active
            suggestions.add(Suggestion.fixOneModeActive(this, modeAnalysis.get()));
          }
          else
          {
            if (analyses.size() == 2)
            {
              if (unknownProperties.isEmpty())
              {
                // Suggestion 1: No action required.
                suggestions.add(Suggestion.noActionRequired(this));
              }
            }
            else
            {
              if (unknownProperties.isEmpty())
              {
                // suggestion 2: Fix the contract of a sub component
                suggestions.add(Suggestion.strengthenSubComponentContract(this));
              }
            }
          }
        }
        else
        {
          if (unknownProperties.isEmpty())
          {
            // suggestion 2: Fix the contract of a sub component
            suggestions.add(Suggestion.strengthenSubComponentContract(this));
          }
        }
      }
    }
    else
    {
      if (falsifiedAssumptions.size() == falsifiedProperties.size())
      {
        // suggestion 3: Either complete specification of N ’s contract, or remove component M.
        if (unknownProperties.isEmpty())
        {
          suggestions.add(Suggestion.
                                             completeSpecificationOrRemoveSubNodes(this, falsifiedAssumptions, true));
        }
        else
        {
          // we are not sure if contract is satisfied with unknown properties.
          suggestions.add(Suggestion.
                                             completeSpecificationOrRemoveSubNodes(this, falsifiedAssumptions, false));
        }
      }
      else // some guarantees or ensures are falsified
      {
        if (falsifiedAssumptions.size() > 0)
        {
          // some assumptions are falsified
          // suggestion 4: Either make A weaker, or fix N ’s definition to satisfy A.
          suggestions.add(Suggestion.makeWeakerOrFixDefinition(this, falsifiedAssumptions));
        }
        else
        {
          // check whether the subcomponents satisfy their contracts

          List<Property> subComponentUnprovenProperties = new ArrayList<>();
          for (NodeResult nodeResult : children)
          {
            List<Property> properties = nodeResult
                .getLastAnalysis().getFalsifiedProperties();
            properties.addAll(nodeResult.getLastAnalysis().getUnknownProperties());
            subComponentUnprovenProperties.addAll(properties);
          }

          if (subComponentUnprovenProperties.size() == 0)
          {
            // suggestion 5: Either make assumptions stronger, or fix definition to satisfy guarantees
            suggestions.add(Suggestion.makeAssumptionStrongerOrFixDefinition(this,
                falsifiedProperties));
          }
          else
          {
            // suggestion 6: fix reported issues for N ’s subcomponents.
            suggestions.add(Suggestion.fixSubComponentIssues(this,
                subComponentUnprovenProperties));
          }
        }
      }
    }

    if (!unknownProperties.isEmpty())
    {
      // increase the time out for unknown properties
      suggestions.add(Suggestion.increaseTimeout(this, unknownProperties));
    }

    isAnalyzed = true;
  }

  /**
   * @return the associated kind2 result.
   */
  public Result getKind2Result()
  {
    return kind2Result;
  }

  /**
   * @return the final list of falsified properties for this component and its subcomponents.
   */
  public Set<Property> getFalsifiedProperties()
  {
    Set<Property> falsifiedProperties = new HashSet<>();

    for (NodeResult child : children)
    {
      falsifiedProperties.addAll(child.getFalsifiedProperties());
    }

    for (Analysis analysis : analyses)
    {
      if (analysis.isModeAnalysis())
      {
        falsifiedProperties.addAll(analysis.getFalsifiedProperties());
      }
    }
    falsifiedProperties.addAll(getLastAnalysis().getFalsifiedProperties());
    return falsifiedProperties;
  }

  /**
   * @return the final list of valid properties for this component and its subcomponents.
   */
  public Set<Property> getValidProperties()
  {
    Set<Property> validProperties = new HashSet<>();

    for (NodeResult child : children)
    {
      validProperties.addAll(child.getValidProperties());
    }

    for (Analysis analysis : analyses)
    {
      validProperties.addAll(analysis.getValidProperties());
    }
    return validProperties;
  }

  /**
   * @return the final list of unknown properties for this component and its subcomponents.
   */
  public Set<Property> getUnknownProperties()
  {
    Set<Property> unknownProperties = new HashSet<>();

    for (NodeResult child : children)
    {
      unknownProperties.addAll(child.getUnknownProperties());
    }

    unknownProperties.addAll(getLastAnalysis().getUnknownProperties());

    return unknownProperties;
  }

  /**
   * @return the final list of reachable properties for this component and its subcomponents.
   */
  public Set<Property> getReachableProperties()
  {
    Set<Property> reachableProperties = new HashSet<>();

    for (NodeResult child : children)
    {
      reachableProperties.addAll(child.getReachableProperties());
    }

    reachableProperties.addAll(getLastAnalysis().getReachableProperties());

    return reachableProperties;
  }

  /**
   * @return the final list of unreachable properties for this component and its subcomponents.
   */
  public Set<Property> getUnreachableProperties()
  {
    Set<Property> unreachableProperties = new HashSet<>();

    for (NodeResult child : children)
    {
      unreachableProperties.addAll(child.getUnreachableProperties());
    }

    unreachableProperties.addAll(getLastAnalysis().getUnreachableProperties());

    return unreachableProperties;
  }
}
