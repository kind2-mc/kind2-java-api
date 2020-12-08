/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.results.Kind2Result;
import edu.uiowa.kind2.lustre.Program;

/**
 * The primary interface to Kind2.
 */
public class Kind2Api {
  public static final String KIND2 = "kind2";
  private static final long POLL_INTERVAL = 100;

  // module smt
  private SolverOption smtSolver;
  private String smtLogic;
  private Boolean checkSatAssume;
  private Boolean smtShortNames;
  private String boolectorBin;
  private String z3Bin;
  private String cvc4Bin;
  private String yicesBin;
  private String yices2Bin;
  private Boolean smtTrace;

  // module ind
  private Boolean indPrintCex;

  // module ic3
  private IC3Abstraction ic3Abstr;

  // module test
  private Boolean testgen;
  private Boolean testgenGraphOnly;
  private Integer testgenLen;

  // module interpreter
  private String interpreterInputFile;
  private Integer interpreterSteps;

  // module contracts
  private Boolean compositional;
  private Boolean checkModes;
  private Boolean checkImplem;
  private Boolean refinement;

  // module ivc
  private Boolean ivc;
  private HashSet<IVCCategory> ivcCategories;
  private Boolean ivcAll;
  private Boolean ivcApproximate;
  private Boolean ivcSmallestFirst;
  private Boolean ivcOnlyMainNode;
  private Boolean ivcMustSet;
  private Boolean printIVC;
  private Boolean printIVCComplement;
  private String minimizeProgram;
  private String ivcOutputDir;
  private Integer ivcPrecomputedMCS;
  private Integer ivcUCTimeout;

  // module mcs
  private Set<MCSCategory> mcsCategories;
  private Boolean mcsOnlyMainNode;
  private Boolean mcsAll;
  private Integer mcsMaxCardinality;
  private Boolean printMCS;
  private Boolean printMCSComplement;
  private Boolean printMCSCounterexample;
  private Boolean mcsPerProperty;

  // general
  private String outputDir;
  private List<String> includeDirs;
  private String realPrecision;
  private Boolean logInvs;
  private Float timeout;
  private Boolean onlyParse;
  private Set<Module> enabledSet;
  private Set<Module> disabledSet;
  private Boolean modular;
  private Boolean sliceNodes;
  private Boolean checkSubproperties;

  public Kind2Api() {
    smtSolver = null;
    smtLogic = null;
    checkSatAssume = null;
    smtShortNames = null;
    boolectorBin = null;
    z3Bin = null;
    cvc4Bin = null;
    yicesBin = null;
    yices2Bin = null;
    smtTrace = null;
    indPrintCex = null;
    ic3Abstr = null;
    testgen = null;
    testgenGraphOnly = null;
    testgenLen = null;
    interpreterInputFile = null;
    interpreterSteps = null;
    compositional = null;
    checkModes = null;
    checkImplem = null;
    refinement = null;
    ivc = null;
    ivcCategories = new HashSet<>();
    ivcAll = null;
    ivcApproximate = null;
    ivcSmallestFirst = null;
    ivcOnlyMainNode = null;
    ivcMustSet = null;
    printIVC = null;
    printIVCComplement = null;
    minimizeProgram = null;
    ivcOutputDir = null;
    ivcPrecomputedMCS = null;
    ivcUCTimeout = null;
    mcsCategories = new HashSet<>();
    mcsOnlyMainNode = null;
    mcsAll = null;
    mcsMaxCardinality = null;
    printMCS = null;
    printMCSComplement = null;
    printMCSCounterexample = null;
    mcsPerProperty = null;
    outputDir = null;
    includeDirs = new ArrayList<>();
    realPrecision = null;
    logInvs = null;
    timeout = null;
    onlyParse = null;
    enabledSet = new HashSet<>();
    disabledSet = new HashSet<>();
    modular = null;
    sliceNodes = null;
    checkSubproperties = null;
  }

  DebugLogger debug = new DebugLogger();

  /**
   * Put the KindApi into debug mode where it saves all output
   */
  public void setApiDebug() {
    debug = new DebugLogger("-api-debug-");
  }

  /**
   * Print string to debug log (assuming setApiDebug() has been called)
   *
   * @param text text to print to debug log
   */
  public void apiDebug(String text) {
    if (debug != null) {
      debug.println(text);
    }
  }

