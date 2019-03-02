package com.neaterbits.structuredlog.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogEntry {

	private String logMessage;
	
	private List<String> path;
	
	private List<LogData> data;

	public LogEntry() {

	}

	public LogEntry(List<String> path, String logMessage) {
		this.logMessage = logMessage;
		
		this.path = path != null ? new ArrayList<>(path) : null;
	}

	@XmlElement
	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	@XmlElementWrapper(name="path")
	@XmlElement(name="entry")
	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	@XmlElement(name="data")
	public List<LogData> getData() {
		return data;
	}

	public void setData(List<LogData> data) {
		this.data = data;
	}
}
