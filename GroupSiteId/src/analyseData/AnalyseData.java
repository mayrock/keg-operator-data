package analyseData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class AnalyseData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String inputAddr = "D:\\result\\location\\200_27features_0.75_4_org";
		double[][] info = new double[4][9];
		int[] count = new int[4];
		String[] name = {"工作区","生活区","商业区","0:00~3:59","4:00~7:59",
				"8:00~11:59","12:00~15:59","16:00~19:59","20:00~23:59"};
		try {
			String str;
			String[] temp;
			for(int i = 0; i < 4; i++) {
				Scanner in = new Scanner(
						new File(inputAddr + "\\Result" + (i + 1) + ".txt"));
				str = in.nextLine();
				temp = str.split("\t");
				for (int j = 0; j < 9; j++)
					info[i][j] = Double.parseDouble(temp[j + 1]);
				in.close();
			}
			PrintWriter out = new PrintWriter(inputAddr + "\\AnalyseResult.txt");
			for (int i = 0; i < 9; i++) {
				double max1 = 0.0,max2 = 0.0,min1 = 0.0,min2 = 0.0;
				if(info[0][i] > info[1][i]) {
					max1 = info[0][i];
					min1 = info[1][i];
					count[0] = 2;
					count[1] = 0;
				}
				else {
					max1 = info[1][i];
					min1 = info[0][i];
					count[0] = 0;
					count[1] = 2;
				}
				if(info[2][i] > info[3][i]) {
					max2 = info[2][i];
					min2 = info[3][i];
					count[2] = 2;
					count[3] = 0;
				}
				else {
					max2 = info[3][i];
					min2 = info[2][i];
					count[2] = 0;
					count[3] = 2;
				}
				if(max1 > max2) {
					if(count[0] == 2) count[0] = 3;
					else count[1] = 3;
				}
				else {
					if(count[2] == 2) count[2] = 3;
					else count[3] = 3;
				}
				if(min1 > min2) {
					if(count[0] == 0) count[0] = 1;
					else count[1] = 1;
				}
				else {
					if(count[2] == 0) count[2] = 1;
					else count[3] = 1;
				}
				out.print(name[i]);
				for(int j = 0; j < 4; j++)
					out.print("\t" + info[j][i] + "(" + count[j] + ")");
				out.println();
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}