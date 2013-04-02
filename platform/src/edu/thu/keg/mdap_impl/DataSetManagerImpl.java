/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.datafield.DataField;
import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap_impl.dataset.DataSetImpl;

/**
 * @author Yuanchao Ma
 *
 */
public class DataSetManagerImpl implements DataSetManager {

	private HashMap<String, DataSet> datasets = null;
	private DataProviderManager providerManager = null;
	private XStream xstream;
	
	private XStream getXstream() {
		if (xstream == null)
			xstream = new XStream(new StaxDriver());
		return xstream;
	}
	
	public DataSetManagerImpl(DataProviderManager providerManager) {
		this.providerManager = providerManager;
		try {
			loadDataSets();
		} catch (Exception ex) {
			datasets = new HashMap<String, DataSet>();
		}
	}
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSet(java.lang.String)
	 */
	@Override
	public DataSet getDataSet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSetList()
	 */
	@Override
	public Collection<DataSet> getDataSetList() {
		return datasets.values();
	}
	
	@SuppressWarnings("unchecked")
	private void loadDataSets() {
		String f = Config.getDataSetFile();
		Collection<DataSet> sets = (Collection<DataSet>)getXstream().fromXML(new File(f));
		for (DataSet ds : sets) {
			datasets.put(ds.getName(), ds);
		}
		
	}
	@Override
	public void storeDataSet(DataSet ds) {
		datasets.put(ds.getName(), ds);
		FileWriter fw;
		try {
			fw = new FileWriter(Config.getDataSetFile(), false);
			getXstream().marshal(datasets, new PrettyPrintWriter(fw));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public DataSet createDataSet(String name, String connString,
			DataField[] fields, boolean loadable) {
		DataProvider provider = providerManager.getProvider(connString);
		return new DataSetImpl(name, provider, loadable, fields);
	}
	

}
