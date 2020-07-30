package edu.uiowa.kind2.api;

import java.io.File;

import edu.uiowa.kind2.api.results.Result;
import edu.uiowa.kind2.lustre.Program;

public abstract class KindApi {
	protected DebugLogger debug = new DebugLogger();

	/**
	 * Put the KindApi into debug mode where it saves all output
	 */
	public void setApiDebug() {
		debug = new DebugLogger("-api-debug-");
	}

	/**
	 * Print string to debug log (assuming setApiDebug() has been called)
	 * 
	 * @param text
	 *            text to print to debug log
	 */
	public void apiDebug(String text) {
		if (debug != null) {
			debug.println(text);
		}
	}

	/**
	 * Run Kind on a Lustre program
	 * 
	 * @param program
	 *            Lustre program
	 * @param result
	 *            Place to store results as they come in
	 * @param monitor
	 *            Used to check for cancellation
	 * @throws .Kind2Exception
	 */
	public void execute(Program program, Result result, IProgressMonitor monitor) {
		execute(program.toString(), result, monitor);
	}

	/**
	 * Run Kind on a Lustre program
	 * 
	 * @param program
	 *            Lustre program as text
	 * @param result
	 *            Place to store results as they come in
	 * @param monitor
	 *            Used to check for cancellation
	 * @throws .Kind2Exception
	 */
	public void execute(String program, Result result, IProgressMonitor monitor) {
		File lustreFile = null;
		try {
			lustreFile = ApiUtil.writeLustreFile(program);
			execute(lustreFile, result, monitor);
		} finally {
			debug.deleteIfUnneeded(lustreFile);
		}
	}

	/**
	 * Run Kind on a Lustre program
	 * 
	 * @param lustreFile
	 *            File containing Lustre program
	 * @param result
	 *            Place to store results as they come in
	 * @param monitor
	 *            Used to check for cancellation
	 * @throws .Kind2Exception
	 */
	public abstract void execute(File lustreFile, Result result, IProgressMonitor monitor);

	/**
	 * Check if the KindApi is available for running and throw exception if not
	 * 
	 * @return Availability information when Kind is available
	 * @throws java.lang.Exception
	 *             When Kind is not available
	 */
	public abstract String checkAvailable() throws Exception;
}
