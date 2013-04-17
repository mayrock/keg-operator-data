/**
 * 
 */
package edu.thu.keg.mdap.datasetfeature;

import java.util.HashSet;
import java.util.Set;

import edu.thu.keg.mdap.datamodel.DataField;

/**
 * A certain feature of a dataset, supporting a specific function set
 * - e.g., visualization of a certain type.
 * Type of a feature is identified by direct sub-interface of this interface.
 * @author Yuanchao Ma
 *
 */
public abstract class DataSetFeature {
	
	private DataField[] keyFields;
	private DataField[] valueFields;
	
	/**
	 * 
	 * @return the key DataFields
	 */
	public DataField[] getKeyFields() {
		return this.keyFields;
	}
	/**
	 * @throws IllegalStateException when there is more than 1 key field
	 * @return the unique key DataField
	 */
	public DataField getKeyField() throws IllegalStateException {
		if (keyFields.length != 1)
			throw new IllegalStateException();
		return keyFields[0];
	}
	/**
	 * 
	 * @return value fields
	 */
	public DataField[] getValueFields() {
		return this.valueFields;
	}
	/**
	 * 
	 * @return All DataField's including keys and values
	 */
	public DataField[] getAllFields() {
		DataField[] all = new DataField[this.keyFields.length + this.valueFields.length];
		for (int i = 0; i < this.keyFields.length; i++) {
			all[i] = this.keyFields[i];
		}
		for (int i = 0; i < this.valueFields.length; i++) {
			all[this.keyFields.length + i] = this.valueFields[i];
		}
		return all;
	}
	/**
	 * Get the unique type standing for type of this feature.
	 * @return A subclass of DataSetFeature, which uniquely identifies
	 * the type of this feature.
	 */
	public Class<? extends DataSetFeature> getFeatureType() {
		return this.getClass();
	}
	
	/**
	 * Get all the feature types supported by this object, containing 
	 * the type and all its super classes of this object
	 * @return A set containing all the feature types
	 */
	@SuppressWarnings("unchecked")
	public Set<Class<? extends DataSetFeature>> getAllFeatureTypes() {
		HashSet<Class<? extends DataSetFeature>> features 
			= new HashSet<Class<? extends DataSetFeature>>();
		Class<? extends DataSetFeature> c = this.getFeatureType();
		while (c != null) {
			features.add(c);
			c = (Class<? extends DataSetFeature>) c.getSuperclass();
		}
		return features;
	}
	/**
	 * Determine whether a specific feature type is supported by this object
	 * @param type A Class which is a subclass of DataSetFeature
	 * @return true is the type is supported (within getFeatureType())
	 */
	public boolean hasFeatureType(Class<? extends DataSetFeature> type) {
		if (type == getFeatureType())
			return true;
		else {
			return getAllFeatureTypes().contains(type);
		}
	}
	/**
	 * Init a new DataSetFeature which key fields and value fields
	 * @param keyFields the key DataFields
	 * @param valueFields the value DataFields
	 */
	public DataSetFeature(DataField[] keyFields, DataField[] valueFields) {
		this.keyFields = keyFields;
		this.valueFields = valueFields;
	}
	
	/**
	 * Init a new StatisticsDataSet with single key field and single value field
	 * @param keyField the key DataField
	 * @param valueField the value field
	 */
	public DataSetFeature(DataField keyField, DataField valueField) {
		this.keyFields = new DataField[]{keyField };
		this.valueFields = new DataField[]{valueField };
	}
}
