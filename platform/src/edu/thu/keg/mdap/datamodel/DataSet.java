/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * Contains all metadata describing a dataset, stored in a data provider.
 * 
 * @author Yuanchao Ma
 * 
 */
public interface DataSet extends TableStructure {
	public static int PERMISSION_PUBLIC = 0;
	public static int PERMISSION_LIMITED = 1;
	public static int PERMISSION_PRIVATE = 2;

	/**
	 * Return a Query for querying data of the specific feature supported by
	 * this dataset. The returned query only contains fields related to the
	 * feature, and is by default ordered by the first value field (if there is
	 * any) descending.
	 * 
	 * @param featureType
	 *            type of the expected feature
	 * @return a Query against the provider of this dataset.
	 * @throws OperationNotSupportedException
	 *             when dataset is not loadable
	 * @throws DataProviderException
	 *             when actual data of the DataSet does not exist on the
	 *             DataProvider
	 */
	public Query getQuery(DataFeatureType featureType)
			throws OperationNotSupportedException, DataProviderException;

	/**
	 * Get the provider of this DataSet
	 * 
	 * @return a DataProvider instance
	 */
	public DataProvider getProvider();

	/**
	 * Whether this dataset is small enough to be loaded into memory
	 * 
	 * @return a boolean indicates whether this dataset is small enough to be
	 *         loaded into memory
	 */
	public boolean isLoadable();

	/**
	 * Get an array of the fields contained in the dataset
	 * 
	 * @return an array containing all the fields in the schema
	 */
	public List<DataField> getDataFields();

	/**
	 * Get a DataField reference according to its name
	 * 
	 * @param columnName
	 *            name of this field
	 * @return the DataField instance
	 */
	public DataField getField(String columnName);

	/**
	 * Get a map containing all features of this dataset
	 */
	public Set<DataFeature> getFeatures();

	/**
	 * Get an instance representing the feature of this dataset supporting the
	 * specified feature type.
	 * 
	 * @param type
	 *            The feature type whose instance is expected
	 * @return A instance of DataSetFeature. Return null if the type is not
	 *         supported
	 */
	public DataFeature getFeature(DataFeatureType type);

	/**
	 * Write the actual data of this datset on its provider
	 * 
	 * @param content
	 *            The DataContent to be written
	 * @throws DataProviderException
	 *             when writing the data provider
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

	public int getPermission();

	public void setPermission(int permission);

	public List<String> getLimitedUsers();

	public void setLimitedUsers(List<String> limitedUsers);

}
