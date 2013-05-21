package edu.thu.keg.mdap.user;

public class User {
	public static final int ADMIN = 3;
	public static final int DEVELOP = 2;
	public static final int BROWSER = 1;

	private String userid;
	private String username;
	private String password;
	private int permission;

	public User(String userid, String username, String password, int permission) {
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.permission = permission;

	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public int getPermission() {
		return permission;
	}

}
