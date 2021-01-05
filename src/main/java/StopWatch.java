/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

import java.util.Arrays;
import java.util.Collections;
import edu.uiowa.kind2.api.Kind2Api;
import edu.uiowa.kind2.lustre.ComponentBuilder;
import edu.uiowa.kind2.lustre.ContractBodyBuilder;
import edu.uiowa.kind2.lustre.ContractBuilder;
import edu.uiowa.kind2.lustre.Expr;
import edu.uiowa.kind2.lustre.ExprUtil;
import edu.uiowa.kind2.lustre.IdExpr;
import edu.uiowa.kind2.lustre.ImportedComponentBuilder;
import edu.uiowa.kind2.lustre.ModeBuilder;
import edu.uiowa.kind2.lustre.ProgramBuilder;
import edu.uiowa.kind2.lustre.TypeUtil;
import edu.uiowa.kind2.results.Kind2Result;

/**
 * This is an illustration of how to use the Java API for Kind 2 to implement a StopWatch Lustre
 * Program.
 */
public class StopWatch {
  /**
   * Run the main function to print the generated Lustre program and results of calling Kind 2.
   *
   * @param args inputs to the program (ignored)
   */
  public static void main(String[] args) {
    // Begin constructing the Lustre program by creating a ProgramBuilder object
    ProgramBuilder pb = new ProgramBuilder();

    // Add components to the program builder using pb's member methods
    pb.importFunction(sqrt());
    pb.addContract(stopWatchSpec());
    pb.addFunction(even());
    pb.addFunction(toInt());
    pb.addNode(count());
    pb.addNode(sofar());
    pb.addNode(since());
    pb.addNode(sinceIncl());
    pb.addNode(increased());
    pb.addNode(stable());
    pb.addNode(stopWatch());

    // Build the program by calling the build() method
    // You can display the program using the toString() method
    System.out.println(pb.build().toString());

    // Create a Kind2Api object to set options and check the Lustre program
    Kind2Api api = new Kind2Api();
    // Call Kind2Api's execute method to run Kind 2 analysis on the lustre program. The results of
    // the analysis are saved in a Kind2Result object
    Kind2Result result = api.execute(pb.build());

    // Check if the result object is initialized before printing it.
    if (result.isInitialized()) {
      System.out.println(result);
    }
  }

  /**
   * This methods constructs the following Lustre imported function:
   *
   * <pre>
   * function imported sqrt (n : real) returns (r : real);
   * (*@contract
   *   assume (n) >= (0.0);
   *   guarantee ((r) >= (0.0)) and (((r) * (r)) = (n));
   * *)
   * </pre>
   *
   * @return a builder for the imported sqrt function
   */
  static ImportedComponentBuilder sqrt() {
    // Use ExprUtil to construct various expressions
    IdExpr n = ExprUtil.id("n");
    IdExpr r = ExprUtil.id("r");

    // Use ContractBodyBuilder to construct a contract body
    ContractBodyBuilder cbb = new ContractBodyBuilder();
    cbb.assume(ExprUtil.greaterEqual(n, ExprUtil.real("0.0")));
    cbb.guarantee(ExprUtil.and(ExprUtil.greaterEqual(r, ExprUtil.real("0.0")),
        ExprUtil.equal(ExprUtil.multiply(r, r), n)));

    // Use ImportedComponentBuilder to construct an imported component (a function in this case)
    ImportedComponentBuilder fb = new ImportedComponentBuilder("sqrt");
    fb.createVarInput("n", TypeUtil.REAL);
    fb.createVarOutput("r", TypeUtil.REAL);
    fb.setContractBody(cbb);

    return fb;
  }

