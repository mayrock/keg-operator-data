package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataset's name of field
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JFieldName {
	private String fieldName;
	private String description;
	private String type;
	private boolean isKey;
	private String functionality;
	private String datasetName;
	private String datasetOwner;

	public void setFieldName(String fieldname) {
		this.fieldName = fieldname;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setIsKey(boolean iskey) {
		this.isKey = iskey;
	}

	public boolean getIsKey() {
		return this.isKey;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public String getFunctionality() {
		return functionality;
	}

	public void setFunctionality(String functionality) {
		this.functionality = functionality;
	}

	/**
	 * @return the datasetName
	 */
	public String getDatasetName() {
		return datasetName;
	}

	/**
	 * @param datasetName
	 *            the datasetName to set
	 */
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	/**
	 * @return the datasetOwner
	 */
	public String getDatasetOwner() {
		return datasetOwner;
	}

	/**
	 * @param datasetOwner
	 *            the datasetOwner to set
	 */
	public void setDatasetOwner(String datasetOwner) {
		this.datasetOwner = datasetOwner;
	}

}
