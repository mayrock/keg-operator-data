package classify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import net.sf.json.JSONObject;

public class Analy {

	/**
	 * @param args
	 */
	private double[] lat = new double[2000];
	private double[] lng = new double[2000];
	private String[][] content = new String[2000][11];
	private String title;
	private int[][] count = new int[2000][17];

	private final static String[] aeraSpecies = {
		"美食","休闲娱乐","购物","结婚","宾馆酒店","旅游景点",
		"教育培育","生活服务","医疗服务","丽人","汽车服务",
		"运动健身","商务服务","机构","金融银行","楼宇小区","交通出行"};

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Analy ob = new Analy();
		String inputAddr = "C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\location\\AreaCount_500_allKind_new.txt";
		String outputAddr = "C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\location\\AeraCount_200_allKind.txt";
		try {
			Scanner in = new Scanner(new File(inputAddr));
			int i;
			ob.title = in.nextLine();
			String[] temp = ob.title.split("\t");
			int len = temp.length;
			ob.title = temp[0];
			for(i = 1; i < 24; i++)
				ob.title += "\t" + temp[i];
			for(i = 24; i < 30; i++)
				ob.title += "\t" + temp[i + 3];
			String str;
			i = 0;
			int gz;
			int sh;
			int sy;
			while(in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split("\t");
				gz = Integer.parseInt(temp[len - 9]);
				sh = Integer.parseInt(temp[len - 8]);
				sy = Integer.parseInt(temp[len - 7]);
				if(gz == 0 && sh == 0 && sy == 0)
					continue;
				ob.content[i][0] = temp[0];
				ob.lat[i] = Double.parseDouble(temp[1]);
				ob.lng[i] = Double.parseDouble(temp[2]);
				for(int j = 1; j < 5; j++)
					ob.content[i][j] = temp[j + 2];
				for(int j = 6; j > 0; j--)
					ob.content[i][11 - j] = temp[len - j];
				i++;
			}
			in.close();
			int n = i;
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new FileWriter(outputAddr,false)));
			output.println(ob.title);
			output.flush();
			System.out.println(ob.title);
			String city = URLEncoder.encode("呼和浩特", "UTF-8");
			for (i = 0; i < n; i++) {
				for (int j = 0; j < aeraSpecies.length; j++) {
					JSONObject json_result = new JSONObject();
					str = "http://openapi.aibang.com/search" +
							"?app_key=f41c8afccc586de03a99c86097e98ccb" +
							"&city=" + city +
							"&lat=" + ob.lat[i] +
							"&lng=" + ob.lng[i] +
							"&q=" + URLEncoder.encode(aeraSpecies[j],"UTF-8") +
							"&as=200&alt=json&from=1&to=300";
					System.out.print((i + 1) + "*" + (j + 1) + "\t" + str);
					URL url = new URL(str);
					HttpURLConnection connection = (HttpURLConnection)url.openConnection();
					connection.connect();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"UTF-8"));
					String line = reader.readLine();
					json_result = JSONObject.fromObject(line);
					ob.count[i][j] = json_result.getInt("index_num");
					System.out.println("\t" + ob.count[i][j]);
					reader.close();
					connection.disconnect();
				}
				output.print(ob.content[i][0] + "\t" + ob.lat[i] + "\t" + ob.lng[i]);
				for(int j = 3; j < 7; j++)
					output.print("\t" + ob.content[i][j - 2]);
				for(int j = 7; j < 24; j++) {
					output.print("\t" + ob.count[i][j - 7]);
					System.out.print("\t" + ob.count[i][j - 7]);
				}
				for(int j = 24; j < 30; j++)
					output.print("\t" + ob.content[i][j - 19]);
				output.println();
				output.flush();
				System.out.println();
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}