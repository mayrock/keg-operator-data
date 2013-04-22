package edu.thu.keg.mdap.restful.jerseyclasses;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * Jersey class for dataset's location info
 * @author Law
 *
 */
@XmlRootElement
public class JGeograph {
	private String tag;
	private double weight;
	private double longitude;
	private double latitude;
	
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
}
