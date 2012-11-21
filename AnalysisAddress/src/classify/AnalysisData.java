package classify;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class AnalysisData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String inputAddr = "C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\location\\groups";
		double[][] count = new double[3][20];
		String[] name = new String[20];
		try {
			Scanner in = new Scanner(new File(inputAddr + "Result1.txt"));
			in.nextLine();
			String str = in.nextLine();
			String[] temp = str.split("\t");
			int l = temp.length - 10;
			for (int i = 0; i < l; i++)
				name[i] = temp[i + 7];
			in.close();
			for (int i = 0; i < 3; i++) {
				in = new Scanner(new File(inputAddr + "Result" + (i + 1) + ".txt"));
				str = in.nextLine();
				temp = str.split("\t");
				for(int j = 0; j < l; j++)
					count[i][j] = Double.parseDouble(temp[j + 1]);
				in.close();
			}
			PrintWriter out = new PrintWriter("C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
					"result\\location\\Result2.txt");
			double max;
			double min;
			int m;
			int n;
			for (int i = 0; i < l; i++) {
				max = 0.0;
				min = 1.0;
				m = 0;
				n = 0;
				out.print(name[i]);
				for (int j = 0; j < 3; j++) {
					if (count[j][i] > max) {
						max = count[j][i];
						n = j;
					}
					if (count[j][i] < min) {
						min = count[j][i];
						m = j;
					}
				}
				for (int j = 0; j < 3; j++) {
					if (n == j)
						out.print("\t(max)");
					else if (m == j)
						out.print("\t(min)");
					else
						out.print("\t(mid)");
				}
				out.println();
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}