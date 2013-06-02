package edu.thu.keg.mdap.restful.user;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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

import edu.thu.keg.mdap.management.ManagementPlatform;
import edu.thu.keg.mdap.management.impl.user.UserManagerImpl;
import edu.thu.keg.mdap.management.provider.IllegalUserManageException;
import edu.thu.keg.mdap.management.user.IUserManager;
import edu.thu.keg.mdap.management.user.User;


@Path("/up")
public class UserPostFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;
	@Context
	HttpServletRequest httpServletRequest;

	@POST
	@Path("/adduser")
	@Produces({ "application/javascript",MediaType.APPLICATION_JSON  })
	public JSONWithPadding addUser(
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback,
			@FormParam("userid") String userid,
			@FormParam("username") String username,
			@FormParam("password") String password) {
		System.out.println(userid + " " + username + " " + password);

		JSONObject job = new JSONObject();
		try {
			User user = new User(userid, username, password, User.BROWSER);
			ManagementPlatform mi = new ManagementPlatform();
			boolean status = mi.getUserManager().addUser(user);
			job = new JSONObject();
			job.put("status", status);
			System.out.println("添加用户成功：" + userid + " " + username);
		} catch (JSONException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalUserManageException e) {
			System.err.print(e.getMessage());
		}
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);

	}

	@POST
	@Path("/testuser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({ MediaType.APPLICATION_JSON, "application/javascript" })
	public String testUser(@FormParam("userid") String userid) {
		System.out.print(userid);
		return userid;
	}
}
