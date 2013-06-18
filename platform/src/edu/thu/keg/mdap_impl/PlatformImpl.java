/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
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
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap_impl.datamodel.DataSetImpl;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.Platform#getDataSetManager()
	 */
	@Override
	public DataSetManager getDataSetManager() {
		return DataSetManagerImpl.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.Platform#getDataProviderManager()
	 */
	@Override
	public DataProviderManager getDataProviderManager() {
		return DataProviderManagerImpl.getInstance();
	}

	public void crud() {
		// Platform p = new PlatformImpl(
		// "C:\\Users\\ybz\\GitHub\\keg-operator-data\\platform\\config.xml");
		// Construct a new dataset
		DataProvider provider = getDataProviderManager().getDefaultSQLProvider(
				"BeijingData");

		Query q = null;
		DataField[] fields = null;
		DataSet dsSite = null;
		DataView dv = null;

		fields = new DataField[2];
		fields[0] = new GeneralDataField("WebsiteId", FieldType.Int, "", true,
				FieldFunctionality.Identifier);
		fields[1] = new GeneralDataField("URL", FieldType.Double, "", false,
				FieldFunctionality.Other);
		getDataSetManager().createDataSet("WebsiteId_URL", "myc",
				DataSet.PERMISSION_PUBLIC, "网站信息", provider, true, fields);
		List<String> users = new ArrayList<>();
		users.add("wc");
		users.add("xm");
		getDataSetManager().setDataSetPermission("WebsiteId_URL", "myc",
				DataSetImpl.PERMISSION_LIMITED, users);

		fields = new DataField[4];
		fields[0] = new GeneralDataField("Region", FieldType.Int, "", true,
				FieldFunctionality.Other);
		fields[1] = new GeneralDataField("Name", FieldType.ShortString, "",
				false, FieldFunctionality.Other);
		fields[2] = new GeneralDataField("Latitude", FieldType.Double, "",
				false, FieldFunctionality.Latitude);
		fields[3] = new GeneralDataField("Longitude", FieldType.Double, "",
				false, FieldFunctionality.Longitude);
		dsSite = getDataSetManager().createDataSet("RegionInfo3", "xm",
				DataSet.PERMISSION_PUBLIC, "地理区域", provider, true, fields);

		fields = new DataField[5];
		fields[0] = new GeneralDataField("SiteId", FieldType.Int, "", true,
				FieldFunctionality.Identifier);
		fields[1] = new GeneralDataField("SiteName", FieldType.ShortString, "",
				false, FieldFunctionality.Identifier);
		fields[2] = new GeneralDataField("Latitude", FieldType.Double, "",
				false, FieldFunctionality.Latitude);
		fields[3] = new GeneralDataField("Longitude", FieldType.Double, "",
				false, FieldFunctionality.Longitude);
		fields[4] = new GeneralDataField("Region", FieldType.Int, "", false,
				false, true, FieldFunctionality.Other);
		dsSite = getDataSetManager().createDataSet("RegionInfo2", "xm",
				DataSet.PERMISSION_PUBLIC, "基站信息", provider, true, fields);

		try {
			q = dsSite
					.getQuery()
					.select(dsSite.getField("Region"),
							new AggregatedDataField(dsSite.getField("SiteId"),
									AggrFunction.COUNT, "SiteCount"))
					.orderBy("SiteCount", Order.DESC);
		} catch (OperationNotSupportedException | DataProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dv = getDataSetManager().defineView("RegionSta", "区域内基站数统计",
				DataView.PERMISSION_PUBLIC, DataFeatureType.ValueFeature, q);
		dv.setDescription(Locale.ENGLISH, "Cell tower count within regions");

		fields = new DataField[6];
		fields[0] = new GeneralDataField("Domain", FieldType.LongString,
				"Domain", true, FieldFunctionality.Identifier);
		fields[1] = new GeneralDataField("DayCount", FieldType.Int,
				"appear days of this domain", false, FieldFunctionality.Value);
		fields[2] = new GeneralDataField("HourCount", FieldType.Int,
				"appear hours of this domain", false, FieldFunctionality.Value);
		fields[3] = new GeneralDataField("LocCount", FieldType.Int,
				"appear locations of this domain", false,
				FieldFunctionality.Value);
		fields[4] = new GeneralDataField("UserCount", FieldType.Int,
				"number of users visiting this domain", false,
				FieldFunctionality.Value);
		fields[5] = new GeneralDataField("TotalCount", FieldType.Int,
				"total visits of this domain", false, FieldFunctionality.Value);
		getDataSetManager().createDataSet("FilteredByCT_Domain", "wc",
				DataSet.PERMISSION_PUBLIC, "Domain statistics", provider,
				false, fields);

		fields = new DataField[2];
		fields[0] = new GeneralDataField("ContentType", FieldType.LongString,
				"Content Type of websites", true, FieldFunctionality.Identifier);
		fields[1] = new GeneralDataField("times", FieldType.Int,
				"appear times of the ContentType", false,
				FieldFunctionality.Value);
		dsSite = getDataSetManager().createDataSet(
				"DataAggr_ContentTypes_Up90", "wc", DataSet.PERMISSION_PUBLIC,
				"ContentType分布集", provider, true, fields);

		try {
			q = dsSite.getQuery().orderBy("times", Order.DESC);
		} catch (OperationNotSupportedException | DataProviderException e1) {
			e1.printStackTrace();
		}
		dv = getDataSetManager().defineView("ContentTypeView", "ContentType分布",
				DataView.PERMISSION_PUBLIC,
				DataFeatureType.DistributionFeature, q);
		dv.setDescription(Locale.ENGLISH, "ContentType distribution");

		fields = new DataField[4];
		fields[0] = new GeneralDataField("Imsi", FieldType.ShortString,
				"User IMSI", true, FieldFunctionality.Identifier);
		fields[1] = new GeneralDataField("WebsiteCount", FieldType.Int,
				"Total count of visited websites", false,
				FieldFunctionality.Value);
		fields[2] = new GeneralDataField("RegionCount", FieldType.Int,
				"Total count of appeared regions", false,
				FieldFunctionality.Value);
		fields[3] = new GeneralDataField("TotalCount", FieldType.Int,
				"Total count of requests", false, FieldFunctionality.Value);

		dsSite = getDataSetManager().createDataSet("slot_Imsi_All", "myc",
				DataSet.PERMISSION_PUBLIC, "User statistics by time slot",
				provider, true, fields);

		try {
			DataField va = new AggregatedDataField(dsSite.getField("Imsi"),
					AggrFunction.COUNT, "UserCount");
			q = dsSite.getQuery().select(dsSite.getField("WebsiteCount"), va)
					.orderBy("WebsiteCount", Order.ASC);
			DataField[] vas = { va };
			dv = getDataSetManager().defineView("UserWebsiteCountView",
					"用户网站访问数分布", DataView.PERMISSION_PUBLIC,
					DataFeatureType.ValueFeature, q,
					dsSite.getField("WebsiteCount"), vas);
			dv.setDescription(Locale.ENGLISH, "User website count distribution");
		} catch (OperationNotSupportedException | DataProviderException e1) {
			e1.printStackTrace();
		}

		fields = new DataField[3];
		fields[0] = new GeneralDataField("SiteId", FieldType.ShortString,
				"基站ID", true, FieldFunctionality.Identifier);
		fields[1] = new GeneralDataField("ConnHour", FieldType.Int, "时间段(小时)",
				false, FieldFunctionality.Identifier);
		fields[2] = new GeneralDataField("TotalCount", FieldType.Int, "连接数",
				false, FieldFunctionality.Value);

		dsSite = getDataSetManager().createDataSet("SiteId_ConnHour", "myc",
				DataSet.PERMISSION_PUBLIC, "每小时每基站连接数", provider, true, fields);

		try {
			q = dsSite
					.getQuery()
					.select(dsSite.getField("ConnHour"),
							new AggregatedDataField(dsSite
									.getField("TotalCount"), AggrFunction.MAX,
									"TotalCount"))
					.orderBy("ConnHour", Order.ASC);
		} catch (OperationNotSupportedException | DataProviderException e1) {
			e1.printStackTrace();
		}
		getDataSetManager().defineView("ConnHourView", "每小时连接数统计",
				DataView.PERMISSION_PUBLIC, DataFeatureType.ValueFeature, q);

		// fields = new DataField[2];
		// fields[0] = new GeneralDataField("ContentType", FieldType.LongString,
		// "Content Type of websites", true);
		// fields[1] = new GeneralDataField("times", FieldType.Int,
		// "appear times of the ContentType", false);
		// DataSet tds2 = p.getDataSetManager().createDataSet("New_Test",
		// "Top 90% Content Type distribution", provider, fields, true,
		// new StatisticsFeature(fields[0], fields[1]));
		//
		// try {
		// tds2.writeData(tds.getQuery());
		// } catch (OperationNotSupportedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (DataProviderException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// //remove a dataset
		// try {
		// p.getDataSetManager().removeDataSet(tds2);
		// } catch (DataProviderException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		try {
			getDataSetManager().saveChanges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("----------------------------"
				+ getDataSetManager().getDataSetList().size());
		for (DataSet ds : getDataSetManager().getDataSetList()) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}
		System.out.println("PUBLIC-----------------------------"
				+ getDataSetManager().getPublicDataSetList().size());
		for (DataSet ds : getDataSetManager().getPublicDataSetList()) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}
		System.out.println("LIMITED-----------------------------"
				+ getDataSetManager().getLimitedDataSetList("wc").size());
		for (DataSet ds : getDataSetManager().getLimitedDataSetList("wc")) {
			System.out.println(ds.getName() + " " + ds.getDescription());
		}
		// System.out.println("myc-----------------------------"
		// + getDataSetManager().getPrivateDataSetList("myc").size());
		// for (DataSet ds : getDataSetManager().getPrivateDataSetList("myc")) {
		// System.out.println(ds.getName() + " " + ds.getDescription());
		// }
		//
		// System.out.println("wc-----------------------------"
		// + getDataSetManager().getPrivateDataSetList("wc").size());
		// for (DataSet ds : getDataSetManager().getPrivateDataSetList("wc")) {
		// System.out.println(ds.getName() + " " + ds.getDescription());
		// }
		// System.out.println();
	}

	public static void main(String[] args) {

		PlatformImpl p = new PlatformImpl("config.xml");
		p.crud();
		p.query();
	}

	private void query() {
		// Get a dataset
		for (DataView v : getDataSetManager().getDataViewList(
				DataFeatureType.ValueFeature)) {
			System.out.println(v.getName());
			System.out.println(v.getFeatureType());
			try {
				System.out.println(v.getQuery().toString());
			} catch (OperationNotSupportedException | DataProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DataView dv = getDataSetManager().getDataView("UserWebsiteCountView");
		Query q;
		try {
			q = dv.getQuery();

			q.open();
			while (q.next()) {
				System.out.println(q.getValue(dv.getKeyField()) + " "
						+ q.getValue(dv.getValueFields()[0]));
			}
			q.close();
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for (DataSet ds :
		// getDataSetManager().getDataSetList(DataFeatureType.GeoFeature)) {
		// //Read data from a dataset
		// try {
		// System.out.println(ds.getDescription());
		// DataContent q = ds.getQuery();
		// // GeoDataSet gds = (GeoDataSet)ds.getFeature(GeoDataSet.class);
		// q.open();
		// int count = 0;
		// DataFeature feature =
		// ds.getFeature(DataFeatureType.DistributionFeature);
		// while (q.next()) {
		// count ++;
		// System.out.println(
		// q.getValue(feature.getKeyFields()[0]).toString()
		// + " " + q.getValue(feature.getValueFields()[0]).toString());
		// }
		// q.close();
		// System.out.println(count);
		// } catch (DataProviderException | OperationNotSupportedException ex) {
		// ex.printStackTrace();
		// }
		// }
		// DataSet ds = getDataSetManager().getDataSet("RegionInfo2");
		// try {
		// System.out.println(ds.getDescription());
		// DataField aggr = new AggregatedDataField(ds.getField("SiteId"),
		// AggrFunction.COUNT, "TotalCount");
		// DataContent q = ds.getQuery()
		// .select(aggr, ds.getField("Region")).orderBy("TotalCount", Order.ASC)
		// .where("Region", Operator.GT,100).where("TotalCount", Operator.GT,
		// 1);
		// // GeoDataSet gds = (GeoDataSet)ds.getFeature(GeoDataSet.class);
		// // System.out.println(q.ge)
		// Query qq = (Query)q;
		// System.out.println(qq.getProvider().getQueryString(qq));
		// q.open();
		// int count = 0;
		// while (q.next()) {
		// count ++;
		// System.out.println(
		// q.getValue(q.getFields()[0]).toString()
		// + " " + q.getValue(q.getFields()[1]).toString());
		// }
		// q.close();
		// System.out.println(count);
		// } catch (DataProviderException | OperationNotSupportedException ex) {
		// ex.printStackTrace();
		// }
		// ds = getDataSetManager().getDataSet("RegionInfo2");
		// System.out.println("ss".equals(new String("ss")));

	}
}
