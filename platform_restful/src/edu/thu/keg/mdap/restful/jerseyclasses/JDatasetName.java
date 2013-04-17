package edu.thu.keg.mdap.restful.jerseyclasses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * Jersey class for dataset's name
 * @author Law
 *
 */
@XmlRootElement
public class JDatasetName {
	private String datasetName;
	private String description;
	private List<String> schema;
	private List<Class> type;
	public void setDatasetName(String datasetname) {
		this.datasetName = datasetname;
	}
	public String getDatasetName() {
		return this.datasetName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return this.description;
	}
	public void setSchema(List<String> schema )
	{
		this.schema=schema;
	}
	public List<String> getSchema()
	{
		return this.schema;
	}
	public void setType(List<Class> type )
	{
		this.type=type;
	}
	public List<Class> getType()
	{
		return this.type;
	}
}
