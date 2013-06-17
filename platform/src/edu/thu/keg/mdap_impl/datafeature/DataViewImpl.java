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

	private Query q;
	private String name;
	private LocalizedMessage desps;
	private DataFeatureType type;
	private List<DataField> keyFields;
	private List<DataField> valueFields;
	private int permission;
	private List<String> users;

	/**
	 * @param q
	 * @param name
	 * @param desps
	 */
	public DataViewImpl(String name, int permission, DataFeatureType type,
			Query q) {
		super();
		this.q = q;
		this.name = name;
		this.permission = permission;
		this.desps = new LocalizedMessage();
		this.type = type;
		initKeyValueFields();
	}

	public DataViewImpl(String name, int permission, DataFeatureType type,
			Query q, DataField[] keyFields, DataField[] valueFields) {
		super();
		this.q = q;
		this.name = name;
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
	public int getPermission() {
		return this.permission;
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
		this.desps.setMessage(desp);
	}

}
