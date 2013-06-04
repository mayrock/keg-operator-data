package edu.thu.keg.mdap.management.favorite;

import java.sql.SQLException;
import java.util.List;

import edu.thu.keg.mdap.management.provider.IllegalFavManageException;

public interface IFavoriteManager {

	public boolean addFav(String userid, String favstr) throws SQLException,
			IllegalFavManageException;

	public Favorite getFav(String userid, String favid) throws SQLException,
			IllegalFavManageException;

	public List<Favorite> getAllFavs(String userid) throws SQLException,
			IllegalFavManageException;

	public boolean setFavid(String userid, String oldfavid, String newfavid)
			throws SQLException, IllegalFavManageException;

	public boolean setFavstring(String userid, String favid, String newfavstring)
			throws SQLException, IllegalFavManageException;

	public boolean removeFav(String userid, String favid) throws SQLException,
			IllegalFavManageException;

	public boolean removeAllFav(String userid) throws SQLException,
			IllegalFavManageException;

	public boolean isFavExsist(String userid, String favid)
			throws SQLException, IllegalFavManageException;
}
