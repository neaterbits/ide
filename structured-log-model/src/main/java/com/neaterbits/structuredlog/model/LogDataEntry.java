package com.neaterbits.structuredlog.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class LogDataEntry {

	private List<String> path;
	private String data;

	public LogDataEntry() {

	}
	
	public LogDataEntry(List<String> path, String data) {
		this.path = path;
		this.data = data;
	}

	@XmlElementWrapper(name="path")
	@XmlElement(name="entry")
	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	@XmlElement
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
