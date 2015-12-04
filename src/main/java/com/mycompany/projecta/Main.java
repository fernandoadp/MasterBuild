/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projecta;

//import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.time.ZonedDateTime;

/**
 *
 * @author fernandorodriguez
 */
public class Main {
  
  static String masterbuildLog = "/Users/fernandorodriguez/Desktop/MasterBuildLog.txt";
  static String masterbuildPath = "/Users/fernandorodriguez/Desktop/MasterBuild/";
  static String result = "";
  
  
  public static void main (String[] args) throws IOException, Exception {
       
    //Implementing the Interface methods
    CommonTasks Icommon = new CommonTasks();
    
    //Delete the ".DS_Store" file created by the system (mac osx) into the "MasterBuild" folder
    Path filepath = FileSystems.getDefault().getPath(masterbuildPath + ".DS_Store");
    Files.deleteIfExists(filepath);
    
    try {
      
      File folder = new File(masterbuildPath);
      File[] listOfFiles = folder.listFiles();
      
      if(listOfFiles.length < 1) {
        
        //If the "MasterBuild" folder is empty, we send an error message to the Log    
        Icommon.LogMessage(masterbuildLog, " MasterBuild folder is empty. ");
        
      } else {
             
        for (File file : listOfFiles) {
        
          if (file.isFile()) {   
            
            result = Icommon.ReadFile(file.getAbsolutePath());
            
            if("delete passed".equals(result)) break;
            
            if(!"passed".equals(result)) {
              
              Icommon.LogMessage(masterbuildLog, file.getName() + " " + result);
              break;
            }
          }   
        } 
      }
    } 
    
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
  
}
