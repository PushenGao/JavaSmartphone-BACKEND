package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import model.AccountPO;

public class TestRestFulAPIPOST {
	  private static final String targetURL = "http://localhost:8080/Jersey/rest/werun/register1";

      public static void main(String[] args) {

             try {

                    URL targetUrl = new URL(targetURL);

                    HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
                    httpConnection.setDoOutput(true);
                    httpConnection.setRequestMethod("POST");
                    httpConnection.setRequestProperty("Content-Type", "application/json");

                    AccountPO apo = new AccountPO();
                    apo.setPassword("123");
                    //String input = "{\"password\":\"9999\",\"account\":{\"id\":\"pushengao\",\"name\":\"\",\"age\":\"22\",\"gender\":\"male\",\"historyRecord\":{\"userId\":\"\",\"totalTime\":\"\",\"totalDistance\":\"\",\"lastLocation\":\"\"}},\"pendingFriends\":[],\"activeFriends\":[]}";

                    OutputStream outputStream = httpConnection.getOutputStream();
                    outputStream.write(apo.toString().getBytes());
                    outputStream.flush();

                    if (httpConnection.getResponseCode() != 200) {
                           throw new RuntimeException("Failed : HTTP error code : "
                                  + httpConnection.getResponseCode());
                    }

                    BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                                  (httpConnection.getInputStream())));

                    String output;
                    System.out.println("Output from Server:\n");
                    while ((output = responseBuffer.readLine()) != null) {
                           System.out.println(output);
                    }

                    httpConnection.disconnect();

               } catch (MalformedURLException e) {

                    e.printStackTrace();

               } catch (IOException e) {

                    e.printStackTrace();

              }

             }     
}
