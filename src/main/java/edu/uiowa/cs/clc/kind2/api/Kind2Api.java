/*
 * Copyright (c) 2012-2013, Rockwell Collins Copyright (c) 2020, Board of Trustees of the University
 * of Iowa All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.uiowa.cs.clc.kind2.Kind2Exception;
import edu.uiowa.cs.clc.kind2.results.Result;
import edu.uiowa.cs.clc.kind2.lustre.Program;

/**
 * The primary interface to Kind2.
 */
public class Kind2Api {
  public static String KIND2 = "kind2";
  private static final long POLL_INTERVAL = 100;

  private List<String> otherOptions;

  // module smt
  private SolverOption smtSolver;
  private String smtLogic;
  private Boolean checkSatAssume;
  private Boolean smtShortNames;
  private String bitwuzlaBin;
  private String z3Bin;
  private String cvc5Bin;
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
  private Boolean dumpCex;
  private Float timeout;
  private Boolean oldFrontend;
  private Boolean onlyParse;
  private Boolean lsp;
  private Set<Module> enabledSet;
  private Set<Module> disabledSet;
  private Boolean modular;
  private Boolean sliceNodes;
  private Boolean checkSubproperties;
  private LogLevel logLevel;
  private String lusMain;
  private String fakeFilepath;

