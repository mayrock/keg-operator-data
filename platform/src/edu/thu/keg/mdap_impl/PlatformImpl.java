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
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datasetfeature.DataSetFeature;
import edu.thu.keg.mdap.datasetfeature.GeoFeature;
import edu.thu.keg.mdap.datasetfeature.StatisticsFeature;
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
		
		
		DataField[] fields = new DataField[2];
		fields[0] = new GeneralDataField("WebsiteId", FieldType.Int, "", true );
		fields[1] = new GeneralDataField("URL", FieldType.Double, "", false );
		DataSet tds = p.getDataSetManager().createDataSet("WebsiteId_URL", "Website info", 
				provider, fields, true);
		
		fields = new DataField[4];
		fields[0] = new GeneralDataField("Region", FieldType.Int, "", true );
		fields[1] = new GeneralDataField("Name", FieldType.ShortString, "", false );
		fields[2] = new GeneralDataField("Latitude", FieldType.Double, "", false );
		fields[3] = new GeneralDataField("Longitude", FieldType.Double, "", false );
		tds = p.getDataSetManager().createDataSet("RegionInfo3", "Region info 3",
				provider, fields, true,
				new GeoFeature(fields[2], fields[3], fields[1], null, false));
		
		fields = new DataField[3];
		fields[0] = new GeneralDataField("SiteName", FieldType.ShortString, "", false );
		fields[1] = new GeneralDataField("Latitude", FieldType.Double, "", false );
		fields[2] = new GeneralDataField("Longitude", FieldType.Double, "", false );
		tds = p.getDataSetManager().createDataSet("RegionInfo2", "Region info 2",
				provider, fields, true,
				new GeoFeature(fields[1], fields[2], fields[0], null, false));
		
		fields = new DataField[6];
		fields[0] = new GeneralDataField("Domain", FieldType.LongString, 
				"Domain", true);
		fields[1] = new GeneralDataField("DayCount", FieldType.Int, 
				"appear days of this domain", false);
		fields[2] = new GeneralDataField("HourCount", FieldType.Int, 
				"appear hours of this domain", false);
		fields[3] = new GeneralDataField("LocCount", FieldType.Int, 
				"appear locations of this domain", false);
		fields[4] = new GeneralDataField("UserCount", FieldType.Int, 
				"number of users visiting this domain", false);
		fields[5] = new GeneralDataField("TotalCount", FieldType.Int, 
				"total visits of this domain", false);
		DataSetFeature feature = new StatisticsFeature(
				new DataField[]{fields[0]},
				new DataField[]
						{fields[1],fields[2],fields[3],fields[4],fields[5]});
		tds = p.getDataSetManager().createDataSet("FilteredByCT_Domain", 
				"Domain statistics", provider, fields, false, feature);
		
		fields = new DataField[2];
		fields[0] = new GeneralDataField("ContentType", FieldType.LongString, 
				"Content Type of websites", true);
		fields[1] = new GeneralDataField("times", FieldType.Int, 
				"appear times of the ContentType", false);
		tds = p.getDataSetManager().createDataSet("DataAggr_ContentTypes_Up90", 
				"Top 90% Content Type distribution", provider, fields, true,
				new StatisticsFeature(fields[0], fields[1]));
		
		fields = new DataField[2];
		fields[0] = new GeneralDataField("ContentType", FieldType.LongString, 
				"Content Type of websites", true);
		fields[1] = new GeneralDataField("times", FieldType.Int, 
				"appear times of the ContentType", false);
		DataSet tds2 = p.getDataSetManager().createDataSet("New_Test", 
				"Top 90% Content Type distribution", provider, fields, true,
				new StatisticsFeature(fields[0], fields[1]));
		
		try {
			tds2.writeData(tds.getQuery());
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Get a dataset
		for (DataSet ds : p.getDataSetManager().getDataSetList(GeoFeature.class)) {		
			//Read data from a dataset
			try {
				System.out.println(ds.getDescription());
				DataContent q = ds.getQuery();
//				GeoDataSet gds = (GeoDataSet)ds.getFeature(GeoDataSet.class);
				q.open();
				int count = 0;
				while (q.next()) {
					count ++;
//					System.out.println(
//							q.getValue(gds.getTagField()).toString()
//							+ " " + q.getValue(gds.getLatitudeField()).toString()
//							+ " " + q.getValue(gds.getLongitudeField()).toString());
				}
				q.close();
				System.out.println(count);
			} catch (DataProviderException | OperationNotSupportedException ex) {
				ex.printStackTrace();
			}
		}
		
		for (DataSet ds : p.getDataSetManager().getDataSetList(StatisticsFeature.class)) {
			try {
				System.out.println(ds.getDescription());
				int count = 0;
				Query q = ds.getQuery();
				q.open();
				while (q.next()) {
					count ++;
				}
				q.close();
				System.out.println(count);
			} catch (DataProviderException | OperationNotSupportedException ex) {
				ex.printStackTrace();
			}
		}
		//remove a dataset
		try {
			p.getDataSetManager().removeDataSet(tds2);
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			p.getDataSetManager().saveChanges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
