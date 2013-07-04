package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataset's location info
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JGeograph {
	private String tag;
	private double weight;
	private double longitude;
	private double latitude;
	private List<String> valus;

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return this.tag;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * @return the valus
	 */
	public List<String> getValus() {
		return valus;
	}

	/**
	 * @param valus the valus to set
	 */
	public void setValus(List<String> valus) {
		this.valus = valus;
	}
	
}
