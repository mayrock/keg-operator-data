package classify;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;

public class XXX {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] length = {5,3,2,3,4,4,3};
		try {
			PrintWriter output = new PrintWriter(
					"D:\\result\\location\\200_27features_4\\Result_all.txt");
			output.println("\t1\t2\t3\t4\t5\t6\t7");
			for (int k = 1; k <= 4; k++) {
				Scanner in = new Scanner(new File(
						"D:\\result\\location\\200_27features_4\\Result" + k +"_new.txt"));
				in.nextLine();
				String str;
				String[] temp;
				int i = 0;
				int[] count = new int[7];
				double total = 0.0;
				while (in.hasNext()) {
					str = in.nextLine();
					temp = str.split("\t");
					for (int j = 0; j < 7; j++) {
						count[j] += Integer.parseInt(temp[j + 10]);
					}
					i++;
				}
				in.close();
				int n = i;
				output.print(n);
				for (i = 0; i < 7; i++) {
					total += ((double)count[i]) / length[i];
				}
				for (i = 0; i < 7; i++) {
					output.print("\t" + ((((double)count[i]) / length[i]) / total));
				}
				output.println();
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
