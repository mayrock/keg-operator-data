/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.Locale;

/**
 * 
 * A general implementation of the interface DataField. This class can be
 * instantiated directly, or sub-classed to form other specific-purpose fields.
 * 
 * @author Yuanchao Ma
 */
public class GeneralDataField implements DataField {
	protected String name;
	protected String queryName;
	protected DataSet dataset;
	protected LocalizedMessage desps;
	protected boolean isKey;
	protected FieldType type;
	protected boolean allowNull;
	protected FieldFunctionality func;
	protected Query query;
	private boolean isDim;

	@Override
	public String getQueryName() {
		return this.queryName;
	}

	@Override
	public DataSet getDataSet() {
		return dataset;
	}

	public void setDataSet(DataSet dataset) {
		this.dataset = dataset;
	}

	@Override
	public String getDescription() {
		return this.desps.getMessage();
	}

	@Override
	public boolean isKey() {
		return this.isKey;
	}

	/**
	 * Construct a instance using the given name, type and description, and
	 * whether is key Initialize the fields of the object
	 * 
	 * @param name
	 *            Name of the column
	 * @param type
	 *            FieldType of this field
	 * @param description
	 *            Description of the field
	 * @param isKey
	 *            Whether the field is key
	 * @param allowNull
	 *            Whether the field allows null value
	 * @param isDim
	 *            Whether the field is dim
	 * @param func
	 *            what is the field function is
	 * @param query
	 *            whether the field is belonged to any query table
	 * @param dataset
	 *            which dataset the field belongs to
	 */
	public GeneralDataField(String name, FieldType type, String description,
			boolean isKey, boolean allowNull, boolean isDim,
			FieldFunctionality func, Query query, DataSet dataset) {
		if (isKey == true && allowNull == true)
			throw new IllegalArgumentException();
		this.name = name;
		this.queryName = name;
		this.type = type;
		this.dataset = dataset;
		this.query = query;

		this.desps = new LocalizedMessage();
		this.desps.setMessage(description);
		this.isKey = isKey;
		this.allowNull = allowNull;
		this.func = func;
	}

	/**
	 * Construct a instance using the given name, type and description, and
	 * whether is key Initialize the fields of the object
	 * 
	 * @param name
	 *            Name of the column
	 * @param type
	 *            FieldType of this field
	 * @param description
	 *            Description of the field
	 * @param func
	 *            Functionality of the field
	 */
	public GeneralDataField(String name, FieldType type, String description,
			boolean isKey, boolean allowNull, boolean isDim,
			FieldFunctionality func) {
		this.name = name;
		this.queryName = name;
		this.type = type;
		this.dataset = null;
		this.desps = new LocalizedMessage();
		this.query = null;

		this.desps.setMessage(description);
		this.isKey = isKey;
		this.allowNull = allowNull;
		this.func = func;
		this.isDim = isDim;
	}

	@Override
	public FieldType getFieldType() {
		return this.type;
	}

	@Override
	public boolean allowNull() {
		return this.allowNull;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public FieldFunctionality getFunction() {
		return this.func;
	}

	@Override
	public boolean isDim() {
		return this.isDim;
	}

	@Override
	public String getDescription(Locale locale) {
		return this.desps.getMessage(locale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		GeneralDataField other = (GeneralDataField) obj;
		if (dataset == null) {
			if (other.dataset != null)
				return false;
		} else if (!dataset.equals(other.dataset))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public Query getQuery() {
		return this.query;
	}

	@Override
	public void setQuery(Query query) {
		this.query = query;
	}

	public GeneralDataField clone() {
		return new GeneralDataField(this.getName(), this.getFieldType(),
				this.getDescription(), this.isKey(), this.allowNull(),
				this.isDim(), this.getFunction(), this.getQuery(),
				this.getDataSet());
	}
}
