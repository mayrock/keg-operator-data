/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.sql.Date;
import java.util.Locale;

/**
 * 
 * This interface stands for a field in a dataset. A concrete field should
 * extend this class, or its sub-classes
 * 
 * @author Yuanchao Ma
 */
public interface DataField {

	@SuppressWarnings("rawtypes")
	public enum FieldType {
		Int(Integer.class), Double(Double.class), DateTime(Date.class), ShortString(
				String.class), LongString(String.class), Text(String.class);

		private Class c;

		/**
		 * @param isNumber
		 */
		private FieldType(Class c) {
			this.c = c;
		}

		/**
		 * @return whether this is member field type
		 */
		public boolean isNumber() {
			return this.c == Integer.class || this.c == Double.class;
		}

		@Override
		public String toString() {
			return this.name();
		}

		/**
		 * 
		 * @return The Java Class of this type
		 */
		public Class getJavaClass() {
			return this.c;
		}

		/**
		 * Get a FieldType from its string representation Exception is throwed
		 * is the string is illegal
		 * 
		 * @param str
		 *            String representation
		 * @return the FieldType constant
		 */
		public static FieldType parse(String str) {
			return FieldType.valueOf(str.trim());
		}
	}

	public enum FieldFunctionality {
		Latitude, Longitude, TimeStamp, Value, Identifier, Other;

		/**
		 * Get a FieldFunctionality instance from its string representation
		 * 
		 * @param str
		 *            The string representation
		 * @return a FieldFunctionality object
		 */
		public static FieldFunctionality parse(String str) {
			return FieldFunctionality.valueOf(str);
		}
	}

	/**
	 * 
	 * @return Functionality of this field. E.g., whether it's a key field,
	 *         timestamp field, or a value field that can be aggregated
	 */
	public FieldFunctionality getFunction();

	/**
	 * Get the name identifying the field
	 * 
	 * @return The column name as a string
	 */
	public String getName();

	/**
	 * 
	 * @return name used for querying data in this field
	 */
	public String getQueryName();

	/**
	 * Get the dataset which the field belongs
	 * 
	 * @return Reference of the dataset
	 */
	public DataSet getDataSet();
	
	public Query getQuery();

	/**
	 * Get the description of the field, containing information about the column
	 * for human
	 * 
	 * @return The description as a string
	 */
	public String getDescription();

	/**
	 * Get description
	 * 
	 * @param locale
	 *            which language
	 * @return
	 */
	public String getDescription(Locale locale);

	/**
	 * Whether the field is a key field in the dataset
	 * 
	 * @return true when it is key, false otherwise
	 */
	public boolean isKey();

	/**
	 * 
	 * @return
	 */
	public boolean isDim();

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
	 * 
	 * @param ds
	 *            the DataSet instance
	 */
	public void setDataSet(DataSet ds);
}
