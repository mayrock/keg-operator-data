package edu.thu.keg.mdap.management.impl.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.thu.keg.mdap.management.impl.provider.SqlServerProviderImpl;
import edu.thu.keg.mdap.management.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.management.provider.IllegalUserManageException;
import edu.thu.keg.mdap.management.user.IUserManager;
import edu.thu.keg.mdap.management.user.User;

public class UserManagerImpl implements IUserManager {
	public static UserManagerImpl instance;

	public static UserManagerImpl getInstance() {
		System.out.print("instance ");
		// TODO multi-thread
		if (instance == null)
			instance = new UserManagerImpl();

		return instance;
	}

	@Override
	public boolean addUser(User user) throws SQLException,
			IllegalUserManageException {
		System.out.println("add user ");
		String sql = "insert into [User] ( userid, username, password, permission) "
				+ " values (?,?,?,?)";
		AbsSqlServerProvider ssp = null;

		if (isUseridExist(user.getUserid()))
			throw new IllegalUserManageException(
					"UserManager the user already exists!");
		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, user.getUserid());
		pstmt.setString(2, user.getUsername());
		pstmt.setString(3, user.getPassword());
		pstmt.setInt(4, user.getPermission());
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		return true;
	}

	@Override
	public User getUser(String userid) throws SQLException,
			IllegalUserManageException {
		String sql = "select username, password, permission" + " from [User]"
				+ " where userid = ?";
		AbsSqlServerProvider ssp = null;
		User user = null;

		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql);
		pstmt.setString(1, userid);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			user = new User(userid, rs.getString(2), rs.getString(3),
					rs.getInt(4));
		} else
			throw new IllegalUserManageException(
					"UserManager: the userid don't exist");

		return user;
	}

	@Override
	public boolean setNewPassword(String userid, String newpass, String oldpass)
			throws SQLException {
		if (!checkPassword(userid, oldpass))
			return false;
		String sql = "update [User] set password = ? " + "where userid=?";
		AbsSqlServerProvider ssp = null;

		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, newpass);
		pstmt.setString(2, userid);
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();

		return true;
	}

	@Override
	public boolean checkPassword(String userid, String password)
			throws SQLException {
		String sql = "select  password" + " from [User]" + " where userid = ?";
		AbsSqlServerProvider ssp = null;

		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql);
		pstmt.setString(1, userid);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			if (!rs.getString(1).equals(password))
				return false;
		}
		return true;
	}

	@Override
	public boolean isUseridExist(String userid) throws SQLException,
			IllegalUserManageException {
		String sql = "select userid" + " from [User]" + " where userid = ?";
		AbsSqlServerProvider ssp = null;

		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql);
		pstmt.setString(1, userid);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return true;

		return false;
	}

	@Override
	public boolean removeUser(String userid, String password)
			throws SQLException {
		if (checkPassword(userid, password))
			return false;
		String sql = "delete from User " + "where userid=?";
		AbsSqlServerProvider ssp = null;

		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, userid);
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();

		return true;
	}

}
