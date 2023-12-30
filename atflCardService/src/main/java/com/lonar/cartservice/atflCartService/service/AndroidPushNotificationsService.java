package com.lonar.cartservice.atflCartService.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class AndroidPushNotificationsService {
	
	@Autowired
	private Environment env;

	//private static final String FIREBASE_SERVER_KEY = "AAAAYLymvbU:APA91bFymXZfuiP_9d3aet9KnboP2Y9hXdB88XZji3D2pIvywrxDV4zBk4vksZ-WsbE5cUyaySsQhQTXavZ8iDAmqbi0Bhk4MXoOs1KMQbIfS8bZ2F3ZI5zaInxylf5R9ryCMhITA4UO";
	//private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
	
	@Async
	public CompletableFuture<String> send(HttpEntity<String> entity) {
		
		String fireBaseServerKey = env.getProperty("FIREBASE_SERVER_KEY");
		String fireBaseApiUrl = env.getProperty("FIREBASE_API_URL");

		RestTemplate restTemplate = new RestTemplate();

		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		//interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + fireBaseServerKey));
		
		restTemplate.setInterceptors(interceptors);

		//String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
		String firebaseResponse = restTemplate.postForObject(fireBaseApiUrl, entity, String.class);

		return CompletableFuture.completedFuture(firebaseResponse);
	}
}
