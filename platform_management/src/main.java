import java.sql.ResultSet;
import java.sql.SQLException;

import edu.thu.keg.mdap.management.ManagementImpl;
import edu.thu.keg.mdap.management.favorite.Favorite;
import edu.thu.keg.mdap.management.favorite.IFavoriteManager;
import edu.thu.keg.mdap.management.impl.provider.SqlServerProviderImpl;
import edu.thu.keg.mdap.management.impl.user.UserManagerImpl;
import edu.thu.keg.mdap.management.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.management.provider.IllegalFavManageException;
import edu.thu.keg.mdap.management.provider.IllegalQueryException;
import edu.thu.keg.mdap.management.provider.IllegalUserManageException;
import edu.thu.keg.mdap.management.user.IUserManager;
import edu.thu.keg.mdap.management.user.User;

public class main {

	public static void main(String[] law) {
		AbsSqlServerProvider dp = SqlServerProviderImpl.getInstance();
		try {
			ManagementImpl mi = new ManagementImpl();
			IUserManager ium = mi.getUserManager();
			// boolean status = ium.addUser(null);
			IFavoriteManager ifm = mi.getFavoriteManager();
			// ifm.addFav(new Favorite("myc2", "lovexb", "b"));
			System.out.println(ifm.removeFav("myc2", "lovewc"));

			// ium.addUser(new User("myc2", "mapang", "12", User.ADMIN));
			// ResultSet rs = dp.executeQuery(dp.SelectFrom("*", "User"));
			// while (rs.next()) {
			// System.out.print(rs.getString("userid"));
			// System.out.print(rs.getString("username"));
			// System.out.print(rs.getString("password"));
			// System.out.println();
			// }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalFavManageException e) {
			System.err.print(e.getMessage());
			e.printStackTrace();
		}
	}
}
