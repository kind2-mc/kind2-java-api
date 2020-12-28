/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

import com.google.gson.annotations.Expose;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class stores the result of all analyses done by kind2 for a given component.
 */
public class Kind2NodeResult
{
  /**
   * the name of the component.
   */
  private final String name;

  /**
   * A list of analyses done by kind2 for the current component.
   */
  private List<Kind2Analysis> analyses = new ArrayList<>();

  /**
   * The list of node results for the subcomponents.
   */
  private Set<Kind2NodeResult> children = new HashSet<>();

  /**
   * The list of components that call this component.
   */
  private List<Kind2NodeResult> parents = new ArrayList<>();

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
  private List<Kind2Suggestion> suggestions = new ArrayList<>();

  /**
   * The associated kind2 result.
   */
  @Expose(serialize = false)
  private final Kind2Result kind2Result;

  public Kind2NodeResult(Kind2Result kind2Result, String name)
  {
    this.kind2Result = kind2Result;
    this.name = name;
  }

  /**
   * Add an analysis for this component.
   * @param analysis
   */
  public void addAnalysis(Kind2Analysis analysis)
  {
    getAnalyses().add(analysis);
    analysis.setNodeResult(this);
  }

  /**
   * @return the list of suggestions for this component.
   */
  public List<Kind2Suggestion> getSuggestions()
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

    for (Kind2NodeResult child : children)
    {
      stringBuilder.append(child.toString() + "\n");
    }

    stringBuilder.append("Component: " + name + "\n");

    for (Kind2Suggestion suggestion : suggestions)
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
    Set<Kind2Property> validProperties = this.getValidProperties();
    printProperties(stringBuilder, validProperties);

    stringBuilder.append("\nFalsified properties:\n");
    Set<Kind2Property> falsifiedProperties = this.getFalsifiedProperties();
    printProperties(stringBuilder, falsifiedProperties);

    stringBuilder.append("\nUnknown properties:\n");
    Set<Kind2Property> unknownProperties = this.getUnknownProperties();
    printProperties(stringBuilder, unknownProperties);

