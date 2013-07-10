/**
 * 
 */
package edu.thu.keg.mdap_impl.datafeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oracle.sql.ARRAY;

import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datamodel.DataField;

/**
 * @author Yuanchao Ma
 * 
 */
public class DataFeatureImpl implements DataFeature {
	private DataFeatureType type;
	private List<DataField> keyFields;
	private List<DataField> valueFields;

	@Override
	public List<DataField> getKeyFields() {
		return this.keyFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.thu.keg.mdap.datafeature.DataFeature#setKeyFields(java.util.List)
	 */
	@Override
	public void setKeyFields(List<DataField> keyFields) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.thu.keg.mdap.datafeature.DataFeature#setValueFields(java.util.List)
	 */
	@Override
	public void setValueFields(List<DataField> valueFields) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataField getKeyField() throws IllegalStateException {
		if (keyFields.size() != 1)
			throw new IllegalStateException();
		return keyFields.get(0);
	}

	@Override
	public List<DataField> getValueFields() {
		return this.valueFields;
	}

	@Override
	public List<DataField> getAllFields() {
		List<DataField> all = new ArrayList<DataField>();
		for (int i = 0; i < this.keyFields.size(); i++) {
			all.add(this.keyFields.get(i));
		}
		for (int i = 0; i < this.valueFields.size(); i++) {
			all.add(this.valueFields.get(i));
		}
		return all;
	}

	@Override
	public DataFeatureType getFeatureType() {
		return this.type;
	}

	/**
	 * Init a new DataSetFeature which key fields and value fields
	 * 
	 * @param keyFields
	 *            the key DataFields
	 * @param valueFields
	 *            the value DataFields
	 */
	public DataFeatureImpl(DataFeatureType type, List<DataField> keyFields,
			List<DataField> valueFields) {
		this.type = type;
		this.keyFields = keyFields;
		this.valueFields = valueFields;
	}

	/**
	 * Init a new StatisticsDataSet with single key field and single value field
	 * 
	 * @param keyField
	 *            the key DataField
	 * @param valueFields
	 *            the value fields
	 */
	public DataFeatureImpl(DataFeatureType type, DataField keyField,
			List<DataField> valueFields) {
		this.type = type;
		List<DataField> keyf = new ArrayList<DataField>();
		keyf.add(keyField);
		this.keyFields = keyf;
		this.valueFields = valueFields;
	}
}
