package edu.thu.keg.mdap.user;

import java.sql.SQLException;

public interface IUserManager {
	public boolean addUser(User user) throws SQLException;

	public User getUser(String userid) throws SQLException;

	public boolean setNewPassword(String userid, String newpass, String oldpass)
			throws SQLException;

	public boolean checkUserid(String userid) throws SQLException;

	public boolean removeUser(String userid) throws SQLException;
}