  /**
   * Run Kind on a Lustre program
   *
   * @param program Lustre program
   * @return result of running kind2 on program
   */
  public Kind2Result execute(Program program) {
    Kind2Result result = new Kind2Result();
    execute(program.toString(), result, new IProgressMonitor() {
      @Override
      public boolean isCanceled() {
        return false;
      }

      @Override
      public void done() {
      }
    });
    return result;
  }

  /**
   * Run Kind on a Lustre program
   *
   * @param program Lustre program as text
   * @return result of running kind2 on program
   * @throws Kind2Exception
   */
  public Kind2Result execute(String program) {
    Kind2Result result = new Kind2Result();
    execute(program, result, new IProgressMonitor() {
      @Override
      public boolean isCanceled() {
        return false;
      }

      @Override
      public void done() {
      }
    });
    return result;
  }

  /**
   * Run Kind on a Lustre program
   *
   * @param program Lustre program as text
   * @param result  Place to store results as they come in
   * @param monitor Used to check for cancellation
   * @throws Kind2Exception
   */
  private void execute(String program, Kind2Result result, IProgressMonitor monitor) {
    File lustreFile = null;
    try {
      lustreFile = ApiUtil.writeLustreFile(program);
      execute(lustreFile, result, monitor);
    } finally {
      debug.deleteIfUnneeded(lustreFile);
    }
  }

  /**
   * Run Kind2 on a Lustre program
   *
   * @param lustreFile File containing Lustre program
   * @param result     Place to store results as they come in
   * @param monitor    Used to check for cancellation
   * @throws Kind2Exception
   */
  private void execute(File lustreFile, Kind2Result result, IProgressMonitor monitor) {
    debug.println("Lustre file", lustreFile);
    try {
      callKind2(lustreFile, result, monitor);
    } catch (Kind2Exception e) {
      throw e;
    } catch (Throwable t) {
      throw new Kind2Exception(t.getMessage(), t);
    }
  }

  private void callKind2(File lustreFile, Kind2Result result, IProgressMonitor monitor)
      throws IOException, InterruptedException {
    ProcessBuilder builder = getKind2ProcessBuilder(lustreFile);
    debug.println("Kind 2 command: " + ApiUtil.getQuotedCommand(builder.command()));
    Process process = null;
    int code = 0;

    try {
      process = builder.start();
      while (!monitor.isCanceled() && process.isAlive()) {
        sleep(POLL_INTERVAL);
      }
    } finally {
      if (!monitor.isCanceled()) {
        int available = process.getInputStream().available();
        byte[] bytes = new byte[available];
        process.getInputStream().read(bytes);
        result.initialize(new String(bytes));
      }

      if (process != null) {
        process.destroy();
        code = process.waitFor();
      }

      monitor.done();

      if (!Arrays.asList(0, 10, 20).contains(code) && !monitor.isCanceled()) {
        throw new Kind2Exception("Abnormal termination, exit code " + code);
      }
    }
  }

  private ProcessBuilder getKind2ProcessBuilder(File lustreFile) {
    List<String> args = new ArrayList<>();
    args.add(KIND2);
    args.addAll(getArgs());
    args.add(lustreFile.toString());

    ProcessBuilder builder = new ProcessBuilder(args);
    builder.redirectErrorStream(true);
    return builder;
  }

