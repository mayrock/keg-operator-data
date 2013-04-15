/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datasetfeature.DataSetFeature;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap_impl.datamodel.DataSetImpl;
import edu.thu.keg.mdap_impl.provider.JdbcProvider;

/**
 * Implementation of the interface DataSetManager
 * @author Yuanchao Ma
 *
 */
public class DataSetManagerImpl implements DataSetManager {

	private static DataSetManagerImpl instance;
	public static DataSetManagerImpl getInstance() {
		//TODO multi-thread
		if (instance == null)
			instance = new DataSetManagerImpl();
		return instance;
	}
	
	private HashMap<String, DataSet> datasets = null;
	private HashMap<Class<? extends DataSetFeature>, Set<DataSet>> features = null;
	private XStream xstream;
	
	private XStream getXstream() {
		if (xstream == null) {
			xstream = new XStream(new StaxDriver());
			xstream.alias("Jdbc", JdbcProvider.class);
			xstream.registerConverter(new AbstractSingleValueConverter() {

				@Override
				public boolean canConvert(@SuppressWarnings("rawtypes") Class arg0) {
					return arg0.equals(JdbcProvider.class);
				}

				@Override
				public Object fromString(String str) {
					return new JdbcProvider(str);
				}
				
			});
		}
		return xstream;
	}
	
	private DataSetManagerImpl() {
		datasets = new HashMap<String, DataSet>();
		features = new HashMap<Class<? extends DataSetFeature>, Set<DataSet>>();
		try {
			loadDataSets();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSet(java.lang.String)
	 */
	@Override
	public DataSet getDataSet(String name) {
		DataSet ds = datasets.get(name);
		if (ds == null) {
			throw new IllegalArgumentException("the dataset \"" + name + "\" does not exist.");
		}
		return ds;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSetList()
	 */
	@Override
	public Collection<DataSet> getDataSetList() {
		return datasets.values();
	}
	
	private void loadDataSets() {
		String f = Config.getDataSetFile();
		DataSet[] sets = (DataSet[])getXstream().fromXML(new File(f));
		for (DataSet ds : sets) {
			addDataSet(ds);
		}
	}

	@Override
	public void saveChanges() throws IOException {
		FileWriter fw;
		DataSet[] dss = new DataSet[datasets.size()];
		int i = 0;
		for (DataSet d : datasets.values()) {
			dss[i++] = d;
		}
		fw = new FileWriter(Config.getDataSetFile(), false);
		getXstream().marshal(dss, new PrettyPrintWriter(fw));
		fw.close();
	}
	@Override
	public DataSet createDataSet(String name, String description, DataProvider provider,
			DataField[] fields, boolean loadable, DataSetFeature[] features) {
		DataSet ds = new DataSetImpl(name, description, provider, 
				loadable, fields, features);
		addDataSet(ds);
		return ds;
	}
	@Override
	public DataSet createDataSet(String name, String description, DataProvider provider,
			DataField[] fields, boolean loadable, DataSetFeature feature) {
		DataSetFeature[] features = new DataSetFeature[]{feature};
		return createDataSet(name, description, provider, fields, loadable, features);
	}
	@Override
	public DataSet createDataSet(String name, String description, DataProvider provider,
			DataField[] fields, boolean loadable) {
		DataSetFeature[] features = new DataSetFeature[]{};
		return createDataSet(name, description, provider, fields, loadable, features);
	}

	@Override
	public void removeDataSet(DataSet ds) throws DataProviderException {
		if (!datasets.containsValue(ds))
			return;
		ds.getProvider().removeContent(ds);
		removeDSMeta(ds);
	}

	@Override
	public Collection<DataSet> getDataSetList(
			Class<? extends DataSetFeature> feature) {
		return features.get(feature);	
	}
	private void addDataSet(DataSet ds) {
		datasets.put(ds.getName(), ds);
		for (DataSetFeature feature : ds.getFeatures()) {
			for (Class<? extends DataSetFeature> type : feature.getAllFeatureTypes()) {
				if (!features.containsKey(type))
					features.put(type, new HashSet<DataSet>());
				features.get(type).add(ds);
			}
		}
	}
	private void removeDSMeta(DataSet ds) {
		datasets.remove(ds);
		for (Set<DataSet> list : features.values()) {
			list.remove(ds);
		}
	}
}
