import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;

import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;
import org.xml.sax.SAXException;



public class Main {

	static String nomFichierEntree;
	static String nomFichierSortie;
	
	public static void main(String [] args)
	{
		DBpediaSpotlightClient dBpediaSpotlightClient = new DBpediaSpotlightClient();		
		InteractionXML interact = new InteractionXML();
		switch (args.length){
			case 0:
				nomFichierEntree = "Texts.xml";
				nomFichierSortie = "URIs.xml";
				break;
			case 1:
				nomFichierEntree = args[0];
				nomFichierSortie = "URIs.xml";
				break;
			case 2:
				nomFichierEntree = args[0];
				nomFichierSortie = args[1];
				break;
		}
				
		Map<Cle, String> entree;
		Map<Cle, List<String>> sortie = new HashMap<Cle, List<String>>();
		try {
			entree = interact.entreeFichier(nomFichierEntree);
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
			interact.sortieFichier(nomFichierSortie, sortie);
			
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