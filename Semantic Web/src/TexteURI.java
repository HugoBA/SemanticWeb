import java.util.ArrayList;

public class TexteURI {
	
	public int texteId;
	public String url;
	public ArrayList<String> uris;
	
	public TexteURI(int anId,String anUrl){
		this.texteId = anId;
		this.url = anUrl;
		this.uris = new ArrayList<String>();
	}
	
	public void addUri(String uri){
		this.uris.add(uri);		
	}
	
	
	public String toString(){
		String s = "";
		s += "ID: " + texteId + "\n";
		s += "URL: " + url + "\n";
		for(String uri:uris){
			s += uri ;			
		}
		return s;
		
	}

}
