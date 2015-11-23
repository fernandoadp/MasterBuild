/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projecta;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author fernandorodriguez
 */
public class ReadFiles {
  
  static String masterbuildLog = "/Users/fernandorodriguez/Desktop/MasterBuildLog.txt";
  
  public String ReadFile(String project) throws Exception {
    
    //Implementing the Interface methods
    CommonTasks messageLog = new CommonTasks();
    
    String source = "";
    String target = "";
    String buildResult = "";
    String fileExtension = "";
    
    GET_POSTconnections conn = new GET_POSTconnections();
    Copy_DeleteFiles cd = new Copy_DeleteFiles();
    
    try(BufferedReader br = new BufferedReader(new FileReader(project))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        
        switch (line) {
          
          case "Project Dependency":
                     line = br.readLine();
                     conn.sendPost(line, "trigger");
                     Thread.sleep(4000);
                     buildResult = conn.sendPost(line, "build result");
                                         
                     if("failed".equals(buildResult)) {return "failed";};
                     Thread.sleep(20000);
          break;
            
          case "Copy Directory to Directory":  
                     
                     line = br.readLine();                    
                     if("source".equals(line)) {source = br.readLine();}
                       line = br.readLine();
                       
                       target = br.readLine();
                       cd.CopyTo(source, target);
          break;
            
          case "Copy File to Directory":  
                     
                     line = br.readLine();                    
                     if("source".equals(line)) {source = br.readLine();}
                       line = br.readLine();
                       
                       if("file extension".equals(line)) {fileExtension = br.readLine();};
                       line = br.readLine();
                       
                       target = br.readLine();
                       cd.CopyFilesTo(source, target, fileExtension);
          break;  
            
          case "Project":
                     line = br.readLine();
                     conn.sendPost(line, "trigger");
                     Thread.sleep(4000);
                     buildResult = conn.sendPost(line, "build result");
                                         
                     if("failed".equals(buildResult)) {return "failed";};
                     Thread.sleep(20000);
          break;  
            
            
        }
        
        line = br.readLine();
      }   
    } 
    return "passed";
  }

  
  
  
  
}
