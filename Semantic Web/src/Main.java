import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class Main {

	
	public static void main(String [] args)
	{
		DBpediaSpotlightClient dBpediaSpotlightClient = new DBpediaSpotlightClient();		
		InteractionXML interact = new InteractionXML();
		
		Map<Cle, String> entree;
		Map<Cle, List<String>> sortie = new HashMap<Cle, List<String>>();
		try {
			entree = interact.entreeFichier("test entree.xml");
			for (Cle cle : entree.keySet()){
				String texte = entree.get(cle);
				Text description = new Text(texte);
				System.out.println(texte);
				
				List<DBpediaResource> list = dBpediaSpotlightClient.extract(description);
				System.out.println("Size ::: " + list.size());
				System.out.println(list.toString());
				List<String> uriList = new ArrayList<String>();
				for(DBpediaResource rsrc: list) 
				{
					uriList.add(rsrc.getFullUri());
				}
				System.out.println("Size ::: " + uriList.size());
				System.out.println(uriList.toString());
				sortie.put(cle, uriList);
			}
			interact.sortieFichier("liste_uri.xml", sortie);
			
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (AnnotationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}