package com.neaterbits.structuredlog.swt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import com.neaterbits.structuredlog.binary.model.BinaryLogReader;
import com.neaterbits.structuredlog.binary.model.LogModel;
import com.neaterbits.structuredlog.xml.model.Log;
import com.neaterbits.structuredlog.xml.model.LogIO;

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
				
				if (logFile.getName().endsWith(".xml")) {
				
					final LogIO logIO = new LogIO();
					
					try (InputStream inputStream = new FileInputStream(logFile)) {
					
						final Log log = logIO.readLog(inputStream);
	
						final SWTXmlLogUI logUI = new SWTXmlLogUI(log);
						
						logUI.start();
					}
				}
				else {
					// Binary log
					
					final BinaryLogReader logReader = new BinaryLogReader();
					
					final LogModel logModel = logReader.readLog(logFile);
					
					final SWTBinaryLogUI logUI = new SWTBinaryLogUI(logModel);
					
					logUI.start();
				}
			}
		}
	}
}
