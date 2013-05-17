package edu.thu.keg.mdap.restful.user;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;

@Path("/up")
public class UserPostFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;

	@POST
	@Path("/adduser")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONWithPadding addUser(
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback,
			JSONObject JContent) {
		JSONObject job = null;
		System.out.println(JContent.toString());
		try {
			job = new JSONObject();
			job.put("status", true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(job.toString());
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);
	}
}
