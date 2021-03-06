/**
 * 
 */
package edu.thu.keg.mobiledata.internetgraph.visualizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.thu.keg.mobiledata.internetgraph.dbprocesser.URIMerger;

/**
 * @author myc
 *
 */
public class GraphVisualizer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\myc\\Desktop\\" +
					"doc.txt"));
			String line;
			StringBuilder sb = new StringBuilder();
			int i = 0;
			while ((line = reader.readLine()) != null && i <= 300) {
				String[] arr = line.split("\t");
				String uri1 = URIMerger.processUri(arr[0]);
				String uri2 = URIMerger.processUri(arr[1]);
				if (uri1.equals(uri2))
					continue;
//				if (Double.parseDouble(arr[2]) > 0.1)
//					continue;
				sb.append("\"" + uri1 + "\" -- \""
				   + uri2 + "\"");
				sb.append(" [label=" + arr[2] + "];");
				sb.append(System.getProperty("line.separator"));
				++i;
			}
			reader.close();
			BufferedWriter writer = new BufferedWriter(new 
					FileWriter("C:\\Users\\myc\\Desktop\\" +
							"dot_top_merge.txt"));
			writer.write(toDOTFile(sb.toString()));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    public static String toDOTFile(String content)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("graph G {");
        sb.append(System.getProperty("line.seperator"));
        sb.append(content);
        sb.append("}");

        return sb.toString();
    }
}
