/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;


/**
 *
 * @author intCorp
 */
public class sparql {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> uris = new ArrayList<String>();
        String queryString = "";
        
        uris.add("http://dbpedia.org/resource/Student");
        uris.add("http://dbpedia.org/resource/Michelle_Obama");
        
        try {
			queryString = makeQuery(uris);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        URL url = null;
		try {
			url = new URL(queryString);
		} catch (MalformedURLException e1) {
			//System.out.println(queryString);
			e1.printStackTrace();
		}

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"))) {
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
    
    public static String makeQuery(ArrayList<String> uris) throws UnsupportedEncodingException{
    	String queryEnd = "&format=xml&timeout=30000";
        String queryString = "SELECT * WHERE { ?s ?p ?o. FILTER(?s in (";
        String encodedQuery = "";
        
        for(String s : uris){
            queryString += "<" + s + ">" + ", ";
        }
        
        queryString = queryString.substring(0, queryString.length()-2);
        queryString += ")) } ";
        encodedQuery = "http://dbpedia.org/sparql?default-graph-uri="
        				+URLEncoder.encode("http://dbpedia.org","UTF-8")+"&query="
        				+URLEncoder.encode(queryString,"UTF-8")+queryEnd;

        return encodedQuery;
    }
    
    
    
}
