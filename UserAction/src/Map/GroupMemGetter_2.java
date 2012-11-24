package Map;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

public class GroupMemGetter_2 {

	private double[] lat = new double[2000];
	private double[] lng = new double[2000];
	private String groupNum;

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public String execute()throws IOException{
		// TODO Auto-generated method stub

		ActionContext ac = ActionContext.getContext();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
		int n = getLatLng();
		String result = getJsonData(n);
		System.out.println(result);
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		response.getWriter().print(result);
		return null;
	}

	private String getJsonData(int n) {
		//打包经纬度数据
		JSONObject json_result = new JSONObject();
		JSONArray loc_list = new JSONArray();
		JSONObject pim = null;
		try {
			for(int i = 0;i < n;i++) {
				pim = new JSONObject();
				pim.put("lat",lat[i]);
				pim.put("lng",lng[i]);
				System.out.println(i + ":" + lat[i] + "\t" + lng[i]);
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

	private int getLatLng() {
		//得到经纬度数据
		String groupNum_new = this.groupNum.trim();
		//去除不必要的空格
		int i = 0;
		try{
			String inputPath = "D:\\result\\location\\" +
					"Result_4\\Result" + groupNum_new + ".txt";
			System.out.println(inputPath);
			Scanner in = new Scanner(new File(inputPath));
			in.nextLine();
			String str;
			String[] temp;
			while(in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split("\t");
				lat[i] = Double.parseDouble(temp[1]);
				lng[i] = Double.parseDouble(temp[2]);
				i++;
			}
			in.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return i;
	}
}