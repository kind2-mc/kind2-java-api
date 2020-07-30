package edu.uiowa.kind2.api;

import static java.util.stream.Collectors.joining;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.uiowa.kind2.Kind2Exception;
import edu.uiowa.kind2.util.Util;

public class ApiUtil {
	public static File writeLustreFile(String program) {
		return writeTempFile("kind2-api-", ".lus", program);
	}

	public static File writeTempFile(String fileName, String fileExt, String contents) {
		File file = null;
		try {
			file = File.createTempFile(fileName, fileExt);
			if (contents != null) {
				Util.writeToFile(contents, file);
			}
			return file;
		} catch (IOException e) {
			throw new Kind2Exception("Cannot write to file: " + file, e);
		}
	}

	public static String readOutput(Process process, IProgressMonitor monitor) throws IOException {
		InputStream stream = new BufferedInputStream(process.getInputStream());
		StringBuilder text = new StringBuilder();

		while (true) {
			if (!process.isAlive()) {
				return text.toString();
			}

			checkMonitor(monitor, process);
			while (stream.available() > 0) {
				int c = stream.read();
				if (c == -1) {
					return text.toString();
				}
				text.append((char) c);
				checkMonitor(monitor, process);
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void checkMonitor(IProgressMonitor monitor, Process process) throws IOException {
		if (monitor.isCanceled()) {
			process.getOutputStream().write(Util.END_OF_TEXT);
			process.getOutputStream().flush();
		}
	}

	public static String readAll(InputStream inputStream) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedInputStream buffered = new BufferedInputStream(inputStream);
		int i;
		while ((i = buffered.read()) != -1) {
			result.append((char) i);
		}
		return result.toString();
	}

	public static String getQuotedCommand(List<String> pieces) {
		return pieces.stream().map(p -> p.contains(" ") ? "\"" + p + "\"" : p).collect(joining(" "));
	}
}
