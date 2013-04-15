package edu.thu.keg.mdap.restful.jerseyclasses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JStatistic {
	private List<Object> keys;
	private Object value;
	
	public void setKey(List<Object> keys) {
		this.keys=keys;
	}

	public List<Object> getKey() {
		return this.keys;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}
	
}
