import java.sql.ResultSet;
import java.sql.SQLException;

import edu.thu.keg.mdap.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.provider.IllegalQueryException;
import edu.thu.keg.mdap_impl.provider.SqlServerProviderImpl;

public class main {

	public static void main(String[] law) {
		AbsSqlServerProvider dp = SqlServerProviderImpl.getInstance();
		try {
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
		}
	}
}
