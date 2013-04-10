package edu.thu.keg.mdap.restful.jerseyclasses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JDatasetName {
	private String datasetName;
	public void setDatasetName(String datasetname) {
		this.datasetName = datasetname;
	}

	public String getDatasetName() {
		return this.datasetName;
	}
}
