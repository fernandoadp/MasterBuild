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
public interface ICommonTasks {
  
  
  public String ReadFile(String project) throws Exception;
  
  public void LogMessage(String logPath, String logMessage);
    
  public String SendPost(String JobName, String task, String urlRepository) throws Exception;
  
  public String DirectoryToDirectory(String source, String target) throws Exception;
  
  public String FileToDirectory(String source, String target, String fileExtension) throws Exception;
  
  public String ExecuteCommandPing(String command);
  
  public void ExecuteCommand(String command);
  
}
