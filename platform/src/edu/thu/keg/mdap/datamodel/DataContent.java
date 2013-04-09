/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import edu.thu.keg.mdap.provider.DataProvider;

/**
 * 
 * @author Yuanchao Ma
 *
 */
public interface DataContent {
	public DataProvider getProvider();
	public DataField[] getFields();
	public DataRow nextRow();
}
