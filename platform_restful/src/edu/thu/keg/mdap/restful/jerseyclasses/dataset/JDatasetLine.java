package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataset
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JDatasetLine {
	private List<JField> fields;

	public List<JField> getField() {
		return this.fields;
	}

	public void setField(List<JField> fields) {
		this.fields = fields;
	}
}