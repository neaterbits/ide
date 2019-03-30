package com.neaterbits.structuredlog.binary.logging;

public abstract class BaseBinaryLogIO {

	protected final void debugWrite(int sequenceNo, LogCommand logCommand, String ... strings) {
		debug("Writing", sequenceNo, logCommand, strings);
	}

	protected final void debugRead(int sequenceNo, LogCommand logCommand, String ... strings) {
		debug("Reading", sequenceNo, logCommand, strings);
	}

	private void debug(String action, int sequenceNo, LogCommand logCommand, String ... strings) {

		System.out.print(action + " " + logCommand.name() + " " + sequenceNo + " ");
		
		if (strings.length > 0) {
			if (strings.length % 2 != 0) {
				throw new IllegalArgumentException();
			}
			
			for (int i = 0; i < strings.length;) {
				System.out.print(" " + strings[i ++] + "=" + strings[i ++]);
			}
		}
		
		System.out.println();
	}
}
