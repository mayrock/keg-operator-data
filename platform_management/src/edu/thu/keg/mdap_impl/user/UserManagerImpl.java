package edu.thu.keg.mdap_impl.user;

import edu.thu.keg.mdap.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.provider.DatabaseProviderException;
import edu.thu.keg.mdap.user.User;
import edu.thu.keg.mdap.user.IUserManager;
import edu.thu.keg.mdap_impl.provider.DbSqlServerProviderImpl;

public class UserManagerImpl implements IUserManager {
	private static UserManagerImpl instance;

	public static UserManagerImpl getInstance() {
		// TODO multi-thread
		if (instance == null)
			instance = new UserManagerImpl();
		return instance;
	}

	@Override
	public boolean addUser(User user) {
		AbsSqlServerProvider ssp = DbSqlServerProviderImpl.getInstance();
		String ex = "INSERT INTO [User] VALUE ('" + user.getUserid() + "','"
				+ user.getUsername() + "','" + user.getPassword() + "',"
				+ String.valueOf(user.getPermission()) + ")";
		try {
			ssp.execute(ex);
		} catch (DatabaseProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public User getUser(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setNewPassword(String userid, String newpass, String oldpass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkUserid(String userid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeUser(String userid) {
		// TODO Auto-generated method stub
		return false;
	}

}
