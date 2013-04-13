/**
 * 
 */
package edu.thu.keg.mdap_impl.datamodel;

import java.util.HashMap;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.DataSetFeature;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.SimpleQuery;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * A general implementation of the interface DataSet
 * @author Yuanchao Ma
 *
 */
public class DataSetImpl implements DataSet {

	private String name = null;
	private DataProvider provider = null;
	private String description = null;
	private boolean loadable;
	private DataField[] fields;
	private HashMap<Class<? extends DataSetFeature>, DataSetFeature> features;

	private String defaultStmt(DataField[] fields) {
		StringBuffer sb = new StringBuffer("SELECT ");

		for (int i = 0; i < fields.length - 1; i++) {
			DataField df = fields[i];
			sb.append(df.getColumnName()).append(",");
		}
		sb.append(fields[fields.length - 1].getColumnName());

		sb.append(" FROM ").append(getName()).append(" ");
		return sb.toString();
	}
	public DataSetImpl(){
		features = new HashMap<Class<? extends DataSetFeature>,
				DataSetFeature>();
	}


	public DataSetImpl(String name, String description, DataProvider provider,
			 boolean loadable, DataField[] fields) {
		super();
		this.name = name;
		this.provider = provider;
		this.loadable = loadable;
		this.description = description;
		setDataFields(fields);
		features = new HashMap<Class<? extends DataSetFeature>, DataSetFeature>();
	}
	
	private void setDataFields(DataField[] fields) {
		this.fields = fields;
		for (DataField field : fields) {
			field.setDataSet(this);
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
	public DataField[] getDataFields() {
		return this.fields;
	}
	@Override
	public DataProvider getProvider() {
		return this.provider;
	}
	
	@Override
	public Query getQuery() throws OperationNotSupportedException,
			DataProviderException {
		Query q = new SimpleQuery(getDataFields(),
				defaultStmt(getDataFields()));
		q.setProvider(this.getProvider());
		return q;
	}
	@Override
	public HashMap<Class<? extends DataSetFeature>, DataSetFeature> getFeatures() {
		return features;
	}
	@Override
	public void addFeature(DataSetFeature feature) {
		features.put(feature.getFeatureType(), feature);
	}
	@Override
	public String getDescription() {
		return this.description;
	}
}
