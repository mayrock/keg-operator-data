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
		Platform p = new PlatformImpl(
				"C:\\Users\\ybz\\GitHub\\keg-operator-data\\platform\\config.xml");
//		Platform p = new PlatformImpl(
//				"D:\\GitHub\\keg-operator-data\\platform\\config.xml");
		// Construct a new dataset
		DataProvider provider = p.getDataProviderManager().getDefaultSQLProvider("BeijingData");
		DataSet[] dss = new DataSet[3];
		
		DataField[] fields = new DataField[2];
		fields[0] = new GeneralDataField("WebsiteId", Integer.class, "", true );
		fields[1] = new GeneralDataField("URL", String.class, "", false );
		dss[0] = p.getDataSetManager().createDataSet("WebsiteId_URL", "Website info", 
				provider, fields, true);		
		
		fields = new DataField[4];
		fields[0] = new GeneralDataField("Region", Integer.class, "", true );
		fields[1] = new GeneralDataField("Name", String.class, "", false );
		fields[2] = new GeneralDataField("Latitude", Double.class, "", false );
		fields[3] = new GeneralDataField("Longitude", Double.class, "", false );
		dss[1] = p.getDataSetManager().createDataSet("RegionInfo3", "Region info 3",
				provider, fields, true);
		dss[1].addFeature(new GeneralGeoDataSet(fields[2], fields[3], fields[1], false));
		
		fields = new DataField[3];
		fields[0] = new GeneralDataField("SiteName", String.class, "", false );
		fields[1] = new GeneralDataField("Latitude", Double.class, "", false );
		fields[2] = new GeneralDataField("Longitude", Double.class, "", false );
		dss[2] = p.getDataSetManager().createDataSet("RegionInfo2", "Region info 2",
				provider, fields, true);
		dss[2].addFeature(new GeneralGeoDataSet(fields[1], fields[2], fields[0], false));
		
		//Store a dataset
		for (DataSet ds : dss) {
			p.getDataSetManager().storeDataSet(ds);
		}
		
		//Get a dataset
		for (DataSet ds : p.getDataSetManager().getDataSetList(GeoDataSet.class)) {		
			//Read data from a dataset
			try {
				System.out.println(ds.getDescription());
				DataContent q = ds.getQuery();
				GeoDataSet gds = (GeoDataSet)ds.getFeatures().get(GeoDataSet.class);
				q.open();
				int count = 0;
				while (q.next()) {
					count ++;
					System.out.println(
							q.getValue(gds.getTagField()).toString()
							+ " " + q.getValue(gds.getLatitudeField()).toString()
							+ " " + q.getValue(gds.getLongitudeField()).toString());
				}
				q.close();
				System.out.println(count);
			} catch (DataProviderException | OperationNotSupportedException ex) {
				ex.printStackTrace();
			}
		}
	}

}
