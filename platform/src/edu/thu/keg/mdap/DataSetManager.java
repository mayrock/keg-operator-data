package edu.thu.keg.mdap;

import java.io.IOException;
import java.util.Collection;

import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * This object provides information
 * about all datasets stored in the platform,
 * including all suppliers. Metadata stored in 
 * all data providers are found here.
 * @author Yuanchao Ma
 *
 */
public interface DataSetManager {
	/**
	 * Get a dataset stored in the platform and return a reference.
	 * It can be stored in all registered providers.
	 * @param name the name of the requested dataset
	 * @return a reference of DataSet
	 */
	public DataSet getDataSet(String name);
	/**
	 * Get all datasets stored in the platform
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
	 * @param name The unique name of the DataSet
	 * @param owner name of the owner of the dataset
	 * @param description description of the dataset
	 * @param provider its DataProvider
	 * @param loadable if this dataset can be loaded into memory
	 * @param fields DataField set
	 * @return A new DataSet instance
	 */
	public DataSet createDataSet(String name, String owner, String description, DataProvider provider, 
			boolean loadable, DataField ... fields);
	
	
	public DataView defineView(String name, String description, DataFeatureType type,
			Query q);
	/**
	 * Remove a DataSet from the platform, as well as remove 
	 * its actual data from its provider.
	 * No operation is performed if the DataSet does not exist.
	 * @param ds The DataSet to be removed
	 * @throws DataProviderException when removing DataSet actual data throws error
	 */
	public void removeDataSet(DataSet ds) throws DataProviderException;
	
	/**
	 * Get all datasets that supports the certain type of feature
	 * @param type the feature type demanded
	 * @return all datasets that supports the certain type of feature
	 */
	public Collection<DataSet> getDataSetList(DataFeatureType type);
	/**
	 * 
	 * @param type
	 * @return
	 */
	public Collection<DataView> getDataViewList(DataFeatureType type);
	
	public DataView getDataView(String name);
	/**
	 * Save current DataSet list to disk
	 * @throws IOException
	 */
	public void saveChanges() throws IOException;
	DataView defineView(String name, String description, DataFeatureType type,
			Query q, DataField key, DataField[] values);

}
