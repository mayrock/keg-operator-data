package edu.thu.keg.mdap;

import edu.thu.keg.mdap.user.IUserManager;
import edu.thu.keg.mdap_impl.user.UserManagerImpl;

public class ManagementImpl {

	public IUserManager getUserManager() {
		return UserManagerImpl.getInstance();
	}
}
