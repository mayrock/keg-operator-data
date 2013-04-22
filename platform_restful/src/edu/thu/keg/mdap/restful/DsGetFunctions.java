package edu.thu.keg.mdap.restful;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.crypto.Data;

import com.sun.jersey.api.json.JSONWithPadding;

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
import edu.thu.keg.mdap.restful.jerseyclasses.JGeograph;
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
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset names
	 */
	@GET
	@Path("/getdss")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public JSONWithPadding getDatasetsNames(
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		System.out.println("getDatasetsNames " + uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {

			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager.getDataSetList();
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
		return new JSONWithPadding(new GenericEntity<List<JDatasetName>>(
				datasetsName) {
		}, callback);

	}

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset Geo names
	 */
	@GET
	@Path("/getgeodss")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<JDatasetName> getGeoDatasetsNames() {
		System.out.println("getGeoDatasetsNames " + uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {

			Platform p = (Platform) servletcontext.getAttribute("platform");
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
	public List<JDatasetName> getStaDatasetsNames() {
		System.out.println("getStaDatasetsNames " + uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
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
	@Path("/getds/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JDataset> getDataset(@PathParam("datasetname") String dataset) {
		System.out.println("getDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JDataset> datasetList = new ArrayList<>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
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
	@Path("/getgeods/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JGeograph> getGeoDataset(
			@PathParam("datasetname") String dataset) {
		System.out.println("getLocDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		System.out.println("getDataSetLocation " + dataset);
		List<JGeograph> al_rs = new ArrayList<JGeograph>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
			GeoFeature gds = (GeoFeature) ds.getFeature(GeoFeature.class);
			if (gds == null)
				throw new OperationNotSupportedException(
						"can't find the geograph Exception");
			rs.open();
			while (rs.next()) {
				JGeograph location = new JGeograph();
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
	@Path("/getstads/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JStatistic> getStaDataset(
			@PathParam("datasetname") String dataset) {
		System.out.println("getStaDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JStatistic> al_rs = new ArrayList<JStatistic>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
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
	@Path("/getstatds/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JStatistic> getStaTimeDataset(
			@PathParam("datasetname") String dataset) {
		System.out.println("getStaTimeDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JStatistic> al_rs = new ArrayList<JStatistic>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
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
	@Path("/getdsfds/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JFieldName> getDatasetFieldsNames(
			@PathParam("datasetname") String dataset) {
		System.out.println("getDatasetFieldsNames " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JFieldName> all_fn = new ArrayList<JFieldName>();
			try {

			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
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
	public List<JField> getDatasetField(
			@PathParam("datasetname") String dataset,
			@PathParam("fieldname") String fieldname) {
		System.out.println("getDatasetField " + dataset + " " + fieldname + " "
				+ uriInfo.getAbsolutePath());
		System.out.println("getField " + dataset);
		List<JField> all_jf = new ArrayList<JField>();
		try {

			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataField df = ds.getField(fieldname);
			DataContent rs = ds.getQuery();
			rs.open();
			while (rs.next()) {
				JField jf = new JField();
				jf.setField(rs.getValue(df));
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
	public List<JDataset> getDatasetValueOfOpr(
			@PathParam("datasetname") String dataset,
			@PathParam("fieldname") String fieldname,
			@PathParam("opr") String opr, @PathParam("value") String value) {
		System.out.println("getDatasetValueOfOpr " + dataset + " " + fieldname
				+ " " + opr + " " + value + " " + uriInfo.getAbsolutePath());
		List<JDataset> datasetList = new ArrayList<>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery().where(ds.getField(fieldname),
					Operator.parse(opr), value);
			rs.open();
			while (rs.next()) {
				JDataset jdataset = new JDataset();
				List<JField> fields = new ArrayList<JField>();
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
	@Produces({ MediaType.APPLICATION_JSON })
	public String getString() {
		String a = "{\"city\":\"helloworld_json\"}";
		System.out.println(a);
		return a;
	}

	@GET
	@Path("/hello")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getString2() {
		System.out.println("{helloworld_json}");
		return "{helloworld_json}";
	}

	@GET
	@Path("/jsonp")
	@Produces({ MediaType.TEXT_PLAIN,MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public JSONWithPadding readAllP(
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback,
			@QueryParam("acronym") String acronym,
			@QueryParam("title") String title,
			@QueryParam("competition") String competition) {
		String a = "{\"city\":\"Beijing\",\"street\":\" Chaoyang Road \",\"postcode\":100025}";
		System.out.println(a);
		return new JSONWithPadding(new GenericEntity<String>(a) {
		}, callback);
	}
}
