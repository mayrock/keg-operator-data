package edu.thu.keg.mdap.management.favorite;

public class Favorite {
	String userid;
	String favid;
	String favstring;

	public Favorite(String userid, String favid, String favstring) {
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

}
