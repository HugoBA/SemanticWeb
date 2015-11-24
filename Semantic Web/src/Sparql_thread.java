import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class Sparql_thread implements Runnable {

	private Thread t;
	private String threadName;
	private Iterator<TexteURI> it;
	private ArrayList<String> results;

	public Sparql_thread(String threadName, Iterator<TexteURI> it, ArrayList<String> results) {
		super();
		this.threadName = threadName;
		this.it = it;
		this.results = results;
	}

	public String makeQuery(TexteURI t) throws UnsupportedEncodingException {
		String queryEnd = "&format=xml&timeout=30000";
		String queryString = "SELECT * WHERE { ?s ?p ?o. FILTER(?s in (";
		String encodedQuery = "";

		for (String s : t.uris) {
			queryString += "<" + s + ">" + ", ";
		}

		queryString = queryString.substring(0, queryString.length() - 2);
		queryString += ")) } ";
		encodedQuery = "http://dbpedia.org/sparql?default-graph-uri=" + URLEncoder.encode("http://dbpedia.org", "UTF-8")
				+ "&query=" + URLEncoder.encode(queryString, "UTF-8") + queryEnd;

		return encodedQuery;
	}

	public String query(String query, String u) throws MalformedURLException {
		String resultString = "";
		URL url = new URL(query);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
			for (String line; (line = reader.readLine()) != null;) {
				if (line.contains("variable")) {
				} else if (line.contains("<head>")) {
				} else if (line.contains("</head>")) {
				} else if (line.contains("<results distinct=\"false\" ordered=\"true\">")) {
					line = "<rdf url=\"" + u + "\">";
					resultString += line;
					resultString += "\n";
				} else if (line.contains("</results>")) {
					line = "</rdf>";
					resultString += line;
					resultString += "\n";
				} else if (line.contains("<result>")) {
					line = "<triplet>";
					resultString += line;
					resultString += "\n";
				} else if (line.contains("</result>")) {
					line = "</triplet>";
					resultString += line;
					resultString += "\n";
				} else if (line.contains("<binding name=\"s\">")) {
					line = "<s>" + line.substring(21, line.length() - 10) + "</s>\n";
					resultString += line;
				} else if (line.contains("<binding name=\"p\">")) {
					line = "<p>" + line.substring(21, line.length() - 10) + "</p>\n";
					resultString += line;
				} else if (line.contains("<binding name=\"o\">")) {
					line = "<o>" + line.substring(21, line.length() - 10) + "</o>\n";
					resultString += line;
				} else if (line.contains("<sparql")) {
				} else if (line.contains("</sparql>")) {
				}

			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultString;
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	@Override
	public void run() {
		String result = "";
		String query = "";
		while (it.hasNext()) {
			TexteURI texteURI = it.next();
			try {
				query = this.makeQuery(texteURI);
				result = this.query(query, texteURI.url);
			} catch (UnsupportedEncodingException | MalformedURLException e) {
				e.printStackTrace();
			}
			System.out.println(threadName +" "+ texteURI.url);
			synchronized(results){
				results.add(result);
			}
		}

	}

}