  List<String> getArgs() {
    List<String> args = new ArrayList<>();
    args.add("-json");
    args.add("-v");
    if (smtSolver != null) {
      args.add("--smt_solver");
      args.add(smtSolver.toString());
    }
    if (smtLogic != null) {
      args.add("--smt_logic");
      args.add(smtLogic);
    }
    if (checkSatAssume != null) {
      args.add("--check_sat_assume");
      args.add(checkSatAssume.toString());
    }
    if (smtShortNames != null) {
      args.add("--smt_short_names");
      args.add(smtShortNames.toString());
    }
    if (boolectorBin != null) {
      args.add("--boolector_bin");
      args.add(boolectorBin);
    }
    if (cvc4Bin != null) {
      args.add("--cvc4_bin");
      args.add(cvc4Bin);
    }
    if (yicesBin != null) {
      args.add("--yices_bin");
      args.add(yicesBin);
    }
    if (yices2Bin != null) {
      args.add("--yices2_bin");
      args.add(yices2Bin);
    }
    if (z3Bin != null) {
      args.add("--z3_bin");
      args.add(z3Bin);
    }
    if (smtTrace != null) {
      args.add("--smt_trace");
      args.add(smtTrace.toString());
    }
    if (indPrintCex != null) {
      args.add("--ind_print_cex");
      args.add(indPrintCex.toString());
    }
    if (ic3Abstr != null) {
      args.add("--ic3_abstr");
      args.add(ic3Abstr.toString());
    }
    if (testgen != null) {
      args.add("--testgen");
      args.add(testgen.toString());
    }
    if (testgenGraphOnly != null) {
      args.add("--testgen_graph_only");
      args.add(testgenGraphOnly.toString());
    }
    if (testgenLen != null) {
      args.add("--testgen_len");
      args.add(testgenLen.toString());
    }
    if (interpreterInputFile != null) {
      args.add("--interpreter_input_file");
      args.add(interpreterInputFile.toString());
    }
    if (interpreterSteps != null) {
      args.add("--interpreter_steps");
      args.add(interpreterSteps.toString());
    }
    if (compositional != null) {
      args.add("--compositional");
      args.add(checkSubproperties.toString());
    }
    if (checkModes != null) {
      args.add("--check_modes");
      args.add(checkModes.toString());
    }
    if (checkImplem != null) {
      args.add("--check_implem");
      args.add(checkImplem.toString());
    }
    if (refinement != null) {
      args.add("--refinement");
      args.add(refinement.toString());
    }
    if (ivc != null) {
      args.add("--ivc");
      args.add(ivc.toString());
    }
    if (!ivcCategories.isEmpty()) {
      for (IVCCategory category : ivcCategories) {
        args.add("--ivc_category");
        args.add(category.toString());
      }
    }
    if (ivcAll != null) {
      args.add("--ivc_all");
      args.add(ivcAll.toString());
    }
    if (ivcApproximate != null) {
      args.add("--ivc_approximate");
      args.add(ivcApproximate.toString());
    }
    if (ivcSmallestFirst != null) {
      args.add("--ivc_smallest_first");
      args.add(ivcSmallestFirst.toString());
    }
    if (ivcOnlyMainNode != null) {
      args.add("--ivc_only_main_node");
      args.add(ivcOnlyMainNode.toString());
    }
    if (ivcMustSet != null) {
      args.add("--ivc_must_set");
      args.add(ivcMustSet.toString());
    }
    if (printIVC != null) {
      args.add("--print_ivc");
      args.add(printIVC.toString());
    }
    if (printIVCComplement != null) {
      args.add("--print_ivc_complement");
      args.add(printIVCComplement.toString());
    }
    if (minimizeProgram != null) {
      args.add("--minimize_program");
      args.add(minimizeProgram.toString());
    }
    if (ivcOutputDir != null) {
      args.add("--ivc_output_dir");
      args.add(ivcOutputDir.toString());
    }
    if (ivcPrecomputedMCS != null) {
      args.add("--ivc_precomputed_mcs");
      args.add(ivcPrecomputedMCS.toString());
    }
    if (ivcUCTimeout != null) {
      args.add("--ivc_uc_timeout");
      args.add(ivcUCTimeout.toString());
    }
    if (!mcsCategories.isEmpty()) {
      for (MCSCategory category : mcsCategories) {
        args.add("--mcs_category");
        args.add(category.toString());
      }
    }
    if (mcsOnlyMainNode != null) {
      args.add("--mcs_only_main_node");
      args.add(mcsOnlyMainNode.toString());
    }
    if (mcsAll != null) {
      args.add("--mcs_all");
      args.add(mcsAll.toString());
    }
    if (mcsMaxCardinality != null) {
      args.add("--mcs_max_cardinality");
      args.add(mcsMaxCardinality.toString());
    }
    if (printMCS != null) {
      args.add("--print_mcs");
      args.add(printMCS.toString());
    }
    if (printMCSComplement != null) {
      args.add("--print_mcs_complement");
      args.add(printMCSComplement.toString());
    }
    if (printMCSCounterexample != null) {
      args.add("--print_mcs_counterexample");
      args.add(printMCSCounterexample.toString());
    }
    if (mcsPerProperty != null) {
      args.add("--mcs_per_property");
      args.add(mcsPerProperty.toString());
    }
    if (outputDir != null) {
      args.add("--output_dir");
      args.add(outputDir);
    }
    if (!includeDirs.isEmpty()) {
      for (String dir : includeDirs) {
        args.add("--include_dir");
        args.add(dir);
      }
    }
    if (realPrecision != null) {
      args.add("--real_precision");
      args.add(realPrecision);
    }
    if (logInvs != null) {
      args.add("--log_invs");
      args.add(logInvs.toString());
    }
    if (timeout != null) {
      args.add("--timeout");
      args.add(timeout.toString());
    }
    if (onlyParse != null) {
      args.add("--only_parse");
      args.add(onlyParse.toString());
    }
    if (!enabledSet.isEmpty()) {
      for (Module module : enabledSet) {
        args.add("--enable");
        args.add(module.toString());
      }
    }
    if (!disabledSet.isEmpty()) {
      for (Module module : disabledSet) {
        args.add("--disable");
        args.add(module.toString());
      }
    }
    if (modular != null) {
      args.add("--modular");
      args.add(modular.toString());
    }
    if (sliceNodes != null) {
      args.add("--slice_nodes");
      args.add(sliceNodes.toString());
    }
    if (checkSubproperties != null) {
      args.add("--check_subproperties");
      args.add(checkSubproperties.toString());
    }
    return args;
  }

