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

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;

import edu.thu.keg.mdap.management.ManagementPlatform;
import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.provider.IllegalFavManageException;
import edu.thu.keg.mdap.management.provider.IllegalUserManageException;
import edu.thu.keg.mdap.management.user.IUserManager;
import edu.thu.keg.mdap.management.user.User;
import edu.thu.keg.mdap.restful.jerseyclasses.user.JUser;

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
	private static Logger log = Logger.getLogger(UserPostFunctions.class);

	@POST
	@Path("/adduser")
	@Produces({ MediaType.APPLICATION_JSON })
	public JUser addUser(@FormParam("userid") String userid,
			@FormParam("username") @DefaultValue("wcxm") String username,
			@FormParam("password") String password,
			@FormParam("language") int language) {
		log.info(uriInfo.getAbsolutePath());
		JUser juser = new JUser();
		boolean status = false;
		try {
			User user = new User(userid, username, password, User.BROWSER,
					language);
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IUserManager ium = mp.getUserManager();
			status = ium.addUser(user);
			juser.setStatus(status);
			System.out.println("添加用户成功：" + userid + " " + username);
		} catch (IllegalUserManageException | SQLException e) {
			// TODO Auto-generated catch block
			juser.setStatus(false);
			log.warn(e.getMessage());

		}
		return juser;

	}

	@POST
	@Path("/setlanguage")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONObject setLanguage(@FormParam("userid") String userid,
			@FormParam("language") int language) {
		log.info(uriInfo.getAbsolutePath());
		JSONObject job = new JSONObject();
		System.out.print(userid+" "+language);
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IUserManager ium = mp.getUserManager();
			ium.setLanguage(userid, language);
			job.put("status", true);
			System.out
					.println("变更language:" + userid + " language:" + language);
		} catch (JSONException | SQLException | IllegalUserManageException e) {
			try {
				job.put("status", false);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			log.warn(e.getMessage());
		}
		return job;
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
