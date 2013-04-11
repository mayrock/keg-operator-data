package edu.thu.keg.mdap.restful.jerseyclasses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JFieldName {
	private String fieldName;
	private String description;
	private String type;
private boolean isKey;
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
	public void setType(String type)
	{
		this.type=type;
	}
	public String getType()
	{
		return this.type;
	}
}
