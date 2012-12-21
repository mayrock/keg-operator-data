package classify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

/**
 * 
 * @author Ariesnix
 *
 */
public class Kmeans {

	private int sizeVector = 7;
	private int numClusters = 3;
	private int[][] pattern = new int[2000][sizeVector];//原始数据
	private double[][] norm = new double[2000][sizeVector];//归一化后数据
	private double[][] clusterCenter = new double[numClusters][sizeVector];//簇中心点数据
	private int[][] clusterMember = new int[numClusters][2000];//簇包含成员
	private int numPatterns;
	private final static String[] aeraSpecies = {
		"美食","休闲娱乐","购物","结婚","宾馆酒店","旅游景点",
		"教育培育","生活服务","医疗服务","丽人","汽车服务",
		"运动健身","商务服务","机构","金融银行","楼宇小区","交通出行"};
	private int[][] countArea = new int[2000][aeraSpecies.length];//Area数据
	private String[][] latlng = new String[2000][3];

	private void loadPatterns(String input) {
		try {
			Scanner in = new Scanner(new File(input));
			in.nextLine();
			String[] s;
			int t;
			int c = 0;
			String str;
			while (in.hasNextLine()) {
				str = in.nextLine();
				s = str.split("\t");
				t = 0;
				for (int i = 0; i < aeraSpecies.length; i++) {
					countArea[c][i] = Integer.parseInt(s[i + 7]);
					t += countArea[c][i];
				}
				if (t == 0)
					continue;
				for (int i = 0; i < 3; i++)
					latlng[c][i] = s[i];
				for (int i = 0; i < sizeVector; i++)
					pattern[c][i] = Integer.parseInt(s[i + 24]);
				c++;
			}
			numPatterns = c;
			in.close();
			File f = new File("D:\\result\\location\\Result_norm.txt");
			PrintWriter out = new PrintWriter(f.getAbsolutePath());
			for (int i = 0; i < sizeVector; i++) {
				t = 0;
				for (int j = 0; j < numPatterns; j++)
					t += pattern[j][i];
				for (int j = 0; j < numPatterns; j++) {
					norm[j][i] = ((double) pattern[j][i]) / t;
				}
			}
			for (int j = 0; j < numPatterns; j++) {
				for (int i = 0; i < sizeVector - 1; i++) {
					out.print(norm[j][i] + "\t");
				}
				out.println(norm[j][sizeVector - 1]);
			}
			out.close();
			System.out.println("numPatterns = " + numPatterns
					+ "\tsizeVector = " + sizeVector + "\tnumClusters = "
					+ numClusters);
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cann't read file " +  new File(input).getAbsolutePath());
		}
    }

	public void initClusters() {
		//初始化簇
		System.out.println("Initial cluster centers:");
		for (int i = 0; i < numClusters; i++){
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
//			System.out.println("pattern " + pat + " belong to cluster[" + clustid + "]");
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
//			System.out.println("pattern " + pat + " to cluster [" + i + "]'s " +
//					"distance is " + d + "!");
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
//		System.out.println("Now calculate pattern " + p + " to cluster[" + c + "]'s distance:");
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

	public void saveCluster() {
		Calendar now = Calendar.getInstance();
		String nowTime = now.get(Calendar.YEAR) + "-"
				+ (now.get(Calendar.MONTH) + 1) + "-"
				+ now.get(Calendar.DAY_OF_MONTH) + "-"
				+ now.get(Calendar.HOUR_OF_DAY) + "-"
				+ now.get(Calendar.MINUTE) + "-" + now.get(Calendar.SECOND);
		File f = new File("D:\\result\\location\\Result_"
				+ numClusters + "_" + nowTime);
		f.mkdir();
		try {
			PrintWriter output = new PrintWriter(f.getAbsolutePath()
					+ "\\Result_all.txt");
			for (int j = 0; j < sizeVector; j++)
				output.print("\t" + j);
			for (int j = 0; j < aeraSpecies.length; j++)
				output.print("\t" + aeraSpecies[j]);
			output.println();
			for (int i = 0; i < numClusters; i++) {
				PrintWriter out = new PrintWriter(f.getAbsolutePath()
						+ "\\Result" + (i + 1) + ".txt");
				out.print("SiteId\tLatitude\tLongitude");
				for (int j = 0; j < sizeVector; j++) {
					out.print("\t" + j);
				}
				for (int j = 0; j < aeraSpecies.length; j++) {
					out.print("\t" + aeraSpecies[j]);
				}
				out.println();
				output.print(clusterMember[i][0] + "\t");
				output.print(clusterCenter[i][0]);
				for (int j = 1; j < sizeVector; j++) {
					output.print("\t" + clusterCenter[i][j]);
				}
				int[] count = new int[aeraSpecies.length];
				for (int j = 1; j <= clusterMember[i][0]; j++) {
					for (int k = 0; k < 3; k++)
						out.print(latlng[clusterMember[i][j]][k] + "\t");
					for (int k = 0; k < sizeVector; k++)
						out.print(norm[clusterMember[i][j]][k] + "\t");
					for (int k = 0; k < aeraSpecies.length - 1; k++) {
						out.print(countArea[clusterMember[i][j]][k] + "\t");
						count[k] += countArea[clusterMember[i][j]][k];
					}
					out.println(countArea[i][aeraSpecies.length - 1]);
				}
				for (int k = 0; k < aeraSpecies.length; k++) {
					output.print("\t" + (((double)count[k]) / clusterMember[i][0]));
				}
				output.println();
				out.close();
			}
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Kmeans kmean = new Kmeans();
		kmean.loadPatterns("D:\\result\\location" +
				"\\AreaCount_200_allKind_new_2.txt");
		kmean.initClusters();
		kmean.runKmeans();
		kmean.saveCluster();
	}

}