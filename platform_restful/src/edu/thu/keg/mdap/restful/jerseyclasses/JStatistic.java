package edu.thu.keg.mdap.restful.jerseyclasses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JStatistic {
	private String key;
	private String value;
	
	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
}