  /**
   * Choose an SMT solver
   * <p>
   * Default: detect
   *
   * @param smtSolver the SMT solver to use
   */
  public void setSmtSolver(SolverOption smtSolver) {
    this.smtSolver = smtSolver;
  }

  /**
   * Select logic for SMT solvers (none, detect, ALL, QF_UF, LIA, ...)
   * <p>
   * Default: detect
   *
   * @param smtLogic the logic to use
   */
  public void setSmtLogic(String smtLogic) {
    this.smtLogic = smtLogic;
  }

  /**
   * Use check-sat-assuming, or simulate with push/pop when false
   * <p>
   * Default: true
   *
   * @param checkSatAssume whether or not to use check-sat-assuming
   */
  public void setCheckSatAssume(boolean checkSatAssume) {
    this.checkSatAssume = checkSatAssume;
  }

  /**
   * Send short variable names to SMT solver, send full names if false
   * <p>
   * Default: true
   *
   * @param smtShortNames whether or not to send short variable names
   */
  public void setSmtShortNames(boolean smtShortNames) {
    this.smtShortNames = smtShortNames;
  }

  /**
   * Executable of Boolector solver
   * <p>
   * Default: "boolector"
   *
   * @param boolectorBin path to Boolector executable
   */
  public void setBoolectorBin(String boolectorBin) {
    this.boolectorBin = boolectorBin;
  }

  /**
   * Executable of CVC4 solver
   * <p>
   * Default: "cvc4"
   *
   * @param cvc4Bin path to CVC4 executable
   */
  public void setCvc4Bin(String cvc4Bin) {
    this.cvc4Bin = cvc4Bin;
  }

  /**
   * Executable of Yices solver
   * <p>
   * Default: "yices"
   *
   * @param yicesBin path to Yices executable
   */
  public void setYicesBin(String yicesBin) {
    this.yicesBin = yicesBin;
  }

  /**
   * Executable of Yices2 solver
   * <p>
   * Default: "yices-smt2"
   *
   * @param yices2Bin path to Yices2 executable
   */
  public void setYices2Bin(String yices2Bin) {
    this.yices2Bin = yices2Bin;
  }

  /**
   * Executable of Z3 solver
   * <p>
   * Default: "z3"
   *
   * @param z3Bin path to Z3 executable
   */
  public void setZ3Bin(String z3Bin) {
    this.z3Bin = z3Bin;
  }

  /**
   * Write all SMT commands to files
   * <p>
   * Default: false
   *
   * @param smtTrace whether or not to write all SMT commands to files
   */
  public void setSmtTrace(boolean smtTrace) {
    this.smtTrace = smtTrace;
  }

  /**
   * Print counterexamples to induction
   * <p>
   * Default: false
   *
   * @param indPrintCex whether or not to print counterexamples to induction
   */
  public void setIndPrintCex(boolean indPrintCex) {
    this.indPrintCex = indPrintCex;
  }

  /**
   * Choose method of abstraction in IC3
   * <p>
   * Default: None
   *
   * @param ic3Abstr abstraction method
   */
  public void setIC3Abstr(IC3Abstraction ic3Abstr) {
    this.ic3Abstr = ic3Abstr;
  }

  /**
   * Activates test generation for systems proved correct
   * <p>
   * Default: false
   *
   * @param testgen whether or not to activate test generation
   */
  public void setTestgen(boolean testgen) {
    this.testgen = testgen;
  }

