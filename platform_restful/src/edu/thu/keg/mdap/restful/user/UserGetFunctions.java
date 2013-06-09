package edu.thu.keg.mdap.restful.user;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;

import edu.thu.keg.mdap.management.ManagementPlatform;
import edu.thu.keg.mdap.management.provider.IllegalUserManageException;
import edu.thu.keg.mdap.management.user.IUserManager;
import edu.thu.keg.mdap.management.user.User;
import edu.thu.keg.mdap.restful.dataset.DsPostFunctions;
import edu.thu.keg.mdap.restful.jerseyclasses.user.JUser;

@Path("/ug")
public class UserGetFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;
	@Context
	HttpServletRequest httpServletRequest;
	private static Logger log = Logger.getLogger(UserGetFunctions.class);

	@GET
	@Path("/verifyuser")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding verifyUser(@QueryParam("userid") String userid,
			@QueryParam("password") String password,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		log.info(uriInfo.getAbsolutePath());
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

	@GET
	@Path("/login")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding login(@QueryParam("userid") String userid,
			@QueryParam("password") String password,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		log.info(uriInfo.getAbsolutePath());
		JSONObject job = null;
		JUser juser = new JUser();
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IUserManager ium = mp.getUserManager();
			if (ium.checkPassword(userid, password)) {
				juser.setStatus(true);
				juser.setUser(ium.getUser(userid));
				System.out.println("用户名密码正确:" + userid);
			} else
				juser.setStatus(false);

		} catch (SQLException | IllegalUserManageException e) {
			// TODO Auto-generated catch block
			juser.setStatus(false);
			System.out.println("用户名密码错误:" + userid);
			log.warn(e.getMessage());
			e.printStackTrace();
		}
		// return Response.created(uriInfo.getAbsolutePath()).build();
		return new JSONWithPadding(new GenericEntity<JUser>(juser) {
		}, callback);
	}

	@GET
	@Path("/getlanguage")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getLanguage(@QueryParam("userid") String userid,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		log.info(uriInfo.getAbsolutePath());
		Integer language = 0;
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IUserManager ium = mp.getUserManager();
			language = ium.getLanguage(userid);
		} catch (SQLException | IllegalUserManageException e) {
			log.warn(e.getMessage());
		}
		return new JSONWithPadding(new GenericEntity<String>(
				String.valueOf(language)) {
		}, callback);
	}
}
