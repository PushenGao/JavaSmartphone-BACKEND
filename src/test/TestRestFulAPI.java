package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestRestFulAPI {
	 private static final String targetURL = "http://localhost:8080/Jersey/rest/werun/getImage/lover";

	 @SuppressWarnings("resource")
	public static void main(String args[]){

              try {

                   URL restServiceURL = new URL(targetURL);

                   HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
                   httpConnection.setRequestMethod("GET");
                   //httpConnection.setRequestProperty("Accept", "application/json");

                   if (httpConnection.getResponseCode() != 200) {
                       System.out.println("No images");   
                	   return;
                   }
                   String header = httpConnection.getHeaderField("imgFrom");
                   System.out.println(header);
                   InputStream is = httpConnection.getInputStream();

                   OutputStream os = null;
                   File fileDownloaded = new File("/home/pushen/"+header);
                   os = new FileOutputStream(fileDownloaded);
                   byte[] b = new byte[2048];
                   int length;
                   while ((length = is.read(b)) != -1) {
                       os.write(b, 0, length);
                   }
                   
                   httpConnection.disconnect();

              } catch (MalformedURLException e) {

                   e.printStackTrace();

              } catch (IOException e) {

                   e.printStackTrace();

              }

            }
}
