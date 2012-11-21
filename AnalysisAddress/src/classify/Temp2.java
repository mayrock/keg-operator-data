package classify;

import java.io.File;
import java.util.Scanner;

public class Temp2 {
	public static void main(String[] args) {
		String inputAddr = "C:\\Users\\wuchao\\Git\\keg-operator-data\\" +
				"result\\location\\AeraCount_200_allKind.txt";
		try {
			Scanner in = new Scanner(new File(inputAddr));
			int i = 0;
			int j = 0;
			in.nextLine();
			while(in.hasNextLine()) {
				String str = in.nextLine();
				String[] temp = str.split("\t");
				int count = 0;
				j++;
				for(int k = 0; k < 17; k++) {
					if(Integer.parseInt(temp[k + 7]) == 0)
						count ++;
				}
				if(count >= 13)
					continue;
				i++;
			}
			in.close();
			int n = i;
			int m = j;
			System.out.println(n + "*" + m);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}