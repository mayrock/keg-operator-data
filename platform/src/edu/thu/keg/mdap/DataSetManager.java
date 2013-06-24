package edu.thu.keg.mdap;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * This object provides information about all datasets stored in the platform,
 * including all suppliers. Metadata stored in all data providers are found
 * here.
 * 
 * @author Yuanchao Ma
 * 
 */
public interface DataSetManager {
	/**
	 * Get a dataset stored in the platform and return a reference. It can be
	 * stored in all registered providers.
	 * 
	 * @param name
	 *            the name of the requested dataset
	 * @return a reference of DataSet
	 */
	public DataSet getDataSet(String name);

	/**
	 * Get all datasets stored in the platform
	 * 
	 * @return a array containing all the dataset references
	 */
	public Collection<DataSet> getDataSetList();

	/**
	 * 
	 * @return
	 */
	public Collection<DataView> getDataViewList();

	/**
	 * Create a new DataSet instance, using the given name, connection string
	 * data fields, and a single features
	 * 
	 * @param name
	 *            The unique name of the DataSet
	 * @param owner
	 *            name of the owner of the dataset
	 * @param description
	 *            description of the dataset
	 * @param provider
	 *            its DataProvider
	 * @param loadable
	 *            if this dataset can be loaded into memory
	 * @param fields
	 *            DataField set
	 * @return A new DataSet instance
	 */
	public DataSet createDataSet(String name, String owner, String description,
			DataProvider provider, boolean loadable, DataField... fields);

	public DataView defineView(String name, String description, int permission,
			DataFeatureType type, Query q);

	/**
	 * Remove a DataSet from the platform, as well as remove its actual data
	 * from its provider. No operation is performed if the DataSet does not
	 * exist.
	 * 
	 * @param ds
	 *            The DataSet to be removed
	 * @throws DataProviderException
	 *             when removing DataSet actual data throws error
	 */
	public void removeDataSet(DataSet ds) throws DataProviderException;

	/**
	 * Remove a DataView from the platform, as well as remove its actual data
	 * from its provider. No operation is performed if the DataView does not
	 * exist.
	 * 
	 * @param dv
	 * @throws DataProviderException
	 */
	public void removeDataView(DataView dv) throws DataProviderException;

	/**
	 * Get all datasets that supports the certain type of feature
	 * 
	 * @param type
	 *            the feature type demanded
	 * @return all datasets that supports the certain type of feature
	 */
	public Collection<DataSet> getDataSetList(DataFeatureType type);

	/**
	 * Get all datasets which the permission is Public, therefore everyone can
	 * read it
	 * 
	 * @return all datasets which suitble for beyound conditions
	 */
	public Collection<DataSet> getPublicDataSetList();

	/**
	 * Get all datasets which userid can read
	 * 
	 * @param userid
	 * @return the collection of datasets
	 */
	public Collection<DataSet> getLimitedDataSetList(String userid);

	/**
	 * Get all datasets which are belonged to the owner
	 * 
	 * @param owner
	 * @return the collection of datasets
	 */
	public Collection<DataSet> getOwnDataSetList(String owner);

	/**
	 * 
	 * @param type
	 * @return
	 */
	public Collection<DataView> getDataViewList(DataFeatureType type);

	public void setDataSetPermission(String name, String owner, int permisson,
			List<String> userid);

	public DataView getDataView(String name);

	/**
	 * Save current DataSet list to disk
	 * 
	 * @throws IOException
	 */
	public void saveChanges() throws IOException;

	DataView defineView(String name, String description, int permission,
			DataFeatureType type, Query q, DataField key, DataField[] values);

}
