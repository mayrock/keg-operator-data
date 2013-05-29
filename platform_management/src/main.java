import java.sql.ResultSet;
import java.sql.SQLException;

import edu.thu.keg.mdap.ManagementImpl;
import edu.thu.keg.mdap.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.provider.IllegalQueryException;
import edu.thu.keg.mdap.provider.IllegalUserManageException;
import edu.thu.keg.mdap.user.IUserManager;
import edu.thu.keg.mdap.user.User;
import edu.thu.keg.mdap_impl.provider.SqlServerProviderImpl;
import edu.thu.keg.mdap_impl.user.UserManagerImpl;

public class main {

	public static void main(String[] law) {
		AbsSqlServerProvider dp = SqlServerProviderImpl.getInstance();
		try {
			ManagementImpl mi = new ManagementImpl();
			IUserManager ium = mi.getUserManager();
//			boolean status = ium.addUser(null);
			ium.addUser(
					new User("myc2", "mapang", "12", User.ADMIN));
			ResultSet rs = dp.executeQuery(dp.SelectFrom("*", "User"));
			while (rs.next()) {
				System.out.print(rs.getString("userid"));
				System.out.print(rs.getString("username"));
				System.out.print(rs.getString("password"));
				System.out.println();
			}
		} catch (IllegalQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalUserManageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
