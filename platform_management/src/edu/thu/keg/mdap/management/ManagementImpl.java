package edu.thu.keg.mdap.management;

import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.impl.favorite.FavoriteManagerImpl;
import edu.thu.keg.mdap.management.impl.user.UserManagerImpl;
import edu.thu.keg.mdap.management.user.IUserManager;

public class ManagementImpl {

	public IUserManager getUserManager() {
		return UserManagerImpl.getInstance();
	}

	public IFavoriteManager getFavoriteManager() {
		return FavoriteManagerImpl.getInstance();
	}
}
