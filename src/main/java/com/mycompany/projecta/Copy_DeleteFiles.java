/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projecta;

import java.io.File;
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
  
}
