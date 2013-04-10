package edu.thu.keg.mdap.restful.jerseyclasses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JLocation {
	private double longitude;
	private double latitude;
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
