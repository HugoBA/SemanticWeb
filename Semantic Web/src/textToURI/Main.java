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
	
				
		Map<Cle, String> entree;
		Map<Cle, Set<String>> sortie = new HashMap<Cle, Set<String>>();
		
		ThreadURL.setSortie(sortie);
		
		List<ThreadURL> listThread = new ArrayList<ThreadURL>();
		
		try {
			entree = interact.entreeFichier(NOMFICHIERENTREE);
			for (Cle cle : entree.keySet()){
				String texte = entree.get(cle);
				Text description = new Text(texte);
				
				DBpediaSpotlightClient dBpediaSpotlightClient = new DBpediaSpotlightClient();
				ThreadURL threadURL = new ThreadURL(description, dBpediaSpotlightClient, cle);
				listThread.add(threadURL);
				threadURL.start();
			}
			
			for (int i = 0; i < listThread.size(); i++){
				listThread.get(i).join();
			}
			
			sortie = ThreadURL.getSortie();
			interact.sortieFichier(NOMFICHIERSORTIE, sortie);
			
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}