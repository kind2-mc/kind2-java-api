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
import edu.uiowa.kind2.api.results.Result;
import edu.uiowa.kind2.api.xml.XmlParseThread;
import edu.uiowa.kind2.lustre.Program;
import edu.uiowa.kind2.lustre.visitors.PrettyPrintVisitor;

/**
 * The primary interface to Kind2.
 */
public class Kind2Api extends KindApi {
	public static final String KIND2 = "kind2";
	private static final long POLL_INTERVAL = 100;
	private XmlParseThread parseThread;

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

	// module contracts
	private Boolean compositional;
	private Boolean checkModes;
	private Boolean checkImplem;
	private Boolean refinement;

	// general
	// private String lusMain;
	private String outputDir;
	private List<String> includeDirs;
	private String realPrecision;
	private Boolean logInvs;
	// private Boolean printInvs;
	private Float timeout;
	private Boolean onlyParse;
	private Set<Module> enabledSet;
	private Set<Module> disabledSet;
	private Boolean modular;
	private Boolean sliceNodes;
	private Boolean checkSubproperties;

	public Kind2Api() {
		parseThread = null;
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
		compositional = null;
		checkModes = null;
		checkImplem = null;
		refinement = null;
		outputDir = null;
		includeDirs = new ArrayList<>();
		realPrecision = null;
		logInvs = null;
		// printInvs = null;
		timeout = null;
		onlyParse = null;
		enabledSet = new HashSet<>();
		disabledSet = new HashSet<>();
		modular = null;
		sliceNodes = null;
		checkSubproperties = null;
	}

	/**
	 * Run Kind on a Lustre program
	 *
	 * @param program Lustre program
	 * @param result  Place to store results as they come in
	 * @param monitor Used to check for cancellation
	 * @throws .Kind2Exception
	 */
	@Override
	public void execute(Program program, Result result, IProgressMonitor monitor) {
		PrettyPrintVisitor kind2Printer = new PrettyPrintVisitor();
		kind2Printer.visit(program);
		execute(kind2Printer.toString(), result, monitor);
	}

	/**
	 * Run Kind2 on a Lustre program
	 *
	 * @param lustreFile File containing Lustre program
	 * @param result     Place to store results as they come in
	 * @param monitor    Used to check for cancellation
	 * @throws .Kind2Exception
	 */
	@Override
	public void execute(File lustreFile, Result result, IProgressMonitor monitor) {
		debug.println("Lustre file", lustreFile);
		try {
			callKind2(lustreFile, result, monitor);
		} catch (Kind2Exception e) {
			throw e;
		} catch (Throwable t) {
			throw new Kind2Exception(result.getText(), t);
		}
	}

	private void callKind2(File lustreFile, Result result, IProgressMonitor monitor)
			throws IOException, InterruptedException {
		ProcessBuilder builder = getKind2ProcessBuilder(lustreFile);
		debug.println("Kind 2 command: " + ApiUtil.getQuotedCommand(builder.command()));
		Process process = null;
		int code = 0;

		try {
			result.start();
			process = builder.start();
			parseThread = new XmlParseThread(process.getInputStream(), result);
			parseThread.start();
			while (!monitor.isCanceled() && parseThread.isAlive()) {
				sleep(POLL_INTERVAL);
			}
		} finally {
			if (process != null) {
				process.destroy();
				code = process.waitFor();
			}

			if (parseThread != null) {
				parseThread.join();
			}

			if (monitor.isCanceled()) {
				result.cancel();
			} else {
				result.done();
			}
			monitor.done();

			if (!Arrays.asList(0, 10, 20).contains(code) && !monitor.isCanceled()) {
				StringBuilder sb = new StringBuilder();
				for (String log : getLogs(LogLevel.FATAL)) {
					sb.append(log).append('\n');
				}
				sb.deleteCharAt(sb.length() - 1);
				throw new Kind2Exception("Abnormal termination, exit code " + code + ", fatal: " + sb.toString());
			}
		}

		if (parseThread.getThrowable() != null) {
			throw new Kind2Exception("Error parsing XML", parseThread.getThrowable());
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

	protected List<String> getArgs() {
		List<String> args = new ArrayList<>();
		args.add("-xml");
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
	 * Set the solver to use (Boolector, CVC4, Yices, Yices2, Z3)
	 */
	public void setSmtSolver(SolverOption smtSolver) {
		this.smtSolver = smtSolver;
	}

	/**
	 * Set the SMT logic to use (ALL, QF_UF, LIA, ...)
	 */
	public void setSmtLogic(String smtLogic) {
		this.smtLogic = smtLogic;
	}

	public void setCheckSatAssume(boolean checkSatAssume) {
		this.checkSatAssume = checkSatAssume;
	}

	public void setSmtShortNames(boolean smtShortNames) {
		this.smtShortNames = smtShortNames;
	}

	public void setBoolectorBin(String boolectorBin) {
		this.boolectorBin = boolectorBin;
	}

	public void setCvc4Bin(String cvc4Bin) {
		this.cvc4Bin = cvc4Bin;
	}

	public void setYicesBin(String yicesBin) {
		this.yicesBin = yicesBin;
	}

	public void setYices2Bin(String yices2Bin) {
		this.yices2Bin = yices2Bin;
	}

	public void setZ3Bin(String z3Bin) {
		this.z3Bin = z3Bin;
	}

	public void setSmtTrace(boolean smtTrace) {
		this.smtTrace = smtTrace;
	}

	public void setIndPrintCex(boolean indPrintCex) {
		this.indPrintCex = indPrintCex;
	}

	public void setCompositional(boolean compositional) {
		this.compositional = compositional;
	}

	public void setCheckModes(boolean checkModes) {
		this.checkModes = checkModes;
	}

	public void setCheckImplem(boolean checkImplem) {
		this.checkImplem = checkImplem;
	}

	public void setRefinement(boolean refinement) {
		this.refinement = refinement;
	}

	public void outputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void includeDir(String dir) {
		this.includeDirs.add(dir);
	}

	public void includeDirs(ArrayList<String> dirs) {
		this.includeDirs.addAll(dirs);
	}

	public void setRealPrecision(String realPrecision) {
		this.realPrecision = realPrecision;
	}

	public void logInvs() {
		this.logInvs = true;
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

	public void setOnlyParse(boolean onlyParse) {
		this.onlyParse = onlyParse;
	}

	public void enable(Module module) {
		this.enabledSet.add(module);
	}

	public void disable(Module module) {
		this.disabledSet.add(module);
	}

	public void setModular(boolean modular) {
		this.modular = modular;
	}

	public void setSliceNodes(boolean sliceNodes) {
		this.sliceNodes = sliceNodes;
	}

	public void setCheckSubproperties(boolean checkSubproperties) {
		this.checkSubproperties = checkSubproperties;
	}

	protected void sleep(long interval) {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
		}
	}

	public List<String> getLogs(LogLevel logLevel)
	{
		return parseThread.getLogs(logLevel);
	}

	@Override
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
