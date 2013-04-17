/**
 * 
 */
package edu.thu.keg.mdap.datamodel;



/**

 * This interface stands for a field in a dataset.
 * A concrete field should 
 * extend this class, or its sub-classes
 * @author Yuanchao Ma 
 */
public interface DataField {
	
	public enum FieldType { 
		Int(true), Double(true), DateTime(false),
		ShortString(false), LongString(false), Text(false);
		
		private boolean isNumber;
		/**
		 * @param isNumber
		 */
		private FieldType(boolean isNumber) {
			this.isNumber = isNumber;
		}

		/**
		 * @return whether this is member field type
		 */
		public boolean isNumber() {
			return isNumber;
		}
		
		@Override
		public String toString() {
			return this.name();
		}
		/**
		 * Get a FieldType from its string representation
		 * Exception is throwed is the string is illegal
		 * @param str String representation
		 * @return the FieldType constant
		 */
		public static FieldType parse(String str) {
			return FieldType.valueOf(str.trim());
		}
	}
	/**
	 * Get the column name of the field
	 * @return The column name as a string
	 */
	public String getColumnName();
	/**
	 * Get the dataset which the field belongs
	 * @return Reference of the dataset
	 */
	public DataSet getDataSet();
	/**
	 * Get the description of the field, containing information
	 * about the column for human
	 * @return The description as a string
	 */
	public String getDescription();
	/**
	 * Whether the field is a key field in the dataset
	 * @return true when it is key, false otherwise
	 */
	public boolean isKey();
	/**
	 * 
	 * @return whether null value is allowed for this field
	 */
	public boolean allowNull();
	/**
	 * @return the type of the field
	 */
	public FieldType getFieldType();
	/**
	 * Set the DataSet which this field belongs to
	 * @param ds the DataSet instance
	 */
	public void setDataSet(DataSet ds);
}
