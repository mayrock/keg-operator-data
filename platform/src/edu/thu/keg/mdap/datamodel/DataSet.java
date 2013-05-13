/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datasetfeature.DataSetFeature;
import edu.thu.keg.mdap.datasetfeature.DataSetFeatureType;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * Contains all metadata describing a dataset, stored in a data provider.
 * @author Yuanchao Ma
 *
 */
public interface DataSet {
	/**
	 * Get the name (unique identifier) of the dataset,
	 * like a table name in SQL or HIVE.
	 * @return the unique name. 
	 */
	public String getName();
	/**
	 * Return a Query for querying actual data of this dataset, if it is loadable.
	 * @return a Query against the provider of this dataset. 
	 * @throws OperationNotSupportedException when dataset is not loadable
	 * @throws DataProviderException when actual data of the DataSet does not
	 * exist on the DataProvider
	 */
	public Query getQuery() throws OperationNotSupportedException, DataProviderException;
	/**
	 * Return a Query for querying data of the specific feature supported by this dataset.
	 * The returned query only contains fields related to the feature, and is by default 
	 * ordered by the first value field (if there is any) descending.
	 * @param featureType type of the expected feature
	 * @return a Query against the provider of this dataset. 
	 * @throws OperationNotSupportedException when dataset is not loadable
	 * @throws DataProviderException when actual data of the DataSet does not
	 * exist on the DataProvider
	 */
	public Query getQuery(DataSetFeatureType featureType) throws OperationNotSupportedException, DataProviderException;
	
	/**
	 * Get the provider of this DataSet
	 * @return a DataProvider instance
	 */
	public DataProvider getProvider();
	/**
	 * Whether this dataset is small enough to be loaded into memory
	 * @return a boolean indicates whether this dataset is small enough to be loaded into memory
	 */
	public boolean isLoadable();
	/**
	 * Get description of this dataset.
	 * @return Human-friendly description.
	 */
	public String getDescription();
	/**
	 * Get an array of the fields contained in the dataset
	 * @return an array containing all the fields in the schema
	 */
	public List<DataField> getDataFields();
	/**
	 * Get a DataField reference according to its name
	 * @param columnName name of this field
	 * @return the DataField instance
	 */
	public DataField getField(String columnName);
	/**
	 * Get a map containing all features of this dataset
	 */
	public Set<DataSetFeature> getFeatures();
	/**
	 * Get an instance representing the feature of this dataset supporting
	 * the specified feature type.
	 * @param type The feature type whose instance is expected
	 * @return A instance of DataSetFeature. Return null if the type is not supported
	 */
	public DataSetFeature getFeature(DataSetFeatureType type);
	/**
	 * Write the actual data of this datset on its provider
	 * @param content The DataContent to be written
	 * @throws DataProviderException when writing the data provider
	 */
	public void writeData(DataContent content) throws DataProviderException;
	/**
	 * Get the key DataFields of this dataset
	 */
	public Collection<DataField> getKeyFields();
	/**
	 * Get the value fields of this dataset
	 */
	public Collection<DataField> getValueFields();
	/**
	 * Get the name of the owner of this dataset
	 */
	public String getOwner();
}
