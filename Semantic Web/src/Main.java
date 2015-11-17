import java.util.ArrayList;
import java.util.List;

import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;


public class Main {

	
	public static void main(String [] args)
	{
		DBpediaSpotlightClient dBpediaSpotlightClient = new DBpediaSpotlightClient();
		Text text = new Text("First documented in the 13th century, Berlin was the capital of the Kingdom of Prussia (1701–1918), the German Empire (1871–1918), the Weimar Republic (1919–33) and the Third Reich (1933–45). Berlin in the 1920s was the third largest municipality in the world. After World War II, the city became divided into East Berlin -- the capital of East Germany -- and West Berlin, a West German exclave surrounded by the Berlin Wall from 1961–89. Following German reunification in 1990, the city regained its status as the capital of Germany, hosting 147 foreign embassies.");
		try {
			List<DBpediaResource> list = dBpediaSpotlightClient.extract(text);
			System.out.println("Size ::: " + list.size());
			System.out.println(list.toString());
			List<String> uriList = new ArrayList<String>();
			for(DBpediaResource rsrc: list) 
			{
				uriList.add(rsrc.getFullUri());
			}
			System.out.println("Size ::: " + uriList.size());
			System.out.println(uriList.toString());
		} catch (AnnotationException e) {
			e.printStackTrace();
		}
	}
}
