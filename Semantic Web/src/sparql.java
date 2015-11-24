/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *
 * @author intCorp
 */
public class sparql {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	ArrayList<TexteURI> extractedURIs = extractURI("/home/ling/Documents/test.xml");
    	ArrayList<String> uris = new ArrayList<String>();
        for(TexteURI t:extractedURIs){
        	for(String uri:t.uris){
        		uris.add(uri);
        		
        	}        	
        }
    	
        String queryString = "";
        
        
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
		
		String resultString = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
               if(line.contains("variable")){}
               else if(line.contains("<head>")){}
               else if(line.contains("</head>")){}
               else if(line.contains("<results distinct=\"false\" ordered=\"true\">")){line="<rdf>";resultString+=line;}
               else if(line.contains("</results>")){line="</rdf>";resultString+=line;}
               else if(line.contains("<result>")){line="<triplet>";resultString+=line;}
               else if(line.contains("</result>")){line="</triplet>";resultString+=line;}
               else if(line.contains("<binding name=\"s\">")){line="<s>" + line.substring(21, line.length()-10) + "</s>";resultString+=line;}
               else if(line.contains("<binding name=\"p\">")){line="<p>" + line.substring(21, line.length()-10) + "</p>";resultString+=line;}
               else if(line.contains("<binding name=\"o\">")){line="<o>" + line.substring(21, line.length()-10) + "</o>";resultString+=line;}
               else if(line.contains("<sparql")){line="<list_rdf>"; resultString+=line;}
               else if(line.contains("</sparql>")){line="</list_rdf>"; resultString+=line;}
               
               PrintWriter out = new PrintWriter("output_sparql.xml");
               out.println(resultString);
               out.close();
            }
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(resultString);
        
        
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
    
    public static Document createDOM(String input){
    	DocumentBuilder db = null;
    	Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	InputSource is = new InputSource();
    	is.setCharacterStream(new StringReader(input));
		try {
			doc = db.parse(is);
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return doc;
    }
    
    public static ArrayList<TexteURI> extractURI(String filePath){
    	ArrayList<TexteURI> uris = new ArrayList<TexteURI>();
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	DocumentBuilder db = null;
    	Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			doc = db.parse(new File(filePath));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	NodeList texts = doc.getElementsByTagName("Texte");
    	for(int i = 0;i < texts.getLength();i++){
    		Element text = (Element) texts.item(i);
    		TexteURI t = new TexteURI(Integer.parseInt(text.getAttribute("TextId")),text.getAttribute("url"));
    		for(int j = 0;j < text.getChildNodes().getLength();j++){
    			if(text.getChildNodes().item(j).getNodeType()==Node.ELEMENT_NODE){
	    			String uri = (String) text.getChildNodes().item(j).getTextContent();
	    			t.addUri(uri);
    			}
    		}
    		uris.add(t);
    	}
    	
    	
    	return uris;
    }
    
}
