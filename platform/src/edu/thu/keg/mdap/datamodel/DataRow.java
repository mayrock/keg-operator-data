/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

/**
 * @author myc
 *
 */
public interface DataRow {
	public DataContent getDataContent();
	public Object getValue(DataField field);
}
