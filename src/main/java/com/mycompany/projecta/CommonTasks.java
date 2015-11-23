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
import com.mycompany.projecta.Copy_DeleteFiles;
import com.mycompany.projecta.GET_POSTconnections;
import com.mycompany.projecta.ICommonTasks;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZonedDateTime;
import org.apache.commons.io.FileUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fernandorodriguez
 */
public class CommonTasks implements ICommonTasks{
  
  //Object with all the "ICommonTasks" methods
  Object Iobject;
  ICommonTasks common = (ICommonTasks)Iobject;
  
  //Static parameter for the GET and POST http communication
  private final String USER_AGENT = "Mozilla/5.0";
  
  
  @Override
  public void LogMessage(String logPath, String logMessage) {
    
     try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logPath, true), "utf-8"))) {
          writer.write(logMessage + ZonedDateTime.now() + "\n");}
     
     catch (Exception ex) {ex.printStackTrace(System.out);}
  }
  
  
  @Override
  public String ReadFile(String project) throws Exception {
    
    String source = "";
    String target = "";
    
    try(BufferedReader br = new BufferedReader(new FileReader(project))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        
        switch (line) {
          
          case "Project Dependency":
                     line = br.readLine();
                     //common.sendPost(line);
                     break;
            
          case "Copy Binary files":  
                     
                     line = br.readLine();                    
                     if("source".equals(line)) {
                       
                       source = br.readLine();
                       line = br.readLine();
                       target = br.readLine();
                       common.DirectoryToDirectory(source, target);
                     } 
                     else return "failed"; //messageLog.CopyTo(masterbuildLog, "MasterBuild folder is empty. Build at: " + ZonedDateTime.now() + "\n");
                     break;
            
            
        }
        
        line = br.readLine();
      }   
    } 
    return "";
  }
  
  
  // HTTP POST request
  @Override
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
  
  
  @Override
  public void DirectoryToDirectory(String source, String target) throws Exception {
     
    try {
      
      File Source = new File(source);
      File Target = new File(target);

      FileUtils.copyDirectory(Source, Target);    
      FileUtils.cleanDirectory(Source);
    } 
    
    catch (Exception ex) {ex.printStackTrace(System.out);}   
  }
  
  
  @Override
  public void FileToDirectory(String sourceFile, String targetDirectory, String fileExtension) throws Exception {
     
    try {
      
      File Source = new File(sourceFile);
      File[] listOfFiles = Source.listFiles();
      
      File Target = new File(targetDirectory);
      
      for (File file : listOfFiles) {
        
        if (file.isFile() && file.getName().contains(fileExtension)) {
            
          FileUtils.copyFileToDirectory(file, Target);   
        }   
      } 
    } 
    
    catch (Exception ex) {ex.printStackTrace(System.out);}   
  }
  
  
}

