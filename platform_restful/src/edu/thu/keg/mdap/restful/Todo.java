package edu.thu.keg.mdap.restful;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Todo {
	private String summary;
	private String description;

	public void inpuutDescription(String description) {
		this.description = description;

	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}