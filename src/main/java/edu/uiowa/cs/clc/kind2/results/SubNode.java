/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the input, output, and local streams for the current component.
 */
public class SubNode
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;
  /**
   * The name of the current component.
   */
  private final String name;

  /**
   * The type of the current component. Can be "node", "function", or "state".
   */
  private final String blockType;
  /**
   * The input, output, and local streams of the current component
   */
  private final List<Stream> streams;
  /**
   * The streams of the subcomponents for this current component
   */
  private final List<SubNode> subNodes;
  /**
   * The associated counter example.
   */
  private final CounterExample counterExample;

  public SubNode(CounterExample counterExample, JsonElement jsonElement)
  {
    this.counterExample = counterExample;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

    if (jsonElement.isJsonArray())
    {
      jsonElement = jsonElement.getAsJsonArray().get(0);
    }

    blockType = jsonElement.getAsJsonObject().get(Labels.blockType).getAsString();
    name = jsonElement.getAsJsonObject().get(Labels.name).getAsString();

    streams = new ArrayList<>();
    JsonElement streamElements = jsonElement.getAsJsonObject().get(Labels.streams);

    if (streamElements != null)
    {
      if (streamElements.isJsonArray())
      {
        for (JsonElement element : streamElements.getAsJsonArray())
        {
          Stream stream = new Stream(this, element);
          streams.add(stream);
        }
      }
      else
      {
        Stream stream = new Stream(this, streamElements);
        streams.add(stream);
      }
    }
    subNodes = new ArrayList<>();
    JsonElement subNodeElements = jsonElement.getAsJsonObject().get(Labels.subNodes);

    if (subNodeElements != null)
    {
      if (subNodeElements.isJsonArray())
      {
        for (JsonElement element : subNodeElements.getAsJsonArray())
        {
          SubNode node = new SubNode(counterExample, element);
          subNodes.add(node);
        }
      }
      else
      {
        SubNode node = new SubNode(counterExample, subNodeElements);
        subNodes.add(node);
      }
    }
  }

  /**
   * @return the name of the current component.
   */
  public String getName()
  {
    return Result.getOpeningSymbols() + name + Result.getClosingSymbols();
  }

  /**
   * @return the associated kind2 result.
   */
  public Result getKind2Result()
  {
    return counterExample.getKind2Result();
  }

  /**
   * @return the streams for this component.
   */
  public List<Stream> getStreams()
  {
    return streams;
  }

  /**
   * @return the streams of the subcomponents
   */
  public List<SubNode> getSubNodes()
  {
    return subNodes;
  }

  /**
   * @return the type of the current component
   */
  public String getBlockType()
  {
    return blockType;
  }

  @Override
  public String toString()
  {
    return print(getMaxNameLength(), getMaxValueLength());
  }

  /**
   * @return Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  /**
   * @return the maximum string length of all variable names in order to align the string output of streams.
   * This method is used internally and should not be public.
   */
  int getMaxNameLength()
  {
    int maxLength = 0;
    for (Stream stream : streams)
    {
      if (stream.getName().length() > maxLength)
      {
        maxLength = stream.getName().length();
      }
    }

    // visit sub nodes
    for (SubNode node : subNodes)
    {
      int subNodeMaxLength = node.getMaxNameLength();
      if (subNodeMaxLength > maxLength)
      {
        maxLength = subNodeMaxLength;
      }
    }

    return maxLength;
  }

  /**
   * @return the maximum string length of values in order to align values of streams.
   * This method is used internally and should not be public.
   */
  int getMaxValueLength()
  {
    int maxLength = 0;
    for (Stream stream : streams)
    {
      for (StepValue value : stream.getStepValues())
      {
        if (value.toString().length() > maxLength)
        {
          maxLength = value.toString().length();
        }
      }
    }

    // visit sub nodes
    for (SubNode node : subNodes)
    {
      int subNodeMaxLength = node.getMaxValueLength();
      if (subNodeMaxLength > maxLength)
      {
        maxLength = subNodeMaxLength;
      }
    }

    return maxLength;
  }

  /**
   * Print the result of this component in friendly format.
   * @param maxNameLength the maximum string lengths of variable names in streams
   * @param maxValueLength the maximum string length of values in streams.
   * @return a friendly formatted string for this component.
   */
  String print(int maxNameLength, int maxValueLength)
  {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\n  Node " + name + ":\n");
    stringBuilder.append("    == Inputs ==");
    // first print the time
    for (Stream stream : streams)
    {
      if (stream.getStreamClass().equals(Labels.input))
      {
        printStream(maxNameLength, maxValueLength, stringBuilder, stream);
      }
    }
    stringBuilder.append("\n    == Outputs ==");

    for (Stream stream : streams)
    {
      if (stream.getStreamClass().equals(Labels.output))
      {
        printStream(maxNameLength, maxValueLength, stringBuilder, stream);
      }
    }

    stringBuilder.append("\n    == Locals ==");
    for (Stream stream : streams)
    {
      if (stream.getStreamClass().equals(Labels.local))
      {
        printStream(maxNameLength, maxValueLength, stringBuilder, stream);
      }
    }

    // visit sub nodes
    for (SubNode node : subNodes)
    {
      stringBuilder.append(node.print(maxNameLength, maxValueLength));
    }

    return stringBuilder.toString();
  }

  /**
   * Print the result of a given stream in friendly format.
   * @param maxNameLength the maximum string lengths of variable names in streams.
   * @param maxValueLength the maximum string length of values in streams.
   * @param stringBuilder used to append a friendly formatted string for the given stream.
   */
  private void printStream(int maxNameLength, int maxValueLength, StringBuilder stringBuilder, Stream stream)
  {
    String streamName = String.format("%-" + maxNameLength + "s", stream.getName());
    stringBuilder.append("\n    " + streamName + "\t");
    for (StepValue value : stream.getStepValues())
    {
      String paddedValue = String.format("%1$" + maxValueLength + "s", value.print());
      stringBuilder.append(paddedValue + "\t");
    }
  }
}
