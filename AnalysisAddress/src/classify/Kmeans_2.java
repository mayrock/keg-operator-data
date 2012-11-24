package classify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @author Ariesnix
 *
 */
public class Kmeans_2 {

	private double[] pro = new double[15];
	private int numClusters = 5;
	private int sizeVector = 15;
	private int numPatterns = 125897;
	private double[][] norm = new double[125897][sizeVector];//数据
	private double[][] clusterCenter = new double[numClusters][sizeVector];//簇中心点数据
	private int[][] clusterMember = new int[numClusters][numPatterns];//簇包含成员
	private HashMap<Integer,Long> map = new HashMap<Integer,Long>();
	private HashMap<Long,Integer> map2 = new HashMap<Long,Integer>();
	private HashMap<Long,String> map3 = new HashMap<Long,String>();

	private void loadPatterns(String input) {
		try {
			Scanner in = new Scanner(new File(input));
			String str;
			String[] temp = null;
			int i = 0;
	//		double[] count = new double[numPatterns];
			while(in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split(" ");
				for (int j = 0; j < temp.length; j++) {
					norm[i][j] = Double.parseDouble(temp[j]);
				}
				i++;
	/*			for (int j = 0; j < 15; j++) {
					norm[i][j] = Double.parseDouble(temp[j]);
				}
				i++;*/
			}
		/*	numPatterns = temp.length;
			in.close();
			for (i = 0; i < numPatterns; i++) {
				for (int j = 0; j < 15; j++) {
					norm[i][j] = norm[i][j] / count[i];
				}
			}*/
		/*	long n = -1;
			int totalCount;
			int[] timeZone = new int[6];
			for (i = 0; i < numPatterns; i++) {
				n = map.get(i);
				totalCount = map2.get(n);
				norm[i][15] = totalCount;
				str = map3.get(n);
				temp = str.split(" ");
				for (int k = 0; k < 6; k++) {
					timeZone[k] = Integer.parseInt(temp[k]);
					norm[i][k + 16] = timeZone[k];
				}
			}
			for (i = 0; i < sizeVector; i++) {
				for (int j = 0; j < numPatterns; j++)
					count[i] += norm[j][i];
				for (int j = 0; j < numPatterns; j++)
					norm[j][i] = norm[j][i] / count[i];
			}*/
		/*	PrintWriter output = new PrintWriter("D:\\result\\model-final_2.txt");
			for(i = 0; i < numPatterns; i++) {
				for(int j = 0; j < sizeVector - 1; j++)
					output.print(norm[i][j] + " ");
				output.println(norm[i][sizeVector - 1]);
			}
			output.close();*/
		}catch (IOException e) {
			System.out.println("Cann't read file " +  new File(input).getAbsolutePath());
		}
    }

	public void initClusters() {
		//初始化簇
		System.out.println("Initial cluster centers:");
		for (int i = 0; i < numClusters; i++) {
			double a = Math.random();
			int k = (int)(numPatterns * a);
			for (int j = 0; j < sizeVector; j++)
				clusterCenter[i][j] = norm[k][j];
		}
		for (int i = 0; i < numClusters; i++) {
			System.out.print("ClusterCenter[" + i + "] =");
			for (int j = 0; j < sizeVector; j++)
				System.out.print(" " + clusterCenter[i][j]);
			System.out.println();
		}
	}

	public void runKmeans() {
		//执行K-means算法
		boolean converged = false;
		int runCount = 1;
		while (converged == false) {
			System.out.println("Run " + runCount++ + " times!");
			runSimpleTime();
			converged = calNewCenter();
			for (int i = 0; i < numClusters; i++) {
				System.out.print("ClusterCenter[" + i + "] =");
				for (int j = 0; j < sizeVector; j++)
					System.out.print(" " + clusterCenter[i][j]);
				System.out.println();
			}
//			showCluster();
		}
	}

	private void runSimpleTime() {
		//一次执行
		int clustid;
		for (int i = 0; i < numClusters; i++)
			clusterMember[i][0] = 0;
		for (int pat = 0; pat < numPatterns; pat++) {
			clustid = findClosestCluster(pat);
	//		System.out.println("pattern " + pat + " belong to cluster[" + clustid + "]");
			clusterMember[clustid][0] += 1;
			clusterMember[clustid][clusterMember[clustid][0]] = pat;
		}
	}

