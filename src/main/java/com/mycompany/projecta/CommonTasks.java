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

import static com.mycompany.projecta.JenkinsTest.listJobs;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
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
import java.util.ArrayList;
import java.util.List;
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
  
  
  //Static parameter for the GET and POST http communication
  private final String USER_AGENT = "Mozilla/5.0";
  
  static String written_locatedError = " is not correctly written or located ";
  static String path_writtenError = " The path is not correctly written or the file/directory was not founded ";
  static String result = "";
  
  //'MasterJob' execution
  static Boolean MasterJob = false;
  //'true' by default
  Boolean noText = true;
   
  
  
  //Read all the Files inside the 'MasterJob' folder
  @Override
  public String ReadFile(String project) throws Exception {
    
    //Implementing the Interface methods
    CommonTasks commonObject = new CommonTasks();
    
    String source = "";
    String target = "";
    String buildResult = "";
    String fileExtension = "";
    String result = "";
    String urlRepository = "";
    String jobName = "";
    
    String localhost = "http://localhost:8080";
    
    List<String> jobList = null;
    
    Boolean deleteJobs = false;
    
    
    try(BufferedReader br = new BufferedReader(new FileReader(project))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      
      //Running 'MasterJob' by default if 'true'
      if(MasterJob) {
        
        SendPost("MasterJob", "trigger", "");
        Thread.sleep(16000);

        buildResult = SendPost("MasterJob", "build result", "");

        if ("failed".equals(buildResult)) {return "MasterJob build error. None of the Services will be executed ";}
        Thread.sleep(16000);
      }
      
      //Once the 'MarterJob' is executed with no errors, we continue with
      //the rest of the files
      while (line != null && !"".equals(line)) {
        
        noText = false;
        
        sb.append(line);
        sb.append(System.lineSeparator());
        
        if(!NamingConvention(line)) return line + written_locatedError;
        
        switch (line) {
          
          case "Project Build":
                     line = br.readLine();
                     
                     if(line.contains("Service")) {SendPost(line, "trigger", "");}
                     else return line + written_locatedError;
                     
                     Thread.sleep(20000);
                     
                     buildResult = SendPost(line, "build result", "");
                                         
                     if("failed".equals(buildResult)) {return line + " Build has failed ";};
                     
                     Thread.sleep(20000);
          break;
            
          case "Copy Directory to Directory":  
                     
                     line = br.readLine();                    
                     if("source".equals(line)) {source = br.readLine();}
                     else return line + written_locatedError;
                     
                     line = br.readLine();
                     
                     if("target".equals(line)) {target = br.readLine();}
                     else return line + written_locatedError;
                       
                     result = DirectoryToDirectory(source, target);
                     if(!"passed".equals(result)) return source + "" + target + path_writtenError;
                     
          break;
            
          case "Copy File to Directory":  
                     
                     line = br.readLine();                    
                     if("source".equals(line)) {source = br.readLine();}
                     else return line + written_locatedError;
                     
                     line = br.readLine();
                       
                     if("file extension".equals(line)) {fileExtension = br.readLine();}
                     else return line + written_locatedError;
                     
                     line = br.readLine();
                       
                     if("target".equals(line)) {target = br.readLine();}
                     else return line + written_locatedError;
                     
                     result = FileToDirectory(source, target, fileExtension);
                     if(!"passed".equals(result)) return source + "" + target + path_writtenError;
          break;  
            
          case "Project Creation":
                     line = br.readLine();
                     
                     if(line.contains("Service")) {jobName = line;}
                     else return line + written_locatedError;
                     
                     if(listJobs(localhost, jobName)) return jobName + " is already created "; //Verify whether the Services is already created or not
                     
                     line = br.readLine();
                     
                     if(line.contains("url repository")) {line = br.readLine();}
                     else return line + written_locatedError;
                     
                     if(line.contains("https")) {urlRepository = line;}
                     else return line + written_locatedError;
                     
                     result = SendPost(jobName, "creation", urlRepository);
                                         
                     if("failed".equals(result)) {return "failed";};
                     
                     Thread.sleep(14000);
          break;  
            
          case "Shell command Ping":
                     line = br.readLine();
                     
                     result = ExecuteCommandPing(line);
                     if(result.contains("Ping execution failed")) return result;
                       
                     System.out.println(result);
          break;
            
          case "Shell command":
                     line = br.readLine();                 
                     ExecuteCommand(line);
          break;  
            
          case "Project Delete":
            
                   while (line != null && !"".equals(line)) {
                     
                     line = br.readLine();
                     if("end".equals(line)) return "delete passed";
                     
                     if(!listJobs(localhost, line)) return line + " is already deleted or it has not been created ";
                     
                     Thread.sleep(2000);
                     
                     if(line.contains("Service")) {deleteJob(localhost, line);}
                     else return line + written_locatedError;
                     
                     Thread.sleep(2000);
                   }
                     
                   deleteJobs = true;
          break;
                     
        }
        
        if(deleteJobs) return "delete passed";
        
        line = br.readLine();
      }
      
      if(noText) return "Text Line was not founded ";
    } 
    return "passed";
  }
  
  
  
  // HTTP POST request
  @Override
  public String SendPost(String JobName, String task, String urlRepository) throws Exception {
    
    String postResponse = "";
    String inputLine;
    String url = "";
    String urlParameters = "";
    
    java.net.URL obj;
    HttpURLConnection con = null;
    
    switch (task) {
          
      case "trigger":
        url = "http://localhost:8080/job/" + JobName + "/build?token=build";
        
        obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
        con = (HttpURLConnection) obj.openConnection();
        
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        
        urlParameters = "";
      break;
            
      case "build result":                    
        url = "http://localhost:8080/job/" + JobName + "/lastBuild/api/json"; 
        
        obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
        con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        
        urlParameters = "";
      break;
        
      case "creation":                    
        url = "http://localhost:8080/createItem?name=" + JobName;   
        
        obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
        con = (HttpURLConnection) obj.openConnection();
         
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/xml");
        
        urlParameters = JobTemplate(urlRepository);
      break;  
        
    }
    
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
      
      response = new StringBuilder();
      
      while ((inputLine = in.readLine()) != null) {
        
        if(inputLine.contains("\"result\":\"SUCCESS\"")) {       
          return "passed";   
        } 
        if(inputLine.contains("\"result\":\"FAILURE\"")) {       
          return "failed";   
        }
      }
    }
    //print result
    System.out.println(response.toString());
    return "passed";
  }
  
  
  
  @Override
  public String DirectoryToDirectory(String source, String target) throws Exception {
     
    try {
      
      File Source = new File(source);
      File Target = new File(target);
      
      FileUtils.copyDirectory(Source, Target);
      FileUtils.cleanDirectory(Source);
    } 
    
    catch (Exception ex) {
      ex.printStackTrace(System.out);
      return ex.toString();
    }  
    
    return "passed";
  }
  
  
  
  @Override
  public String FileToDirectory(String sourceFile, String targetDirectory, String fileExtension) throws Exception {
     
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
    
    catch (Exception ex) {
      ex.printStackTrace(System.out);
      return ex.toString();
    }
    
    return "passed";
  }
  
  
  
  //This method is used just for the 'ReadFile' method, and it´ not implemented by
  //the interface. This method is 'private'
  private Boolean NamingConvention(String line) throws Exception {
     
    try {
      
      List<String> namingConvention = new ArrayList<String>();
      
      //Only checks the "Cases" (switch-case-break) in the 'ReadFile' method
      namingConvention.add("Project Build");
      namingConvention.add("Project Creation");
      namingConvention.add("Copy Directory to Directory");
      namingConvention.add("Copy File to Directory");
      namingConvention.add("Shell command Ping");
      namingConvention.add("Shell command"); 
      namingConvention.add("Project Delete");     
      
      if(!namingConvention.contains(line)) return false;
    } 
    
    catch (Exception ex) {ex.printStackTrace(System.out);} 
    
    return true;
  }
  
  
  
  //General message for any Log file
  @Override
  public void LogMessage(String logPath, String logMessage) {
    
     try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logPath, true), "utf-8"))) {
          writer.write(logMessage + ZonedDateTime.now() + "\n");}
     
     catch (Exception ex) {ex.printStackTrace(System.out);}
  }
  
  
  
  //Execute and validate a ping command, linux or windows
  @Override
  public String ExecuteCommandPing(String command) {

    StringBuffer output = new StringBuffer();
    String result = "";

    Process p;
    try {
      p = Runtime.getRuntime().exec(command);
      p.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line = "";
      while ((line = reader.readLine()) != null) {
        
        result += line;
        output.append(line + "\n");
      }
      
      if(result.contains("Request timeout") || "".equals(result)) return "Ping execution failed. The total number of packets was not received ";
      

    } catch (Exception e) {
      e.printStackTrace();
    }

    return output.toString();
  }
  
  
  //Execute a System command, linux or windows
  @Override
  public void ExecuteCommand(String command) {

    StringBuffer output = new StringBuffer();
    String result = "";

    Process p;
    try {
      p = Runtime.getRuntime().exec(command);
      p.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line = "";
      while ((line = reader.readLine()) != null) {
        
        result += line;
        output.append(line + "\n");
      }
    } 
    
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  
  
  //This method is used just for the 'ReadFile' method, and it´ not implemented by
  //the interface. This method is 'private'
  //Job xml.config Template
  private String JobTemplate(String urlRepository) throws Exception {
    
    String jobTemplate = "";
     
    try {
      
      jobTemplate =
              
        "<?xml version='1.0' encoding='UTF-8'?>\n"
              + "<project>\n"
              + "  <actions/>\n"
              + "  <description>Service or component developed by the Development Team</description>\n"
              + "  <keepDependencies>false</keepDependencies>\n"
              + "  <properties/>\n"
              + "  <scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@2.4.0\">\n"
              + "    <configVersion>2</configVersion>\n"
              + "    <userRemoteConfigs>\n"
              + "      <hudson.plugins.git.UserRemoteConfig>\n"
              + "        <url>" + urlRepository + "/</url>\n"
              + "        <credentialsId>b4fe8748-7db2-4043-b6db-7ba12bdc3342</credentialsId>\n"
              + "      </hudson.plugins.git.UserRemoteConfig>\n"
              + "    </userRemoteConfigs>\n"
              + "    <branches>\n"
              + "      <hudson.plugins.git.BranchSpec>\n"
              + "        <name>*/master</name>\n"
              + "      </hudson.plugins.git.BranchSpec>\n"
              + "    </branches>\n"
              + "    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n"
              + "    <submoduleCfg class=\"list\"/>\n"
              + "    <extensions/>\n"
              + "  </scm>\n"
              + "  <canRoam>true</canRoam>\n"
              + "  <disabled>false</disabled>\n"
              + "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n"
              + "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n"
              + "  <authToken>build</authToken>\n"
              + "  <triggers/>\n"
              + "  <concurrentBuild>false</concurrentBuild>\n"
              + "  <builders>\n"
              + "    <hudson.tasks.Maven>\n"
              + "      <targets>clean\n"
              + "install</targets>\n"
              + "      <mavenName>NetbeansMaven</mavenName>\n"
              + "      <usePrivateRepository>false</usePrivateRepository>\n"
              + "      <settings class=\"jenkins.mvn.DefaultSettingsProvider\"/>\n"
              + "      <globalSettings class=\"jenkins.mvn.DefaultGlobalSettingsProvider\"/>\n"
              + "    </hudson.tasks.Maven>\n"
              + "  </builders>\n"
              + "  <publishers/>\n"
              + "  <buildWrappers/>\n"
              + "</project>";     
    } 
    
    catch (Exception ex) {ex.printStackTrace(System.out);} 
    
    return jobTemplate;
  }
  
  
  //Verify whether the Service is already created or not
  private boolean listJobs(String url, String servicetoCreate) {
    
    Client client = Client.create();
    
    //client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME, PASSWORD));
    WebResource webResource = client.resource(url + "/api/xml");
    ClientResponse response = webResource.get(ClientResponse.class);
    String jsonResponse = response.getEntity(String.class);
    client.destroy();

    // Get name from <job><name>...
    //List<String> jobList = new ArrayList<>();
    String[] jobs = jsonResponse.split("job>"); // 1, 3, 5, 7, etc will contain jobs
    
    for (String job : jobs) {
      
      String[] names = job.split("name>");
      
      if (names.length == 3) {
        
        String name = names[1];
        name = name.substring(0, name.length() - 2); // Take off </ for the closing name tag: </name>
        if(name.contains(servicetoCreate)) return true;
      }
    }
    
    return false;
  }
  
  
  //Delete the Job
  private static String deleteJob(String url, String jobName) {
    
    Client client = Client.create();
    WebResource webResource = client.resource(url + "/job/" + jobName + "/doDelete");
    ClientResponse response = webResource.post(ClientResponse.class);
    String jsonResponse = response.getEntity(String.class);
    client.destroy();
    System.out.println("Response deleteJobs:::::" + jsonResponse);
    return jsonResponse;
  }
  
  
}

