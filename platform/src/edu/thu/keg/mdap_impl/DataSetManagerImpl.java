/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.acl.Owner;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jws.Oneway;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
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

	private HashMap<String, Set<DataSet>> ownerMap = null;
	private HashMap<String, Set<DataSet>> limitedMap = null;
	private HashSet<DataSet> publicMap = null;
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

		ownerMap = new HashMap<String, Set<DataSet>>();
		limitedMap = new HashMap<String, Set<DataSet>>();
		publicMap = new HashSet<DataSet>();
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

	@Override
	public Collection<DataSet> getPublicDataSetList() {
		// TODO Auto-generated method stub
		return publicMap;
	}

	@Override
	public Collection<DataSet> getLimitedDataSetList(String userid) {
		return limitedMap.get(userid);
	}

	@Override
	public Collection<DataSet> getPrivateDataSetList(String owner) {
		return ownerMap.get(owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.thu.keg.mdap.DataSetManager#setDataSetPermission(java.lang.String,
	 * java.lang.String, int, java.util.List)
	 */
	@Override
	public void setDataSetPermission(String name, String owner, int permisson,
			List<String> limitedUsers) {
		DataSet ds = getDataSet(name);
		if (!ds.getOwner().equals(owner))
			throw new IllegalArgumentException("the " + owner + " dataset \""
					+ name + "\" does not compareble.");
		switch (permisson) {
		case DataSet.PERMISSION_PUBLIC:
			publicMap.add(ds);
			if (ds.getPermission() == DataSet.PERMISSION_LIMITED) {
				for (Set<DataSet> dset : limitedMap.values()) {
					dset.remove(ds);
				}
			}
			break;
		case DataSet.PERMISSION_LIMITED:
			if (permisson == DataSet.PERMISSION_LIMITED && limitedUsers == null)
				throw new IllegalArgumentException("the limited users is null ");
			for (String user : limitedUsers) {
				if (!limitedMap.containsKey(user))
					limitedMap.put(user, new HashSet<DataSet>());
				limitedMap.get(user).add(ds);
			}
			ds.setLimitedUsers(limitedUsers);
			if (ds.getPermission() == DataSet.PERMISSION_PUBLIC) {
				publicMap.remove(ds);
			}
			break;
		case DataSet.PERMISSION_PRIVATE:
			publicMap.remove(ds);
			for (Set<DataSet> dset : limitedMap.values()) {
				dset.remove(ds);
			}
			break;
		default:
		}
		ds.setPermission(permisson);

	}

	private void loadDataSets() {
		String f = Config.getDataSetFile();
		File Fi = new File(f);
		Storage sto = (Storage) getXstream().fromXML(Fi);
		for (DataSet ds : sto.datasets) {
			addDataSet(ds);
		}
		for (DataView ds : sto.views) {
			addDataView(ds);
		}

		System.out.println("----------------------------"
				+ getDataSetList().size());
		for (DataSet ds : getDataSetList()) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}

		System.out.println("PUBLIC-----------------------------"
				+ getPublicDataSetList().size());
		for (DataSet ds : getPublicDataSetList()) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}
		System.out.println("myc-----------------------------"
				+ getPrivateDataSetList("myc").size());
		for (DataSet ds : getPrivateDataSetList("myc")) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}

		System.out.println("wc-----------------------------"
				+ getPrivateDataSetList("wc").size());
		for (DataSet ds : getPrivateDataSetList("wc")) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}
		System.out.println();
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

		if (!ownerMap.containsKey(ds.getOwner())) {
			ownerMap.put(ds.getOwner(), new HashSet<DataSet>());
		}
		if (!ownerMap.get(ds.getOwner()).contains(ds))
			ownerMap.get(ds.getOwner()).add(ds);

		setDataSetPermission(ds.getName(), ds.getOwner(), ds.getPermission(),
				null);
		// if (ds.getPermission() == DataSet.PERMISSION_PUBLIC) {
		// if (!publicMap.contains(ds))
		// publicMap.add(ds);
		// } else if (ds.getPermission() == DataSet.PERMISSION_LIMITED) {
		// for (String user : ds.getLimitedUsers()) {
		// if (!limitedMap.containsKey(user))
		// limitedMap.put(user, new HashSet<DataSet>());
		// limitedMap.get(user).add(ds);
		// }
		// }
	}

	private void removeDSMeta(DataSet ds) {
		datasets.remove(ds.getName());
		ownerMap.get(ds.getOwner()).remove(ds);
		publicMap.remove(ds);
		for (Set<DataSet> list : limitedMap.values()) {
			list.remove(ds);
		}
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
	public DataSet createDataSet(String name, String owner, int permission,
			String description, DataProvider provider, boolean loadable,
			DataField... fields) {
		DataSet ds = new DataSetImpl(name, owner, permission, provider,
				loadable, fields);
		ds.setDescription(description);
		addDataSet(ds);
		return ds;
	}

	@Override
	public DataView defineView(String name, String description, int permission,
			DataFeatureType type, Query q) {
		DataView v = new DataViewImpl(name, permission, type, q);
		v.setDescription(description);
		addDataView(v);
		return v;
	}

	@Override
	public DataView defineView(String name, String description, int permission,
			DataFeatureType type, Query q, DataField key, DataField[] values) {
		DataField[] keys = { key };
		DataView v = new DataViewImpl(name, permission, type, q, keys, values);
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
