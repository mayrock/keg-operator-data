/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

/**
 * @author myc
 *
 */
public interface Convertor<T> {
	public DataRow convert(T obj);
}
