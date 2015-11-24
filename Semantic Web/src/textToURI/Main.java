package textToURI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		Map<Cle, Set<String>> sortie = new HashMap<Cle, Set<String>>();
		try {
			entree = interact.entreeFichier(NOMFICHIERENTREE);
			for (Cle cle : entree.keySet()){
				String texte = entree.get(cle);
				Text description = new Text(texte);
				
				List<DBpediaResource> list = dBpediaSpotlightClient.extract(description);
				List<String> uriList = new ArrayList<String>();
				for(DBpediaResource rsrc: list) 
				{
					uriList.add(rsrc.getFullUri());
				}
				
				Set<String> uniquesURI = new HashSet<String>(uriList);
				
				sortie.put(cle, uniquesURI);
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