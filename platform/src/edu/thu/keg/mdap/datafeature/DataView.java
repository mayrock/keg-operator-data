/**
 * 
 */
package edu.thu.keg.mdap.datafeature;

import java.util.List;

import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.TableStructure;

/**
 * @author myc
 * 
 */
public interface DataView extends DataFeature, TableStructure {
	/**
	 * get dataview id
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * get the dataset name which is genareted the dataview
	 * 
	 * @return
	 */
	public String getDataSet();

	/**
	 * get the owner of the dataview
	 * 
	 * @return
	 */
	public String getOwner();

	/**
	 * get the Query of the dataview
	 * 
	 * @return
	 */
	public Query getQ();

	/**
	 * set the Query of the dataview
	 * 
	 * @param q
	 */
	public void setQ(Query q);

	public void resetView(String description, String name, Query q,
			List<DataField> key, List<DataField> values);

	public void resetView(String description, String name, Query q);
}
