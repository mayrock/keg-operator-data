/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.IOException;
import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.GeneralGeoDataSet;
import edu.thu.keg.mdap.datamodel.GeoDataSet;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

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
		return new DataSetManagerImpl();
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.Platform#getDataProviderManager()
	 */
	@Override
	public DataProviderManager getDataProviderManager() {
		return new DataProviderManagerImpl();
	}
	
	
	public static void main(String[] args) {
//		Platform p = new PlatformImpl(
//				"C:\\Users\\myc\\git\\mayrock\\keg-operator-data\\platform\\config.xml");
		Platform p = new PlatformImpl(
				"D:\\GitHub\\keg-operator-data\\platform\\config.xml");
		// Construct a new dataset
		DataProvider provider = p.getDataProviderManager().getDefaultSQLProvider("BeijingData");
		DataSet[] dss = new DataSet[2];
		
		DataField[] fields = new DataField[2];
		fields[0] = new GeneralDataField("WebsiteId", Integer.class, "", true );
		fields[1] = new GeneralDataField("URL", String.class, "", false );
		dss[0] = p.getDataSetManager().createDataSet("WebsiteId_URL", 
				provider, fields, true);		
		
		fields = new DataField[4];
		fields[0] = new GeneralDataField("Region", Integer.class, "", true );
		fields[1] = new GeneralDataField("Name", String.class, "", false );
		fields[2] = new GeneralDataField("Latitude", Double.class, "", false );
		fields[3] = new GeneralDataField("Longitude", Double.class, "", false );
		dss[1] = p.getDataSetManager().createDataSet("RegionInfo3", 
				provider, fields, true);
		dss[1].addFeature(new GeneralGeoDataSet(fields[2], fields[3], false));
		
		//Store a dataset
		for (DataSet ds : dss) {
			p.getDataSetManager().storeDataSet(ds);
		}
		
		//Get a dataset
		DataSet ds = p.getDataSetManager().getDataSet("RegionInfo3");
				
		//Read data from a dataset
		try {
			DataContent q = ds.getQuery();
			GeoDataSet gds = (GeoDataSet)ds.getFeatures().get(GeoDataSet.class);
			q.open();
			while (q.next()) {
				System.out.println(
						q.getValue(ds.getDataFields()[0]).toString()
						+ " " + q.getValue(ds.getDataFields()[1]).toString()
						+ " " + q.getValue(gds.getLatitudeField()).toString()
						+ " " + q.getValue(gds.getLatitudeField()).toString());
			}
			q.close();
		} catch (DataProviderException | OperationNotSupportedException ex) {
			ex.printStackTrace();
		}
	}

}
