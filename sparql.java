/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparql_projet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


/**
 *
 * @author vlungenstr
 */
public class Sparql_projet {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> uris = new ArrayList<String>();
        uris.add("http://dbpedia.org/resource/Student&gt");
        uris.add("http://dbpedia.org/resource/Michelle_Obama");
        
        String queryString = makeQuery(uris);
        
        URL url = null;
		try {
			url = new URL("http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=SELECT+*+WHERE+%7B+%3Fs+%3Fp+%3Fo.+FILTER%28%3Fs+in+%28%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FStudent%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FMichelle_Obama%3E%29%29+%7D+&format=json&timeout=30000"");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
                System.out.println(line);
            }
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
       
        
        
    }
    
    public static String makeQuery(ArrayList<String> uris){
        String queryString = "SELECT * WHERE { ?s ?p ?o. FILTER(?s in(";
            for(String s : uris){
                queryString += "<" + s + ">" + ",";
            }
        
        queryString = queryString.substring(0, queryString.length()-1);
        queryString += "))}";
        return queryString;
    }
    
}
