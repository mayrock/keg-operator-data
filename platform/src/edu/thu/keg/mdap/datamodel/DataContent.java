/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * 
 * @author Yuanchao Ma
 *
 */
public interface DataContent {
	public DataField[] getFields();

	public void open() throws DataProviderException;
	public void close() throws DataProviderException;
	public boolean next() throws DataProviderException;
	public Object getValue(DataField field) throws DataProviderException;
}
