package com.neaterbits.structuredlog.swt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.neaterbits.structuredlog.model.Log;
import com.neaterbits.structuredlog.model.LogIO;

public class StructuredLogMain {

	private static void usage() {
		System.err.println("usage: <logfile>");
	}
	
	public static void main(String [] args) throws JAXBException, IOException{
		
		if (args.length != 1) {
			usage();
		}
		else {
			final File logFile = new File(args[0]);
			
			if (!logFile.isFile() || !logFile.canRead()) {
				usage();
			}
			else {
				
				final LogIO logIO = new LogIO();
				
				try (FileInputStream inputStream = new FileInputStream(logFile)) {
				
					final Log log = logIO.readLog(inputStream);

					final LogUI logUI = new LogUI(log);
					
					logUI.start();
				}
			}
		}
	}
}
