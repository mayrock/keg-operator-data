package edu.thu.keg.mdap.restful.jerseyclasses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JStatistic {
	private List<Object> keys;
	private List<String> names;
	private List<Object> values;
	
	public void setKey(List<Object> keys) {
		this.keys=keys;
	}

	public List<Object> getKey() {
		return this.keys;
	}
	public void setName(List<String> names) {
		this.names = names;
	}

	public List<String> getName() {
		return this.names;
	}
	public void setValue(List<Object> values) {
		this.values = values;
	}

	public List<Object> getValue() {
		return this.values;
	}
	
}
