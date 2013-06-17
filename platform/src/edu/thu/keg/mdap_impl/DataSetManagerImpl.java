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
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap_impl.datamodel.DataSetImpl;
import edu.thu.keg.mdap_impl.datafeature.DataViewImpl;
import edu.thu.keg.mdap_impl.provider.JdbcProvider;

/**
 * Implementation of the interface DataSetManager
 * 
 * @author Yuanchao Ma
 * 
 */
public class DataSetManagerImpl implements DataSetManager {

	public static class Storage {
		DataSet[] datasets;
		DataView[] views;

		/**
		 * @param datasets
		 * @param views
		 */
		private Storage(DataSet[] datasets, DataView[] views) {
			super();
			this.datasets = datasets;
			this.views = views;
		}

	}

	private static DataSetManagerImpl instance;

	public synchronized static DataSetManagerImpl getInstance() {
		// TODO multi-thread
		if (instance == null)
			instance = new DataSetManagerImpl();
		return instance;
	}

	private HashMap<String, DataSet> datasets = null;
	private HashMap<DataFeatureType, Set<DataSet>> features = null;
	private HashMap<String, DataView> views = null;
	private HashMap<DataFeatureType, Set<DataView>> viewsMap = null;
	private XStream xstream;

	private XStream getXstream() {
		if (xstream == null) {
			xstream = new XStream(new StaxDriver());
			xstream.alias("Jdbc", JdbcProvider.class);
			xstream.registerConverter(new AbstractSingleValueConverter() {

				@Override
				public boolean canConvert(
						@SuppressWarnings("rawtypes") Class arg0) {
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
		features = new HashMap<DataFeatureType, Set<DataSet>>();
		views = new HashMap<String, DataView>();
		viewsMap = new HashMap<DataFeatureType, Set<DataView>>();

		try {
			loadDataSets();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSet(java.lang.String)
	 */
	@Override
	public DataSet getDataSet(String name) {
		DataSet ds = datasets.get(name);
		if (ds == null) {
			throw new IllegalArgumentException("the dataset \"" + name
					+ "\" does not exist.");
		}
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSetList()
	 */
	@Override
	public Collection<DataSet> getDataSetList() {
		return datasets.values();
	}

	private void loadDataSets() {
		String f = Config.getDataSetFile();
		Storage sto = (Storage) getXstream().fromXML(new File(f));
		for (DataSet ds : sto.datasets) {
			addDataSet(ds);
		}
		for (DataView ds : sto.views) {
			addDataView(ds);
		}
	}

	@Override
	public void saveChanges() throws IOException {
		FileWriter fw;
		Storage sto = new Storage(datasets.values().toArray(new DataSet[0]),
				views.values().toArray(new DataView[0]));

		fw = new FileWriter(Config.getDataSetFile(), false);
		getXstream().marshal(sto, new PrettyPrintWriter(fw));
		fw.close();
	}

	@Override
	public void removeDataSet(DataSet ds) throws DataProviderException {
		if (!datasets.containsValue(ds))
			return;
		ds.getProvider().removeContent(ds);
		removeDSMeta(ds);
	}
	@Override
	public void removeDataView(DataView dv) throws DataProviderException {
		if (!views.containsValue(dv))
			return;
		// dv.getProvider().removeContent(dv);
		removeDSMeta(dv);
	}
	

	@Override
	public Collection<DataSet> getDataSetList(DataFeatureType type) {
		return features.get(type);
	}

	private void addDataSet(DataSet ds) {
		datasets.put(ds.getName(), ds);
		for (DataFeature feature : ds.getFeatures()) {
			DataFeatureType type = feature.getFeatureType();
			if (!features.containsKey(type))
				features.put(type, new HashSet<DataSet>());
			features.get(type).add(ds);
		}
	}

	private void removeDSMeta(DataSet ds) {
		datasets.remove(ds);
		for (Set<DataSet> list : features.values()) {
			list.remove(ds);
		}
	}

	private void removeDSMeta(DataView dv) {
		views.remove(dv);
		for (Set<DataView> list : viewsMap.values()) {
			list.remove(dv);
		}
	}

	@Override
	public DataSet createDataSet(String name, String owner, String description,
			DataProvider provider, boolean loadable, DataField... fields) {
		DataSet ds = new DataSetImpl(name, owner, provider, loadable, fields);
		ds.setDescription(description);
		addDataSet(ds);
		return ds;
	}

	@Override
	public DataView defineView(String name, String description,
			DataFeatureType type, Query q) {
		DataView v = new DataViewImpl(name, type, q);
		v.setDescription(description);
		addDataView(v);
		return v;
	}

	@Override
	public DataView defineView(String name, String description,
			DataFeatureType type, Query q, DataField key, DataField[] values) {
		DataField[] keys = { key };
		DataView v = new DataViewImpl(name, type, q, keys, values);
		v.setDescription(description);
		addDataView(v);
		return v;
	}

	private void addDataView(DataView v) {
		views.put(v.getName(), v);
		DataFeatureType type = v.getFeatureType();
		if (!viewsMap.containsKey(v.getFeatureType())) {
			viewsMap.put(type, new HashSet<DataView>());
		}
		viewsMap.get(type).add(v);
	}

	@Override
	public Collection<DataView> getDataViewList() {
		return views.values();
	}

	@Override
	public Collection<DataView> getDataViewList(DataFeatureType type) {
		return viewsMap.get(type);
	}

	@Override
	public DataView getDataView(String name) {
		DataView ds = views.get(name);
		if (ds == null) {
			throw new IllegalArgumentException("the dataview \"" + name
					+ "\" does not exist.");
		}
		return ds;
	}

}