  /**
   * Only draw the graph of reachable modes, do not log test cases.
   * <p>
   * Default: false
   *
   * @param testgenGraphOnly whether or not to only draw the graph of reachable modes
   */
  public void setTestgenGraphOnly(boolean testgenGraphOnly) {
    this.testgenGraphOnly = testgenGraphOnly;
  }

  /**
   * Maximum length for test generation
   * <p>
   * Default: 5
   *
   * @param testgenLen maximum length for test generation
   */
  public void setTestgenLen(int testgenLen) {
    this.testgenLen = testgenLen;
  }

  /**
   * Read input from file
   *
   * @param interpreterInputFile path to file
   */
  public void setInterpreterInputFile(String interpreterInputFile) {
    this.interpreterInputFile = interpreterInputFile;
  }

  /**
   * Run number of steps, override the number of steps given in the input file
   * <p>
   * Default: 0
   *
   * @param interpreterSteps number of steps to run
   */
  public void setInterpreterSteps(int interpreterSteps) {
    this.interpreterSteps = interpreterSteps;
  }

  /**
   * Abstract subnodes with a contract
   * <p>
   * Default: false
   *
   * @param compositional whether or not to abstract subnodes with a contract
   */
  public void setCompositional(boolean compositional) {
    this.compositional = compositional;
  }

  /**
   * Checks the modes of contracts for exhaustiveness
   * <p>
   * Default: true
   *
   * @param checkModes whether or not to check if the modes are exhaustive
   */
  public void setCheckModes(boolean checkModes) {
    this.checkModes = checkModes;
  }

  /**
   * Checks the implementation of nodes
   * <p>
   * Default: true
   *
   * @param checkImplem whether or not to check the implementation of nodes
   */
  public void setCheckImplem(boolean checkImplem) {
    this.checkImplem = checkImplem;
  }

  /**
   * (De)activates refinement in compositional reasoning
   * <p>
   * Default: true
   *
   * @param refinement whether or not to activate refinement in compositional reasoning
   */
  public void setRefinement(boolean refinement) {
    this.refinement = refinement;
  }

  /**
   * Enable inductive validity core generation
   * <p>
   * Default: false
   *
   * @param ivc whether or not to enable IVC generation
   */
  public void setIVC(boolean ivc) {
    this.ivc = ivc;
  }

  /**
   * Minimize only a specific category of elements, repeat option to minimize multiple categories
   * <p>
   * Default: minimize all categories of elements
   *
   * @param ivcCategory IVC category to minimize
   */
  public void setIVCCategory(IVCCategory ivcCategory) {
    this.ivcCategories.add(ivcCategory);
  }

  /**
   * Compute all the Minimal Inductive Validity Cores.
   * <p>
   * Default: false
   *
   * @param ivcAll whether or not to compute all the MIVC
   */
  public void setIVCAll(boolean ivcAll) {
    this.ivcAll = ivcAll;
  }

  /**
   * Compute an approximation (superset) of a MIVC. Ignored if --ivc_all is true.
   * <p>
   * Default: true
   *
   * @param ivcApproximate whether or not to compute an approximation of a MIVC
   */
  public void setIVCApproximate(boolean ivcApproximate) {
    this.ivcApproximate = ivcApproximate;
  }

  /**
   * Compute a smallest IVC first. If --ivc_all is false, the computed IVC will be a smallest one.
   * <p>
   * Default: false
   *
   * @param ivcSmallestFirst whether or not to compute a smallest IVC first
   */
  public void setIVCSmallestFirst(boolean ivcSmallestFirst) {
    this.ivcSmallestFirst = ivcSmallestFirst;
  }

  /**
   * Only elements of the main node are considered in the computation
   * <p>
   * Default: false
   *
   * @param ivcOnlyMainNode whether or not to consider only elements of the main node
   */
  public void setIVCOnlyMainNode(boolean ivcOnlyMainNode) {
    this.ivcOnlyMainNode = ivcOnlyMainNode;
  }

  /**
   * Compute the MUST set in addition to the IVCs
   * <p>
   * Default: false
   *
   * @param ivcMustSet whether or not to compute the MUST set
   */
  public void setIVCMustSet(boolean ivcMustSet) {
    this.ivcMustSet = ivcMustSet;
  }

