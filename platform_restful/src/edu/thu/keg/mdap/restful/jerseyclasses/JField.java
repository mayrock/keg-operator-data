package edu.thu.keg.mdap.restful.jerseyclasses;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * Jersey class for dataset's field
 * @author Law
 *
 */
@XmlRootElement
public class JField {
	private Object field;

	public Object getField() {
		return this.field;
	}

	public void setField( Object field) {
		this.field = field;
	}
}