	private int findClosestCluster(int pat) {
		//找到一个点属于的簇
		int clustID = -1;
		double min, d;
		min = 9.9e+99;
		for (int i = 0; i < numClusters; i++) {
			d = Distance(pat,i);
		//	System.out.println("pattern " + pat + " to cluster [" + i + "]'s " +
		//			"distance is " + d + "!");
			if (d < min) {
				min = d;
				clustID = i;
            }
		}
		if (clustID < 0) {
			System.out.println("Error!");
			System.exit(0);
		}
		return clustID;
	}

	private double Distance(int p, int c) {
		//计算一个点到簇中心点的距离
		double x = 0.0;
	//	System.out.println("Now calculate pattern " + p + " to cluster[" + c + "]'s distance:");
		for (int i = 0; i < sizeVector; i++)
			x += (clusterCenter[c][i] - norm[p][i]) * (clusterCenter[c][i] - norm[p][i]);
		return Math.sqrt(x);
	}

	public boolean calNewCenter() {
		//计算簇的中心点
		int vectID;
		boolean Flag = true;
		double tmp[] = new double[2000];
		for(int i = 0; i < 2000; i++)
			tmp[i] = 0.0;
		for (int i = 0; i < numClusters; i++) {
			for (int j = 1; j <= clusterMember[i][0]; j++) {
				vectID = clusterMember[i][j];
				for (int k = 0; k < sizeVector; k++)
					tmp[k] += norm[vectID][k];
			}
			for (int j = 0; j < sizeVector; j++) {
				tmp[j] = tmp[j] / clusterMember[i][0];
				if (tmp[j] != clusterCenter[i][j])
					Flag = false;
				clusterCenter[i][j] = tmp[j];
			}
		}
		return Flag;
	}
/*
	public void showCluster() {
		for(int i = 0; i < numClusters; i++) {
			System.out.print("ClusterCenter[" + i + "] =");
			for (int j = 0; j < sizeVector; j++)
				System.out.print(" " + clusterCenter[i][j]);
			System.out.println();
			for(int j = 1; j <= clusterMember[i][0]; j++) {
				System.out.print("The Patterns(" + clusterMember[i][j] + ")" +
						"in the Cluster[" + i + "] are: ");
				for (int k = 0; k < sizeVector; k++)
					System.out.print(" " + norm[clusterMember[i][j]][k]);
				System.out.println();
			}
		}
	}
*/
	public void saveCluster() {
		Calendar now = Calendar.getInstance();
		String nowTime = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) +
				"-" + now.get(Calendar.DAY_OF_MONTH) + "-" + now.get(Calendar.HOUR_OF_DAY) +
				"-" + now.get(Calendar.MINUTE) + "-" + now.get(Calendar.SECOND);
		File f = new File("D:\\result\\result_" + numClusters + "_" + nowTime);
		f.mkdir();
		try {
			PrintWriter output = new PrintWriter(f.getAbsolutePath()
					+ "\\Result_all.txt");
			output.print("userCount\ttotalCount");
			for (int i = 0; i < 24; i++)
				output.print("\t" + i);
			for (int i = 0; i < 15; i++)
				output.print("\t" + i);
			output.println();
			PrintWriter outputUser = new PrintWriter(f.getAbsolutePath()
					+ "\\Result_all_user.txt");
			for (int i = 0; i < numClusters; i++) {
				PrintWriter out = new PrintWriter(f.getAbsolutePath()
						+ "\\Result" + (i + 1) + ".txt");
				outputUser.print(clusterMember[i][0]);
				for(int j = 1; j <= clusterMember[i][0]; j++) {
					outputUser.print("\t" + clusterMember[i][j]);
					for(int k = 0; k < sizeVector - 1; k++)
						out.print(norm[clusterMember[i][j]][k] + "\t");
					out.println(norm[clusterMember[i][j]][sizeVector - 1]);
				}
				outputUser.println();
				out.close();
				output.print(clusterMember[i][0]);
				int totalCount;
				int total = 0;
				String time;
				String[] temp;
				int[] timeZone;
				int[] totalTimeZone = new int[24];
				for (int j = 1; j <= clusterMember[i][0]; j++) {
					timeZone = new int[24];
					totalCount = map2.get(map.get(clusterMember[i][j]));
					total += totalCount;
					time = map3.get(map.get(clusterMember[i][j]));
					temp = time.split(" ");
					for (int k = 0; k < 24; k++) {
						timeZone[k] = Integer.parseInt(temp[k]);
						totalTimeZone[k] += timeZone[k];
					}
					System.out.println(i + "*" + j);
				}
				double average = (((double) total) / clusterMember[i][0]);
				output.print("\t" + average);
				for (int k = 0; k < 24; k++) {
					output.print("\t" + ((((double) totalTimeZone[k]) / clusterMember[i][0]) / average));
				}
				for (int k = 0; k < sizeVector; k++) {
					output.print("\t" + clusterCenter[i][k]);
				}
				output.println();
			}
			outputUser.close();
			output.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Kmeans_2 kmean = new Kmeans_2();
	/*	kmean.initPro();
		System.out.println("Done!");*/
		kmean.createMap();
		System.out.println("Done!");
		kmean.createMap2();
		System.out.println("Done!");
		kmean.createMap3();
		System.out.println("Done!");
		kmean.loadPatterns("D:\\result\\internet\\topic\\15-newnew\\model-final.theta");
		System.out.println("Done!");
		kmean.initClusters();
		System.out.println("Done!");
		kmean.runKmeans();
		System.out.println("Done!");
		kmean.saveCluster();
	}

