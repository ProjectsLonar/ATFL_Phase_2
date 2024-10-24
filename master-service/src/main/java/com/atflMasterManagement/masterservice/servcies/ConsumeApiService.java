package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.atflMasterManagement.masterservice.dto.QueryHostName;

public class ConsumeApiService {

	String host= new QueryHostName().getHostName();
	//String host = "http://174.138.187.142:8085";
	
	public <T> List<T> consumeApi(String query, Object[] body,Class<T> clazz) throws IOException, InterruptedException {
        List<T> result = new ArrayList<>();
//		List<LtMastUsers> ltMastUsers = new ArrayList<LtMastUsers>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());

        // Convert the Object[] to JSON string
        String jsonBody = objectMapper.writeValueAsString(body);

        // Build the URI
        //String uri = "http://10.245.4.74/OrderApi/ExecuteQueryWithParams?query=" + encodedQuery;
        String uri = host +"/OrderApi/ExecuteQueryWithParams?query=" + encodedQuery; 
        
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

        // Build the URI
        //String uri = "http://10.245.4.74/OrderApi/ExecuteQueryWithParams?query=" + encodedQuery;
        String uri = host +"/OrderApi/ExecuteCountQueryWithParams?query=" + encodedQuery; 
        
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
            
            count = objectMapper.readValue(responseBody, Long.class);
            System.out.println("Result = " + count);
            
            //System.out.println("LtMastUsers = " + ltMastUsers);
       }
//		return ltMastUsers;
        return count;
    }

	public class ApiRequestBody {
		public String query;
		public Object[] parameterArray;
		
		
	public String getQuery() {
			return query;
		}


		public void setQuery(String query) {
			this.query = query;
		}


		public Object[] getParameterArray() {
			return parameterArray;
		}


		public void setParameterArray(Object[] parameterArray) {
			this.parameterArray = parameterArray;
		}

		
	@Override
		public String toString() {
			return "ConsumeApiService [query=" + query + ", parameterArray=" + Arrays.toString(parameterArray) + "]";
		}

	}
		
	
	public <T> List<T> consumeApiWithRequestBody(String query, Object[] body,Class<T> clazz) throws IOException, InterruptedException {
        List<T> result = new ArrayList<>();
     
        
//		List<LtMastUsers> ltMastUsers = new ArrayList<LtMastUsers>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        
        ApiRequestBody apiRequestBody = new ApiRequestBody();
        apiRequestBody.setQuery(query);
        apiRequestBody.setParameterArray(body);
        
        //System.out.println("apiRequestBody for headerIdList is = "+ apiRequestBody);
        // Convert the Object[] to JSON string
        String jsonBody = objectMapper.writeValueAsString(apiRequestBody);
  //      System.out.println("apiRequestBody for headerIdList is = "+ jsonBody);
        
        // Build the URI
           //String uri = "http://10.245.4.74/OrderApi/ExecuteQueryWithParamsWithRequestBody"; // this is for uat server
           String uri = host +"/OrderApi/ExecuteQueryWithParamsWithRequestBody";  // this is for local
        
        // Create HttpPost request
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
          System.out.println("jsonBody =" +jsonBody);
   //         System.out.println("After response body = "+ LocalDateTime.now());
        
            //List<LtMastUsers> usersArray = objectMapper.readValue(responseBody, LtMastUsers(LtMastUsers.class));
            //ltMastUsers = objectMapper.readValue(responseBody, new TypeReference<List<LtMastUsers>>() {});
            
            result = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
   //         System.out.println("Result = " + result);
            
            //System.out.println("LtMastUsers = " + ltMastUsers);
       }
//		return ltMastUsers;
        return result;
    }
	
	
	public Long consumeApiForCountWithRequestBody(String query, Object[] body) throws IOException, InterruptedException {
        Long count;
//		List<LtMastUsers> ltMastUsers = new ArrayList<LtMastUsers>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());

        ApiRequestBody apiRequestBody = new ApiRequestBody();
        apiRequestBody.setQuery(query);
        apiRequestBody.setParameterArray(body);
        
      //  System.out.println("apiRequestBody for count is = "+ apiRequestBody);
        // Convert the Object[] to JSON string
        String jsonBody = objectMapper.writeValueAsString(apiRequestBody);
 //       System.out.println("apiRequestBody for count is = "+ jsonBody);

        // Build the URI
           //String uri = "http://10.245.4.74/OrderApi/ExecuteCountQueryWithRequestBody";
           String uri = host +"/OrderApi/ExecuteCountQueryWithRequestBody"; 
        
        // Create HttpPost request
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
  //          System.out.println(responseBody);
  //          System.out.println("After response body = "+ LocalDateTime.now());
            
            //List<LtMastUsers> usersArray = objectMapper.readValue(responseBody, LtMastUsers(LtMastUsers.class));
            //ltMastUsers = objectMapper.readValue(responseBody, new TypeReference<List<LtMastUsers>>() {});
            
            count = objectMapper.readValue(responseBody, Long.class);
   //         System.out.println("Result = " + count);
            
            //System.out.println("LtMastUsers = " + ltMastUsers);
       }
//		return ltMastUsers;
        return count;
    }

}
