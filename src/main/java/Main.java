/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

import java.util.Arrays;
import java.util.Collections;

import edu.uiowa.kind2.api.IProgressMonitor;
import edu.uiowa.kind2.api.Kind2Api;
import edu.uiowa.kind2.results.Kind2Result;
import edu.uiowa.kind2.lustre.IdExpr;
import edu.uiowa.kind2.lustre.LustreUtil;
import edu.uiowa.kind2.lustre.TypeUtil;
import edu.uiowa.kind2.lustre.ContractBodyBuilder;
import edu.uiowa.kind2.lustre.ContractBuilder;
import edu.uiowa.kind2.lustre.Expr;
import edu.uiowa.kind2.lustre.FunctionBuilder;
import edu.uiowa.kind2.lustre.ModeBuilder;
import edu.uiowa.kind2.lustre.NodeBuilder;
import edu.uiowa.kind2.lustre.ProgramBuilder;

public class Main {
  public static void main(String[] args) {
    ProgramBuilder program = new ProgramBuilder();

    program.importFunction(sqrt());
    program.addContract(stopWatchSpec());
    program.addFunction(even());
    program.addFunction(toInt());
    program.addNode(count());
    program.addNode(sofar());
    program.addNode(since());
    program.addNode(sinceIncl());
    program.addNode(increased());
    program.addNode(stable());
    program.addNode(stopWatch());

    System.out.println(program.build().toString());

    Kind2Api api = new Kind2Api();
    Kind2Result result = new Kind2Result();

    api.execute(program.build(), result, new IProgressMonitor() {
      @Override
      public boolean isCanceled() {
        return false;
      }

      @Override
      public void done() {
      }
    });

    if (result.isInitialized()) {
      System.out.println(result);
    }
  }

  static FunctionBuilder sqrt() {
    IdExpr n = LustreUtil.id("n");
    IdExpr r = LustreUtil.id("r");

    ContractBodyBuilder cbb = new ContractBodyBuilder();
    cbb.addAssumption(LustreUtil.greaterEqual(n, LustreUtil.real("0.0")));
    cbb.addGuarantee(LustreUtil.and(LustreUtil.greaterEqual(r, LustreUtil.real("0.0")),
        LustreUtil.equal(LustreUtil.multiply(r, r), n)));

    FunctionBuilder fb = new FunctionBuilder("sqrt");
    fb.createVarInput("n", TypeUtil.REAL);
    fb.createVarOutput("r", TypeUtil.REAL);
    fb.setContractBody(cbb);

    return fb;
  }

  static ContractBuilder stopWatchSpec() {
    ContractBuilder cb = new ContractBuilder("StopWatchSpec");

    IdExpr toggle = cb.createVarInput("toggle", TypeUtil.BOOL);
    IdExpr reset = cb.createVarInput("reset", TypeUtil.BOOL);
    IdExpr time = cb.createVarOutput("time", TypeUtil.INT);

    IdExpr on = LustreUtil.id("on");

    cb.createVarDef("on", 
        TypeUtil.BOOL,
        LustreUtil.arrow(toggle,
            LustreUtil.or(LustreUtil.and(LustreUtil.pre(on), LustreUtil.not(toggle)),
                LustreUtil.and(LustreUtil.not(LustreUtil.pre(on)), toggle))));

    cb.addAssumption(LustreUtil.not(LustreUtil.and(toggle, reset)));
    cb.addGuarantee(LustreUtil.arrow(
        LustreUtil.implies(on, LustreUtil.equal(time, LustreUtil.integer(1))), LustreUtil.TRUE));
    cb.addGuarantee(LustreUtil.arrow(
        LustreUtil.implies(LustreUtil.not(on), LustreUtil.equal(time, LustreUtil.integer(0))),
        LustreUtil.TRUE));
    cb.addGuarantee(LustreUtil.greaterEqual(time, LustreUtil.integer(0)));

    cb.addGuarantee(LustreUtil.implies(
        LustreUtil.and(LustreUtil.not(reset),
            LustreUtil.nodeCall(LustreUtil.id("Since"), reset,
                LustreUtil.functionCall(LustreUtil.id("even"),
                    LustreUtil.nodeCall(LustreUtil.id("Count"), toggle)))),
        LustreUtil.nodeCall(LustreUtil.id("Stable"), time)));

    cb.addGuarantee(
        LustreUtil.implies(
            LustreUtil.and(LustreUtil.not(reset),
                LustreUtil.nodeCall(LustreUtil.id("Since"), reset,
                    LustreUtil.not(LustreUtil.functionCall(LustreUtil.id("even"),
                        LustreUtil.nodeCall(LustreUtil.id("Count"),
                            Collections.singletonList(toggle)))))),
            LustreUtil.nodeCall(LustreUtil.id("Increased"), Collections.singletonList(time))));

    cb.addGuarantee(
        LustreUtil
            .arrow(LustreUtil.TRUE,
                LustreUtil.implies(
                    LustreUtil.and(
                        LustreUtil.not(LustreUtil.functionCall(LustreUtil.id("even"),
                            LustreUtil.nodeCall(LustreUtil.id("Count"),
                                Collections.singletonList(toggle)))),
                        LustreUtil.equal(LustreUtil.nodeCall(LustreUtil.id("Count"), reset),
                            LustreUtil.integer(0))),
                    LustreUtil.greater(time, LustreUtil.pre(time)))));

    ModeBuilder resetting = new ModeBuilder("resetting");
    resetting.addRequire(reset);
    resetting.addEnsure(LustreUtil.equal(time, LustreUtil.integer(0)));

    cb.addMode(resetting);

    ModeBuilder running = new ModeBuilder("running");
    running.addEnsure(on);
    running.addEnsure(LustreUtil.not(reset));
    running.addRequire(LustreUtil.arrow(LustreUtil.TRUE,
        LustreUtil.equal(time, LustreUtil.plus(LustreUtil.pre(time), LustreUtil.integer(1)))));

    cb.addMode(running);

    ModeBuilder stopped = new ModeBuilder("stopped");
    stopped.addEnsure(LustreUtil.not(reset));
    stopped.addEnsure(LustreUtil.not(on));
    stopped.addRequire(
        LustreUtil.arrow(LustreUtil.TRUE, LustreUtil.equal(time, LustreUtil.pre(time))));

    cb.addMode(stopped);

    return cb;
  }

