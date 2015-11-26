package Enrichissement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.List;

public class EcritureGrapheDansTxt {

	public static void ecrireTxtFile(int nbRDF, List<String> urls, double[][] coefficients){
		Writer output = null;
		String content = "";
		for (int i = 0; i < nbRDF; i++) {
			for (int j = i; j < nbRDF; j++) {
				if (coefficients[i][j] > 0 && coefficients[i][j] < 1){
					content += urls.get(i) + " " + urls.get(j) + " " + (new DecimalFormat("#.##").format(coefficients[i][j]).replace(',', '.')) + "\r\n";
				}
			}
		}
		
		File file = new File("GraphFile.txt");
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(content);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Your file has been written"); 
	}
}
