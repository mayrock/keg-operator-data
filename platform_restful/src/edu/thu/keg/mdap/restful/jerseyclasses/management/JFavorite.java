package edu.thu.keg.mdap.restful.jerseyclasses.management;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import edu.thu.keg.mdap.management.favorite.Favorite;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JField;

/**
 * Jersey class for JFavorite
 * 
 * @author Law
 * 
 */
@XmlRootElement
public class JFavorite {
	String userid;
	String favid;
	String favstring;

	public JFavorite() {
	}

	public JFavorite(String userid, String favid, String favstring) {
		this.userid = userid;
		this.favid = favid;
		this.favstring = favstring;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setFavid(String favid) {
		this.favid = favid;
	}

	public String getFavid() {
		return this.favid;
	}

	public void setFavstring(String favstring) {
		this.favstring = favstring;
	}

	public String getFavstring() {
		return this.favstring;
	}
	// public static JFavorite convertFromFav(Favorite fav)
	// {
	// return new JFavorite(fav.getUserid(), fav.getFavid(),
	// fav.getFavstring());
	// }

}