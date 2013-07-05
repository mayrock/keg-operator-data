package edu.thu.keg.mdap.management;

import java.util.HashMap;
import java.util.HashSet;

import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.impl.favorite.FavoriteManagerImpl;
import edu.thu.keg.mdap.management.impl.user.UserManagerImpl;
import edu.thu.keg.mdap.management.user.IUserManager;
import edu.thu.keg.mdap.management.user.User;

public class ManagementPlatform {

	HashSet<String> userPool;

	public ManagementPlatform() {
		userPool = new HashSet<String>();
	}

	public void addUserToPool(String userid) {
		if (!userPool.contains(userid))
			userPool.add(userid);
	}

	public boolean isUserInPool(String userid) {
		return userPool.contains(userid);
	}

	public void removeUserFromPool(String userid) {
		userPool.remove(userid);
	}

	public IUserManager getUserManager() {
		return UserManagerImpl.getInstance();
	}

	public IFavoriteManager getFavoriteManager() {
		return FavoriteManagerImpl.getInstance();
	}
}
