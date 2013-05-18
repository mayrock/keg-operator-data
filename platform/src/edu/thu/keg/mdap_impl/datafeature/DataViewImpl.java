package edu.thu.keg.mdap_impl.datafeature;

import java.util.ArrayList;

import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;

public class DataViewImpl implements DataView {

	private Query q;
	private String name;
	private String desp;
	private DataFeatureType type;


	/**
	 * @param q
	 * @param name
	 * @param desp
	 */
	public DataViewImpl(String name, String desp, DataFeatureType type, Query q) {
		super();
		this.q = q;
		this.name = name;
		this.desp = desp;
		this.type = type;
	}

	@Override
	public DataField[] getKeyFields() {
		ArrayList<DataField> fs = new ArrayList<DataField>();
		for (DataField f : q.getDataFields()) {
			if (isKeyField(f) ) {
				fs.add(f);
			}
		}
		return fs.toArray(new DataField[0]);
	}
	private boolean isKeyField(DataField f) {
		return f.isDim();
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
		ArrayList<DataField> fs = new ArrayList<DataField>();
		for (DataField f : q.getDataFields()) {
			if (!isKeyField(f) ) {
				fs.add(f);
			}
		}
		return fs.toArray(new DataField[0]);
	}

	@Override
	public DataField[] getAllFields() {
		return q.getDataFields();
	}

	@Override
	public DataFeatureType getFeatureType() {
		return this.type;
	}

	@Override
	public Query getQuery() {
		return this.q;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.desp;
	}

}
