package com.users.usersmanagement.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumeApiService {

	public <T> List<T> consumeApi(String query, Object[] body,Class<T> clazz) throws IOException, InterruptedException {
        List<T> result = new ArrayList<>();
//		List<LtMastUsers> ltMastUsers = new ArrayList<LtMastUsers>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());

        // Convert the Object[] to JSON string
        String jsonBody = objectMapper.writeValueAsString(body);
        System.out.println("jsonReqBody = " +jsonBody);
        
        // Build the URI
        //String uri = "http://10.245.4.74/OrderApi/ExecuteQueryWithParams?query=" + encodedQuery;
        String uri = "http://174.138.187.142:8085/OrderApi/ExecuteQueryWithParams?query=" + encodedQuery; 
        
        // Create HttpPost request
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
            System.out.println("After response body = "+ LocalDateTime.now());
        
            //List<LtMastUsers> usersArray = objectMapper.readValue(responseBody, LtMastUsers(LtMastUsers.class));
            //ltMastUsers = objectMapper.readValue(responseBody, new TypeReference<List<LtMastUsers>>() {});
            
            result = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            System.out.println("Result = " + result);
            
            //System.out.println("LtMastUsers = " + ltMastUsers);
       }
//		return ltMastUsers;
        return result;
    }
	
	public Long consumeApiForCount(String query, Object[] body) throws IOException, InterruptedException {
        Long count;
//		List<LtMastUsers> ltMastUsers = new ArrayList<LtMastUsers>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());

        // Convert the Object[] to JSON string
        String jsonBody = objectMapper.writeValueAsString(body);
        System.out.println("query = " + query);
        System.out.println("jsonBody = " + jsonBody);
        
        // Build the URI
        //String uri = "http://10.245.4.74/OrderApi/ExecuteCountQueryWithParams?query=" + encodedQuery;
        String uri = "http://174.138.187.142:8085/OrderApi/ExecuteCountQueryWithParams?query=" + encodedQuery; 
        
        // Create HttpPost request
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
    //        System.out.println(responseBody);
    //        System.out.println("After response body = "+ LocalDateTime.now());
        
            //List<LtMastUsers> usersArray = objectMapper.readValue(responseBody, LtMastUsers(LtMastUsers.class));
            //ltMastUsers = objectMapper.readValue(responseBody, new TypeReference<List<LtMastUsers>>() {});
            
            count = objectMapper.readValue(responseBody, Long.class);
            System.out.println("Result = " + count);
            
            //System.out.println("LtMastUsers = " + ltMastUsers);
       }
//		return ltMastUsers;
        return count;
    }
	
	
	public String SiebelAPILog(String url, String jsonPayload, String response) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	        HttpPost postRequest = new HttpPost("http://174.138.187.142:8085/OrderApi/SiebelAPILog");
			//HttpPost postRequest = new HttpPost("http://10.245.4.74/OrderApi/SiebelAPILog");
	        postRequest.setHeader("Content-Type", "application/json");
	        postRequest.setHeader("Accept", "text/plain");//"application/json");

	        // Create JSON payload
	        String json = "{\"url\": \" "+url.toString()+" \",  \"requestBody\": "+jsonPayload+",  \"responseBody\": "+response+"}";  
            postRequest.setEntity(new StringEntity(json));

	        try (CloseableHttpResponse response1 = httpClient.execute(postRequest)) {
	            String responseBody = EntityUtils.toString(response1.getEntity(), "UTF-8");
	            System.out.println("Response: " + responseBody);
	        }
	     catch (Exception e) {
	        e.printStackTrace();
	    }
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		return response;
		}
	
	
	public String consumeApiForString(String query, Object[] body) throws IOException, InterruptedException {
        String count;
//		List<LtMastUsers> ltMastUsers = new ArrayList<LtMastUsers>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());

        // Convert the Object[] to JSON string
        String jsonBody = objectMapper.writeValueAsString(body);

        // Build the URI
        //String uri = "http://10.245.4.74/OrderApi/ExecuteCountQueryWithParams?query=" + encodedQuery;
        String uri = "http://174.138.187.142:8085/OrderApi/ExecuteCountQueryWithParams?query=" + encodedQuery; 
        
        // Create HttpPost request
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
   //         System.out.println(responseBody);
   //         System.out.println("After response body = "+ LocalDateTime.now());
        
            //List<LtMastUsers> usersArray = objectMapper.readValue(responseBody, LtMastUsers(LtMastUsers.class));
            //ltMastUsers = objectMapper.readValue(responseBody, new TypeReference<List<LtMastUsers>>() {});
            
            count = responseBody.toString();
  //          System.out.println("Result = " + count);
            
            //System.out.println("LtMastUsers = " + ltMastUsers);
       }
//		return ltMastUsers;
        return count;
    }

}
