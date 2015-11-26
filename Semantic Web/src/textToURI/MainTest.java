package textToURI;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class MainTest {

	public static void main(String[] args) {
		
		String url = "http://spotlight.dbpedia.org/rest/annotate";

	    // Create an instance of HttpClient.
	    HttpClient client = new HttpClient();

	    // Create a method instance.
	    GetMethod method = new GetMethod(API_URL + "rest/annotate/?" +
				"confidence=" + confidence
				+ "&support=" + support
				+ "&text=" + URLEncoder.encode(text.text(), "utf-8"));

		getMethod.addRequestHeader(new Header("Accept", "application/json"));
	    
	    // Provide custom retry handler is necessary
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler(3, false));

	    try {
	      // Execute the method.
	      int statusCode = client.executeMethod(method);

	      if (statusCode != HttpStatus.SC_OK) {
	        System.err.println("Method failed: " + method.getStatusLine());
	      }

	      // Read the response body.
	      byte[] responseBody = method.getResponseBody();

	      // Deal with the response.
	      // Use caution: ensure correct character encoding and is not binary data
	      System.out.println(new String(responseBody));

	    } catch (HttpException e1) {
	      System.err.println("Fatal protocol violation: " + e1.getMessage());
	      e1.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("Fatal transport error: " + e.getMessage());
	      e.printStackTrace();
	    } finally {
	      // Release the connection.
	      method.releaseConnection();
	    }  
	}
}
