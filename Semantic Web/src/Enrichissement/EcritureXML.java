package Enrichissement;

import java.text.DecimalFormat;
import java.util.List;
import textToURI.InteractionXML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EcritureXML {

	public static void ecrireXmlCluster(int nbRDF, List<String> urls, double[][] coefficients){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlSortie = builder.newDocument();
			Element racine = xmlSortie.createElement("GrapheURL");
			xmlSortie.appendChild(racine);
			
			for (int i = 0; i < nbRDF; i++) {
				for (int j = i; j < nbRDF; j++) {
					if (coefficients[i][j] > 0 && coefficients[i][j] < 1){
						
						Element lien = xmlSortie.createElement("Lien");
						lien.setAttribute("cofficient", (new DecimalFormat("#.##").format(coefficients[i][j])) );
						
						Element url1 = xmlSortie.createElement("URL");
						url1.setAttribute("value", urls.get(i));
						lien.appendChild(url1);
						
						Element url2 = xmlSortie.createElement("URL");
						url2.setAttribute("value", urls.get(j));
						lien.appendChild(url2);
						
						racine.appendChild(lien);
					}
				}
			}
			
			InteractionXML interactionXML = new InteractionXML();
			try {
				interactionXML.ecritureXML(xmlSortie, "GrapheURLEnrichi.xml");
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			}
			
		}catch (ParserConfigurationException e) {
			e.printStackTrace();
		}	
	}
}
