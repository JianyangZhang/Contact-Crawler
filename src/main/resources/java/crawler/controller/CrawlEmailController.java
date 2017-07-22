package crawler.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import crawler.model.CrawlerQuery;
import crawler.service.CrawlEmailService;

@RestController
public class CrawlEmailController {
	
	@Autowired
	private CrawlEmailService crawlEmailService;
	
	@SuppressWarnings("deprecation")
	@CrossOrigin
	@RequestMapping("/salesforce/connect")
	public String connect() throws ClientProtocolException, IOException, ParseException, org.json.simple.parser.ParseException {
		StringBuilder sb = new StringBuilder();
		sb.append("-H \"Authorization: Bearer ");
		String env ="https://login.salesforce.com/";
		String username = "crm@thevelozgroup.com";
		String password = "123qweasd";
		String clientId = "3MVG9szVa2RxsqBbPIaAc0M3lqlZp9fi_wgZZJoNh4tLudBejHB69Bw6vaPRPLs.rsT8q7jjMfDr2ZySlN2Ah";
		String client_secret = "4195093487627360090";
		HttpPost post = new HttpPost(env);

		// Set up an HTTP client that makes a connection to REST API.
	    DefaultHttpClient client = new DefaultHttpClient();
	    HttpParams params = client.getParams();
	    HttpClientParams.setCookiePolicy(params, CookiePolicy.RFC_2109);
	    params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 30000);

	    // Set the SID.
	    System.out.println("Logging in as " + username + " in environment " + env);
	    String baseUrl = env + "/services/oauth2/token";
	    // Send a post request to the OAuth URL.
	    HttpPost oauthPost = new HttpPost(baseUrl);
	    // The request body must contain these 5 values.
		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
		parametersBody.add(new BasicNameValuePair("grant_type", "password"));
		parametersBody.add(new BasicNameValuePair("client_id", clientId));
		parametersBody.add(new BasicNameValuePair("client_secret", client_secret));
		parametersBody.add(new BasicNameValuePair("username", username));
		parametersBody.add(new BasicNameValuePair("password", password));
		parametersBody.add(new BasicNameValuePair("redirect_uri", "https://login.salesforce.com/"));
	    oauthPost.setEntity((HttpEntity) new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

	    // Execute the request.
	    System.out.println("POST " + baseUrl);
	    JSONParser JSON = new JSONParser();
	    HttpResponse response = client.execute(oauthPost);
	    int code = response.getStatusLine().getStatusCode();
	    Map<String, String> oauthLoginResponse = (Map<String, String>) JSON.parse(EntityUtils.toString(response.getEntity()));
	    System.out.println("OAuth login response");
	    for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet()) 
	    {
	        System.out.println(String.format("  %s = %s", entry.getKey(), entry.getValue()));
	    }
	    System.out.println("");

	    // Get user info.
	    String userIdEndpoint = oauthLoginResponse.get("id");
	    String accessToken = oauthLoginResponse.get("access_token");
	    sb.append(accessToken);
	    sb.append("\"");
	    List<BasicNameValuePair> qsList = new ArrayList<BasicNameValuePair>();
	    qsList.add(new BasicNameValuePair("oauth_token", accessToken));
	    String queryString = URLEncodedUtils.format(qsList, HTTP.UTF_8);
	    HttpGet userInfoRequest = new HttpGet(userIdEndpoint + "?" + queryString);
	    HttpResponse userInfoResponse = client.execute(userInfoRequest);
	    Map<String, Object> userInfo = (Map<String, Object>) JSON.parse(EntityUtils.toString(userInfoResponse.getEntity()));
	    System.out.println("User info response");
	    for (Map.Entry<String, Object> entry : userInfo.entrySet()) 
	    {
	        System.out.println(String.format("  %s = %s", entry.getKey(), entry.getValue()));
	    }
	    System.out.println("");

	    // Use the user info in interesting ways.
	    System.out.println("Username is " + userInfo.get("username"));
	    System.out.println("User's email is " + userInfo.get("email"));
	    Map<String, String> urls = (Map<String, String>)userInfo.get("urls");
	    System.out.println("REST API url is " + urls.get("rest").replace("{version}", "40.0"));
	    return sb.toString();
	}
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value="/salesforce/info")
	public void crawl(@RequestBody CrawlerQuery query) {
		System.out.println("https://na35.salesforce.com/services/data/v40.0/sobjects/SObject/fieldName/fieldValue");
	}
}