  /**
   * Print the inductive validity core computed
   * <p>
   * Default: true
   *
   * @param printIVC whether or not to print the inductive validity core
   */
  public void setPrintIVC(boolean printIVC) {
    this.printIVC = printIVC;
  }

  /**
   * Print the complement of the inductive validity core computed (= the elements that were not
   * necessary to prove the properties)
   * <p>
   * Default: false
   *
   * @param printIVCComplement whether or not to print the complement of the IVC computed
   */
  public void setPrintIVCComplement(boolean printIVCComplement) {
    this.printIVCComplement = printIVCComplement;
  }

  /**
   * Minimize the source Lustre program according to the inductive validity core(s) computed
   * <ul>
   * <li>"no" to disable this feature</li>
   * <li>"valid_lustre" to replace useless expressions by a valid node call</li>
   * <li>"concise" to replace useless expressions by a '_'</li>
   * </ul>
   * <p>
   * Default: "no"
   *
   * @param minimizeProgram whether or not to minimize program according to IVC computed
   */
  public void setMinimizeProgram(String minimizeProgram) {
    this.minimizeProgram = minimizeProgram;
  }

  /**
   * Output directory for the minimized programs
   * <p>
   * Default: {@code <INPUT_FILENAME>}
   *
   * @param ivcOutputDir output directory for the minimized programs
   */
  public void setIVCOutputDir(String ivcOutputDir) {
    this.ivcOutputDir = ivcOutputDir;
  }

  /**
   * When computing all MIVCs, set a cardinality upper bound for the precomputed MCSs (helps prune
   * space of candidates).
   * <p>
   * Default: 0
   *
   * @param ivcPrecomputedMCS cardinality upper bound for the computed MCSs
   */
  public void setIVCPrecomputedMCS(int ivcPrecomputedMCS) {
    this.ivcPrecomputedMCS = ivcPrecomputedMCS;
  }

  /**
   * Set a timeout for each unsat core check sent to the solver. This setting is ignored if a solver
   * different than Z3 is used. Set to 0 to disable timeout.
   * <p>
   * Default: 0
   *
   * @param ivcUCTimeout timeout for each unsat core check sent to the solver
   */
  public void setIVCUCTimeout(int ivcUCTimeout) {
    this.ivcUCTimeout = ivcUCTimeout;
  }

  /**
   * Consider only a specific category of elements, repeat option to consider multiple categories
   * <p>
   * Default: annotations
   *
   * @param category MCS category to consider
   */
  public void setMCSCategory(MCSCategory category) {
    this.mcsCategories.add(category);
  }

  /**
   * Only elements of the main node are considered in the computation
   * <p>
   * Default: false
   *
   * @param mcsOnlyMainNode whether or not to consider only elements of the main node
   */
  public void setMCSOnlyMainNode(boolean mcsOnlyMainNode) {
    this.mcsOnlyMainNode = mcsOnlyMainNode;
  }

  /**
   * Specify whether all the Minimal Cut Sets must be computed or just one
   * <p>
   * Default: false
   *
   * @param mcsAll whether all the Minimal Cut Sets must be computed or just one
   */
  public void setMCSAll(boolean mcsAll) {
    this.mcsAll = mcsAll;
  }

  /**
   * Only search for MCSs of cardinality lower or equal to this parameter. If -1, all MCSs will be
   * considered.
   * <p>
   * Default: -1
   *
   * @param mcsMaxCardinality max cardinality of MCSs to search for
   */
  public void setMCSMaxCardinality(int mcsMaxCardinality) {
    this.mcsMaxCardinality = mcsMaxCardinality;
  }

  /**
   * Print the minimal cut set computed
   * <p>
   * Default: true
   *
   * @param printMCS whether or not to print the MCS
   */
  public void setPrintMCS(boolean printMCS) {
    this.printMCS = printMCS;
  }

  /**
   * Print the complement of the minimal cut set computed (this is equivalent to computing a Maximal
   * Unsafe Abstraction)
   * <p>
   * Default: false
   *
   * @param printMCSComplement whether or not to print the MCS complement
   */
  public void setPrintMCSComplement(boolean printMCSComplement) {
    this.printMCSComplement = printMCSComplement;
  }

  /**
   * Print a counterexample for each MCS found
   * <p>
   * Default: false
   *
   * @param printMCSCounterexample whether or not to print a counterexample for each MCS
   */
  public void setPrintMCSCounterexample(boolean printMCSCounterexample) {
    this.printMCSCounterexample = printMCSCounterexample;
  }

