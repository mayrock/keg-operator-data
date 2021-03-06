package edu.thu.keg.mdap.management.impl.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.thu.keg.mdap.management.impl.provider.SqlServerProviderImpl;
import edu.thu.keg.mdap.management.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.management.provider.IllegalFavManageException;
import edu.thu.keg.mdap.management.provider.IllegalUserManageException;
import edu.thu.keg.mdap.management.user.IUserManager;
import edu.thu.keg.mdap.management.user.User;

public class UserManagerImpl implements IUserManager {
	public static UserManagerImpl instance;

	public static UserManagerImpl getInstance() {
		// TODO multi-thread
		if (instance == null)
			instance = new UserManagerImpl();

		return instance;
	}

	@Override
	public boolean addUser(User user) throws SQLException,
			IllegalUserManageException {
		System.out.println("add user ");
		String sql = "insert into [User] ( userid, username, password, permission, language) "
				+ " values (?,?,?,?,?)";
		AbsSqlServerProvider ssp = null;

		if (isUseridExist(user.getUserid()))
			throw new IllegalUserManageException(user.getUserid()
					+ " UserManager the user already exists!");
		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, user.getUserid());
		pstmt.setString(2, user.getUsername());
		pstmt.setString(3, user.getPassword());
		pstmt.setInt(4, user.getPermission());
		pstmt.setInt(5, user.getLanguage());
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		pstmt.getConnection().close();
		return true;
	}

	@Override
	public User getUser(String userid) throws SQLException,
			IllegalUserManageException {
		String sql = "select username, password, permission, language"
				+ " from [User]" + " where userid = ?";
		AbsSqlServerProvider ssp = null;
		User user = null;

		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql);
		pstmt.setString(1, userid);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			user = new User(userid, rs.getString(1), rs.getString(2),
					rs.getInt(3), rs.getInt(4));
		} else
			throw new IllegalUserManageException(
					"UserManager: the userid don't exist");
		pstmt.getConnection().close();
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
		pstmt.getConnection().close();
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
			String databasepass = rs.getString(1);
			System.out.println(databasepass + "  " + password);
			if (databasepass.equals(password)) {
				pstmt.getConnection().close();
				return true;
			}
		}
		pstmt.getConnection().close();
		return false;
	}

	@Override
	public int getLanguage(String userid) throws SQLException,
			IllegalUserManageException {
		String sql = "select language " + "from [User] " + "where userid = ?";
		AbsSqlServerProvider ssp = null;
		if (!isUseridExist(userid))
			throw new IllegalUserManageException("the userid not exists!");
		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, userid);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int result = rs.getInt(1);
		pstmt.getConnection().close();
		return result;
	}

	@Override
	public boolean setLanguage(String userid, int language)
			throws SQLException, IllegalUserManageException {
		String sql = "update [User] set language = ? " + "where userid = ?";
		AbsSqlServerProvider ssp = null;
		if (!isUseridExist(userid))
			throw new IllegalUserManageException("the favrite already exists!");
		ssp = SqlServerProviderImpl.getInstance();
		PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, language);
		pstmt.setString(2, userid);
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		pstmt.getConnection().close();
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
		if (rs.next()) {
			pstmt.getConnection().close();
			return true;
		}
		pstmt.getConnection().close();
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
		pstmt.getConnection().close();
		return true;
	}

}
