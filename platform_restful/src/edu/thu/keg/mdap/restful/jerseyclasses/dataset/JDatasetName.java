package edu.thu.keg.mdap.restful.jerseyclasses.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jersey class for dataset's name
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JDatasetName {
	private String id;
	private String datasetName;
	private String permission;
	private String owner;
	private List<String> limitedUsers;
	private String descriptionEn = "玛胖没有给吴超添加英文介绍";
	private String descriptionZh = "玛胖没有给吴超添加中文介绍";

	private List<String> keyFields;
	private List<String> otherFields;
	private List<String> datafeature;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
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
	 * @return the limitedUsers
	 */
	public List<String> getLimitedUsers() {
		return limitedUsers;
	}

	/**
	 * @param limitedUsers
	 *            the limitedUsers to set
	 */
	public void setLimitedUsers(List<String> limitedUsers) {
		this.limitedUsers = limitedUsers;
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
	 * @return the keyFields
	 */
	public List<String> getKeyFields() {
		return keyFields;
	}

	/**
	 * @param keyFields
	 *            the keyFields to set
	 */
	public void setKeyFields(List<String> keyFields) {
		this.keyFields = keyFields;
	}

	/**
	 * @return the otherFields
	 */
	public List<String> getOtherFields() {
		return otherFields;
	}

	/**
	 * @param otherFields
	 *            the otherFields to set
	 */
	public void setOtherFields(List<String> otherFields) {
		this.otherFields = otherFields;
	}

	/**
	 * @return the datafeature
	 */
	public List<String> getDatafeature() {
		return datafeature;
	}

	/**
	 * @param datafeature
	 *            the datafeature to set
	 */
	public void setDatafeature(List<String> datafeature) {
		this.datafeature = datafeature;
	}

}
