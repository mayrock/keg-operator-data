package edu.thu.keg.mobiledata.internetgraph.dbo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
	public int day;
	public Hashtable<String, ConnectionDetail> ConnectionTable;//connection details
	
	public HashMap<String, Integer> LocationConTable;
	public HashMap<String, Integer> TimeConTable;
	public UserHost(User u,Host h)
	{
		ID=u.IMEI+h.ADDR;
		user=u;
		host=h;
		TotalCount=0;
		day=0;
//		ConnectionTable= new Hashtable<String, ConnectionDetail>();
		LocationConTable=new HashMap<String, Integer>();
		TimeConTable=new HashMap<String, Integer>();
	}
	public boolean insertConnnection(String location, int day, int timeSegment,String userAgent)
	{
		String LDTs=location+" "+String.valueOf(day)+" "+String.valueOf(timeSegment);
		String DTs=String.valueOf(day)+" "+String.valueOf(timeSegment);
		//同一天同一时间同一地点
		if(LocationConTable.containsKey(LDTs))
			LocationConTable.put(LDTs, LocationConTable.get(LDTs)+1);
		else
			LocationConTable.put(LDTs, 1);
		//同一天同一时间
		if(TimeConTable.containsKey(DTs))
			TimeConTable.put(DTs, TimeConTable.get(DTs)+1);
		else
			TimeConTable.put(DTs, 1);
		
//**暂时注释		
//		if(ConnectionTable.containsKey(LTs))
//		{
//			ConnectionTable.get(LTs).ConnectionCount++;
//			ConnectionTable.get(LTs).UserAgent.add(userAgent);
//		}
//		else
//		{
//			ConnectionTable.put(LTs, new ConnectionDetail(location, timeSegment, userAgent));
//			ConnectionTable.get(LTs).ConnectionCount++;
//		}
		this.TotalCount++;
		this.host.TotalConnectNum++;
		this.user.TotalConnectNum++;
		return true;
	}
	@Override
	public String toString()
	{
		String s=user.IMEI+" "+host.ADDR;
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
		return Location+" "+Integer.toString(TimeSegment);
	}
	
}
