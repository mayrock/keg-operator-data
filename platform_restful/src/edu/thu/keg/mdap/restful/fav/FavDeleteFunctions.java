package edu.thu.keg.mdap.restful.fav;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import edu.thu.keg.mdap.management.ManagementPlatform;
import edu.thu.keg.mdap.management.favorite.Favorite;
import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.provider.IllegalFavManageException;
import edu.thu.keg.mdap.restful.dataset.DsPostFunctions;

@Path("/favd")
public class FavDeleteFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;
	@Context
	HttpServletRequest httpServletRequest;
	private static Logger log = Logger.getLogger(FavDeleteFunctions.class);

	@POST
	@Path("/rmfav")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONObject rmFav(@FormParam("userid") String userid,
			@FormParam("favid") String favid) {
		Boolean fav = false;
		JSONObject job = new JSONObject();
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IFavoriteManager favManager = mp.getFavoriteManager();
			fav = favManager.removeFav(userid, favid);
			job.put("status", true);
			System.out.println("删除fav:" + userid + " favid:" + favid);
		} catch (JSONException | SQLException | IllegalFavManageException e) {
			log.warn(e.getMessage());
		}
		return job;
	}
}
