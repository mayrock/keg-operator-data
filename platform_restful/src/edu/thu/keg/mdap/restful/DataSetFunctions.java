package edu.thu.keg.mdap.restful;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.jerseyclass.JString;

@Path("/dataset")
public class DataSetFunctions {

	@GET
	@Path("/getdatasets")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JString> getAllDataSetsNames_list() {
		String result = "";
		List<JString> datasetsName = new ArrayList<JString>();
		try {
			String PI = ResourceBundle.getBundle("platform_initial").getString(
					"PlatformImpl");
			Class PIClass;

			PIClass = Class.forName(PI);

			Platform DMSIInstance = (Platform) PIClass.newInstance();
			DataSetManager datasetManager = DMSIInstance.getDataSetManager();
			Collection<DataSet> datasets = datasetManager.getDataSetList();
			Iterator<DataSet> dataset_it = datasets.iterator();
			while (dataset_it.hasNext()) {
				JString dname = new JString();
				datasetsName.add(dname);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return datasetsName;
	}

	@GET
	@Path("/getdatasetlocation/{dataasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JString> getDataSetLocation_list(
			@PathParam("datasetname") String datasetname) {
		String result = "";

		List<JString> al_rs = new ArrayList<JString>();
		try {
			String PI = ResourceBundle.getBundle("platform_initial").getString(
					"PlatformImpl");
			Class PIClass = Class.forName(PI);
			Platform DMSIInstance = (Platform) PIClass.newInstance();
			DataSetManager datasetManager = DMSIInstance.getDataSetManager();
			DataSet dataset = datasetManager.getDataSet(datasetname);
			ResultSet rs = dataset.getResultSet();
			while (rs.next()) {
				JString location = new JString();
				location.inputLocation(rs.getDouble("Longitude"),
						rs.getDouble("Latitude"));
				al_rs.add(location);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return al_rs;
	}

	// public String getDataSetColumn_json() {
	// String result = "";
	// return result;
	// }
	//
	// public String getDataSetColumn_xml() {
	// String result = "";
	// return result;
	// }

}
