/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * Data Object, who gives access to actual data from data providers. It is used
 * for fetching and writing data to data providers. It gives access to data in a
 * streaming style.
 * 
 * @author Yuanchao Ma
 * 
 */
public interface DataContent {

	public enum DataContentState {
		Ready, Opened, Closed
	}

	public DataContentState getState();

	public void setFields(DataField[] fields);

	/**
	 * 
	 * @return A array of contained fields in this DataContent
	 */
	public DataField[] getFields();

	/**
	 * Open the stream for reading data.
	 * 
	 * @throws DataProviderException
	 *             When data provider gives a error.
	 */
	public void open() throws DataProviderException;

	/**
	 * Close the stream, release any resources.
	 * 
	 * @throws DataProviderException
	 *             When data provider gives a error.
	 */
	public void close() throws DataProviderException;

	/**
	 * Advance to next record
	 * 
	 * @return True if it is not the end of the stream
	 * @throws DataProviderException
	 *             When data provider gives a error.
	 */
	public boolean next() throws DataProviderException;

	/**
	 * Get value of the field of the current record
	 * 
	 * @param field
	 *            The DataField whose value is expected
	 * @return The value
	 * @throws DataProviderException
	 *             When data provider gives a error, or the field does not exist
	 *             in this DataContent.
	 */
	public Object getValue(DataField field) throws DataProviderException;
}
