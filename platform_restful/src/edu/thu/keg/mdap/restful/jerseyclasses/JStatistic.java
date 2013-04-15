package edu.thu.keg.mdap.restful.jerseyclasses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JStatistic {
	private List<String> keys;
	private String value;
	
	public void setKey(List<String> keys) {
		this.keys=keys;
	}

	public List<String> getKey() {
		return this.keys;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
}
