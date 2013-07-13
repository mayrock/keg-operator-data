/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.acl.Owner;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.jws.Oneway;

import org.apache.log4j.Logger;

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
 * @author Yuanchao Ma, Bozhi Yuan
 * 
 */
public class DataSetManagerImpl implements DataSetManager {
	private static Logger log = Logger.getLogger(DataSetManagerImpl.class);

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

	private HashMap<String, Set<DataView>> datasetMapViews = null;

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
		datasetMapViews = new HashMap<String, Set<DataView>>();
		ownerMap = new HashMap<String, Set<DataSet>>();
		limitedMap = new HashMap<String, Set<DataSet>>();
		publicMap = new HashSet<DataSet>();
		try {
			 loadDataSets();
		} catch (Exception ex) {
			log.warn(ex.getMessage());

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSet(java.lang.String)
	 */
	@Override
	public DataSet getDataSet(String id) {
		DataSet ds = datasets.get(id);
		if (ds == null) {
			throw new IllegalArgumentException("the dataset \"" + id
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
	public Collection<DataSet> getOwnDataSetList(String owner) {
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
	public void setDataSetPermission(String id, String owner, int permisson,
			List<String> limitedUsers) {
		DataSet ds = getDataSet(id);
		if (!ds.getOwner().equals(owner))
			throw new IllegalArgumentException("the " + owner + " dataset \""
					+ id + "\" does not compareble.");
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
	}

	@Override
	public void saveChanges() throws IOException {
		Writer fw;
		Storage sto = new Storage(datasets.values().toArray(new DataSet[0]),
				views.values().toArray(new DataView[0]));
		fw = new OutputStreamWriter(new FileOutputStream(
				Config.getDataSetFile()), "UTF-8");
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
		if (!views.containsKey(dv.getId()))
			return;
		// dv.getProvider().removeContent(dv);

		removeDSMeta(dv);
	}

	@Override
	public Collection<DataSet> getDataSetList(DataFeatureType type) {
		return features.get(type);
	}

	private void addDataSet(DataSet ds) {
		datasets.put(ds.getId(), ds);

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

		setDataSetPermission(ds.getId(), ds.getOwner(), ds.getPermission(),
				ds.getLimitedUsers());
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
		datasets.remove(ds.getId());
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
		views.remove(dv.getId());
		datasetMapViews.get(dv.getDataSet()).remove(dv);
		for (Set<DataView> list : viewsMap.values()) {
			list.remove(dv);
		}
	}

	@Override
	public DataSet createDataSet(String name, String owner, String description,
			DataProvider provider, boolean loadable, DataField... fields) {
		DataSet ds = new DataSetImpl("DS" + getUUID(name, owner), name, owner,
				provider, loadable, fields);
		ds.setDescription(description);
		ds.setPermission(DataSetImpl.PERMISSION_PRIVATE);
		addDataSet(ds);
		return ds;
	}

	@Override
	public DataView defineView(String name, String owner, String dataset,
			String description, DataFeatureType type, Query q)
			throws IllegalArgumentException {
		if (name == null || owner == null || name.equals("")
				|| owner.equals(""))
			throw new IllegalArgumentException(
					"Dataview name & owner can't be empty!");

		// if (this.views.containsKey(id))
		// throw new IllegalArgumentException("Dataview name: " + id
		// + " exists!");
		DataView v = new DataViewImpl("DV" + getUUID(name, owner), name, owner,
				dataset, type, q);

		v.setDescription(description);
		addDataView(v);
		return v;
	}

	@Override
	public DataView defineView(String name, String owner, String dataset,
			String description, DataFeatureType type, Query q,
			DataField[] keys, DataField[] values)
			throws IllegalArgumentException {
		if (name == null || owner == null || name.equals("")
				|| owner.equals(""))
			throw new IllegalArgumentException(
					"Dataview name & owner can't be empty!");
		// if (this.views.containsKey(id))
		// throw new IllegalArgumentException("Dataview name: " + id
		// + " exists!");
		DataView v = new DataViewImpl("DV" + getUUID(name, owner), name, owner,
				dataset, type, q, keys, values);
		v.setDescription(description);
		addDataView(v);
		return v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.DataSetManager#redefineView(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * int, edu.thu.keg.mdap.datafeature.DataFeatureType,
	 * edu.thu.keg.mdap.datamodel.Query)
	 */
	@Override
	public void redefineView(String oldId, String name, String description,
			Query q, DataField[] key, DataField[] values)
			throws IllegalArgumentException {
		if (name == null || name.equals(""))
			throw new IllegalArgumentException(
					"Dataview name & owner can't be empty!");
		if (this.views.containsKey(name))
			throw new IllegalArgumentException("new dataview name: " + name
					+ " exists!");
		if (!this.views.containsKey(oldId))
			throw new IllegalArgumentException("old dataview name: " + oldId
					+ " not exists!");
		DataView v = this.views.get(oldId);
		v.resetView(description, name, q, Arrays.asList(key),
				Arrays.asList(values));
	}

	@Override
	public void redefineView(String oldId, String name, String description,
			Query q) throws IllegalArgumentException {
		if (name == null || name.equals(""))
			throw new IllegalArgumentException(
					"Dataview name & owner can't be empty!");
		if (this.views.containsKey(name))
			throw new IllegalArgumentException("new dataview name: " + name
					+ " exists!");
		if (!this.views.containsKey(oldId))
			throw new IllegalArgumentException("Dataview name: " + oldId
					+ " not exists!");
		DataView v = this.views.get(oldId);
		v.resetView(description, name, q);
	}

	private void addDataView(DataView v) {
		views.put(v.getId(), v);
		if (!datasetMapViews.containsKey(v.getDataSet()))
			datasetMapViews.put(v.getDataSet(), new HashSet<DataView>());
		datasetMapViews.get(v.getDataSet()).add(v);

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
		if (type == null)
			return views.values();
		return viewsMap.get(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.DataSetManager#getDataViewList(java.lang.String)
	 */
	@Override
	public Collection<DataView> getDataViewList(String dataset) {
		if (dataset == null)
			return views.values();
		return datasetMapViews.get(dataset);
	}

	@Override
	public DataView getDataView(String id) {
		DataView ds = views.get(id);
		if (ds == null) {
			throw new IllegalArgumentException("the dataview \"" + id
					+ "\" does not exist.");
		}
		return ds;
	}

	private String getUUID(String name, String owner) {
		return name + "_" + owner;
	}
}
