package edu.thu.keg.mobiledata.internetgraph.dbo;
import java.io.Serializable;
/**
*
* @author Yuan Bozhi
* 
*/
import java.util.Hashtable;

public class User implements Serializable{
	static final long serialVersionUID=200002L;
	final String IMEI;
	int TotalCount;
	String PhoneBrand;//phone's brand
	Hashtable<String, UserHost> ConnectedHost;//all connected hosts
	public User(String imei)
	{
		IMEI=imei;
		TotalCount=0;
		ConnectedHost=new Hashtable<String, UserHost>();
	}
	public void addConnectedHost(String chID,UserHost uh)
	{
		ConnectedHost.put(chID, uh);
		TotalCount++;
	}
}
