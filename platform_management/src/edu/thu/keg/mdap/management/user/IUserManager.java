package edu.thu.keg.mdap.management.user;

import java.sql.SQLException;

import edu.thu.keg.mdap.management.provider.IllegalFavManageException;
import edu.thu.keg.mdap.management.provider.IllegalUserManageException;

public interface IUserManager {
	/**
	 * create a new user
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public boolean addUser(User user) throws SQLException,
			IllegalUserManageException;

	/**
	 * get the user instance cross the userid
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public User getUser(String userid) throws SQLException,
			IllegalUserManageException;

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
			throws SQLException, IllegalUserManageException;

	public int getLanguage(String userid) throws SQLException,
			IllegalUserManageException;

	public boolean setLanguage(String userid, int language)
			throws SQLException, IllegalUserManageException;

	/**
	 * check the userid is used or not
	 * 
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public boolean isUseridExist(String userid) throws SQLException,
			IllegalUserManageException;

	/**
	 * check the user password
	 * 
	 * @param userid
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public boolean checkPassword(String userid, String password)
			throws SQLException, IllegalUserManageException;

	/**
	 * remove the user from the database
	 * 
	 * @param userid
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public boolean removeUser(String userid, String password)
			throws SQLException, IllegalUserManageException;
}
