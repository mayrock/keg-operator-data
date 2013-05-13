/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.IOException;
import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datamodel.AggregatedDataField;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.AggregatedDataField.AggrFunction;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.Query.Operator;
import edu.thu.keg.mdap.datamodel.Query.Order;
import edu.thu.keg.mdap.datasetfeature.DataSetFeature;
import edu.thu.keg.mdap.datasetfeature.DataSetFeatureType;
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
		return DataSetManagerImpl.getInstance();
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.Platform#getDataProviderManager()
	 */
	@Override
	public DataProviderManager getDataProviderManager() {
		return DataProviderManagerImpl.getInstance();
	}
	
	
	public void crud() {
//		Platform p = new PlatformImpl(
//				"C:\\Users\\ybz\\GitHub\\keg-operator-data\\platform\\config.xml");
		// Construct a new dataset
		DataProvider provider = getDataProviderManager().getDefaultSQLProvider("BeijingData");
		
		
		DataField[] fields = new DataField[2];
		fields[0] = new GeneralDataField("WebsiteId", FieldType.Int, "", FieldFunctionality.ID );
		fields[1] = new GeneralDataField("URL", FieldType.Double, "", FieldFunctionality.Value );
		getDataSetManager().createDataSet("WebsiteId_URL","myc", "Website info", 
				provider, true, fields);
		
		fields = new DataField[4];
		fields[0] = new GeneralDataField("Region", FieldType.Int, "", FieldFunctionality.ID );
		fields[1] = new GeneralDataField("Name", FieldType.ShortString, "", FieldFunctionality.Value );
		fields[2] = new GeneralDataField("Latitude", FieldType.Double, "", FieldFunctionality.Latitude );
		fields[3] = new GeneralDataField("Longitude", FieldType.Double, "", FieldFunctionality.Longitude );
		getDataSetManager().createDataSet("RegionInfo3","myc", "Region info 3",
				provider, true, fields);
		
		fields = new DataField[5];
		fields[0] = new GeneralDataField("SiteId", FieldType.Int, "", FieldFunctionality.ID );
		fields[1] = new GeneralDataField("SiteName", FieldType.ShortString, "", FieldFunctionality.Value );
		fields[2] = new GeneralDataField("Latitude", FieldType.Double, "", FieldFunctionality.Latitude );
		fields[3] = new GeneralDataField("Longitude", FieldType.Double, "", FieldFunctionality.Longitude );
		fields[4] = new GeneralDataField("Region", FieldType.Int, "", FieldFunctionality.Value );
		getDataSetManager().createDataSet("RegionInfo2","myc", "Region info 2",
				provider, true, fields);
		
		fields = new DataField[6];
		fields[0] = new GeneralDataField("Domain", FieldType.LongString, 
				"Domain", FieldFunctionality.ID);
		fields[1] = new GeneralDataField("DayCount", FieldType.Int, 
				"appear days of this domain", FieldFunctionality.Count);
		fields[2] = new GeneralDataField("HourCount", FieldType.Int, 
				"appear hours of this domain", FieldFunctionality.Count);
		fields[3] = new GeneralDataField("LocCount", FieldType.Int, 
				"appear locations of this domain", FieldFunctionality.Count);
		fields[4] = new GeneralDataField("UserCount", FieldType.Int, 
				"number of users visiting this domain", FieldFunctionality.Count);
		fields[5] = new GeneralDataField("TotalCount", FieldType.Int, 
				"total visits of this domain", FieldFunctionality.Count);
		getDataSetManager().createDataSet("FilteredByCT_Domain", "myc",
				"Domain statistics", provider, false, fields);
		
		fields = new DataField[2];
		fields[0] = new GeneralDataField("ContentType", FieldType.LongString, 
				"Content Type of websites", FieldFunctionality.ID);
		fields[1] = new GeneralDataField("times", FieldType.Int, 
				"appear times of the ContentType", FieldFunctionality.Count);
		getDataSetManager().createDataSet("DataAggr_ContentTypes_Up90", "myc",
				"Top 90% Content Type distribution", provider, true, fields);
		
		fields = new DataField[4];
		fields[0] = new GeneralDataField("Imsi", FieldType.ShortString, 
				"User IMSI", FieldFunctionality.ID);
		fields[1] = new GeneralDataField("WebsiteCount", FieldType.Int, 
				"Total count of visited websites", FieldFunctionality.Count);
		fields[2] = new GeneralDataField("RegionCount", FieldType.Int, 
				"Total count of appeared regions", FieldFunctionality.Count);
		fields[3] = new GeneralDataField("TotalCount", FieldType.Int, 
				"Total count of requests", FieldFunctionality.Count);
		
		getDataSetManager().createDataSet("slot_Imsi_All", "myc",
				"User statistics by time slot", provider, true, fields);
		
//		fields = new DataField[2];
//		fields[0] = new GeneralDataField("ContentType", FieldType.LongString, 
//				"Content Type of websites", true);
//		fields[1] = new GeneralDataField("times", FieldType.Int, 
//				"appear times of the ContentType", false);
//		DataSet tds2 = p.getDataSetManager().createDataSet("New_Test", 
//				"Top 90% Content Type distribution", provider, fields, true,
//				new StatisticsFeature(fields[0], fields[1]));
//		
//		try {
//			tds2.writeData(tds.getQuery());
//		} catch (OperationNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DataProviderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


//		//remove a dataset
//		try {
//			p.getDataSetManager().removeDataSet(tds2);
//		} catch (DataProviderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			getDataSetManager().saveChanges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (DataSet ds : getDataSetManager().getDataSetList()) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}
	}
	public static void main(String[] args) {
		
		PlatformImpl p = new PlatformImpl(
				"config.xml");
		p.crud();
		p.query();
	}
	private void query() {
		//Get a dataset
		for (DataSet ds : getDataSetManager().getDataSetList(DataSetFeatureType.GeoFeature)) {		
			//Read data from a dataset
			try {
				System.out.println(ds.getDescription());
				DataContent q = ds.getQuery();
//				GeoDataSet gds = (GeoDataSet)ds.getFeature(GeoDataSet.class);
				q.open();
				int count = 0;
				DataSetFeature feature = ds.getFeature(DataSetFeatureType.GeoFeature);
				while (q.next()) {
					count ++;
					System.out.println(
							q.getValue(feature.getKeyFields()[0]).toString()
							+ " " + q.getValue(feature.getKeyFields()[1]).toString()
							+ " " + q.getValue(feature.getValueFields()[0]).toString());
				}
				q.close();
				System.out.println(count);
			} catch (DataProviderException | OperationNotSupportedException ex) {
				ex.printStackTrace();
			}
		}
		DataSet ds = getDataSetManager().getDataSet("RegionInfo2");
		try {
			System.out.println(ds.getDescription());
			DataField aggr = new AggregatedDataField(ds.getField("SiteId"), 
					AggrFunction.COUNT, "TotalCount");
			DataContent q = ds.getQuery()
					.select(aggr,  ds.getField("Region")).orderBy("TotalCount", Order.ASC)
					.where("Region", Operator.GT,100).where("TotalCount", Operator.GT, 1);
//			GeoDataSet gds = (GeoDataSet)ds.getFeature(GeoDataSet.class);
//			System.out.println(q.ge)
			Query qq = (Query)q;
			System.out.println(qq.getProvider().getQueryString(qq));
			q.open();
			int count = 0;
			while (q.next()) {
				count ++;
				System.out.println(
						q.getValue(q.getFields()[0]).toString()
						+ " " + q.getValue(q.getFields()[1]).toString());
			}
			q.close();
			System.out.println(count);
		} catch (DataProviderException | OperationNotSupportedException ex) {
			ex.printStackTrace();
		}
		ds = getDataSetManager().getDataSet("RegionInfo2");
		System.out.println("ss".equals(new String("ss")));
		
		
		
	}
}
