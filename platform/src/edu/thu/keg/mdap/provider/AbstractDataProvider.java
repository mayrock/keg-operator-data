/**
 * 
 */
package edu.thu.keg.mdap.provider;

/**
 * A general implementation of the interface DataProvider,
 * with some abstract methods implemented. 
 * @author Yuanchao Ma
 */
public abstract class AbstractDataProvider implements DataProvider {

	protected String connString;
	@Override
	public String getConnectionString() {
		return connString;
	}
	
	public AbstractDataProvider(String connString) {
		this.connString = connString;
	}

	
	@Override
	public String toString() {
		return this.getConnectionString();
	}
	
	

}
