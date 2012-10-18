package edu.thu.keg.mobiledata.internetgraph.dbo;
import java.io.Serializable;
import java.util.ArrayList;
/**
*
* @author Yuan Bozhi
* 
*/
import java.util.Hashtable;

public class UserHost implements Serializable{
	static final long serialVersionUID=200001L;
	public User user;
	public Host host;
	final String ID;
	public int TotalCount;
	public Hashtable<String, ConnectionDetail> ConnectionTable;//connection details
	public UserHost(User u,Host h)
	{
		ID=u.IMEI+h.ADDR;
		user=u;
		host=h;
		TotalCount=0;
		ConnectionTable= new Hashtable<String, ConnectionDetail>();
	}
	public boolean insertConnnection(String location,int timeSegment,String userAgent)
	{
		String LTs=location+","+timeSegment;
		if(ConnectionTable.containsKey(LTs))
		{
			ConnectionTable.get(LTs).ConnectionCount++;
			ConnectionTable.get(LTs).UserAgent.add(userAgent);
		}
		else
		{
			ConnectionTable.put(LTs, new ConnectionDetail(location, timeSegment, userAgent));
			ConnectionTable.get(LTs).ConnectionCount++;
		}
		this.TotalCount++;
		return false;
	}
	@Override
	public String toString()
	{
		String s=user.IMEI+host.ADDR;
		return s;
	}
}
class ConnectionDetail implements Serializable{
	static final long serialVersionUID=300001L;
	String Location;//the location info of the user connected the host
	int TimeSegment;//the timeSegment of the user connected the host
	int ConnectionCount;//the number of the user connected the host in particular location & timeSegment
//	int UrlCount;//the number of host's branches 
	ArrayList<String> UserAgent;//UserAgent
	ConnectionDetail(String location,int timeSegment,String userAgent)
	{
		Location=location;
		TimeSegment=timeSegment;
		ConnectionCount=0;
		UserAgent=new ArrayList<String>();
		UserAgent.add(userAgent);
	}
	@Override
	public String toString()
	{
		return Location+Integer.toString(TimeSegment);
	}
	
}
