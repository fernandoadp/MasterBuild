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
  
  public String ReadFile(String project) throws Exception {
    
    String source = "";
    String target = "";
    
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
                     //conn.sendPost(line);
                     break;
            
          case "Copy Binary files":  
                     
                     line = br.readLine();                    
                     if("source".equals(line)) {
                       
                       source = br.readLine();
                       line = br.readLine();
                       target = br.readLine();
                       cd.CopyTo(source, target);
                     } 
                     else return "failed";
                     break;
            
            
            
            
        }
        
        line = br.readLine();
      }   
    } 
    return "";
  }
  
}