  /**
   * This methods constructs the following Kind 2 contract:
   *
   * <pre>
   * contract StopWatchSpec (toggle : bool; reset : bool) returns (time : int);
   * let
   *   var on : bool = (toggle) -> (((pre (on)) and (not (toggle))) or ((not (pre (on))) and (toggle)));
   *   assume not ((toggle) and (reset));
   *   guarantee ((on) => ((time) = (1))) -> (true);
   *   guarantee ((not (on)) => ((time) = (0))) -> (true);
   *   guarantee (time) >= (0);
   *   guarantee ((not (reset)) and (Since(reset, even(Count(toggle))))) => (Stable(time));
   *   guarantee ((not (reset)) and (Since(reset, not (even(Count(toggle)))))) => (Increased(time));
   *   guarantee (true) -> (((not (even(Count(toggle)))) and ((Count(reset)) = (0))) => ((time) > (pre (time))));
   *   mode resetting (
   *     require reset;
   *     ensure (time) = (0);
   *   );
   *   mode running (
   *     require on;
   *     require not (reset);
   *     ensure (true) -> ((time) = ((pre (time)) + (1)));
   *   );
   *   mode stopped (
   *     require not (reset);
   *     require not (on);
   *     ensure (true) -> ((time) = (pre (time)));
   *   );
   * tel
   * </pre>
   *
   * @return a builder for the StopWatchSpec contract
   */
  static ContractBuilder stopWatchSpec() {
    // Use ContractBuilder to construct a contract
    ContractBuilder cb = new ContractBuilder("StopWatchSpec");

    // Use ContractBuilder's methods to specify inputs/outputs of the contract
    // For convenience, some methods return IdExpr objects for later use
    IdExpr toggle = cb.createVarInput("toggle", TypeUtil.BOOL);
    IdExpr reset = cb.createVarInput("reset", TypeUtil.BOOL);
    IdExpr time = cb.createVarOutput("time", TypeUtil.INT);

    IdExpr on = ExprUtil.id("on");

    // Use ContractBodyBuilder to construct a contract body
    ContractBodyBuilder cbb = new ContractBodyBuilder();

    cbb.createVarDef("on", TypeUtil.BOOL,
        ExprUtil.arrow(toggle, ExprUtil.or(ExprUtil.and(ExprUtil.pre(on), ExprUtil.not(toggle)),
            ExprUtil.and(ExprUtil.not(ExprUtil.pre(on)), toggle))));

    cbb.assume(ExprUtil.not(ExprUtil.and(toggle, reset)));
    cbb.guarantee(ExprUtil.arrow(ExprUtil.implies(on, ExprUtil.equal(time, ExprUtil.integer(1))),
        ExprUtil.TRUE));
    cbb.guarantee(ExprUtil.arrow(
        ExprUtil.implies(ExprUtil.not(on), ExprUtil.equal(time, ExprUtil.integer(0))),
        ExprUtil.TRUE));
    cbb.guarantee(ExprUtil.greaterEqual(time, ExprUtil.integer(0)));

    cbb.guarantee(ExprUtil.implies(
        ExprUtil.and(ExprUtil.not(reset),
            ExprUtil.nodeCall(ExprUtil.id("Since"), reset,
                ExprUtil.functionCall(ExprUtil.id("even"),
                    ExprUtil.nodeCall(ExprUtil.id("Count"), toggle)))),
        ExprUtil.nodeCall(ExprUtil.id("Stable"), time)));

    cbb.guarantee(ExprUtil.implies(
        ExprUtil.and(ExprUtil.not(reset),
            ExprUtil.nodeCall(ExprUtil.id("Since"), reset,
                ExprUtil.not(ExprUtil.functionCall(ExprUtil.id("even"),
                    ExprUtil.nodeCall(ExprUtil.id("Count"), toggle))))),
        ExprUtil.nodeCall(ExprUtil.id("Increased"), time)));

    cbb.guarantee(
        ExprUtil
            .arrow(ExprUtil.TRUE,
                ExprUtil.implies(ExprUtil.and(
                    ExprUtil.not(ExprUtil.functionCall(ExprUtil.id("even"),
                        ExprUtil.nodeCall(ExprUtil.id("Count"),
                            Collections.singletonList(toggle)))),
                    ExprUtil.equal(ExprUtil.nodeCall(ExprUtil.id("Count"), reset),
                        ExprUtil.integer(0))),
                    ExprUtil.greater(time, ExprUtil.pre(time)))));

    // Use ModeBuilder to construct a contract mode
    ModeBuilder resetting = new ModeBuilder("resetting");
    resetting.require(reset);
    resetting.ensure(ExprUtil.equal(time, ExprUtil.integer(0)));

    cbb.addMode(resetting);

    ModeBuilder running = new ModeBuilder("running");
    running.require(on);
    running.require(ExprUtil.not(reset));
    running.ensure(ExprUtil.arrow(ExprUtil.TRUE,
        ExprUtil.equal(time, ExprUtil.plus(ExprUtil.pre(time), ExprUtil.integer(1)))));

    cbb.addMode(running);

    ModeBuilder stopped = new ModeBuilder("stopped");
    stopped.require(ExprUtil.not(reset));
    stopped.require(ExprUtil.not(on));
    stopped.ensure(ExprUtil.arrow(ExprUtil.TRUE, ExprUtil.equal(time, ExprUtil.pre(time))));

    cbb.addMode(stopped);

    cb.setContractBody(cbb);

    return cb;
  }

