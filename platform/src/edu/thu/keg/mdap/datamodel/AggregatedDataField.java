/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.Locale;

/**
 * @author myc
 * 
 */
public class AggregatedDataField implements DataField {

	public enum AggrFunction {
		MAX, MIN, AVG, SUM, COUNT;

		private String s;

		AggrFunction(String s) {
			this.s = s;
		}

		AggrFunction() {
			this.s = this.name();
		}

		@Override
		public String toString() {
			return this.s;
		}

		public static AggrFunction parse(String s)
				throws IllegalArgumentException {
			for (AggrFunction func : AggrFunction.values()) {
				if (func.toString().trim().equals(s.trim()))
					return func;
			}
			throw new IllegalArgumentException(s
					+ " cannot be parsed to a AggrFunction");
		}
	}

	private DataField field;
	private AggrFunction func;
	private String name;
	private LocalizedMessage desps;
	private Query query;

	public AggregatedDataField(DataField field, AggrFunction func, String name,
			Query query) {
		this.field = field;
		this.func = func;
		this.name = name;

		this.desps = new LocalizedMessage();
		this.query = query;
		desps.setMessage(Locale.ENGLISH, func.toString() + " of the field "
				+ field.getDescription(Locale.ENGLISH) + " in "
				+ field.getDataSet().getDescription(Locale.ENGLISH));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataField#getColumnName()
	 */
	@Override
	public String getQueryName() {
		return func.toString() + " ( " + field.getQueryName() + " ) ";
	}

	public String getQueryName(String alis) {
		return func.toString() + " ( " + alis + "." + field.getQueryName()
				+ " ) ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataField#getDataSet()
	 */
	@Override
	public DataSet getDataSet() {
		return this.field.getDataSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.thu.keg.mdap.datamodel.DataField#setDataSet(edu.thu.keg.mdap.datamodel
	 * .DataSet)
	 */
	@Override
	public void setDataSet(DataSet dataset) {
		throw new UnsupportedOperationException("Cannot setDataSet of"
				+ " a AggreatedDataField");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataField#getDescription()
	 */
	@Override
	public String getDescription() {
		return desps.getMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataField#isKey()
	 */
	@Override
	public boolean isKey() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataField#allowNull()
	 */
	@Override
	public boolean allowNull() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.datamodel.DataField#getFieldType()
	 */
	@Override
	public FieldType getFieldType() {
		switch (func) {
		case MAX:
		case MIN:
		case SUM:
			return field.getFieldType();
		case AVG:
			return FieldType.Double;
		case COUNT:
			return FieldType.Int;
		default:
			return FieldType.Double;
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public FieldFunctionality getFunction() {
		return FieldFunctionality.Value;
	}

	@Override
	public boolean isDim() {
		return false;
	}

	@Override
	public String getDescription(Locale locale) {
		return desps.getMessage(locale);
	}

	@Override
	public Query getQuery() {
		return this.query;
	}

	@Override
	public void setQuery(Query query) {
		this.query = query;
	}

	public DataField getField() {
		return this.field;
	}

	public AggrFunction getFunc() {
		return this.func;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public DataField clone() {
		return new AggregatedDataField(this.getField().clone(), this.getFunc(),
				this.getName(), this.getQuery());
	}

}
