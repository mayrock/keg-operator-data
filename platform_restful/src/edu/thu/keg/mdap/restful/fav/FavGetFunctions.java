package edu.thu.keg.mdap.restful.fav;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import edu.thu.keg.mdap.management.ManagementPlatform;
import edu.thu.keg.mdap.management.favorite.Favorite;
import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.provider.IllegalFavManageException;
import edu.thu.keg.mdap.restful.dataset.DsPostFunctions;
import edu.thu.keg.mdap.restful.jerseyclasses.management.JFavorite;

@Path("/favg")
public class FavGetFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;
	@Context
	HttpServletRequest httpServletRequest;
	private static Logger log = Logger.getLogger(FavGetFunctions.class);

	@GET
	@Path("/getfav")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding getFav(@QueryParam("userid") String userid,
			@QueryParam("favid") String favid,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		log.info(uriInfo.getAbsolutePath());
		Favorite fav = null;
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IFavoriteManager favManager = mp.getFavoriteManager();
			fav = favManager.getFav(userid, favid);

			System.out.println("得到fav:" + userid + " favid:" + favid);
		} catch (SQLException | IllegalFavManageException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return new JSONWithPadding(new GenericEntity<Favorite>(fav) {
		}, callback);
	}

	@GET
	@Path("/getfavs")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding getAllFavs(@QueryParam("userid") String userid,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback) {
		log.info(uriInfo.getAbsolutePath());
		List<Favorite> favs = null;
		try {
			ManagementPlatform mp = (ManagementPlatform) servletcontext
					.getAttribute("managementplatform");
			IFavoriteManager favManager = mp.getFavoriteManager();
			favs = favManager.getAllFavs(userid);

			System.out.println("得到All favs:" + userid);
		} catch (SQLException | IllegalFavManageException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return new JSONWithPadding(new GenericEntity<List<Favorite>>(favs) {
		}, callback);
	}
}
