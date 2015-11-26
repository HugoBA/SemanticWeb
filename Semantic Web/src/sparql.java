
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

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
	 * @param args
	 *            the command line arguments
	 * @throws InterruptedException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws DocumentException 
	 */
	public static void main(String[] args)
			throws InterruptedException, ParserConfigurationException, SAXException, IOException, DocumentException {
		ArrayList<TexteURI> extractedURIs = extractURI("liste_uri.xml");
		Iterator<TexteURI> it = extractedURIs.iterator();
		ArrayList<String> results = new ArrayList<>();
//		ArrayList<String> queries = makeQueries(extractedURIs);
//		ArrayList<String> urls = extractUrl(extractedURIs);
//		String result = "";
//
//		for (int i = 0; i < queries.size(); i++) {
//			System.out.println(queries.get(i));
//			result += query(queries.get(i), urls.get(i));
//
//		}
		
		Sparql_thread s1 = new Sparql_thread("thread1", it, results);
		Sparql_thread s2 = new Sparql_thread("thread2", it, results);
		Sparql_thread s3 = new Sparql_thread("thread3", it, results);
		Sparql_thread s4 = new Sparql_thread("thread4", it, results);
		
		Thread t1 = new Thread(s1,"thread1");
		Thread t2 = new Thread(s2,"thread2");
		Thread t3 = new Thread(s3,"thread3");
		Thread t4 = new Thread(s4,"thread4");
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		
		System.out.println(results.size());
		String result = "";
		for(String s: results){
			result += s;
		}
		
		result = "<list_rdf>" + result;
		result = result + "</list_rdf>";
		result = "<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"yes\"?>" + result;
		PrintWriter out = new PrintWriter("liste_rdf.xml");
		SAXReader reader = new SAXReader(
	            org.ccil.cowan.tagsoup.Parser.class.getName());
	    org.dom4j.Document doc = (org.dom4j.Document) reader.read(new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8)));
	    XMLWriter writer = new XMLWriter(out);
	    writer.write(doc);
	    writer.flush();
		 
		    
		
	
     	
		

	}

	

	public static ArrayList<TexteURI> extractURI(String filePath) {
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

		NodeList texts = doc.getElementsByTagName("texte");
		for (int i = 0; i < texts.getLength(); i++) {
			Element text = (Element) texts.item(i);
			TexteURI t = new TexteURI(Integer.parseInt(text.getAttribute("id")), text.getAttribute("url"));
			for (int j = 0; j < text.getChildNodes().getLength(); j++) {
				if (text.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
					String uri = (String) text.getChildNodes().item(j).getTextContent();
					t.addUri(uri);
				}
			}
			uris.add(t);
		}

		return uris;
	}

}