package Map;
import java.sql.*;
import java.util.Properties;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class GetLoc2 {

	/**
	 * @param args
	 */
	public static void main(String a[]) {
		// TODO Auto-generated method stub

		Connection conn = null;
		Statement stmt = null;
		String query = "select Loc,Ci " + "from ZhuData.dbo.GN " + "where Imsi = " + "460028333886413";
		try{
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ZhuData;integratedSecurity=true;");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				int Loc = rs.getInt("Loc");
				int Ci = rs.getInt("Ci");
				System.out.println(Loc + "\t" + Ci);
			}
			conn.close();
			stmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
