/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datafield.DataField;
import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap_impl.datafield.GeneralDataField;

/**
 * @author Yuanchao Ma
 *
 */
public class PlatformImpl implements Platform {
	
	public PlatformImpl(String file) {
		try {
			Config.init(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.Platform#getDataSetManager()
	 */
	@Override
	public DataSetManager getDataSetManager() {
		return new DataSetManagerImpl(getDataProviderManager());
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.Platform#getDataProviderManager()
	 */
	@Override
	public DataProviderManager getDataProviderManager() {
		return new DataProviderManagerImpl();
	}
	
	
	public static void main(String[] args) {
		Platform p = new PlatformImpl(
				"C:\\Users\\myc\\git\\mayrock\\keg-operator-data\\platform\\config.xml");
		DataField[] fields = new GeneralDataField[2];
		fields[0] = new GeneralDataField("WebsiteId", Integer.class, "", true );
		fields[1] = new GeneralDataField("URL", String.class, "", false );
		DataSet ds = p.getDataSetManager().createDataSet("WebsiteId_URL", 
				"jdbc:sqlserver://localhost:1433;databaseName=BeijingData;integratedSecurity=true;",
				fields, true);
		p.getDataSetManager().storeDataSet(ds);
		
		try {
			ResultSet rs = ds.getResultSet();
			while (rs.next()) {
				System.out.println(fields[0].getValue(rs).toString() + " " + fields[1].getValue(rs).toString());
			}
			ds.closeResultSet();
		} catch (DataProviderException | SQLException | OperationNotSupportedException ex) {
			ex.printStackTrace();
		}
	}

}
