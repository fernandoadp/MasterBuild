/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projecta;

import java.io.File;

/**
 *
 * @author fernandorodriguez
 */
public class Main {
  
  public static void main (String[] args) {
    
    //GET_POSTconnections conn = new GET_POSTconnections();
    //Copy_DeleteFiles cd = new Copy_DeleteFiles();
    
    ReadFiles read = new ReadFiles();
    
    try {
      
      File folder = new File("/Users/fernandorodriguez/Desktop/MasterBuild/");
      File[] listOfFiles = folder.listFiles();

      for (File file : listOfFiles) {
        
        if (file.isFile()) {
          
          read.ReadFile(file.getAbsolutePath());
          
        }
      }
      
      String s = "";
      
      
    } 
    
    catch (Exception ex) {ex.printStackTrace(System.out);}
  }
  
}
