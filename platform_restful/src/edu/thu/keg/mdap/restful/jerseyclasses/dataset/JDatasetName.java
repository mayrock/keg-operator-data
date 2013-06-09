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
	private String descriptionEn = "玛胖没有给吴超添加英文介绍";
	private String descriptionCh = "玛胖没有给吴超添加中文介绍";
	private List<String> keys;
	private List<String> values;
	private List<Class> keytypes;
	private List<Class> valuetypes;

	public void setDatasetName(String datasetname) {
		this.datasetName = datasetname;
	}

	public String getDatasetName() {
		return this.datasetName;
	}

	public void setDescriptionEn(String descriptionEn) {
		if (descriptionEn == null)
			return;
		this.descriptionEn = descriptionEn;
	}

	public String getDescriptionEn() {
		return this.descriptionEn;
	}

	public void setDescriptionCh(String descriptionCh) {
		this.descriptionCh = descriptionCh;
	}

	public String getDescriptionCh() {
		return this.descriptionCh;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public List<String> getValues() {
		return this.values;
	}

	public void setKeytypes(List<Class> keytypes) {
		this.keytypes = keytypes;
	}

	public List<Class> getKeytypes() {
		return this.keytypes;
	}
}