    return stringBuilder.toString();
  }

  /**
   * Append a friendly string for the given properties to the given string builder
   * @param stringBuilder
   * @param properties
   */
  private void printProperties(StringBuilder stringBuilder, Set<Kind2Property> properties)
  {
    if (properties.isEmpty())
    {
      stringBuilder.append("None.\n");
    }
    else
    {
      for (Kind2Property property : properties)
      {

        stringBuilder.append(property.getSource() + ": ");
        stringBuilder.append(property.getQualifiedName());

        if (Kind2Result.isPrintingLineNumbersEnabled())
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
  public List<Kind2Analysis> getAnalyses()
  {
    return analyses;
  }

 /**
   * @return the list of node results for the subcomponents.
   */
  public Set<Kind2NodeResult> getChildren()
  {
    return children;
  }

  /**
   * @return the last analysis done by kind2 for this component.
   * The last analysis contains the final result.
   */
  public Kind2Analysis getLastAnalysis()
  {
    return analyses.get(analyses.size() - 1);
  }

  /**
   * Add the node result of a subcomponent. This method is used internally, and should not be public.
   * @param child
   */
  void addChild(Kind2NodeResult child)
  {
    children.add(child);
    // add this as another parent to the nodeResult
    child.parents.add(this);
  }

  /**
   * @return the list of components that call this component.
   */
  public List<Kind2NodeResult> getParents()
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
    for (Kind2NodeResult child : children)
    {
      if (!child.isAnalyzed)
      {
        child.analyze();
      }
    }

    Kind2Analysis lastAnalysis = getLastAnalysis();
    List<Kind2Property> unknownProperties = lastAnalysis.getUnknownProperties();
    List<Kind2Property> falsifiedProperties = lastAnalysis.getFalsifiedProperties();
    List<Kind2Property> falsifiedAssumptions = falsifiedProperties
        .stream().filter(p -> p.getSource() == Kind2PropertyType.assumption)
        .collect(Collectors.toList());

    if (falsifiedProperties.isEmpty())
    {
      if (analyses.size() == 1)
      {
        if (unknownProperties.isEmpty())
        {
          // Suggestion 1: No action required.
          suggestions.add(Kind2Suggestion.noActionRequired(this));
        }
      }
      else
      {
        // mode analysis is not included in the last analysis
        Optional<Kind2Analysis> modeAnalysis = analyses
            .stream().filter(a -> a.isModeAnalysis()).findFirst();

        if (modeAnalysis.isPresent())
        {
          if (!modeAnalysis.get().getFalsifiedProperties().isEmpty())
          {
            // suggestion 7: Fix one mode active
            suggestions.add(Kind2Suggestion.fixOneModeActive(this, modeAnalysis.get()));
          }
          else
          {
            if (analyses.size() == 2)
            {
              if (unknownProperties.isEmpty())
              {
                // Suggestion 1: No action required.
                suggestions.add(Kind2Suggestion.noActionRequired(this));
              }
            }
            else
            {
              if (unknownProperties.isEmpty())
              {
                // suggestion 2: Fix the contract of a sub component
                suggestions.add(Kind2Suggestion.strengthenSubComponentContract(this));
              }
            }
          }
        }
        else
        {
          if (unknownProperties.isEmpty())
          {
            // suggestion 2: Fix the contract of a sub component
            suggestions.add(Kind2Suggestion.strengthenSubComponentContract(this));
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
          suggestions.add(Kind2Suggestion.
                                             completeSpecificationOrRemoveSubNodes(this, falsifiedAssumptions, true));
        }
        else
        {
          // we are not sure if contract is satisfied with unknown properties.
          suggestions.add(Kind2Suggestion.
                                             completeSpecificationOrRemoveSubNodes(this, falsifiedAssumptions, false));
        }
      }
      else // some guarantees or ensures are falsified
      {
        if (falsifiedAssumptions.size() > 0)
        {
          // some assumptions are falsified
          // suggestion 4: Either make A weaker, or fix N ’s definition to satisfy A.
          suggestions.add(Kind2Suggestion.makeWeakerOrFixDefinition(this, falsifiedAssumptions));
        }
        else
        {
          // check whether the subcomponents satisfy their contracts

          List<Kind2Property> subComponentUnprovenProperties = new ArrayList<>();
          for (Kind2NodeResult nodeResult : children)
          {
            List<Kind2Property> properties = nodeResult
                .getLastAnalysis().getFalsifiedProperties();
            properties.addAll(nodeResult.getLastAnalysis().getUnknownProperties());
            subComponentUnprovenProperties.addAll(properties);
          }

          if (subComponentUnprovenProperties.size() == 0)
          {
            // suggestion 5: Either make assumptions stronger, or fix definition to satisfy guarantees
            suggestions.add(Kind2Suggestion.makeAssumptionStrongerOrFixDefinition(this,
                falsifiedProperties));
          }
          else
          {
            // suggestion 6: fix reported issues for N ’s subcomponents.
            suggestions.add(Kind2Suggestion.fixSubComponentIssues(this,
                subComponentUnprovenProperties));
          }
        }
      }
    }

    if (!unknownProperties.isEmpty())
    {
      // increase the time out for unknown properties
      suggestions.add(Kind2Suggestion.increaseTimeout(this, unknownProperties));
    }

    isAnalyzed = true;
  }

  /**
   * @return the associated kind2 result.
   */
  public Kind2Result getKind2Result()
  {
    return kind2Result;
  }

  /**
   * @return the final list of falsified properties for this component and its subcomponents.
   */
  public Set<Kind2Property> getFalsifiedProperties()
  {
    Set<Kind2Property> falsifiedProperties = new HashSet<>();

    for (Kind2NodeResult child : children)
    {
      falsifiedProperties.addAll(child.getFalsifiedProperties());
    }

    for (Kind2Analysis analysis : analyses)
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
  public Set<Kind2Property> getValidProperties()
  {
    Set<Kind2Property> validProperties = new HashSet<>();

    for (Kind2NodeResult child : children)
    {
      validProperties.addAll(child.getValidProperties());
    }

    for (Kind2Analysis analysis : analyses)
    {
      validProperties.addAll(analysis.getValidProperties());
    }
    return validProperties;
  }

  /**
   * @return the final list of unknown properties for this component and its subcomponents.
   */
  public Set<Kind2Property> getUnknownProperties()
  {
    Set<Kind2Property> unknownProperties = new HashSet<>();

    for (Kind2NodeResult child : children)
    {
      unknownProperties.addAll(child.getUnknownProperties());
    }

    unknownProperties.addAll(getLastAnalysis().getUnknownProperties());

    return unknownProperties;
  }
}
