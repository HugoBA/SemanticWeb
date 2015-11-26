package extract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.alchemyapi.api.AlchemyAPI;

import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
public class ExtractURL 
{
	private static String ENGINE_URL = "https://www.ecosia.org/search?q=";
	private static String API_KEY = "api_key.txt";
	private static int page = 0;
	
	public static ArrayList<String> getURLs(String search, int nbResult) throws IOException, BadLocationException
	{
		ArrayList<String> list = new ArrayList<String>();
		
            //Charger la page
            URL url = new URL(ENGINE_URL + search + "&p=" + page);
            URLConnection uconnection = url.openConnection();
            Reader rd = new InputStreamReader(uconnection.getInputStream());
            //lire le document HTML
            EditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
            doc.putProperty("IgnoreCharsetDirective", new Boolean(true));
            kit.read(rd, doc, 0);
            
            ElementIterator ite = new ElementIterator(doc);
            Element elem = ite.next();

            while (elem != null) 
            {
                AttributeSet s = elem.getAttributes();
                String clas = (String) s.getAttribute(HTML.Attribute.CLASS);
                
                if (clas != null && (clas.equals("result results-ads js-ad") || clas.equals("result-title js-ad-result-title")))
                
                	doc.removeElement(elem);

                elem = ite.next();
            }
            
            HTMLDocument.Iterator ita = doc.getIterator(HTML.Tag.A);
            ArrayList<String> settedLink = new ArrayList<String>();
            
            while (ita.isValid() && nbResult > 0) 
            {
                SimpleAttributeSet s = (SimpleAttributeSet) ita.getAttributes();
                String link = (String) s.getAttribute(HTML.Attribute.HREF);
                String clas = (String) s.getAttribute(HTML.Attribute.CLASS);
                
                String qUrl;
                
                if (clas != null && clas.equals("result-title js-result-title"))
                {
                  if (link != null)
                  {
                	  qUrl = getHostName(link);
                	  
                	  if (!settedLink.contains(qUrl) && !qUrl.contains("msn.com"))
                	  {
                		  settedLink.add(qUrl);
                		  list.add(link);
                		  nbResult--;
                	  }
                  }
                }
                ita.next();
            }
            
            if (nbResult > 0)
            {
            	++page;
            	ArrayList<String> tmp = getURLs(search, nbResult);
            	
            	list.addAll(tmp);
            }
            
        return list;
	}

	public static void setAPIKey(String url)
	{
		API_KEY = url;
	}
	
	public static void getTextInFileOutPut(String search, int nbResult, int longueur) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, FileNotFoundException, IOException, BadLocationException
	{
		ArrayList<String> list = getURLs(search, nbResult);
		ArrayList<Document> listDoc = new ArrayList<Document>();
		
		Iterator<String> it = list.iterator();
		
		// Create an AlchemyAPI object.
        AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile(API_KEY);
		
		while (it.hasNext())
		
			// Extract page text from a web URL. (ignoring ads, navigation links,
	        // and other content).
			try
			{
				listDoc.add(alchemyObj.URLGetText(it.next()));
			}
			catch (Exception e)
			{}
		
		// Générer le fichier xml
		SerialiseurXML.getInstance().saveFile(listDoc,search,longueur);
	}
	
	private static String getHostName(String urlInput) {
        urlInput = urlInput.toLowerCase();
        String hostName=urlInput;
        if(!urlInput.equals("")){
            if(urlInput.startsWith("http") || urlInput.startsWith("https")){
                try{
                    URL netUrl = new URL(urlInput);
                    String host= netUrl.getHost();
                    if(host.startsWith("www")){
                        hostName = host.substring("www".length()+1);
                    }else{
                        hostName=host;
                    }
                }catch (MalformedURLException e){
                    hostName=urlInput;
                }
            }else if(urlInput.startsWith("www")){
                hostName=urlInput.substring("www".length()+1);
            }
            return  hostName;
        }else{
            return  "";
        }
    }
}
