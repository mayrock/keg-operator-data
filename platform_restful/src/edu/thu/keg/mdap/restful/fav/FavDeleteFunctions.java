package edu.thu.keg.mdap.restful.fav;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import edu.thu.keg.mdap.management.favorite.Favorite;
import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.provider.IllegalFavManageException;

@Path("/favd")
public class FavDeleteFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;

	@GET
	@Path("/rmfav/{userid}/{favid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONWithPadding rmFav(@PathParam("userid") String userid,
			@PathParam("favid") String favid,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		Boolean fav = false;
		JSONObject job = new JSONObject();
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IFavoriteManager favManager = mp.getFavoriteManager();
			fav = favManager.removeFav(userid, favid);
			job.put("status", true);
			System.out.println("删除fav:" + userid + " favid:" + favid);
		} catch (SQLException | IllegalFavManageException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONWithPadding(new GenericEntity<String>(job.toString()) {
		}, callback);
	}
}
