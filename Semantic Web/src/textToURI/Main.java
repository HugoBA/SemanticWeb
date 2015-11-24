package textToURI;

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

	static final String NOMFICHIERENTREE = "Texts.xml";
	static final String NOMFICHIERSORTIE = "URIs.xml";
	
	public static void main(String [] args)
	{	
		InteractionXML interact = new InteractionXML();
		DBpediaSpotlightClient.setConfidence(Double.parseDouble(args[0]));
		DBpediaSpotlightClient.setSupport(Integer.parseInt(args[1]));
		
		DBpediaSpotlightClient dBpediaSpotlightClient = new DBpediaSpotlightClient();	
				
		Map<Cle, String> entree;
		Map<Cle, List<String>> sortie = new HashMap<Cle, List<String>>();
		try {
			entree = interact.entreeFichier(NOMFICHIERENTREE);
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
			interact.sortieFichier(NOMFICHIERSORTIE, sortie);
			
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