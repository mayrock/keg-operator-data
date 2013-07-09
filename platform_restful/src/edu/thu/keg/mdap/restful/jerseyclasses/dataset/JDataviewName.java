package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JDataviewName {
	private String dataviewName;
	private String permission;
	private String owner;
	private List<String> limitedUsers;
	private String descriptionEn = "玛胖没有给吴超添加英文介绍";
	private String descriptionZh = "玛胖没有给吴超添加中文介绍";

	private List<String> identifiers;
	private List<String> values;
	private String datafeature;
	private String dataset;

	/**
	 * @return the dataviewName
	 */
	public String getDataviewName() {
		return dataviewName;
	}

	/**
	 * @param dataviewName
	 *            the dataviewName to set
	 */
	public void setDataviewName(String dataviewName) {
		this.dataviewName = dataviewName;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the descriptionEn
	 */
	public String getDescriptionEn() {
		return descriptionEn;
	}

	/**
	 * @param descriptionEn
	 *            the descriptionEn to set
	 */
	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	/**
	 * @return the descriptionZh
	 */
	public String getDescriptionZh() {
		return descriptionZh;
	}

	/**
	 * @param descriptionZh
	 *            the descriptionZh to set
	 */
	public void setDescriptionZh(String descriptionZh) {
		this.descriptionZh = descriptionZh;
	}

	/**
	 * @return the identifiers
	 */
	public List<String> getIdentifiers() {
		return identifiers;
	}

	/**
	 * @param identifiers
	 *            the identifiers to set
	 */
	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}

	/**
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	/**
	 * @return the datafeature
	 */
	public String getDatafeature() {
		return datafeature;
	}

	/**
	 * @param datafeature
	 *            the datafeature to set
	 */
	public void setDatafeature(String datafeature) {
		this.datafeature = datafeature;
	}

	/**
	 * @return the dataset
	 */
	public String getDataset() {
		return dataset;
	}

	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	
	

}
