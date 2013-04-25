package edu.thu.keg.mdap.restful.jerseyclasses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataset
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JColumn {
	private List<JField> field;

	public void setField(List<JField> field) {
		this.field = field;
	}

	public List<JField> getField() {
		return this.field;
	}
}
