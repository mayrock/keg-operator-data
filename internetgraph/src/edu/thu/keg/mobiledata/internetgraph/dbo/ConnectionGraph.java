package edu.thu.keg.mobiledata.internetgraph.dbo;
/**
*
* @author Yuan Bozhi
* 
*/
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionGraph {
	final String graphID;
	final Logger log;
	Hashtable<String, User> graphUsers;//user's hash
	Hashtable<String, Host> graphHosts;//host's hash
	Hashtable<String, UserHost> graphEdges;//edge's hash
	public ConnectionGraph(String id)
	{
		graphID=id;
		log=Logger.getLogger(id);
		initialGraph();
	}
	//initial the graph
	private void initialGraph()
	{
		graphUsers= new Hashtable<String, User>();
		graphHosts= new Hashtable<String, Host>();
		graphEdges= new Hashtable<String, UserHost>();
	}
	//clear the graph
	public void clear()
	{
		graphUsers.clear();
		graphHosts.clear();
		graphEdges.clear();
	}
	//insert a User Node
	public boolean insertUserNode(User u)
	{
		if(!graphUsers.containsKey(u.IMEI))
		{
			graphUsers.put(u.IMEI, u);
			log.log(Level.INFO, "added a User Node: "+u.IMEI);
			return true;
		}
		return false;
	}
	//insert a Host Node
	public boolean insertHostNode(Host h)
	{
		if(!graphHosts.containsKey(h.ADDR))
		{
			graphHosts.put(h.ADDR, h);
			log.log(Level.INFO, "added a Host Node: "+h.ADDR);
			return true;
		}
		return false;
	}
	//insert a relationship
	public boolean insertEdge(String imei,String addr, String location,int timeSegment,String userAgent)
	{
		if(imei==null || addr==null )
		{
			log.log(Level.INFO,"imei or addr is null");
			return false;
		}
		UserHost tempUH=null;
		if(graphUsers.containsKey(imei) && graphHosts.containsKey(addr))//exist the UserNode & HostNode
		{
			
			if(graphEdges.contains(imei+addr))//already exist an edge
			{
				
				tempUH=graphEdges.get(imei+addr);
				tempUH.insertConnnection(location, timeSegment, userAgent);
				log.log(Level.INFO, "在已有双节点添加边-已经存在一条边");
				return true;
			}
			else//not exit any edges
			{
				User u=graphUsers.get(imei);
				Host h=graphHosts.get(addr);
				tempUH= new UserHost(u, h);
				tempUH.insertConnnection(location, timeSegment, userAgent);
				u.addConnectedHost(tempUH.toString(), tempUH);
				h.addConnectedUser(tempUH.toString(), tempUH);
				graphEdges.put(tempUH.toString(), tempUH);//add to the graphEdges
				log.log(Level.INFO, "在已有双节点添加边-不存在任何一条边");
				return true;
			}
		}
		else if(graphUsers.containsKey(imei))
		{
			User u=graphUsers.get(imei);
			Host h=new Host(addr);
			this.insertHostNode(h);
			tempUH= new UserHost(u, h);
			tempUH.insertConnnection(location, timeSegment, userAgent);
			u.addConnectedHost(tempUH.toString(), tempUH);
			h.addConnectedUser(tempUH.toString(), tempUH);
			graphEdges.put(tempUH.toString(), tempUH);//add to the graphEdges
			log.log(Level.INFO, "有User，无Host-添加Host节点，添加一条边");
			return true;
			
		}
		else if(graphHosts.containsKey(addr))
		{
			User u=new User(imei);
			this.insertUserNode(u);
			Host h=graphHosts.get(addr);
			tempUH= new UserHost(u, h);
			tempUH.insertConnnection(location, timeSegment, userAgent);
			u.addConnectedHost(tempUH.toString(), tempUH);
			h.addConnectedUser(tempUH.toString(), tempUH);
			graphEdges.put(tempUH.toString(), tempUH);//add to the graphEdges
			log.log(Level.INFO, "无User，有Host-添加User节点，添加一条边");
			return true;
		}
		else if(!graphUsers.containsKey(imei) &&!graphHosts.containsKey(addr))
		{
			User u=new User(imei);
			this.insertUserNode(u);
			Host h=new Host(addr);
			this.insertHostNode(h);
			tempUH= new UserHost(u, h);
			tempUH.insertConnnection(location, timeSegment, userAgent);
			u.addConnectedHost(tempUH.toString(), tempUH);
			h.addConnectedUser(tempUH.toString(), tempUH);
			graphEdges.put(tempUH.toString(), tempUH);//add to the graphEdges
			log.log(Level.INFO, "无User，无Host-添加User，Host节点，添加一条边");
			return true;
		}
		log.log(Level.INFO, "没有添加成功任何一条边");
		return false;
		
	}
	//get the user node reference
	public User getUserNode(User u)
	{
		User user=null;
		return user;
	}
	//get the host node reference
	public Host getHostNode(Host h)
	{
		Host host=null;
		return host;
	}
	//get the relationship node reference
	public UserHost getEdge(User u,Host h)
	{
		UserHost uh=null;
		return uh;
	}
	//remove a user node
	public void removeUserNode(User u)
	{
		
	}
	//remove a host node
	public void removeHostNode(Host h)
	{
		
	}
	//remove a relationship
	public void removeEdge(User u,Host h)
	{
		
	}
public static void main(String args[])
{
	ConnectionGraph app= new ConnectionGraph("test");
	app.initialGraph();
	app.insertEdge("0001", "baidu.com", "home", 1, "@0001");
	app.insertEdge("0001", "baidu.com", "home", 1, "@0002");
	app.insertEdge("0001", "baidu.com", "home", 2, "@0003");
	app.insertEdge("0002", "baidu.com", "home", 3, "@0004");
	app.insertEdge("0001", "baidu.com", "outside", 1, "@0005");
	app.insertEdge("0002", "baidu.com", "outside", 1, "@0006");
	app.insertEdge("0002", "baidu.com", "outside", 1, "@0007");
	app.insertEdge("0001", "sina.com", "home", 1, "@0008");
	app.insertEdge("0001", "sina.com", "home", 2, "@0009");
	app.insertEdge("0002", "sina.com", "home", 3, "@0010");
	app.insertEdge("0002", "baidu.com", "home", 2, "@0011");
	app.insertEdge("0002", "baidu.com", "home", 1, "@0012");
	int a=0;

}
}
