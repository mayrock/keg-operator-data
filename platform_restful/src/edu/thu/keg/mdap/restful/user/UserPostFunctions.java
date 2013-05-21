package edu.thu.keg.mdap.restful.user;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
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

import edu.thu.keg.mdap.user.User;
import edu.thu.keg.mdap_impl.user.UserManagerImpl;

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
			@FormParam("userid") String userid,
			@FormParam("username") String username,
			@FormParam("password") String password) {
		System.out.println("POST");
		JSONObject job = null;
		try {
			User user = new User(userid, username, password, User.BROWSER);
			boolean status = UserManagerImpl.getInstance().addUser(user);
			job = new JSONObject();
			job.put("status", status);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(job.toString());
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);

	}
}