  /**
   * This methods constructs the following Lustre function:
   *
   * <pre>
   * function even (N : int) returns (B : bool);
   * let
   *   B = ((N) mod (2)) = (0);
   * tel
   * </pre>
   *
   * @return a builder for the even function
   */
  static ComponentBuilder even() {
    // Use ComponentBuilder to construct a component (a function in this case)
    ComponentBuilder fb = new ComponentBuilder("even");
    IdExpr N = fb.createVarInput("N", TypeUtil.INT);
    IdExpr B = fb.createVarOutput("B", TypeUtil.BOOL);
    fb.addEquation(B, ExprUtil.equal(ExprUtil.mod(N, ExprUtil.integer(2)), ExprUtil.integer(0)));
    return fb;
  }

  /**
   * This methods constructs the following Lustre function:
   *
   * <pre>
   * function toInt (X : bool) returns (N : int);
   * let
   * N = if (X) then (1) else (0);
   * tel
   * </pre>
   *
   * @return a builder for the toInt function
   */
  static ComponentBuilder toInt() {
    ComponentBuilder f = new ComponentBuilder("toInt");
    IdExpr X = f.createVarInput("X", TypeUtil.BOOL);
    IdExpr N = f.createVarOutput("N", TypeUtil.INT);
    f.addEquation(N, ExprUtil.ite(X, ExprUtil.integer(1), ExprUtil.integer(0)));
    return f;
  }

  /**
   * This methods constructs the following Lustre node:
   *
   * <pre>
   * node Count (X : bool) returns (N : int);
   * let
   *   N = (toInt(X)) -> ((toInt(X)) + (pre (N)));
   * tel
   * </pre>
   *
   * @return a builder for the Count node
   */
  static ComponentBuilder count() {
    ComponentBuilder nb = new ComponentBuilder("Count");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr N = nb.createVarOutput("N", TypeUtil.INT);
    Expr toIntX = ExprUtil.nodeCall(ExprUtil.id("toInt"), X);
    nb.addEquation(N, ExprUtil.arrow(toIntX, ExprUtil.plus(toIntX, ExprUtil.pre(N))));
    return nb;
  }

  /**
   * This methods constructs the following Lustre node:
   *
   * <pre>
   * node Sofar (X : bool) returns (Y : bool);
   * let
   *   Y = (X) -> ((X) and (pre (Y)));
   * tel
   * </pre>
   *
   * @return a builder for the SoFar node
   */
  static ComponentBuilder sofar() {
    ComponentBuilder nb = new ComponentBuilder("Sofar");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarOutput("Y", TypeUtil.BOOL);
    nb.addEquation(Y, ExprUtil.arrow(X, ExprUtil.and(X, ExprUtil.pre(Y))));
    return nb;
  }

  /**
   * This methods constructs the following Lustre node:
   *
   * <pre>
   * node Since (X : bool; Y : bool) returns (Z : bool);
   * let
   *   Z = (X) or ((Y) and ((false) -> (pre (Z))));
   * tel
   * </pre>
   *
   * @return a builder for the Since node
   */
  static ComponentBuilder since() {
    ComponentBuilder nb = new ComponentBuilder("Since");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarInput("Y", TypeUtil.BOOL);
    IdExpr Z = nb.createVarOutput("Z", TypeUtil.BOOL);
    nb.addEquation(Z,
        ExprUtil.or(X, ExprUtil.and(Y, ExprUtil.arrow(ExprUtil.FALSE, ExprUtil.pre(Z)))));
    return nb;
  }

