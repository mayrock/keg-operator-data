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
public class JColumn {
	private List<JField> column ;

	public void setColumn(List<JField> column) {
		this.column = column;
	}

	public List<JField> getColumn() {
		return this.column;
	}
}
