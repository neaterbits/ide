package com.neaterbits.ide.util.scheduling.dependencies.builder;

public class ActionLog {

	private final String commandLine;
	private final int exitCode;
	
	public ActionLog(String commandLine, int exitCode) {
		this.commandLine = commandLine;
		this.exitCode = exitCode;
	}

	public String getCommandLine() {
		return commandLine;
	}

	public int getExitCode() {
		return exitCode;
	}
}