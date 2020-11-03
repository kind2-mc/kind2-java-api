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

  static ImportedComponentBuilder sqrt() {
    IdExpr n = ExprUtil.id("n");
    IdExpr r = ExprUtil.id("r");

    ContractBodyBuilder cbb = new ContractBodyBuilder();
    cbb.addAssumption(ExprUtil.greaterEqual(n, ExprUtil.real("0.0")));
    cbb.addGuarantee(ExprUtil.and(ExprUtil.greaterEqual(r, ExprUtil.real("0.0")),
        ExprUtil.equal(ExprUtil.multiply(r, r), n)));

    ImportedComponentBuilder fb = new ImportedComponentBuilder("sqrt");
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

    IdExpr on = ExprUtil.id("on");

    ContractBodyBuilder cbb = new ContractBodyBuilder();

    cbb.createVarDef("on", TypeUtil.BOOL,
        ExprUtil.arrow(toggle, ExprUtil.or(ExprUtil.and(ExprUtil.pre(on), ExprUtil.not(toggle)),
            ExprUtil.and(ExprUtil.not(ExprUtil.pre(on)), toggle))));

    cbb.addAssumption(ExprUtil.not(ExprUtil.and(toggle, reset)));
    cbb.addGuarantee(ExprUtil.arrow(ExprUtil.implies(on, ExprUtil.equal(time, ExprUtil.integer(1))),
        ExprUtil.TRUE));
    cbb.addGuarantee(ExprUtil.arrow(
        ExprUtil.implies(ExprUtil.not(on), ExprUtil.equal(time, ExprUtil.integer(0))),
        ExprUtil.TRUE));
    cbb.addGuarantee(ExprUtil.greaterEqual(time, ExprUtil.integer(0)));

    cbb.addGuarantee(ExprUtil.implies(
        ExprUtil.and(ExprUtil.not(reset),
            ExprUtil.nodeCall(ExprUtil.id("Since"), reset,
                ExprUtil.functionCall(ExprUtil.id("even"),
                    ExprUtil.nodeCall(ExprUtil.id("Count"), toggle)))),
        ExprUtil.nodeCall(ExprUtil.id("Stable"), time)));

    cbb.addGuarantee(
        ExprUtil
            .implies(
                ExprUtil.and(ExprUtil.not(reset),
                    ExprUtil.nodeCall(ExprUtil.id("Since"), reset,
                        ExprUtil.not(ExprUtil.functionCall(ExprUtil.id("even"),
                            ExprUtil.nodeCall(ExprUtil.id("Count"),
                                Collections.singletonList(toggle)))))),
                ExprUtil.nodeCall(ExprUtil.id("Increased"), Collections.singletonList(time))));

    cbb.addGuarantee(
        ExprUtil
            .arrow(ExprUtil.TRUE,
                ExprUtil.implies(ExprUtil.and(
                    ExprUtil.not(ExprUtil.functionCall(ExprUtil.id("even"),
                        ExprUtil.nodeCall(ExprUtil.id("Count"),
                            Collections.singletonList(toggle)))),
                    ExprUtil.equal(ExprUtil.nodeCall(ExprUtil.id("Count"), reset),
                        ExprUtil.integer(0))),
                    ExprUtil.greater(time, ExprUtil.pre(time)))));

    ModeBuilder resetting = new ModeBuilder("resetting");
    resetting.addRequire(reset);
    resetting.addEnsure(ExprUtil.equal(time, ExprUtil.integer(0)));

    cbb.addMode(resetting);

    ModeBuilder running = new ModeBuilder("running");
    running.addEnsure(on);
    running.addEnsure(ExprUtil.not(reset));
    running.addRequire(ExprUtil.arrow(ExprUtil.TRUE,
        ExprUtil.equal(time, ExprUtil.plus(ExprUtil.pre(time), ExprUtil.integer(1)))));

    cbb.addMode(running);

    ModeBuilder stopped = new ModeBuilder("stopped");
    stopped.addEnsure(ExprUtil.not(reset));
    stopped.addEnsure(ExprUtil.not(on));
    stopped.addRequire(ExprUtil.arrow(ExprUtil.TRUE, ExprUtil.equal(time, ExprUtil.pre(time))));

    cbb.addMode(stopped);

    cb.setContractBody(cbb);

    return cb;
  }

  static ComponentBuilder even() {
    ComponentBuilder fb = new ComponentBuilder("even");
    IdExpr N = fb.createVarInput("N", TypeUtil.INT);
    IdExpr B = fb.createVarOutput("B", TypeUtil.BOOL);
    fb.addEquation(B, ExprUtil.equal(ExprUtil.mod(N, ExprUtil.integer(2)), ExprUtil.integer(0)));
    return fb;
  }

  static ComponentBuilder toInt() {
    ComponentBuilder f = new ComponentBuilder("toInt");
    IdExpr X = f.createVarInput("X", TypeUtil.BOOL);
    IdExpr N = f.createVarOutput("N", TypeUtil.INT);
    f.addEquation(N, ExprUtil.ite(X, ExprUtil.integer(1), ExprUtil.integer(0)));
    return f;
  }

  static ComponentBuilder count() {
    ComponentBuilder nb = new ComponentBuilder("Count");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr N = nb.createVarOutput("N", TypeUtil.INT);
    Expr toIntX = ExprUtil.nodeCall(ExprUtil.id("toInt"), X);
    nb.addEquation(N, ExprUtil.arrow(toIntX, ExprUtil.plus(toIntX, ExprUtil.pre(N))));
    return nb;
  }

  static ComponentBuilder sofar() {
    ComponentBuilder nb = new ComponentBuilder("Sofar");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarOutput("Y", TypeUtil.BOOL);
    nb.addEquation(Y, ExprUtil.arrow(X, ExprUtil.and(X, ExprUtil.pre(Y))));
    return nb;
  }

  static ComponentBuilder since() {
    ComponentBuilder nb = new ComponentBuilder("Since");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarInput("Y", TypeUtil.BOOL);
    IdExpr Z = nb.createVarOutput("Z", TypeUtil.BOOL);
    nb.addEquation(Z,
        ExprUtil.or(X, ExprUtil.and(Y, ExprUtil.arrow(ExprUtil.FALSE, ExprUtil.pre(Z)))));
    return nb;
  }

  static ComponentBuilder sinceIncl() {
    ComponentBuilder nb = new ComponentBuilder("SinceIncl");
    IdExpr X = nb.createVarInput("X", TypeUtil.BOOL);
    IdExpr Y = nb.createVarInput("Y", TypeUtil.BOOL);
    IdExpr Z = nb.createVarOutput("Z", TypeUtil.BOOL);
    nb.addEquation(Z,
        ExprUtil.and(Y, ExprUtil.or(X, ExprUtil.arrow(ExprUtil.FALSE, ExprUtil.pre(Z)))));
    return nb;
  }

  static ComponentBuilder increased() {
    ComponentBuilder nb = new ComponentBuilder("Increased");
    IdExpr N = nb.createVarInput("N", TypeUtil.INT);
    IdExpr B = nb.createVarOutput("B", TypeUtil.BOOL);
    nb.addEquation(B, ExprUtil.arrow(ExprUtil.TRUE, ExprUtil.greater(N, ExprUtil.pre(N))));
    return nb;
  }

  static ComponentBuilder stable() {
    ComponentBuilder nb = new ComponentBuilder("Stable");
    IdExpr N = nb.createVarInput("N", TypeUtil.INT);
    IdExpr B = nb.createVarOutput("B", TypeUtil.BOOL);
    nb.addEquation(B, ExprUtil.arrow(ExprUtil.TRUE, ExprUtil.equal(N, ExprUtil.pre(N))));
    return nb;
  }

  static ComponentBuilder stopWatch() {
    ComponentBuilder nb = new ComponentBuilder("Stopwatch");
    IdExpr toggle = nb.createVarInput("toggle", TypeUtil.BOOL);
    IdExpr reset = nb.createVarInput("reset", TypeUtil.BOOL);
    IdExpr count = nb.createVarOutput("count", TypeUtil.INT);

    ContractBodyBuilder cbb = new ContractBodyBuilder();
    cbb.importContract("StopWatchSpec", Arrays.asList(toggle, reset),
        Collections.singletonList(count));

    cbb.addGuarantee(ExprUtil.not(ExprUtil.and(ExprUtil.modeRef("StopWatchSpec", "resetting"),
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
