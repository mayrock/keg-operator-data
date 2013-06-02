package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * Jersey class for dataset's statistic info
 * @author Law
 *
 */
@XmlRootElement
public class JStatistic {
	private List<String> keys;
	private List<Double> values;
	
	public void setKey(List<String> keys) {
		this.keys=keys;
	}

	public List<String> getKey() {
		return this.keys;
	}
	public void setValue(List<Double> values) {
		this.values = values;
	}

	public List<Double> getValue() {
		return this.values;
	}
	
}
