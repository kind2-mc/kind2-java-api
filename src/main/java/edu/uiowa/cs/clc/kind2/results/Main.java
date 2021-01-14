/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main class of kind2-explanations.
 */
public class Main
{
  public static void main(String args[])
  {
    Result.setPrintingCounterExamplesEnabled(true);
    Result.setPrintingUnknownCounterExamplesEnabled(true);
    Result.setPrintingLineNumbersEnabled(true);
    Result.setOpeningSymbols("{{");
    Result.setClosingSymbols("}}");
    Result.setRealPrecision(2);
    Result.setRealRoundingMode(RoundingMode.HALF_UP);

    if (args.length > 0)
    {
      try
      {
        String json = new String(Files.readAllBytes(Paths.get(args[0])));
        Result result = Result.analyzeJsonResult(json);
        System.out.println(result.toString());
      }
      catch (IOException e)
      {
        System.out.println(e.getMessage());
      }
    }
    else
    {
      System.out.println("Usage: java -jar Kind2JavaClient.jar file.json");
    }
  }
}
