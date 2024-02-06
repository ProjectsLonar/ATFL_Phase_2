package com.lonar.cartservice.atflCartService.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lonar.cartservice.atflCartService.model.Status;

@RestController
@RequestMapping("/siebel")
public class LtSiebelController {
	
	@GetMapping(value = "/siebelCreateOutlet", produces = MediaType.APPLICATION_JSON_VALUE)
	public Status getTransactionDetails() {
		Status status = new Status();
		 try {
	            // Create HttpClient
	            HttpClient httpClient = HttpClients.createDefault();

	            // Define API endpoint
	           // String apiUrl = "https://10.245.4.70:9014/siebel/v1.0/service/Siebel Outlet Integration/InsertOrUpdate?matchrequestformat=y";

	            String apiUrl = "https://10.245.4.70:9014/siebel/v1.0/service/Siebel%20Outlet%20Integration/InsertOrUpdate?matchrequestformat=y";
	            // Create JSON request body
	            String jsonBody = "{" +
	                    "\"SiebelMessage\": {" +
	                    "    \"IntObjectName\": \"Outlet Interface\"," +
	                    "    \"ListOfOutlet Interface\": {" +
	                    "        \"Account\": {" +
	                    "            \"Account Id\": \"1\"," +
	                    "            \"ListOfRelated Organization\": {" +
	                    "                \"Related Organization\": {" +
	                    "                    \"Organization\": \"Agro Tech Foods Limited\"," +
	                    "                    \"IsPrimaryMVG\": \"Y\"" +
	                    "                }" +
	                    "            }," +
	                    "            \"Type\": \"Retailer\"," +
	                    "            \"Rule Attribute 2\": \"Whole Sellers\"," +
	                    "            \"Account Status\": \"Active\"," +
	                    "            \"ListOfBusiness Address\": {" +
	                    "                \"Business Address\": [" +
	                    "                    {" +
	                    "                        \"Postal Code\": \"444603\"," +
	                    "                        \"State\": \"MH\"," +
	                    "                        \"Address Id\": \"1\"," +
	                    "                        \"Street Address\": \"West Side\"," +
	                    "                        \"Country\": \"India\"," +
	                    "                        \"City\": \"Aurangabad\"," +
	                    "                        \"County\": \"\"," +
	                    "                        \"Street Address 2\": \"Amravati\"," +
	                    "                        \"Province\": \"\"," +
	                    "                        \"IsPrimaryMVG\": \"Y\"" +
	                    "                    }" +
	                    "                ]" +
	                    "            }," +
	                    "            \"Name\": \"Dmart Traders Limited\"," +
	                    "            \"Location\": \"Pimpri\"" +
	                    "        }" +
	                    "    }," +
	                    "    \"IntObjectFormat\": \"Siebel Hierarchical\"," +
	                    "    \"MessageType\": \"Integration Object\"," +
	                    "    \"MessageId\": \"\"" +
	                    "}" +
	                    "}";

	            // Create HttpPost request
	            HttpPost httpPost = new HttpPost(apiUrl);
	            httpPost.setHeader("Content-Type", "application/json");
	            httpPost.setHeader("Authorization", "Basic " + getBase64Credentials("Lonar_Test", "Lonar123"));
	            httpPost.setEntity(new StringEntity(jsonBody, "UTF-8"));

	            // Execute the request and get the response
	            org.apache.http.HttpResponse response = httpClient.execute(httpPost);

	            // Parse and print the response
	            HttpEntity entity = response.getEntity();
	            String responseBody = EntityUtils.toString(entity);
	            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
	               System.out.println("Response Body: " + responseBody);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return status;
}
	  private static String getBase64Credentials(String username, String password) {
	        String credentials = username + ":" + password;
	        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	    }
}