	private void createMap() {
		try {
			Scanner in = new Scanner(new File(
					"D:\\result\\internet\\topic\\ImsiCharacterVectors_new2_Group755_4.txt"));
			String str = in.nextLine();
			String[] temp;
			int i = 0;
			while(in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split(" ");
				temp = temp[0].split(":");
				map.put(i,Long.parseLong(temp[0]));
				i++;
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createMap2() {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			String query = "select Imsi,TotalCount " +
					"from ZhuData.dbo.new2_IMSI_Filtered_3";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				map2.put(rs.getLong("Imsi"),rs.getInt("TotalCount"));
			}
			stmt.close();
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createMap3() {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			String query = "Select imsi";
			for(int i = 0; i < 24; i++)
				query += ",[" + i + "]";
			query += " From ZhuData.dbo.new2_IMSI_TimeSeg_5_uu";
			ResultSet rs = stmt.executeQuery(query);
			String msg;
			while (rs.next()) {
				msg = "";
				for(int i = 0; i < 23; i++)
					msg += rs.getInt("" + i) + " ";
				msg += rs.getInt("23");
				map3.put(rs.getLong("imsi"), msg);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPro() {
		Scanner in;
		try {
			double[] userPro = new double[numPatterns];
			double[][] topicPro = new double[numPatterns][15];
			int i = 0;
			in = new Scanner(new File(
					"D:\\result\\internet\\topic\\ImsiCharacterVectors_new2_Group755_4.txt"));
			String str = in.nextLine();
			String[] temp;
			double count = 0.0;
			while (in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split(" ");
				temp = temp[0].split(":");
				userPro[i] = Double.parseDouble(temp[1]);
				count += userPro[i];
				i++;
			}
			in.close();
			for(i = 0; i < numPatterns; i++)
				userPro[i] /= count;
			in = new Scanner(new File(
					"D:\\result\\internet\\topic\\15-newnew\\model-final.theta"));
			i = 0;
			while (in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split(" ");
				for (int j = 0; j < 15; j++)
					topicPro[i][j] = Double.parseDouble(temp[j]);
				i++;
			}
			in.close();
			PrintWriter output = new PrintWriter("D:\\result\\topicProbability_2.txt");
			for(i = 0; i < 15; i++) {
				pro[i] = 0.0;
				for(int j = 0; j < numPatterns; j++)
					pro[i] += userPro[j] * topicPro[j][i];
				output.println(pro[i]);
			}
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}