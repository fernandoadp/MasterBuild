 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projecta;

/**
 *
 * @author fernandorodriguez
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import javax.net.ssl.HttpsURLConnection;

public class GET_POSTconnections {

  private final String USER_AGENT = "Mozilla/5.0";

 
  // HTTP GET request
  public void sendGet() throws Exception {

    String url = "http://localhost:8080/job/MasterJob/build?token=build";
    java.net.URL obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    
    // optional default is GET
    con.setRequestMethod("GET");

    //add request header
    con.setRequestProperty("User-Agent", USER_AGENT);

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    StringBuffer response;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
      String inputLine;
      response = new StringBuffer();
      
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    }

    //print result
    System.out.println(response.toString());
  }
	
  
  // HTTP POST request
  public void sendPost(String JobName) throws Exception {

    String url = "http://localhost:8080/job/" + JobName + "/build?token=build";
    java.net.URL obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    //add reuqest header
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    String urlParameters = "";

    // Send post request
    con.setDoOutput(true);
    try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
      wr.writeBytes(urlParameters);
      wr.flush();
    }

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + urlParameters);
    System.out.println("Response Code : " + responseCode);

    StringBuilder response;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
      String inputLine;
      response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    }

    //print result
    System.out.println(response.toString());
  }

}

