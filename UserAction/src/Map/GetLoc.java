package Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 
 * @author WuChao
 *
 */
public class GetLoc {

	/**
	 * @param args
	 * @throws IOException 
	 */
	private static double[] lat = {40.003834809598516,40.014934809598516,39.996034809598516,40.027134809598516};
	private static double[] lng = {116.3263213634491,116.3274213634491,116.3285213634491,116.3296213634491};
	private static int[] time = {10,9,23,7};

	public String execute()throws IOException{
		// TODO Auto-generated method stub

		HttpServletResponse response = ServletActionContext.getResponse();
		String result = getJsonData();
		System.out.println(result);
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		response.getWriter().print(result);
		return null;
	}

	private String getJsonData() {
		JSONObject json_result = new JSONObject();
		JSONArray loc_list =new JSONArray();
		JSONObject pim = null;
		try {
			for(int i = 0;i < lat.length;i++) {
				pim = new JSONObject();
				pim.put("lat",lat[i]);
				pim.put("lng",lng[i]);
				pim.put("time",time[i]);
				loc_list.put(pim);
			}
			json_result.put("locinfo", loc_list);
		}catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}
		return json_result.toString();
	}

}
