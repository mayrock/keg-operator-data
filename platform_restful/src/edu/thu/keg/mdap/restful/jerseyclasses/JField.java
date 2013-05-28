package edu.thu.keg.mdap.restful.jerseyclasses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataset's field
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JField {
	private String value;
	private String type;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
