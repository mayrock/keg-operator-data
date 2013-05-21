package edu.thu.keg.mdap.user;

import java.sql.SQLException;

public interface IUserManager {
	/**
	 * create a new user
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public boolean addUser(User user) throws SQLException;

	/**
	 * get the user instance cross the userid
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public User getUser(String userid) throws SQLException;

	/**
	 * update the password
	 * 
	 * @param userid
	 * @param newpass
	 * @param oldpass
	 * @return
	 * @throws SQLException
	 */
	public boolean setNewPassword(String userid, String newpass, String oldpass)
			throws SQLException;

	/**
	 * check the userid is used or not
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public boolean checkUserid(String userid) throws SQLException;

	/**
	 * remove the user from the database
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public boolean removeUser(String userid) throws SQLException;
}
