package edu.thu.keg.mdap.restful.fav;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
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
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.management.ManagementPlatform;
import edu.thu.keg.mdap.management.favorite.Favorite;
import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.provider.IllegalFavManageException;
import edu.thu.keg.mdap.restful.dataset.DsPostFunctions;

@Path("\favp")
public class FavPostFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;
	@Context
	HttpServletRequest httpServletRequest;
	private static Logger log = Logger.getLogger(FavPostFunctions.class);

	@POST
	@Path("/addfav")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding addFav(@FormParam("userid") String userid,

	@FormParam("favstring") String favstring,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {

		log.info(uriInfo.getAbsolutePath());
		JSONObject job = null;
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IFavoriteManager favManager = mp.getFavoriteManager();
			favManager.addFav(userid, favstring);
			job = new JSONObject();
			job.put("status", true);
			System.out.println("已经添加fav:" + userid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("用户名密码错误:" + userid);
			e.printStackTrace();
		} catch (SQLException | IllegalFavManageException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println(job.toString());
		// return Response.created(uriInfo.getAbsolutePath()).build();
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);
	}

	@POST
	@Path("/setfavid")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding setFavid(@FormParam("userid") String userid,
			@FormParam("oldfavid") String oldfavid,
			@FormParam("newfavid") String newfavid,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		log.info(uriInfo.getAbsolutePath());
		JSONObject job = null;
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IFavoriteManager favManager = mp.getFavoriteManager();
			favManager.setFavid(userid, oldfavid, newfavid);
			job = new JSONObject();
			job.put("status", true);
			System.out.println("变更id fav:" + userid + " favid:" + newfavid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("变更id fav:错误:" + userid);
			e.printStackTrace();
		} catch (SQLException | IllegalFavManageException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println(job.toString());
		// return Response.created(uriInfo.getAbsolutePath()).build();
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);
	}

	@POST
	@Path("/setfavstring")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding setFavstring(@FormParam("userid") String userid,
			@FormParam("favid") String favid,
			@FormParam("newfavstring") String newfavstring,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		log.info(uriInfo.getAbsolutePath());
		JSONObject job = null;
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IFavoriteManager favManager = mp.getFavoriteManager();
			favManager.setFavstring(userid, favid, newfavstring);
			job = new JSONObject();
			job.put("status", true);
			System.out.println("变更str fav:" + userid + " favstr:"
					+ newfavstring);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("变更str fav:错误:" + userid);
			e.printStackTrace();
		} catch (SQLException | IllegalFavManageException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println(job.toString());
		// return Response.created(uriInfo.getAbsolutePath()).build();
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);
	}
}
