/**
 * 
 */
package edu.thu.keg.mdap_impl.datamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.LocalizedMessage;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.Query.Order;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap_impl.datafeature.DataFeatureImpl;

/**
 * A general implementation of the interface DataSet
 * 
 * @author Yuanchao Ma
 * 
 */
public class DataSetImpl implements DataSet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSetImpl other = (DataSetImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private String id;
	private String name = null;
	private String owner = null;

	private DataProvider provider = null;
	private LocalizedMessage descriptions = null;
	private boolean loadable;
	private HashMap<FieldFunctionality, List<DataField>> fieldsMap;
	private List<DataField> fields;

	private int permission;
	private List<String> limitedUsers;

	public DataSetImpl() {
		fieldsMap = new HashMap<FieldFunctionality, List<DataField>>();
		fields = new ArrayList<DataField>();
		descriptions = new LocalizedMessage();
	}

	public DataSetImpl(String id, String name, String owner,
			DataProvider provider, boolean loadable, DataField... fields) {
		super();
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.provider = provider;
		this.loadable = loadable;
		this.descriptions = new LocalizedMessage();

		this.fieldsMap = new HashMap<FieldFunctionality, List<DataField>>();
		this.fields = new ArrayList<DataField>();

		setDataFields(fields);
	}

	private void setDataFields(DataField[] fields) {
		for (DataField field : fields) {
			field.setDataSet(this);
			this.fields.add(field);

			FieldFunctionality func = field.getFunction();
			if (!this.fieldsMap.containsKey(func)) {
				this.fieldsMap.put(func, new ArrayList<DataField>());
			}
			this.fieldsMap.get(func).add(field);
		}
	}

	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.TableStructure#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isLoadable() {
		return this.loadable;
	}

	@Override
	public List<DataField> getDataFields() {
		return this.fields;
	}

	@Override
	public DataProvider getProvider() {
		return this.provider;
	}

	@Override
	public Query getQuery() throws OperationNotSupportedException,
			DataProviderException {
		Query q = new QueryImpl(this);
		return q;
	}

	@Override
	public String getDescription() {
		return this.descriptions.getMessage();
	}

	@Override
	public DataFeature getFeature(DataFeatureType type) {
		List<DataField> keys = new ArrayList<DataField>();
		List<DataField> values = new ArrayList<DataField>();
		boolean flag = true;

		for (FieldFunctionality func : type.getFuncs()) {
			if (!fieldsMap.containsKey(func)) {
				flag = false;
				break;
			}
		}
		if (flag) {
			// add all fileds except feature is Geo
			if (type.equals(DataFeatureType.GeoFeature)) {
				keys.add(fieldsMap.get(FieldFunctionality.Latitude).get(0));
				keys.add(fieldsMap.get(FieldFunctionality.Longitude).get(0));
				if (fieldsMap.get(FieldFunctionality.Identifier) != null)
					values.addAll(fieldsMap.get(FieldFunctionality.Identifier));
				if (fieldsMap.get(FieldFunctionality.Value) != null)
					values.addAll(fieldsMap.get(FieldFunctionality.Value));
				// add all value feature fields
			} else if (type.equals(DataFeatureType.DistributionFeature)) {
				keys.addAll(fieldsMap.get(FieldFunctionality.Identifier));
				if (fieldsMap.get(FieldFunctionality.Value) != null)
					values.addAll(fieldsMap.get(FieldFunctionality.Value));
			} else {
				keys = this.getPrimaryKeyFields();
				values = this.getOtherFields();
			}

			return new DataFeatureImpl(type, keys, values);
		} else {
			return null;
		}
	}

	@Override
	public void writeData(DataContent content) throws DataProviderException {
		this.getProvider().writeDataSetContent(this, content);
	}

	@Override
	public Query getQuery(DataFeatureType featureType)
			throws OperationNotSupportedException, DataProviderException {
		DataFeature feature = this.getFeature(featureType);
		Query q = this.getQuery();
		q = q.select(feature.getAllFields().toArray(new DataField[0]));
		if (feature.getValueFields() != null) {
			q = q.orderBy(feature.getValueFields().get(0).getName(), Order.DESC);
		}
		return q;
	}

	@Override
	public DataField getField(String columnName) {
		for (DataField f : this.fields) {
			if (f.getName().equals(columnName))
				return f;
		}
		throw new IllegalArgumentException("Field with name " + columnName
				+ " does not exist in DataSet " + this.getName());
	}

	@Override
	public Set<DataFeature> getFeatures() {// 创建数据集的时候调用一次，唯一一次，得到所有的这个数据集的DataFeature
		Set<DataFeature> features = new HashSet<DataFeature>();
		for (DataFeatureType type : DataFeatureType.values()) {
			DataFeature f = this.getFeature(type);
			if (f != null) {
				features.add(f);
			}
		}
		return features;
	}

	@Override
	public List<DataField> getPrimaryKeyFields() {
		List<DataField> ret = new ArrayList<DataField>();
		for (DataField field : fields) {
			if (field.isKey()) {
				ret.add(field);
			}
		}
		return ret;
	}

	@Override
	public List<DataField> getOtherFields() {
		List<DataField> ret = new ArrayList<DataField>();
		for (DataField field : fields) {
			if (!field.isKey()) {
				ret.add(field);
			}
		}
		return ret;
	}

	@Override
	public String getOwner() {
		return this.owner;
	}

	@Override
	public int getPermission() {

		return this.permission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataSet#setPermission()
	 */
	@Override
	public void setPermission(int permission) {
		this.permission = permission;

	}

	@Override
	public List<String> getLimitedUsers() {
		return this.limitedUsers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataSet#setLimitedUsers(java.util.List)
	 */
	@Override
	public void setLimitedUsers(List<String> limitedUsers) {
		this.limitedUsers = limitedUsers;
	}

	@Override
	public String getDescription(Locale locale) {
		return this.descriptions.getMessage(locale);
	}

	@Override
	public void setDescription(String desp) {
		this.descriptions.setMessage(desp);
	}

	@Override
	public void setDescription(Locale locale, String desp) {
		this.descriptions.setMessage(locale, desp);
	}

	public static int parsePermission(String permission) {
		switch (permission) {
		case "public":
			return DataSet.PERMISSION_PUBLIC;
		case "limited":
			return DataSet.PERMISSION_LIMITED;
		case "private":
			return DataSet.PERMISSION_PRIVATE;
		default:
			break;
		}
		return 0;
	}

	public static String permissionToString(int permission) {
		switch (permission) {
		case DataSet.PERMISSION_PUBLIC:
			return "public";
		case DataSet.PERMISSION_LIMITED:
			return "limited";
		case DataSet.PERMISSION_PRIVATE:
			return "private";
		default:
			break;
		}
		return null;
	}

}
