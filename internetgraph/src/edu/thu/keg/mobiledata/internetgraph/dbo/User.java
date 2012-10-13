package edu.thu.keg.mobiledata.internetgraph.dbo;
/**
*
* @author Yuan Bozhi
* 
*/
import java.util.Hashtable;

public class User {
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
