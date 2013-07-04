package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataset's statistic info
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JStatistic {
	private List<String> indentifiers;
	private List<Double> values;

	public void setIndentifiers(List<String> indentifiers) {
		this.indentifiers = indentifiers;
	}

	public List<String> getIndentifiers() {
		return this.indentifiers;
	}

	public void setValue(List<Double> values) {
		this.values = values;
	}

	public List<Double> getValue() {
		return this.values;
	}

}
