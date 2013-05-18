/**
 * 
 */
package edu.thu.keg.mdap_impl.datafeature;

import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datamodel.DataField;

/**
 * @author Yuanchao Ma
 *
 */
public class DataFeatureImpl implements DataFeature {
	private DataFeatureType type;
	private DataField[] keyFields;
	private DataField[] valueFields;
	
	@Override
	public DataField[] getKeyFields() {
		return this.keyFields;
	}
	@Override
	public DataField getKeyField() throws IllegalStateException {
		if (keyFields.length != 1)
			throw new IllegalStateException();
		return keyFields[0];
	}
	@Override
	public DataField[] getValueFields() {
		return this.valueFields;
	}
	@Override
	public DataField[] getAllFields() {
		DataField[] all = new DataField[this.keyFields.length + this.valueFields.length];
		for (int i = 0; i < this.keyFields.length; i++) {
			all[i] = this.keyFields[i];
		}
		for (int i = 0; i < this.valueFields.length; i++) {
			all[this.keyFields.length + i] = this.valueFields[i];
		}
		return all;
	}
	@Override
	public DataFeatureType getFeatureType() {
		return this.type;
	}
	/**
	 * Init a new DataSetFeature which key fields and value fields
	 * @param keyFields the key DataFields
	 * @param valueFields the value DataFields
	 */
	public DataFeatureImpl(DataFeatureType type, DataField[] keyFields, DataField ...valueFields) {
		this.type = type;
		this.keyFields = keyFields;
		this.valueFields = valueFields;
	}
	
	/**
	 * Init a new StatisticsDataSet with single key field and single value field
	 * @param keyField the key DataField
	 * @param valueFields the value fields
	 */
	public DataFeatureImpl(DataFeatureType type, DataField keyField, DataField... valueFields) {
		this.type = type;
		this.keyFields = new DataField[]{keyField };
		this.valueFields = valueFields;
	}
}
