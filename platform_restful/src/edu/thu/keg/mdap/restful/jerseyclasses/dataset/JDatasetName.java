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
	private String datasetName;
	private String permission;
	private String owner;
	private List<String> limitedUsers;
	private String descriptionEn = "玛胖没有给吴超添加英文介绍";
	private String descriptionZh = "玛胖没有给吴超添加中文介绍";

	private List<String> keyFields;
	private List<String> otherFields;
	private List<String> datafeature;

	public void setDatasetName(String datasetname) {
		this.datasetName = datasetname;
	}

	public String getDatasetName() {
		return this.datasetName;
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

	public void setDescriptionEn(String descriptionEn) {
		if (descriptionEn == null)
			return;
		this.descriptionEn = descriptionEn;
	}

	public String getDescriptionEn() {
		return this.descriptionEn;
	}

	public void setDescriptionZh(String descriptionZh) {
		this.descriptionZh = descriptionZh;
	}

	public String getDescriptionZh() {
		return this.descriptionZh;
	}

	public void setKeyFields(List<String> keyFields) {
		this.keyFields = keyFields;
	}

	public List<String> getKeyFields() {
		return this.keyFields;
	}

	public void setOtherFields(List<String> otherFields) {
		this.otherFields = otherFields;
	}

	public List<String> getOtherFields() {
		return this.otherFields;
	}

	public List<String> getDatafeature() {
		return datafeature;
	}

	public void setDatafeature(List<String> datafeature) {
		this.datafeature = datafeature;
	}

}
