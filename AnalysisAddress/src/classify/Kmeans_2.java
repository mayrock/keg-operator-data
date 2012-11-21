package classify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * @author Ariesnix
 *
 */
public class Kmeans_2 {

	private int[][] pattern = new int[2000][15];//原始数据
	private double[][] norm = new double[2000][15];//归一化后数据
	private double[][] clusterCenter = new double[20][15];//簇中心点数据
	private int[][] clusterMember = new int[20][2000];//簇包含成员
	private String[][] countFile = new String[2000][20];//全部原始数据
	private int numPatterns;
	private int sizeVector;
	private int numClusters = 4;
	private int l;
	private String title;
	private double var = 0.75;
	private double[] var2 = {0.47,1.0,0.4,1.0};

	private void loadPatterns(String input) {
		try {
			Scanner in = new Scanner(new File(input));
			title = in.nextLine();
			String[] s = title.split("\t");
			l = s.length;
			sizeVector = l - 7;
			int c = 0;
			String str;
			while (in.hasNextLine()) {
				str = in.nextLine();
				s = str.split("\t");
				for (int i = 0; i < l; i++)
					countFile[c][i] = s[i];
				for (int i = 0; i < sizeVector; i++)
					pattern[c][i] = Integer.parseInt(s[i + 7]);
				c++;
			}
			numPatterns = c;
			int total;
			for(int i = 0; i < sizeVector - 6; i++) {
				total = 0;
				for(int j = 0; j < numPatterns; j++)
					total += pattern[j][i];
				for(int j = 0; j < numPatterns; j++)
					norm[j][i] = ((double)pattern[j][i]) / total;
			}
			for (int i = sizeVector - 6; i < sizeVector; i++) {
				total = 0;
				for(int j = 0; j < numPatterns; j++)
					total += pattern[j][i];
				for(int j = 0; j < numPatterns; j++) {
					norm[j][i] = ((double)pattern[j][i]) / total;
					norm[j][i] *= var;
				}
			}
			System.out.println("numPatterns = " + numPatterns + "\tsizeVector = " + sizeVector +
					"\tnumClusters = " + numClusters);
			in.close();
		}catch (IOException e) {
			System.out.println("Cann't read file " +  new File(input).getAbsolutePath());
		}
    }

	public void initClusters() {
		//初始化簇
		System.out.println("Initial cluster centers:");
		for (int i = 0; i < numClusters; i++)
			for (int j = 0; j < sizeVector; j++)
				clusterCenter[i][j] = norm[i][j];
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
		delNotImp();
	}

	private void delNotImp() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numClusters; i++) {
			double max = 0.0;
			int count = 1;
			System.out.print(clusterMember[i][0]);
			for (int j = 1; j <= clusterMember[i][0]; j++)
				if(Distance(clusterMember[i][j],i) > max)
					max = Distance(clusterMember[i][j],i);
			for (int j = 1; j <= clusterMember[i][0]; j++)
				if(Distance(clusterMember[i][j],i) <= max * var2[i]) {
					clusterMember[i][count] = clusterMember[i][j];
					count ++;
				}
			clusterMember[i][0] = count - 1;
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
						"keg-operator-data\\result\\location\\" +
						"200_27features_" + var + "_" + numClusters);
				f.mkdir();
				PrintWriter out = new PrintWriter(f.getAbsolutePath() + 
						"\\Result" + (i + 1) + ".txt");
				out.print("平均值");
				for (int j = 0; j < sizeVector; j++)
					out.print("\t" + clusterCenter[i][j]);
				out.println();
				out.println(title);
				for(int j = 1; j <= clusterMember[i][0]; j++) {
					for (int k = 0; k < l - 1; k++)
						out.print(countFile[clusterMember[i][j]][k] + "\t");
					out.println(countFile[clusterMember[i][j]][l-1]);
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
		kmean.loadPatterns("C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\location\\AeraCount_200_allKind_new.txt");
		kmean.initClusters();
		kmean.runKmeans();
		kmean.saveCluster();
	}

}