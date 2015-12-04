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

import java.io.IOException;
import java.net.URI;
import static jdk.nashorn.internal.runtime.Debug.id;


import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class JenkinsScraper {
  
  public String scrape(String urlString, String username, String password) throws ClientProtocolException, IOException {
    URI uri = URI.create(urlString);
    HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(username, password));
    // Create AuthCache instance
    AuthCache authCache = new BasicAuthCache();
    // Generate BASIC scheme object and add it to the local auth cache
    BasicScheme basicAuth = new BasicScheme();
    authCache.put(host, basicAuth);
    CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
    HttpGet httpGet = new HttpGet(uri);
    // Add AuthCache to the execution context
    HttpClientContext localContext = HttpClientContext.create();
    localContext.setAuthCache(authCache);

    HttpResponse response = httpClient.execute(host, httpGet, localContext);

    String resp = response.toString();

    return EntityUtils.toString(response.getEntity());
  }
  
  
  public void Authentication() throws ClientProtocolException, IOException {
    
  }
    
    
    
  
  
}