  /**
   * If true, MCSs will be computed for each property separately
   * <p>
   * Default: true
   *
   * @param mcsPerProperty whether or not to compute MCSs for each property separately
   */
  public void setMCSPerProperty(boolean mcsPerProperty) {
    this.mcsPerProperty = mcsPerProperty;
  }

  /**
   * Output directory for the files generated: SMT traces, compilation, testgen, certification...
   * <p>
   * Default: {@code tmp/<filename>.out}
   *
   * @param outputDir path to output directory for the files generated
   */
  public void outputDir(String outputDir) {
    this.outputDir = outputDir;
  }

  /**
   * Include a directory in the search path. The directory will be searched after the current
   * include directory, and any other directory added before it when an include directive is found
   *
   * @param dir the directory to include
   */
  public void includeDir(String dir) {
    this.includeDirs.add(dir);
  }

  /**
   * Include directories in the search path. The directories will be searched following their order
   * in the list
   *
   * @param dirs the directories to include
   */
  public void includeDirs(List<String> dirs) {
    this.includeDirs.addAll(dirs);
  }

  /**
   * Adjust precision of real values in model output In floating-point format {@code f<nn>} means a
   * relative error less than {@code 2^-nn}
   * <p>
   * Default: rational
   *
   * @param realPrecision can be "rational" or "float"
   */
  public void setRealPrecision(String realPrecision) {
    this.realPrecision = realPrecision;
  }

  /**
   * Logs strengthening invariants as contracts after minimization.
   * <p>
   * Default: false
   *
   * @param logInvs whether or not to log strengthening invariants
   */
  public void setLogInvs(boolean logInvs) {
    this.logInvs = logInvs;
  }

  /**
   * Set a maximum run time for entire execution
   *
   * @param timeout A positive timeout in seconds
   */
  public void setTimeout(float timeout) {
    if (timeout <= 0) {
      throw new Kind2Exception("Timeout must be positive");
    }
    this.timeout = timeout;
  }

  /**
   * Only parse the Lustre program. No analysis is performed
   * <p>
   * Default: false
   *
   * @param onlyParse whether or not to only parse the Lustre program
   */
  public void setOnlyParse(boolean onlyParse) {
    this.onlyParse = onlyParse;
  }

  /**
   * Enable Kind module, repeat option to enable several modules
   * <p>
   * Default: [BMC, IND, IND2, IC3, INVGEN, INVGENOS, INVGENINTOS, INVGENREALOS]
   *
   * @param module the module to enable
   */
  public void enable(Module module) {
    this.enabledSet.add(module);
  }

  /**
   * Disable Kind module, repeat option to disable several modules
   *
   * @param module the module to disable
   */
  public void disable(Module module) {
    this.disabledSet.add(module);
  }

  /**
   * Bottom-up analysis of each node
   * <p>
   * Default: false
   *
   * @param modular whether or not enable modular analysis
   */
  public void setModular(boolean modular) {
    this.modular = modular;
  }

  /**
   * Only equations that are relevant for checking the contract and properties of a node are
   * considered during the analysis
   * <p>
   * Default: true
   *
   * @param sliceNodes whether or not to consider only relative equations
   */
  public void setSliceNodes(boolean sliceNodes) {
    this.sliceNodes = sliceNodes;
  }

  /**
   * Check properties of subnodes that are relevant for the analysis of the top node. Only available
   * with monolithic analysis
   * <p>
   * Default: false
   *
   * @param checkSubproperties whether or not to check subproperties
   */
  public void setCheckSubproperties(boolean checkSubproperties) {
    this.checkSubproperties = checkSubproperties;
  }

  void sleep(long interval) {
    try {
      Thread.sleep(interval);
    } catch (InterruptedException e) {
    }
  }

  /**
   * Check if the KindApi is available for running and throw exception if not
   *
   * @return Availability information when Kind is available
   * @throws java.lang.Exception When Kind is not available
   */
  public String checkAvailable() throws Exception {
    ProcessBuilder builder = new ProcessBuilder(KIND2, "--version");
    builder.redirectErrorStream(true);
    Process process = builder.start();

    String output = ApiUtil.readAll(process.getInputStream());
    if (process.exitValue() != 0) {
      throw new Kind2Exception("Error running kind2: " + output);
    }
    return output;
  }
}
