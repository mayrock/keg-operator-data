/**
 * 
 */
package edu.thu.keg.mdap.datafeature;

import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.TableStructure;

/**
 * @author myc
 * 
 */
public interface DataView extends DataFeature, TableStructure {

	public String getId();

	public String getDataSet();

	public String getOwner();

	public Query getQ();

	public void setQ(Query q);
}
