/**
 * 
 */
package edu.thu.keg.mdap_impl.datamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.Query.Order;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap_impl.datafeature.DataFeatureImpl;

/**
 * A general implementation of the interface DataSet
 * @author Yuanchao Ma
 *
 */
public class DataSetImpl implements DataSet {

	private String name = null;
	private String owner = null;
	private DataProvider provider = null;
	private String description = null;
	private boolean loadable;
	private HashMap<FieldFunctionality, List<DataField>> fieldsMap;
	private List<DataField> fields;
	
	public DataSetImpl(){
		fieldsMap = new HashMap<FieldFunctionality, List<DataField>>();
		fields = new ArrayList<DataField>();
	}


	public DataSetImpl(String name, String owner, String description, DataProvider provider,
			 boolean loadable, DataField... fields) {
		super();
		this.name = name;
		this.owner = owner;
		this.provider = provider;
		this.loadable = loadable;
		this.description = description;
		
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
		return this.description;
	}
	@Override
	public DataFeature getFeature(DataFeatureType type) {
		List<DataField> keys = new ArrayList<DataField>();
		boolean flag = true;

		for (FieldFunctionality func : type.getFuncs()) {
			if (!fieldsMap.containsKey(func)) {
				flag = false;
				break;
			} 
		}
		
		keys = this.getKeyFields();
		
		if (flag) {
			return new DataFeatureImpl(type, keys.toArray(new DataField[0]), 
					this.getValueFields().toArray(new DataField[0]));
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
		q = q.select(feature.getAllFields());
		if (feature.getValueFields() != null) {
			q = q.orderBy(feature.getValueFields()[0].getName(), Order.DESC);
		}
		return q;
	}


	@Override
	public DataField getField(String columnName) {
		for (DataField f : this.fields) {
			if (f.getName().equals(columnName) )
				return f;
		}
		throw new IllegalArgumentException("Field with name " 
				+ columnName + " does not exist in DataSet "
				+ this.getName()); 
	}


	@Override
	public Set<DataFeature> getFeatures() {
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
	public List<DataField> getKeyFields() {
		List<DataField> ret = new ArrayList<DataField>();
		for (DataField field : fields) {
			if (field.isKey()) {
				ret.add(field);
			}
		}
		return ret;
	}


	@Override
	public List<DataField> getValueFields() {
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
}
