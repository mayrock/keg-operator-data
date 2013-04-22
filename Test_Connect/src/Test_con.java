import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;



public class Test_con {
	public static void main(String arg[])
	{
		 ClientConfig config = new DefaultClientConfig();
		 
		    Client client = Client.create(config);
		    WebResource service = client.resource(getBaseURI());

//		System.out.println(service.path("rest").path("ds").path("getdatasets")
//		        .accept(MediaType.APPLICATION_XML).get(String.class));
//		
//		System.out.println(service.path("rest").path("dsg").path("getdss")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
		
//		System.out.println(service.path("rest").path("ds").path("getlocation/RegionInfo3")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		
//		System.out.println(service.path("rest").path("ds").path("/getstatistic/FilteredByCT_Domain")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		System.out.println(service.path("rest").path("ds").path("/getlocation/RegionInfo2")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		System.out.println(service.path("rest").path("ds").path("getstadatasets")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		System.out.println(service.path("rest").path("ds").path("getgeodatasets")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		System.out.println(service.path("rest").path("dsg").path("/getdsfds/RegionInfo2")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		System.out.println(service.path("rest").path("dsg").path("/getds/RegionInfo2/SiteName")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		System.out.println(service.path("rest").path("dsg").path("/getds/RegionInfo2")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));		
		System.out.println(service.path("rest").path("dsg").path("/getds/RegionInfo2/SiteName/=/”—“Í“Ω‘∫")
		        .accept(MediaType.APPLICATION_JSON).get(String.class));	
//			System.out.println(service.path("rest").path("dsg").path("hello")
//			        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		    System.out.println(service.path("rest").path("dsg").path("jsonp")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
//		
//		JSONObject job= new JSONObject();
//		try {
//			job.put("name", "asd");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		    service.path("rest").path("dsd").path("/rmds/sa").delete();
//		    ClientResponse response = service.path("rest").path("dsu").path("/upds/a/b")
//		        .accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, job);
//System.out.println(response.getStatus());
		
//		System.out.println(service.path("rest").path("dsd").path("/rmds/a")
//		        .accept(MediaType.APPLICATION_JSON).get(String.class));
	}
	  private static URI getBaseURI() {
		    return UriBuilder.fromUri("http://10.1.1.55:8081/mdap").build();
		  }

}
