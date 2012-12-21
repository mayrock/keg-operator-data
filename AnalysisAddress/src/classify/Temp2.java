package classify;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Temp2 {

	public static void main(String[] args) {
		String inputAddr = "C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\location\\AeraCount_200_allKind.txt";
		String outputAddr = "C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\location\\AeraCount_200_allKind_new.txt";
		int work;
		int bus;
		int life;
		try {
			Scanner in = new Scanner(new File(inputAddr));
			PrintWriter out = new PrintWriter(outputAddr);
			int i = 0;
			String title = in.nextLine();
			String[] temp = title.split("\t");
			title = "";
			for(i = 0; i < 7; i++)
				title += temp[i] + "\t";
			title += "工作区\t生活区\t商业区";
			for(i = 24; i < 30; i++)
				title += "\t" + temp[i];
			out.println(title);
			while(in.hasNextLine()) {
				String str = in.nextLine();
				temp = str.split("\t");
				work = Integer.parseInt(temp[12]) + Integer.parseInt(temp[17]) +
						Integer.parseInt(temp[19]) + Integer.parseInt(temp[20]) +
						Integer.parseInt(temp[21]) + Integer.parseInt(temp[22]) +
						Integer.parseInt(temp[23]);
				life = Integer.parseInt(temp[7]) + Integer.parseInt(temp[13]) +
						Integer.parseInt(temp[14]) + Integer.parseInt(temp[15]) +
						Integer.parseInt(temp[16]) + Integer.parseInt(temp[18]);
				bus = Integer.parseInt(temp[8]) + Integer.parseInt(temp[9]) +
						Integer.parseInt(temp[10]) + Integer.parseInt(temp[11]);
				System.out.println(work + "\t" + life + "\t" + bus);
				if((work + life + bus) == 0)
					continue;
				for(int j = 0; j < 7; j++)
					out.print(temp[j] + "\t");
				out.print(work + "\t" + life + "\t" + bus);
				for(int j = 24; j < 30; j++)
					out.print("\t" + temp[j]);
				out.println();
				System.out.println(i++);
			}
			in.close();
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}