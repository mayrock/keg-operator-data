package edu.thu.keg.mobiledata.internetgraph.dbo;
import java.io.Serializable;
/**
*
* @author Yuan Bozhi
* 
*/
import java.util.Hashtable;
import java.util.Vector;

public class Host implements Serializable{
	static final long serialVersionUID=200003L;
	final String ADDR;
	int UrlCount;
	Hashtable<String, UserHost> ConnectedUser;//all connected users
	Vector<Double> Eigenvector=new Vector<Double>();//record User's connection vector
	public Host(String addr)
	{
		ADDR=addr;
		UrlCount=0;
		ConnectedUser=new Hashtable<String, UserHost>();
		
	}
	public void addConnectedUser(String cu, UserHost uh)
	{
		ConnectedUser.put(cu, uh);
		UrlCount++;
	}
}
