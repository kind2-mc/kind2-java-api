/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.kind2results;

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
    Kind2Result.setPrintingCounterExamplesEnabled(true);
    Kind2Result.setPrintingUnknownCounterExamplesEnabled(true);
    Kind2Result.setPrintingLineNumbersEnabled(true);
    Kind2Result.setOpeningSymbols("{{");
    Kind2Result.setClosingSymbols("}}");
    Kind2Result.setRealPrecision(2);
    Kind2Result.setRealRoundingMode(RoundingMode.HALF_UP);

    if (args.length > 0)
    {
      try
      {
        String json = new String(Files.readAllBytes(Paths.get(args[0])));
        Kind2Result result = Kind2Result.analyzeJsonResult(json);
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