  /**
   * This methods constructs the following Lustre node:
   *
   * <pre>
   * node SinceIncl (X : bool; Y : bool) returns (Z : bool);
   * let
   *   Z = (Y) and ((X) or ((false) -> (pre (Z))));
   * tel
   * </pre>
   *
   * @return a builder for the SinceIncl node
   */
  static ComponentBuilder sinceIncl() {
    ComponentBuilder nb = new ComponentBuilder("SinceIncl");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarInput("Y", TypeUtil.BOOL);
    IdExpr Z = nb.createVarOutput("Z", TypeUtil.BOOL);
    nb.addEquation(Z,
        ExprUtil.and(Y, ExprUtil.or(X, ExprUtil.arrow(ExprUtil.FALSE, ExprUtil.pre(Z)))));
    return nb;
  }

  /**
   * This methods constructs the following Lustre node:
   *
   * <pre>
   * node Increased (N : int) returns (B : bool);
   * let
   *   B = (true) -> ((N) > (pre (N)));
   * tel
   * </pre>
   *
   * @return a builder for the Increased node
   */
  static ComponentBuilder increased() {
    ComponentBuilder nb = new ComponentBuilder("Increased");
    IdExpr N = nb.createVarInput("N", TypeUtil.INT);
    IdExpr B = nb.createVarOutput("B", TypeUtil.BOOL);
    nb.addEquation(B, ExprUtil.arrow(ExprUtil.TRUE, ExprUtil.greater(N, ExprUtil.pre(N))));
    return nb;
  }

  /**
   * This methods constructs the following Lustre node:
   *
   * <pre>
   * node Stable (N : int) returns (B : bool);
   * let
   *   B = (true) -> ((N) = (pre (N)));
   * tel
   * </pre>
   *
   * @return a builder for the Stable node
   */
  static ComponentBuilder stable() {
    ComponentBuilder nb = new ComponentBuilder("Stable");
    IdExpr N = nb.createVarInput("N", TypeUtil.INT);
    IdExpr B = nb.createVarOutput("B", TypeUtil.BOOL);
    nb.addEquation(B, ExprUtil.arrow(ExprUtil.TRUE, ExprUtil.equal(N, ExprUtil.pre(N))));
    return nb;
  }

  /**
   * This methods constructs the following Lustre node:
   *
   * <pre>
   * node Stopwatch (toggle : bool; reset : bool) returns (count : int);
   * (*@contract
   *   import StopWatchSpec (toggle, reset) returns (count);
   *   guarantee not (((::StopWatchSpec::resetting) and (::StopWatchSpec::running)) and (::StopWatchSpec::stopped));
   * *)
   * var
   *   running : bool;
   * let
   *   running = ((false) -> (pre (running))) <> (toggle);
   *   count = if (reset) then (0) else (if (running) then ((1) -> ((pre (count)) + (1))) else ((0) -> (pre (count))));
   * tel
   * </pre>
   *
   * @return a builder for the StopWatch node
   */
  static ComponentBuilder stopWatch() {
    ComponentBuilder nb = new ComponentBuilder("Stopwatch");
    IdExpr toggle = nb.createVarInput("toggle", TypeUtil.BOOL);
    IdExpr reset = nb.createVarInput("reset", TypeUtil.BOOL);
    IdExpr count = nb.createVarOutput("count", TypeUtil.INT);

    ContractBodyBuilder cbb = new ContractBodyBuilder();
    cbb.importContract("StopWatchSpec", Arrays.asList(toggle, reset),
        Collections.singletonList(count));

    cbb.guarantee(ExprUtil.not(ExprUtil.and(ExprUtil.modeRef("StopWatchSpec", "resetting"),
        ExprUtil.modeRef("StopWatchSpec", "running"),
        ExprUtil.modeRef("StopWatchSpec", "stopped"))));

    nb.setContractBody(cbb);

    IdExpr running = nb.createLocalVar("running", TypeUtil.BOOL);

    nb.addEquation(running,
        ExprUtil.notEqual(ExprUtil.arrow(ExprUtil.FALSE, ExprUtil.pre(running)), toggle));
    nb.addEquation(count,
        ExprUtil.ite(reset, ExprUtil.integer(0),
            ExprUtil.ite(running,
                ExprUtil.arrow(ExprUtil.integer(1),
                    ExprUtil.plus(ExprUtil.pre(count), ExprUtil.integer(1))),
                ExprUtil.arrow(ExprUtil.integer(0), ExprUtil.pre(count)))));

    return nb;
  }
}
