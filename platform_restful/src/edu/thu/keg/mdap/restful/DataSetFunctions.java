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

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeoDataSet;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.restful.jerseyclasses.JDatasetName;
import edu.thu.keg.mdap.restful.jerseyclasses.JField;
import edu.thu.keg.mdap.restful.jerseyclasses.JFieldName;
import edu.thu.keg.mdap.restful.jerseyclasses.JLocation;

/**
 * the functions of dataset's operation
 * 
 * @author Law
 * 
 */
@Path("/dataset")
public class DataSetFunctions {
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
	@Path("/getdatasets")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<JDatasetName> getAllDataSetsNames_list(
			@Context ServletContext sc) {
		String result = "";
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {

			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager.getDataSetList();
			Iterator<DataSet> dataset_it = datasets.iterator();
			DataSet dt_it;
			while (dataset_it.hasNext()) {
				JDatasetName dname = new JDatasetName();
				dt_it = dataset_it.next();
				dname.setDatasetName(dt_it.getName());
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
	 * @return a json or xml format location array
	 */
	@GET
	@Path("/getdatasetlocation/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JLocation> getDataSetLocation(
			@PathParam("datasetname") String dataset, @Context ServletContext sc) {
		String result = "";
		System.out.println("进来了" + dataset);
		List<JLocation> al_rs = new ArrayList<JLocation>();
		DataSet ds = null;
		DataContent rs = null;
		try {

			Platform p = (Platform) sc.getAttribute("platform");

			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			rs = ds.getQuery();
			GeoDataSet gds = (GeoDataSet)ds.getFeatures().get(GeoDataSet.class);
			rs.open();
			while (rs.next()) {
				System.out.println(rs.getValue(ds.getDataFields()[0])
						.toString()
						+ " "
						+ rs.getValue(ds.getDataFields()[1]).toString());

				JLocation location = new JLocation();
				location.setLatitude((double)rs.getValue(gds.getLatitudeField()));
				location.setLongitude((double)rs.getValue(gds.getLongtitudeField()));
				al_rs.add(location);
			}
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (DataProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	@Path("/getdatasetfields/{datasetname}")
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
				jfn.setType(df.getDataType().getSimpleName());
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
	@Path("/getdataset/{datasetname}/{fieldname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JField> getField(@PathParam("datasetname") String dataset,
			@PathParam("fieldname") String fieldname, @Context ServletContext sc) {
		DataSet ds = null;
		String result = "";
		System.out.println("进来饿了");
		List<JField> all_jf = new ArrayList<JField>();
		try {

			Platform p = (Platform) sc.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			ds = datasetManager.getDataSet(dataset);
			ResultSet rs = null;
			int column = rs.findColumn(fieldname);
			while (rs.next()) {
				JField jf = new JField();
				jf.setField(rs.getObject(column));
				all_jf.add(jf);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {

		}
		return all_jf;
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
