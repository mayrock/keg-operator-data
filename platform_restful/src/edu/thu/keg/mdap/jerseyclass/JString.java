package edu.thu.keg.mdap.jerseyclass;

import java.lang.String;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class JString {
	private String classname;
	private double longitude;
	private double latitude;

	public void inputLocation(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getClassname() {
		return this.classname;
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
