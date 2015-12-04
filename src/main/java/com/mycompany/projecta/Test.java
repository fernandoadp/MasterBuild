/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projecta;

import java.io.IOException;
import org.apache.commons.httpclient.auth.BasicScheme;
//import org.apache.commons.httpclient.auth;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author fernandorodriguez
 */
public class Test {
  
  public void Auth() throws Exception {
    
    // Credentials
		String username = "fernandoadp";
		String password = "ADP.2017";

		// Jenkins url
		String jenkinsUrl = "http://localhost:8080";

		// Build name
		String jobName = "ServiceA";

		// Build token
		String buildToken = "build";

		// Create your httpclient
		DefaultHttpClient client = new DefaultHttpClient();

		// Then provide the right credentials *******************
                client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new org.apache.http.auth.UsernamePasswordCredentials(username, password));
		//client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), (Credentials) new UsernamePasswordCredentials(username, password));

		// Generate BASIC scheme object and stick it to the execution context
		BasicScheme basicAuth = new BasicScheme();
		BasicHttpContext context = new BasicHttpContext();
		context.setAttribute("preemptive-auth", basicAuth);

		// Add as the first (because of the zero) request interceptor
		// It will first intercept the request and preemptively initialize the authentication scheme if there is not
		client.addRequestInterceptor(new PreemptiveAuth(), 0);

		// You get request that will start the build
		String getUrl = jenkinsUrl + "/job/" + jobName + "/build?token=" + buildToken;
		HttpGet get = new HttpGet(getUrl);

		try {
			// Execute your request with the given context
			HttpResponse response = client.execute(get, context);
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    
  }
  
}
