/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparql_projet;

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
        
        URL url = new URL("http://stackoverflow.com");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
                System.out.println(line);
            }
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
