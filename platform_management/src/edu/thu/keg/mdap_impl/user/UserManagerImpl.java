package edu.thu.keg.mdap_impl.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.thu.keg.mdap.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.user.User;
import edu.thu.keg.mdap.user.IUserManager;
import edu.thu.keg.mdap_impl.provider.SqlServerProviderImpl;

public class UserManagerImpl implements IUserManager {
	private static UserManagerImpl instance;

	public static UserManagerImpl getInstance() {
		// TODO multi-thread
		if (instance == null)
			instance = new UserManagerImpl();
		return instance;
	}

	@Override
	public boolean addUser(User user) throws SQLException {
		String sql = "insert into User ( userid, username, password, permission) "
				+ " values (?,?,?,?)";
		AbsSqlServerProvider ssp = null;
		try {
			ssp = SqlServerProviderImpl.getInstance();
			PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, user.getUserid());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getPassword());
			pstmt.setInt(4, user.getPermission());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
		} finally {
			ssp.closeConnection();
		}
		return true;
	}

	@Override
	public User getUser(String userid) throws SQLException {
		String sql = "select username, password, permission" + " from User"
				+ " where userid = ?";
		AbsSqlServerProvider ssp = null;
		User user = null;
		try {
			ssp = SqlServerProviderImpl.getInstance();
			PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql);
			pstmt.setString(1, userid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new User(userid, rs.getString(2), rs.getString(3),
						rs.getInt(4));
			}
		} finally {
			ssp.closeConnection();
		}
		return user;
	}

	@Override
	public boolean setNewPassword(String userid, String newpass, String oldpass)
			throws SQLException {
		if (!checkPassword(userid, oldpass))
			return false;
		String sql = "update User set password = ? " + "where userid=?";
		AbsSqlServerProvider ssp = null;
		try {
			ssp = SqlServerProviderImpl.getInstance();
			PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, newpass);
			pstmt.setString(2, userid);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
		} finally {
			ssp.closeConnection();
		}
		return true;
	}

	@Override
	public boolean checkPassword(String userid, String password)
			throws SQLException {
		String sql = "select  password" + " from User" + " where userid = ?";
		AbsSqlServerProvider ssp = null;
		try {
			ssp = SqlServerProviderImpl.getInstance();
			PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql);
			pstmt.setString(1, userid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (!rs.getString(1).equals(password))
					return false;
			}
		} finally {
			ssp.closeConnection();
		}
		return true;
	}

	@Override
	public boolean checkUserid(String userid) throws SQLException {
		String sql = "select  userid" + " from User" + " where userid = ?";
		AbsSqlServerProvider ssp = null;
		try {
			ssp = SqlServerProviderImpl.getInstance();
			PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql);
			pstmt.setString(1, userid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return false;
		} finally {
			ssp.closeConnection();
		}
		return true;
	}

	@Override
	public boolean removeUser(String userid, String password)
			throws SQLException {
		if (checkPassword(userid, password))
			return false;
		String sql = "delete from User " + "where userid=?";
		AbsSqlServerProvider ssp = null;
		try {
			ssp = SqlServerProviderImpl.getInstance();
			PreparedStatement pstmt = ssp.getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userid);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
		} finally {
			ssp.closeConnection();
		}
		return true;
	}

}
