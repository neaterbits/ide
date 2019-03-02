package com.neaterbits.structuredlog.model;

import javax.xml.bind.annotation.XmlElement;

public class LogDataEntry {

	private String data;

	public LogDataEntry() {

	}
	
	public LogDataEntry(String data) {
		this.data = data;
	}

	@XmlElement
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
