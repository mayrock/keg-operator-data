/**
 * 
 */
package edu.thu.keg.mdap.datasetfeature;

import java.util.HashSet;
import java.util.Set;

/**
 * A certain feature of a dataset, supporting a specific function set
 * - e.g., visualization of a certain type.
 * Type of a feature is identified by direct sub-interface of this interface.
 * @author Yuanchao Ma
 *
 */
public abstract class DataSetFeature {
	/**
	 * Get the unique type standing for type of this feature.
	 * @return A direct sub-interface of DataSetFeature, which uniquely identifies
	 * the type of this feature.
	 */
	public Class<? extends DataSetFeature> getFeatureType() {
		return this.getClass();
	}
	
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
	
	public boolean hasFeatureType(Class<? extends DataSetFeature> type) {
		if (type == getFeatureType())
			return true;
		else {
			return getAllFeatureTypes().contains(type);
		}
	}
}
