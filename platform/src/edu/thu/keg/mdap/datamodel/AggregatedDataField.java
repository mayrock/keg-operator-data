/**
 * 
 */
package edu.thu.keg.mdap.datamodel;


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
	
	public AggregatedDataField(DataField field, AggrFunction func, String name) {
		this.field = field;
		this.func = func;
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.DataField#getColumnName()
	 */
	@Override
	public String getQueryName() {
		return func.toString() + " ( " + field.getQueryName() + " ) ";
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.DataField#getDataSet()
	 */
	@Override
	public DataSet getDataSet() {
		return field.getDataSet();
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.DataField#getDescription()
	 */
	@Override
	public String getDescription() {
		return func.toString() + " of the field "
				+ field.getName() + " in " + field.getDataSet().getName();
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.DataField#isKey()
	 */
	@Override
	public boolean isKey() {
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.DataField#allowNull()
	 */
	@Override
	public boolean allowNull() {
		return true;
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.DataField#setDataSet(edu.thu.keg.mdap.datamodel.DataSet)
	 */
	@Override
	public void setDataSet(DataSet ds) {
		throw new UnsupportedOperationException("Cannot setDataSet of" +
				" a AggreatedDataField");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public FieldFunctionality getFunction() {
		return FieldFunctionality.Value;
	}

}
