package textToURI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class InteractionXML {
	
	Map<Cle, String> entreeURL;
	
	public InteractionXML() {
		entreeURL = new HashMap<Cle, String>();
	}
	
	public void ecritureXML(Document xmlSortie, String nomFichier) throws TransformerConfigurationException{
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(xmlSortie);
		File directory = new File(".");
	    StreamResult sortie = null;
		try {
			System.out.println(directory.getCanonicalPath() + File.separator + nomFichier);
			sortie = new StreamResult(new File(nomFichier).getPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			
	    		
	    //formatage
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
	    //sortie
	    try {
			transformer.transform(source, sortie);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	public void sortieFichier(String nomFichier, Map<Cle, Set<String>> listeURI) throws TransformerConfigurationException{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlSortie = builder.newDocument();
			Element racine = xmlSortie.createElement("listeURI");
			xmlSortie.appendChild(racine);
			for (Cle cle : listeURI.keySet()){
				Element texte = xmlSortie.createElement("texte");
				texte.setAttribute("id", String.valueOf(cle.getId()));
				texte.setAttribute("url", String.valueOf(cle.getUrl()));
				racine.appendChild(texte);
				for (String uriUnique : listeURI.get(cle)){
					Element uri = xmlSortie.createElement("URI");
					uri.setTextContent(uriUnique);
					texte.appendChild(uri);
				}
			}
			
			ecritureXML(xmlSortie, nomFichier);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}		
	}
	
	public Map<Cle, String> getEntreeURL() {
		return entreeURL;
	}



	public void setEntreeURL(Map<Cle, String> entreeURL) {
		this.entreeURL = entreeURL;
	}



	public Map<Cle,String> entreeFichier(String nomFichier) throws SAXException, IOException{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File input = new File(nomFichier);
			Document document = builder.parse(input);
			
			Element racine = document.getDocumentElement();
			NodeList listeNoeud = racine.getChildNodes();

			for (int i = 0; i < listeNoeud.getLength(); i++){
				Node texte = listeNoeud.item(i);
				if(listeNoeud.item(i).getNodeType() == Node.ELEMENT_NODE) {
			        Element texteElem = (Element) listeNoeud.item(i);
					Integer id = Integer.parseInt(texteElem.getAttribute("id"));
					String url = texteElem.getAttribute("url");
					String text = texte.getTextContent();
					entreeURL.put(new Cle(id, url), text);
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entreeURL;	
	}
}
