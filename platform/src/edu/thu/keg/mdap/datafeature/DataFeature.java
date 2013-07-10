/**
 * 
 */
package edu.thu.keg.mdap.datafeature;

import java.util.List;

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
	public void setKeyFields(List<DataField> keyFields);

	/**
	 * 
	 * @return the key DataFields
	 */
	public List<DataField> getKeyFields();

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
	public void setValueFields(List<DataField> valueFields);

	/**
	 * 
	 * @return value fields
	 */
	public List<DataField> getValueFields();

	/**
	 * 
	 * @return All DataField's including keys and values
	 */
	public List<DataField> getAllFields();

	/**
	 * Get the unique type standing for type of this feature.
	 * 
	 * @return A subclass of DataSetFeature, which uniquely identifies the type
	 *         of this feature.
	 */
	public DataFeatureType getFeatureType();
}
