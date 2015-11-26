package extract;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SerialiseurXML {// Singleton
	
	private Element racineTexte;
	private Document document;
	private String search;
	private int longueur;
	private static SerialiseurXML instance = null;
	
	
	private SerialiseurXML(){}
	public static SerialiseurXML getInstance(){
		if (instance == null)
			instance = new SerialiseurXML();
		return instance;
	}
 
	/**
	 * Ouvre un fichier xml et ecrit dans ce fichier une description xml de plan
	 * @param plan
	 * @throws ParserConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws ExceptionXML
	 */
	public void saveFile(ArrayList<Document> list, String search, int longeur) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException 
	{
		File xml = new File("Texts.xml");
  		StreamResult result = new StreamResult(xml);
       	document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
       	
       	this.search = search.replaceAll("\\+", " ");
       	this.longueur = longeur;

       	document.appendChild(createElements(list));
       	
        DOMSource source = new DOMSource(document);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);
	}

	private Element createElements(ArrayList<Document> list) 
	{
		Element racine = document.createElement("listeTexte");    
		
		Iterator<Document> it = list.iterator();
		int id = 0;
		
		while (it.hasNext())
		{
			Document doc = it.next();
			
			String url = doc.getElementsByTagName("url").item(0).getTextContent();
			String text = doc.getElementsByTagName("text").item(0).getTextContent().replaceAll("\\s{2,}","");
			
			StringTokenizer st = new StringTokenizer(text);
	    	
			text="\n";
			
			int count = 0;
			while (st.hasMoreTokens() && count++ < longueur)
				
				text += " " + st.nextToken();
			
			text += "\n";
				
			racineTexte = document.createElement("texte");
			creerAttribut(racineTexte, "id", Integer.toString(id++));
			creerAttribut(racineTexte, "url", url);
			
			racineTexte.setTextContent(text);
			
			racine.appendChild(racineTexte);
		}
		
		return racine;
	}
	
    private void creerAttribut(Element racine, String nom, String valeur)
    {
    	Attr attribut = document.createAttribute(nom);
    	racine.setAttributeNode(attribut);
    	attribut.setValue(valeur);
    }
}
