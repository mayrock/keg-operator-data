package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataview
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JDataviewLine {
	private List<JField> identifiers;
	private List<JField> values;

	/**
	 * @return the keys
	 */
	public List<JField> getIdentifiers() {
		return identifiers;
	}

	/**
	 * @param keys
	 *            the keys to set
	 */
	public void setIdentifiers(List<JField> identifiers) {
		this.identifiers = identifiers;
	}

	/**
	 * @return the values
	 */
	public List<JField> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<JField> values) {
		this.values = values;
	}

}
