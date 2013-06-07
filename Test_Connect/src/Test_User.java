import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Test_User {
	public static void main(String arg[]) {
		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		System.out.println(".........................");
		// 用户添加
		// MultivaluedMap<String, String> param = new MultivaluedMapImpl();
		// param.add("userid", "addfrom_eclipse");
		// param.add("username", "asd");
		// param.add("password", "asd");
		//
		// ClientResponse response2 = service.path("rest").path("up")
		// .path("adduser")
		// // .type(MediaType.APPLICATION_FORM_URLENCODED)
		// .accept(MediaType.TEXT_PLAIN).post(ClientResponse.class, param);
		// System.out.println(response2.getEntity(String.class));
		// fav添加
		MultivaluedMap<String, String> param = new MultivaluedMapImpl();
		param.add("userid", "aa");

		param.add("favstring", "asd");

		ClientResponse response2 = service.path("rest").path("favp")
				.path("addfav")
				// .type(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, param);
		System.out.println(response2.getEntity(String.class));
		// service.path("rest").path("dsd").path("/rmds/sa").delete();
		// ClientResponse response =
		// service.path("rest").path("dsu").path("/upds/a/b")
		// .accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, job);
		// System.out.println(response.getStatus());

		// System.out.println(service.path("rest").path("dsd").path("/rmds/a")
		// .accept(MediaType.APPLICATION_JSON).get(String.class));
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://10.1.1.55:8082/mdap").build();
	}

}