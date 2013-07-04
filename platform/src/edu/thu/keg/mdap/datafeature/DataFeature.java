/**
 * 
 */
package edu.thu.keg.mdap.datafeature;

import edu.thu.keg.mdap.datamodel.DataField;

/**
 * A certain feature of a dataset, supporting a specific function set - e.g.,
 * visualization of a certain type. Type of a feature is identified by
 * getFeatureType method
 * 
 * @author Yuanchao Ma
 * 
 */
public interface DataFeature {

	/**
	 * 
	 * @return the key DataFields
	 */
	public DataField[] getKeyFields();

	/**
	 * @throws IllegalStateException
	 *             when there is more than 1 key field
	 * @return the unique key DataField
	 */
	public DataField getKeyField() throws IllegalStateException;

	/**
	 * 
	 * @return value fields
	 */
	public DataField[] getValueFields();

	/**
	 * 
	 * @return All DataField's including keys and values
	 */
	public DataField[] getAllFields();

	/**
	 * Get the unique type standing for type of this feature.
	 * 
	 * @return A subclass of DataSetFeature, which uniquely identifies the type
	 *         of this feature.
	 */
	public DataFeatureType getFeatureType();
}
