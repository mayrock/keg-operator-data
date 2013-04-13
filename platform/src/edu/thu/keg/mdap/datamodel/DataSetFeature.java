/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

/**
 * A certain feature of a dataset, supporting a specific function set
 * - e.g., visualization of a certain type.
 * Type of a feature is identified by direct sub-interface of this interface.
 * @author Yuanchao Ma
 *
 */
public interface DataSetFeature {
	/**
	 * Get the unique type standing for type of this feature.
	 * @return A direct sub-interface of DataSetFeature, which uniquely identifies
	 * the type of this feature.
	 */
	public Class<? extends DataSetFeature> getFeatureType();
}
