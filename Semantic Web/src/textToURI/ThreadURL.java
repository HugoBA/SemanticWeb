package textToURI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;

public class ThreadURL extends Thread{
	
	private static Map<Cle, Set<String>> sortie;
	private Text description;
	private DBpediaSpotlightClient dBpediaSpotlightClient;
	private Cle cle;
	
	public static Map<Cle, Set<String>> getSortie() {
		return sortie;
	}

	public static void setSortie(Map<Cle, Set<String>> sortie) {
		ThreadURL.sortie = sortie;
	}

	public ThreadURL(Text description, DBpediaSpotlightClient dBpediaSpotlightClient, Cle cle){
		this.cle = cle;
		this.dBpediaSpotlightClient = dBpediaSpotlightClient;
		this.description = description;
	}
	
	public void run(){
		System.out.println(cle.getUrl());
		try {

			List<DBpediaResource>list = dBpediaSpotlightClient.extract(description);
				
			List<String> uriList = new ArrayList<String>();
			for(DBpediaResource rsrc: list) 
			{
				uriList.add(rsrc.getFullUri());
			}
			
			Set<String> uniquesURI = new HashSet<String>(uriList);
			ecritureMap(uniquesURI);
		} catch (AnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void ecritureMap(Set<String> uniquesURI){
		sortie.put(cle, uniquesURI);
	}
}
