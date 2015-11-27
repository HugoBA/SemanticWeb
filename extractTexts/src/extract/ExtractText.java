package extract;

import javax.xml.transform.TransformerFactoryConfigurationError;
 
/**
 * Extraire les liens d'un document HTML
 */
public class ExtractText 
{
	private static final int EXIT_SUCCESS = 0;
	private static final int EXIT_FAILED = 1;
	
    public static void main(String[] args) throws TransformerFactoryConfigurationError, Exception 
    {
    	try
    	{
        	if (args.length < 4)
        		
        		throw new Exception("Erreur! Il faut 4 paramètres..");
        	
        	ExtractURL.setAPIKey(args[0]);
        	ExtractURL.getTextInFileOutPut(args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]));
        	
        	System.exit(EXIT_SUCCESS);	
        
        	
    	}
        catch (Exception e)
        {
        	System.err.print(e.getMessage());
        	System.exit(EXIT_FAILED);
        }
    }
}