  public Kind2Api() {
    otherOptions = new ArrayList<>();
    smtSolver = null;
    smtLogic = null;
    checkSatAssume = null;
    smtShortNames = null;
    bitwuzlaBin = null;
    z3Bin = null;
    cvc5Bin = null;
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
    dumpCex = null;
    timeout = null;
    onlyParse = null;
    enabledSet = new HashSet<>();
    disabledSet = new HashSet<>();
    modular = null;
    sliceNodes = null;
    checkSubproperties = null;
    logLevel = null;
    lusMain = null;
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
  public Result execute(Program program) {
    Result result = new Result();
    execute(program.toString(), result, new IProgressMonitor() {
      @Override
      public boolean isCanceled() {
        return false;
      }

      @Override
      public void done() {}
    });
    return result;
  }

  /**
   * Run Kind on a Lustre program
   *
   * @param program Lustre program as text
   * @return result of running kind2 on program
   */
  public Result execute(String program) {
    Result result = new Result();
    execute(program, result, new IProgressMonitor() {
      @Override
      public boolean isCanceled() {
        return false;
      }

      @Override
      public void done() {}
    });
    return result;
  }

  public String interpret(URI uri, String main, String json) {
    List<String> options = new ArrayList<>();
    options.add(KIND2);
    options.addAll(getOptions());
    options.add("--lus_main");
    options.add(main);
    options.add("--enable");
    options.add("interpreter");
    options.add("--interpreter_input_file");
    options.add(ApiUtil.writeInterpreterFile(json).toURI().getPath());
    options.add(uri.getPath());
    ProcessBuilder builder = new ProcessBuilder(options);
    try {
      String output = "";
      Process process = builder.start();
      while (process.isAlive()) {
        int available = process.getInputStream().available();
        byte[] bytes = new byte[available];
        process.getInputStream().read(bytes);
        output += new String(bytes);
        sleep(POLL_INTERVAL);
      }
      int available = process.getInputStream().available();
      byte[] bytes = new byte[available];
      process.getInputStream().read(bytes);
      output += new String(bytes);
      return output.substring(output.indexOf("trace") + 9, output.length() - 5);
    } catch (IOException e) {
      throw new Kind2Exception(e.getMessage());
    }
  }

  /**
   * Run Kind on a Lustre program with module options
   *
   * @param program Lustre program as text
   * @param result Place to store results as they come in
   * @param monitor Used to check for cancellation
   * @throws Kind2Exception
   */
  public void execute(String program, Result result, IProgressMonitor monitor, List<Module> modules) {
    for (Module module: modules) {
      enable(module);
    }

    execute(program, result, monitor);
  }

  /**
   * Run Kind on a Lustre program
   *
   * @param program Lustre program as text
   * @param result Place to store results as they come in
   * @param monitor Used to check for cancellation
   * @throws Kind2Exception
   */
  public void execute(String program, Result result, IProgressMonitor monitor) {
    try {
      callKind2(program, result, monitor);
    } catch (Throwable t) {
      throw new Kind2Exception(t.getMessage(), t);
    }
  }

  private void callKind2(String program, Result result, IProgressMonitor monitor)
      throws IOException, InterruptedException {
    ProcessBuilder builder = getKind2ProcessBuilder();
    debug.println("Kind 2 command: " + ApiUtil.getQuotedCommand(builder.command()));
    Process process = null;
    String output = "";

    try {
      process = builder.start();
      process.getOutputStream().write(program.getBytes());
      process.getOutputStream().flush();
      process.getOutputStream().close();
      while (!monitor.isCanceled() && process.isAlive()) {
        int available = process.getInputStream().available();
        byte[] bytes = new byte[available];
        process.getInputStream().read(bytes);
        output += new String(bytes);
        sleep(POLL_INTERVAL);
      }
    } finally {
      if (!monitor.isCanceled()) {
        int available = process.getInputStream().available();
        byte[] bytes = new byte[available];
        process.getInputStream().read(bytes);
        output += new String(bytes);
        try {
          result.initialize(output);
        } catch (RuntimeException e) {
        }
      }
      if (process != null) {
        process.destroy();
      }
      monitor.done();
    }
  }

  private ProcessBuilder getKind2ProcessBuilder() {
    List<String> options = new ArrayList<>();
    options.add(KIND2);
    options.addAll(getOptions());
    ProcessBuilder builder = new ProcessBuilder(options);
    builder.redirectErrorStream(true);
    return builder;
  }

  /**
   * Sets additional options to pass to Kind 2 executable.
   *
   * @param options the additional options to kind2
   */
  public void setOtherOptions(List<String> options) {
    this.otherOptions = options;
  }

  public List<String> getOptions() {
    List<String> options = new ArrayList<>();
    options.add("-json");
    if (logLevel != null) {
      options.add(logLevel.getOption());
    }
    if (lusMain != null) {
      options.add("--lus_main");
      options.add(lusMain);
    }
    if (smtSolver != null) {
      options.add("--smt_solver");
      options.add(smtSolver.toString());
    }
    if (smtLogic != null) {
      options.add("--smt_logic");
      options.add(smtLogic);
    }
    if (checkSatAssume != null) {
      options.add("--check_sat_assume");
      options.add(checkSatAssume.toString());
    }
    if (smtShortNames != null) {
      options.add("--smt_short_names");
      options.add(smtShortNames.toString());
    }
    if (bitwuzlaBin != null) {
      options.add("--bitwuzla_bin");
      options.add(bitwuzlaBin);
    }
    if (cvc5Bin != null) {
      options.add("--cvc5_bin");
      options.add(cvc5Bin);
    }
    if (yicesBin != null) {
      options.add("--yices_bin");
      options.add(yicesBin);
    }
    if (yices2Bin != null) {
      options.add("--yices2_bin");
      options.add(yices2Bin);
    }
    if (z3Bin != null) {
      options.add("--z3_bin");
      options.add(z3Bin);
    }
    if (smtTrace != null) {
      options.add("--smt_trace");
      options.add(smtTrace.toString());
    }
    if (indPrintCex != null) {
      options.add("--ind_print_cex");
      options.add(indPrintCex.toString());
    }
    if (ic3Abstr != null) {
      options.add("--ic3_abstr");
      options.add(ic3Abstr.toString());
    }
    if (testgen != null) {
      options.add("--testgen");
      options.add(testgen.toString());
    }
    if (testgenGraphOnly != null) {
      options.add("--testgen_graph_only");
      options.add(testgenGraphOnly.toString());
    }
    if (testgenLen != null) {
      options.add("--testgen_len");
      options.add(testgenLen.toString());
    }
    if (interpreterInputFile != null) {
      options.add("--interpreter_input_file");
      options.add(interpreterInputFile.toString());
    }
    if (interpreterSteps != null) {
      options.add("--interpreter_steps");
      options.add(interpreterSteps.toString());
    }
    if (compositional != null) {
      options.add("--compositional");
      options.add(compositional.toString());
    }
    if (checkModes != null) {
      options.add("--check_modes");
      options.add(checkModes.toString());
    }
    if (checkImplem != null) {
      options.add("--check_implem");
      options.add(checkImplem.toString());
    }
    if (refinement != null) {
      options.add("--refinement");
      options.add(refinement.toString());
    }
    if (ivc != null) {
      options.add("--ivc");
      options.add(ivc.toString());
    }
    if (!ivcCategories.isEmpty()) {
      for (IVCCategory category : ivcCategories) {
        options.add("--ivc_category");
        options.add(category.toString());
      }
    }
    if (ivcAll != null) {
      options.add("--ivc_all");
      options.add(ivcAll.toString());
    }
    if (ivcApproximate != null) {
      options.add("--ivc_approximate");
      options.add(ivcApproximate.toString());
    }
    if (ivcSmallestFirst != null) {
      options.add("--ivc_smallest_first");
      options.add(ivcSmallestFirst.toString());
    }
    if (ivcOnlyMainNode != null) {
      options.add("--ivc_only_main_node");
      options.add(ivcOnlyMainNode.toString());
    }
    if (ivcMustSet != null) {
      options.add("--ivc_must_set");
      options.add(ivcMustSet.toString());
    }
    if (printIVC != null) {
      options.add("--print_ivc");
      options.add(printIVC.toString());
    }
    if (printIVCComplement != null) {
      options.add("--print_ivc_complement");
      options.add(printIVCComplement.toString());
    }
    if (minimizeProgram != null) {
      options.add("--minimize_program");
      options.add(minimizeProgram.toString());
    }
    if (ivcOutputDir != null) {
      options.add("--ivc_output_dir");
      options.add(ivcOutputDir.toString());
    }
    if (ivcPrecomputedMCS != null) {
      options.add("--ivc_precomputed_mcs");
      options.add(ivcPrecomputedMCS.toString());
    }
    if (ivcUCTimeout != null) {
      options.add("--ivc_uc_timeout");
      options.add(ivcUCTimeout.toString());
    }
    if (!mcsCategories.isEmpty()) {
      for (MCSCategory category : mcsCategories) {
        options.add("--mcs_category");
        options.add(category.toString());
      }
    }
    if (mcsOnlyMainNode != null) {
      options.add("--mcs_only_main_node");
      options.add(mcsOnlyMainNode.toString());
    }
    if (mcsAll != null) {
      options.add("--mcs_all");
      options.add(mcsAll.toString());
    }
    if (mcsMaxCardinality != null) {
      options.add("--mcs_max_cardinality");
      options.add(mcsMaxCardinality.toString());
    }
    if (printMCS != null) {
      options.add("--print_mcs");
      options.add(printMCS.toString());
    }
    if (printMCSComplement != null) {
      options.add("--print_mcs_complement");
      options.add(printMCSComplement.toString());
    }
    if (printMCSCounterexample != null) {
      options.add("--print_mcs_counterexample");
      options.add(printMCSCounterexample.toString());
    }
    if (mcsPerProperty != null) {
      options.add("--mcs_per_property");
      options.add(mcsPerProperty.toString());
    }
    if (outputDir != null) {
      options.add("--output_dir");
      options.add(outputDir);
    }
    if (!includeDirs.isEmpty()) {
      for (String dir : includeDirs) {
        options.add("--include_dir");
        options.add(dir);
      }
    }
    if (realPrecision != null) {
      options.add("--real_precision");
      options.add(realPrecision);
    }
    if (logInvs != null) {
      options.add("--log_invs");
      options.add(logInvs.toString());
    }
    if (dumpCex != null) {
      options.add("--dump_cex");
      options.add(dumpCex.toString());
    }
    if (timeout != null) {
      options.add("--timeout");
      options.add(timeout.toString());
    }
    if (oldFrontend != null) {
      options.add("--old_frontend");
      options.add(oldFrontend.toString());
    }
    if (onlyParse != null) {
      options.add("--only_parse");
      options.add(onlyParse.toString());
    }
    if (lsp != null) {
      options.add("--lsp");
      options.add(lsp.toString());
    }
    if (!enabledSet.isEmpty()) {
      for (Module module : enabledSet) {
        options.add("--enable");
        options.add(module.toString());
      }
    }
    if (!disabledSet.isEmpty()) {
      for (Module module : disabledSet) {
        options.add("--disable");
        options.add(module.toString());
      }
    }
    if (modular != null) {
      options.add("--modular");
      options.add(modular.toString());
    }
    if (sliceNodes != null) {
      options.add("--slice_nodes");
      options.add(sliceNodes.toString());
    }
    if (checkSubproperties != null) {
      options.add("--check_subproperties");
      options.add(checkSubproperties.toString());
    }
    if (fakeFilepath != null) {
      options.add("--fake_filepath");
      options.add(fakeFilepath);
    }
    options.addAll(this.otherOptions);
    return options;
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
   * Executable of Bitwuzla solver
   * <p>
   * Default: "bitwuzla"
   *
   * @param bitwuzlaBin path to Bitwuzla executable
   */
  public void setBitwuzlaBin(String bitwuzlaBin) {
    this.bitwuzlaBin = bitwuzlaBin;
  }

  /**
   * Executable of cvc5 solver
   * <p>
   * Default: "cvc5"
   *
   * @param cvc5Bin path to cvc5 executable
   */
  public void setcvc5Bin(String cvc5Bin) {
    this.cvc5Bin = cvc5Bin;
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
   * Interpreter input as json string
   *
   * @param json interpreter input as json
   */
  public void setInterpreterInput(String json) {
    File interpreterFile = ApiUtil.writeInterpreterFile(json);
    this.interpreterInputFile = interpreterFile.toURI().getPath();
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
   * Dump counterexample to a file. Only in plain text output.
   * <p>
   * Default: false
   *
   * @param dumpCex whether or not to dump counterexample
   */
  public void setDumpCex(boolean dumpCex) {
    this.dumpCex = dumpCex;
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
   * Use the old Lustre front-end.
   * <p>
   * Default: true
   *
   * @param oldFrontend whether or not to use the old Lustre front-end.
   */
  public void setOldFrontend(boolean oldFrontend) {
    this.oldFrontend = oldFrontend;
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
   * Provide AST info for language-servers.
   * <p>
   * Default: false
   *
   * @param lsp whether or not to provide AST info.
   */
  public void setLsp(boolean lsp) {
    this.lsp = lsp;
  }

  /**
   * Enable Kind module, repeat option to enable several modules
   * <p>
   * Default: [BMC, IND, IND2, IC3, INVGEN, INVGENOS, INVGENINTOS, INVGENMACHOS, INVGENREALOS]
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

  /**
   * Set the level of logs generated by Kind.
   * <p>
   * Default: NOTE
   *
   * @param logLevel the log level
   */
  public void setLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * Set the top node in the Lustre input file.
   * <p>
   * Default: "--%MAIN" annotation in source if any, last node otherwise
   *
   * @param lusMain the main node
   */
  public void setLusMain(String lusMain) {
    this.lusMain = lusMain;
  }

   /**
   * Set the fake filepath for error messages.
   * <p>
   * Default: stdin
   *
   * @param fakeFilepath the fake filepath
   */
  public void setFakeFilepath(String fakeFilepath) {
    this.fakeFilepath = fakeFilepath;
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
