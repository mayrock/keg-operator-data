package edu.thu.keg.mdap.user;

public interface IUserManager {
	public boolean addUser(User user);

	public User getUser(String userid);

	public boolean setNewPassword(String userid, String newpass, String oldpass);

	public boolean checkUserid(String userid);

	public boolean removeUser(String userid);
}
