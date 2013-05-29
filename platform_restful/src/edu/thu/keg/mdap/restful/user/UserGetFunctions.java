package edu.thu.keg.mdap.restful.user;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;

@Path("/ug")
public class UserGetFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;

	@GET
	@Path("/verifyuser/{userid}/{password}")
	@Produces({ MediaType.APPLICATION_JSON, "application/javascript" })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding verifyUser(@PathParam("userid") String userid,
			@PathParam("password") String password,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		JSONObject job = null;
		try {
			job = new JSONObject();
			job.put("status", true);
			System.out.println("用户名密码正确:" + userid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("用户名密码错误:" + userid);
			e.printStackTrace();
		}
		System.out.println(job.toString());
		// return Response.created(uriInfo.getAbsolutePath()).build();
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);
	}

}
