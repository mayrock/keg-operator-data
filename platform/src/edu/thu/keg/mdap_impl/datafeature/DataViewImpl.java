package edu.thu.keg.mdap_impl.datafeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.LocalizedMessage;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap_impl.datamodel.QueryImpl;

public class DataViewImpl implements DataView {
	private String id;
	private Query q;
	private String name;
	private String owner;
	private LocalizedMessage desps;
	private DataFeatureType type;
	private List<DataField> keyFields;
	private List<DataField> valueFields;
	private int permission;
	private String dataset;
	private List<String> users;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

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
		DataViewImpl other = (DataViewImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @param q
	 * @param name
	 * @param desps
	 */
	public DataViewImpl(String id, String name, String owner, String dataset,
			int permission, DataFeatureType type, Query q) {
		super();
		this.id = id;
		this.q = q;
		this.name = name;
		this.owner = owner;
		this.dataset = dataset;
		this.permission = permission;
		this.desps = new LocalizedMessage();
		this.type = type;
		initKeyValueFields();
	}

	public DataViewImpl(String id, String name, String owner, String dataset,
			int permission, DataFeatureType type, Query q,
			DataField[] keyFields, DataField[] valueFields) {
		super();
		this.id = id;
		this.q = q;
		this.name = name;
		this.owner = owner;
		this.dataset = dataset;
		this.permission = permission;
		this.desps = new LocalizedMessage();
		this.type = type;
		this.keyFields = Arrays.asList(keyFields);
		this.valueFields = Arrays.asList(valueFields);
	}

	private void initKeyValueFields() {
		this.keyFields = new ArrayList<DataField>();
		this.valueFields = new ArrayList<DataField>();
		for (DataField f : q.getFields()) {
			if (isKeyField(f)) {
				keyFields.add(f);
			} else {
				valueFields.add(f);
			}
		}
	}

	@Override
	public String getDataSet() {
		return this.dataset;
	}

	@Override
	public int getPermission() {
		return this.permission;
	}

	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datafeature.DataView#getOwner()
	 */
	@Override
	public String getOwner() {

		return this.owner;
	}

	@Override
	public DataField[] getKeyFields() {
		return keyFields.toArray(new DataField[0]);
	}

	private boolean isKeyField(DataField f) {
		// TODO: not right!
		if (this.type.equals(DataFeatureType.GeoFeature)) {
			return f.getFunction().equals(FieldFunctionality.Latitude)
					|| f.getFunction().equals(FieldFunctionality.Longitude);
		} else if (this.type.equals(DataFeatureType.DistributionFeature)) {
			return f.getFunction().equals(FieldFunctionality.Identifier);
		} else {
			return !f.getFunction().equals(FieldFunctionality.Value);
		}
	}

	@Override
	public DataField getKeyField() throws IllegalStateException {
		DataField[] fs = getKeyFields();
		if (fs.length != 1)
			throw new IllegalStateException();
		else
			return fs[0];
	}

	@Override
	public DataField[] getValueFields() {
		return valueFields.toArray(new DataField[0]);
	}

	@Override
	public DataField[] getAllFields() {
		return q.getFields();
	}

	@Override
	public DataFeatureType getFeatureType() {
		return this.type;
	}

	@Override
	public Query getQuery() {
		return new QueryImpl(this.q);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.desps.getMessage();
	}

	@Override
	public String getDescription(Locale locale) {
		return this.desps.getMessage(locale);
	}

	@Override
	public void setDescription(String desp) {
		this.desps.setMessage(desp);
	}

	@Override
	public void setDescription(Locale locale, String desp) {
		this.desps.setMessage(locale, desp);
	}

}
