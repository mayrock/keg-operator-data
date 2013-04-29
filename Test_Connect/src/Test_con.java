import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class Test_con {
	public static void main(String arg[]) {
		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		System.out.println("getdss "
				+ service.path("rest").path("dsg").path("getdss")
						.accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("getgeodss "
				+ service.path("rest").path("dsg").path("getgeodss")
						.accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("getstadss "
				+ service.path("rest").path("dsg").path("getstadss")
						.accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("getds/RegionInfo2 "
				+ service.path("rest").path("dsg").path("/getds/RegionInfo2")
						.accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("getgeods/RegionInfo3 "
				+ service.path("rest").path("dsg").path("getgeods/RegionInfo3")
						.accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("getstads/FilteredByCT_Domain "
				+ service.path("rest").path("dsg")
						.path("/getstads/FilteredByCT_Domain")
						.accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("getdsfds/RegionInfo2 "
				+ service.path("rest").path("dsg")
						.path("/getdsfds/RegionInfo2")
						.accept(MediaType.APPLICATION_JSON).get(String.class));

		//  System.out.println(service.path("rest").path("dsg").path("hello")
		// .accept(MediaType.APPLICATION_JSON).get(String.class));
		// System.out.println(service.path("rest").path("dsg").path("jsonp")
		// .accept(MediaType.APPLICATION_JSON).get(String.class));
		//
		System.out.println(".........................");
		JSONObject job = new JSONObject();
		JSONArray fields = new JSONArray();
		fields.put("siteName");
		fields.put("latitude");
		fields.put("longitude");
		try {
			job.put("fields", fields);
			ClientResponse response =service.path("rest").path("dsp").path("getds/RegionInfo2")
					 .accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, job);
					System.out.println(response.getEntity(String.class));
		} catch (JSONException e) {
			
			
			e.printStackTrace();
		}
		// service.path("rest").path("dsd").path("/rmds/sa").delete();
		// ClientResponse response =
		// service.path("rest").path("dsu").path("/upds/a/b")
		// .accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, job);
		// System.out.println(response.getStatus());

		// System.out.println(service.path("rest").path("dsd").path("/rmds/a")
		// .accept(MediaType.APPLICATION_JSON).get(String.class));
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://10.1.1.55:8081/mdap").build();
	}

}
