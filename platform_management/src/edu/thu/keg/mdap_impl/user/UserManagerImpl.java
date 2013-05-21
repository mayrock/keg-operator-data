package edu.thu.keg.mdap_impl.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.thu.keg.mdap.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.provider.DatabaseProviderException;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setNewPassword(String userid, String newpass, String oldpass)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkUserid(String userid) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeUser(String userid) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