  static FunctionBuilder even() {
    FunctionBuilder fb = new FunctionBuilder("even");
    IdExpr N = fb.createVarInput("N", TypeUtil.INT);
    IdExpr B = fb.createVarOutput("B", TypeUtil.BOOL);
    fb.addEquation(B,
        LustreUtil.equal(LustreUtil.mod(N, LustreUtil.integer(2)), LustreUtil.integer(0)));
    return fb;
  }

  static FunctionBuilder toInt() {
    FunctionBuilder f = new FunctionBuilder("toInt");
    IdExpr X = f.createVarInput("X", TypeUtil.BOOL);
    IdExpr N = f.createVarOutput("N", TypeUtil.INT);
    f.addEquation(N, LustreUtil.ite(X, LustreUtil.integer(1), LustreUtil.integer(0)));
    return f;
  }

  static NodeBuilder count() {
    NodeBuilder nb = new NodeBuilder("Count");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr N = nb.createVarOutput("N", TypeUtil.INT);
    Expr toIntX = LustreUtil.nodeCall(LustreUtil.id("toInt"), X);
    nb.addEquation(N, LustreUtil.arrow(toIntX, LustreUtil.plus(toIntX, LustreUtil.pre(N))));
    return nb;
  }

  static NodeBuilder sofar() {
    NodeBuilder nb = new NodeBuilder("Sofar");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarOutput("Y", TypeUtil.BOOL);
    nb.addEquation(Y, LustreUtil.arrow(X, LustreUtil.and(X, LustreUtil.pre(Y))));
    return nb;
  }

  static NodeBuilder since() {
    NodeBuilder nb = new NodeBuilder("Since");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarInput("Y", TypeUtil.BOOL);
    IdExpr Z = nb.createVarOutput("Z", TypeUtil.BOOL);
    nb.addEquation(Z,
        LustreUtil.or(X, LustreUtil.and(Y, LustreUtil.arrow(LustreUtil.FALSE, LustreUtil.pre(Z)))));
    return nb;
  }

  static NodeBuilder sinceIncl() {
    NodeBuilder nb = new NodeBuilder("SinceIncl");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarInput("Y", TypeUtil.BOOL);
    IdExpr Z = nb.createVarOutput("Z", TypeUtil.BOOL);
    nb.addEquation(Z,
        LustreUtil.and(Y, LustreUtil.or(X, LustreUtil.arrow(LustreUtil.FALSE, LustreUtil.pre(Z)))));
    return nb;
  }

  static NodeBuilder increased() {
    NodeBuilder nb = new NodeBuilder("Increased");
    IdExpr N = nb.createVarInput("N", TypeUtil.INT);
    IdExpr B = nb.createVarOutput("B", TypeUtil.BOOL);
    nb.addEquation(B, LustreUtil.arrow(LustreUtil.TRUE, LustreUtil.greater(N, LustreUtil.pre(N))));
    return nb;
  }

  static NodeBuilder stable() {
    NodeBuilder nb = new NodeBuilder("Stable");
    IdExpr N = nb.createVarInput("N", TypeUtil.INT);
    IdExpr B = nb.createVarOutput("B", TypeUtil.BOOL);
    nb.addEquation(B, LustreUtil.arrow(LustreUtil.TRUE, LustreUtil.equal(N, LustreUtil.pre(N))));
    return nb;
  }

  static NodeBuilder stopWatch() {
    NodeBuilder nb = new NodeBuilder("Stopwatch");
    IdExpr toggle = nb.createVarInput("toggle", TypeUtil.BOOL);
    IdExpr reset = nb.createVarInput("reset", TypeUtil.BOOL);
    IdExpr count = nb.createVarOutput("count", TypeUtil.INT);

    ContractBodyBuilder cbb = new ContractBodyBuilder();
    cbb.importContract("StopWatchSpec", Arrays.asList(toggle, reset),
        Collections.singletonList(count));

    cbb.addGuarantee(LustreUtil.not(LustreUtil.and(LustreUtil.modeRef("StopWatchSpec", "resetting"),
        LustreUtil.modeRef("StopWatchSpec", "running"),
        LustreUtil.modeRef("StopWatchSpec", "stopped"))));

    nb.setContractBody(cbb);

    IdExpr running = nb.createLocalVar("running", TypeUtil.BOOL);

    nb.addEquation(running,
        LustreUtil.notEqual(LustreUtil.arrow(LustreUtil.FALSE, LustreUtil.pre(running)), toggle));
    nb.addEquation(count,
        LustreUtil.ite(reset, LustreUtil.integer(0),
            LustreUtil.ite(running,
                LustreUtil.arrow(LustreUtil.integer(1),
                    LustreUtil.plus(LustreUtil.pre(count), LustreUtil.integer(1))),
                LustreUtil.arrow(LustreUtil.integer(0), LustreUtil.pre(count)))));

    return nb;
  }
}
