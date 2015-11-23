/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projecta;

import static com.mycompany.projecta.Main.masterbuildLog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.rmi.server.ObjID.read;
import java.time.ZonedDateTime;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author fernandorodriguez
 */
public class Copy_DeleteFiles {
  
  
  public void CopyTo(String source, String target) throws Exception {
     
    try {
      
      File Source = new File(source);
      File Target = new File(target);
      
      FileUtils.copyDirectory(Source, Target);
      FileUtils.cleanDirectory(Source);
    } 
    
    catch (Exception ex) {ex.printStackTrace(System.out);}   
  }
  
  
  
  public void CopyFilesTo(String sourceFile, String targetDirectory, String fileExtension) throws Exception {
     
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
  
  
  
  public void LogMessage(String logPath, String logMessage) {
    
     try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logPath, true), "utf-8"))) {
          writer.write(logMessage + ZonedDateTime.now() + "\n");}
     
     catch (Exception ex) {ex.printStackTrace(System.out);}
  }
  
  
}
