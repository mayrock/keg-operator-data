package classify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @author Ariesnix
 *
 */
public class Kmeans_2 {

	private double[][] norm = new double[125897][15];//数据
	private double[][] clusterCenter = new double[5][15];//簇中心点数据
	private int[][] clusterMember = new int[5][125897];//簇包含成员
	private int numPatterns = 125897;
	private int sizeVector = 15;
	private int numClusters = 5;
	private HashMap<Integer,Long> map = new HashMap<Integer,Long>();

	private void loadPatterns(String input) {
		try {
			Scanner in = new Scanner(new File(input));
			String str;
			String[] temp = null;
			int i = 0;
			while(in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split(" ");
				for(int j = 0; j < numPatterns; j++)
					norm[j][i] = Double.parseDouble(temp[j]);
				i++;
			}
			in.close();
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
		for(int i = 0; i < numClusters; i++) {
			try {
				File f = new File("C:\\Users\\wuchao\\Git\\" +
						"keg-operator-data\\result\\result");
				PrintWriter out = new PrintWriter(f.getAbsolutePath() + 
						"\\Result" + (i + 1) + ".txt");
				out.print("Average");
				for (int k = 0; k < sizeVector; k++)
					out.print("\t" + clusterCenter[i][k]);
				out.println();
				for(int j = 1; j <= clusterMember[i][0]; j++) {
					out.println(map.get(clusterMember[i][j]) + " " + clusterMember[i][j]);
				}
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Kmeans_2 kmean = new Kmeans_2();
		kmean.createMap();
		kmean.loadPatterns("C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\model-final.phi");
		kmean.initClusters();
		kmean.runKmeans();
		kmean.saveCluster();
	}

	private void createMap() {
		try {
			Scanner in = new Scanner(new File(
					"D:\\result\\internet\\topic\\15-new\\wordmap.txt"));
			String str = in.nextLine();
			String[] temp;
			while(in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split(" ");
				map.put(Integer.parseInt(temp[1]),Long.parseLong(temp[0]));
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}