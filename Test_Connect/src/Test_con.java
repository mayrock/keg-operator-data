import java.net.URI;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;



public class Test_con {
	public static void main(String arg[])
	{
		 ClientConfig config = new DefaultClientConfig();
		    Client client = Client.create(config);
		    WebResource service = client.resource(getBaseURI());

		System.out.println(service.path("rest").path("ds").path("getdatasets")
		        .accept(MediaType.APPLICATION_XML).get(String.class));
		
		System.out.println(service.path("rest").path("ds").path("getdatasets")
		        .accept(MediaType.APPLICATION_JSON).get(String.class));
		
		System.out.println(service.path("rest").path("ds").path("getlocation/RegionInfo3")
		        .accept(MediaType.APPLICATION_JSON).get(String.class));
		
		System.out.println(service.path("rest").path("ds").path("getstatistic/RegionInfo3")
		        .accept(MediaType.APPLICATION_JSON).get(String.class));
		
//		System.out.println(service.path("dataset").path("getdatasets")
//		        .accept(MediaType.TEXT_PLAIN).get(String.class));
//		
		
//		System.out.println(service.path("dataset").path("hello")
//		        .accept(MediaType.TEXT_PLAIN).get(String.class));
	}
	  private static URI getBaseURI() {
		    return UriBuilder.fromUri("http://10.1.1.55:8081/mdap").build();
		  }

}
