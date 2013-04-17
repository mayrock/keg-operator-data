package edu.thu.keg.mdap.restful;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.crypto.Data;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query.Operator;
import edu.thu.keg.mdap.datasetfeature.GeoFeature;
import edu.thu.keg.mdap.datasetfeature.StatisticsFeature;
import edu.thu.keg.mdap.datasetfeature.TimeSeriesFeature;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.restful.jerseyclasses.JDataset;
import edu.thu.keg.mdap.restful.jerseyclasses.JDatasetName;
import edu.thu.keg.mdap.restful.jerseyclasses.JField;
import edu.thu.keg.mdap.restful.jerseyclasses.JFieldName;
import edu.thu.keg.mdap.restful.jerseyclasses.JLocation;
import edu.thu.keg.mdap.restful.jerseyclasses.JStatistic;

/**
 * the functions of dataset's get operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsg")
public class DsGetFunctions {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset names
	 */
	@GET
	@Path("/gstdss")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<JDatasetName> getAllDataSetsNames(@Context ServletContext sc) {
		String result = "";
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {

			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager.getDataSetList();
			for (DataSet dataset : datasets) {
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				ArrayList<String> schema = new ArrayList<>();
				ArrayList<Class> type = new ArrayList<>();
				for (DataField df : dataset.getDataFields()) {
					schema.add(df.getColumnName());
				}
				dname.setSchema(schema);
				datasetsName.add(dname);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return datasetsName;
	}

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset Geo names
	 */
	@GET
	@Path("/getdeodss")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<JDatasetName> getGeoDataSetsNames(@Context ServletContext sc) {
		String result = "";
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {

			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager
					.getDataSetList(GeoFeature.class);
			for (DataSet dataset : datasets) {
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				ArrayList<String> schema = new ArrayList<>();
				for (DataField df : dataset.getDataFields()) {
					schema.add(df.getColumnName());
				}
				dname.setSchema(schema);
				datasetsName.add(dname);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return datasetsName;
	}

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset Sta names
	 */
	@GET
	@Path("/getstadss")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<JDatasetName> getStaDataSetsNames(@Context ServletContext sc) {
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {
			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager
					.getDataSetList(StatisticsFeature.class);
			for (DataSet dataset : datasets) {
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				ArrayList<String> schema = new ArrayList<>();
				for (DataField df : dataset.getDataFields()) {
					schema.add(df.getColumnName());
				}
				dname.setSchema(schema);
				datasetsName.add(dname);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return datasetsName;
	}

	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format all rs array of JDataset
	 */
	@GET
	@Path("/get/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JDataset> getDataSet(@PathParam("datasetname") String dataset,
			@Context ServletContext sc) {
		System.out.println("getDataSet " + dataset);
		List<JDataset> datasetList = new ArrayList<>();
		DataSet ds = null;
		DataContent rs = null;
		try {
			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			rs = ds.getQuery();
			rs.open();
			int i = 0;
			while (rs.next()) {
				JDataset jdataset = new JDataset();
				List<JField> fields = new ArrayList<>();
				DataField[] dfs = ds.getDataFields();
				for (DataField df : dfs) {
					JField field = new JField();
					field.setField(rs.getValue(df));
					fields.add(field);
				}
				jdataset.setField(fields);
				datasetList.add(jdataset);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasetList;
	}

	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format location array
	 */
	@GET
	@Path("/getloc/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JLocation> getDataSetLocation(
			@PathParam("datasetname") String dataset, @Context ServletContext sc) {
		System.out.println("getDataSetLocation " + dataset);
		List<JLocation> al_rs = new ArrayList<JLocation>();
		DataSet ds = null;
		DataContent rs = null;
		try {
			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			rs = ds.getQuery();
			GeoFeature gds = (GeoFeature) ds.getFeature(GeoFeature.class);
			if (gds == null)
				throw new OperationNotSupportedException(
						"can't find the geograph Exception");
			rs.open();
			int i = 0;
			while (rs.next()) {
				JLocation location = new JLocation();
				location.setTag(rs.getValue(gds.getTagField()).toString());
				location.setLatitude((double) rs.getValue(gds.getKeyFields()[0]));
				location.setLongitude((double) rs.getValue(gds.getKeyFields()[1]));
				al_rs.add(location);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return al_rs;
	}

	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format statistics array
	 */
	@GET
	@Path("/getsta/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JStatistic> getDataSetStatistic(
			@PathParam("datasetname") String dataset, @Context ServletContext sc) {
		System.out.println("getDataSetStatistic " + dataset);
		List<JStatistic> al_rs = new ArrayList<JStatistic>();
		DataSet ds = null;
		DataContent rs = null;
		try {

			Platform p = (Platform) sc.getAttribute("platform");

			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			rs = ds.getQuery();
			StatisticsFeature gds = (StatisticsFeature) ds
					.getFeature(StatisticsFeature.class);
			if (gds == null)
				throw new OperationNotSupportedException(
						"can't find the statistic Exception");
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
				System.out.println(rs.getValue(ds.getDataFields()[0])
						.toString()
						+ " "
						+ rs.getValue(ds.getDataFields()[1]).toString());
				JStatistic statistic = new JStatistic();
				ArrayList<String> keys = new ArrayList<>();
				for (DataField key : gds.getKeyFields()) {
					keys.add(rs.getValue(key).toString());
				}
				ArrayList<Double> values = new ArrayList<>();
				for (DataField value : gds.getValueFields()) {
					values.add(Double.valueOf(rs.getValue(value).toString()));
				}
				statistic.setKey(keys);
				statistic.setValue(values);
				al_rs.add(statistic);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return al_rs;
	}

	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format statistics array
	 */
	@GET
	@Path("/getstatime/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JStatistic> getDataSetStatisticTime(
			@PathParam("datasetname") String dataset, @Context ServletContext sc) {
		String result = "";
		System.out.println("getDataSetStatisticTime" + dataset);
		List<JStatistic> al_rs = new ArrayList<JStatistic>();
		DataSet ds = null;
		DataContent rs = null;
		try {

			Platform p = (Platform) sc.getAttribute("platform");

			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			rs = ds.getQuery();
			TimeSeriesFeature gds = (TimeSeriesFeature) ds
					.getFeature(TimeSeriesFeature.class);
			if (gds == null)
				throw new OperationNotSupportedException(
						"can't find the statisticTime Exception");
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 5) {
				System.out.println(rs.getValue(ds.getDataFields()[0])
						.toString()
						+ " "
						+ rs.getValue(ds.getDataFields()[1]).toString());
				JStatistic statistic = new JStatistic();
				ArrayList<String> keys = new ArrayList<>();
				for (DataField key : gds.getKeyFields()) {
					keys.add(rs.getValue(key).toString());
				}
				ArrayList<Double> values = new ArrayList<>();
				ArrayList<String> names = new ArrayList<>();
				for (DataField value : gds.getValueFields()) {
					values.add(Double.valueOf(rs.getValue(value).toString()));
				}
				statistic.setKey(keys);
				statistic.setValue(values);
				al_rs.add(statistic);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return al_rs;
	}

	/**
	 * get all of the dataset's fields
	 * 
	 * @param dataset
	 * @return a list of all fields name
	 */
	@GET
	@Path("/getdsfields/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JFieldName> getFieldNames(
			@PathParam("datasetname") String dataset, @Context ServletContext sc) {
		List<JFieldName> all_fn = new ArrayList<JFieldName>();
		DataSet ds = null;
		try {

			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			DataField[] dfs = ds.getDataFields();
			for (DataField df : dfs) {
				JFieldName jfn = new JFieldName();
				jfn.setFieldName(df.getColumnName());
				jfn.setDescription(df.getDescription());
				jfn.setIsKey(df.isKey());
				jfn.setType(df.getFieldType().name());
				all_fn.add(jfn);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return all_fn;
	}

	/**
	 * get a column form the dataset
	 * 
	 * @param dataset
	 * @param fieldname
	 * @return
	 */
	@GET
	@Path("/getds/{datasetname}/{fieldname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JField> getField(@PathParam("datasetname") String dataset,
			@PathParam("fieldname") String fieldname, @Context ServletContext sc) {
		DataSet ds = null;
		String result = "";
		System.out.println("½øÀ´¶öÁË");
		DataContent rs = null;
		List<JField> all_jf = new ArrayList<JField>();
		try {

			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			rs = ds.getQuery();
			rs.open();
			while (rs.next()) {
				JField jf = new JField();
				jf.setField(rs.getValue(ds.getField(fieldname)));
				all_jf.add(jf);
			}
			rs.close();
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return all_jf;
	}

	@GET
	@Path("/getds/{datasetname}/{fieldname}/{opr}/{value}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JDataset> getValueOfOpr(
			@PathParam("datasetname") String dataset,
			@PathParam("fieldname") String fieldname,
			@PathParam("opr") String opr, @PathParam("value") String value,
			@Context ServletContext sc) {
		System.out.println("getValueOfOpr " + dataset);
		List<JDataset> datasetList = new ArrayList<>();
		DataSet ds = null;
		DataContent rs = null;
		try {
			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			rs = ds.getQuery().where(ds.getField(fieldname),
					Operator.parse(opr), value);
			rs.open();
			int i = 0;
			while (rs.next()) {
				JDataset jdataset = new JDataset();
				List<JField> fields = new ArrayList<>();
				DataField[] dfs = ds.getDataFields();
				for (DataField df : dfs) {
					JField field = new JField();
					field.setField(rs.getValue(df));
					fields.add(field);
				}
				jdataset.setField(fields);
				datasetList.add(jdataset);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasetList;
	}

	@GET
	@Path("/hello")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getString() {
		String a = "{\"city\":\"helloworld_json\"}";
		System.out.println(a);
		return a;
	}

	@GET
	@Path("/hello")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getString2() {
		System.out.println("{helloworld_json}");
		return "{\"city\":\"Beijing\",\"street\":\" Chaoyang Road \",\"postcode\":100025}";
	}
}
