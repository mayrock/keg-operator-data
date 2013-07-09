/**
 * 
 */
package edu.thu.keg.mdap.datafeature;

import edu.thu.keg.mdap.datamodel.TableStructure;

/**
 * @author myc
 * 
 */
public interface DataView extends DataFeature, TableStructure {
	public static int PERMISSION_PUBLIC = 0;
	public static int PERMISSION_LIMITED = 1;
	public static int PERMISSION_PRIVATE = 2;

	public String getId();

	public String getDataSet();

	public String getOwner();

	public int getPermission();

}
