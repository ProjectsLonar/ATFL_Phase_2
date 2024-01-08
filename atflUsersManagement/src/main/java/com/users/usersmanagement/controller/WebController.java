package com.users.usersmanagement.controller;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.users.usersmanagement.model.LtMastOutletsDump;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.repository.NotificationDetailsRepository;
import com.users.usersmanagement.service.AndroidPushNotificationsService;


@Component
public class WebController {
	
	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepository;

	//@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send(LtMastUsers ltMastUsers, LtMastUsers pendingAppUsers) throws JSONException {
		
		JSONObject body = new JSONObject();
		//body.put("to", "/topics/" + TOPIC);
		body.put("to", ltMastUsers.getToken());
		body.put("collapse_key", "type_a");
		//body.put("priority", "high");

		String pendingUserName = "";
		String pendingUserType = "";
		if(pendingAppUsers.getUserName() != null) {
			pendingUserName = pendingAppUsers.getUserName();
		}
		
		if(pendingAppUsers.getUserType() != null) {
			pendingUserType = pendingAppUsers.getUserType(); 
		}
		JSONObject notification = new JSONObject();
		notification.put("title", "Pending activation");
		notification.put("body", "A new user, "+pendingUserName+" "+pendingUserType + " has created a new profile and is pending activation. Please Activate" );
		
		JSONObject data = new JSONObject();
		data.put("objectType", "user");
		data.put("userId",  pendingAppUsers.getUserId()); 
		data.put("click_action",  "FLUTTER_NOTIFICATION_CLICK");  
		
		body.put("notification", notification);
		body.put("data", data);
		
		saveNotificationDetails(ltMastUsers, pendingAppUsers.getUserId(), data, notification);

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}

	@Transactional
	private void saveNotificationDetails(LtMastUsers ltMastUsers, Long userId, JSONObject data,JSONObject notificationTitle) {
		NotificationDetails notification = new NotificationDetails();
		notification.setReadFlag("N");
		notification.setUserId(ltMastUsers.getUserId());  // Pass PENDINGAPPROVAL status user id
		notification.setTokenId(ltMastUsers.getToken());   // Token Pass
		notification.setNotificationTitle(notificationTitle.toString());
		notification.setNotificationBody(data.toJSONString());
		notification.setDistributorId(ltMastUsers.getDistributorId());
		notification.setSendDate(new Date());
		notificationDetailsRepository.save(notification);
	}
	
	public ResponseEntity<String> sendOutletApprovalNotification(LtMastUsers ltMastUsers, LtMastOutletsDump ltMastOutletsDump) throws JSONException {
		
		JSONObject body = new JSONObject();
		//body.put("to", "/topics/" + TOPIC);
		body.put("to", ltMastUsers.getToken());
		body.put("collapse_key", "type_a");
		//body.put("priority", "high");

		String pendingOutletName = "";
		String pendingOutletType = "";
		if(ltMastOutletsDump.getOutletName() != null) {
			pendingOutletName = ltMastOutletsDump.getOutletName();
		}
		
		if(ltMastOutletsDump.getOutletType()!= null) {
			pendingOutletType = ltMastOutletsDump.getOutletType(); 
		}
		JSONObject notification = new JSONObject();
		notification.put("title", "Pending activation");
		notification.put("body", "A new retailer, "+pendingOutletName+" "+pendingOutletType + " has created a new profile and is pending activation. Please Activate" );
		
		JSONObject data = new JSONObject();
		data.put("objectType", "retailer");
		data.put("userId",  ltMastOutletsDump.getUserId()); 
		//data.put("outletName",  ltMastOutletsDump.getOutletName());
		//data.put("outletType",  ltMastOutletsDump.getOutletType());
		//data.put("outletId",  ltMastOutletsDump.getOutletId());
		data.put("click_action",  "FLUTTER_NOTIFICATION_CLICK");  
		
		body.put("notification", notification);
		body.put("data", data);
		
		//commented beacuse of outletid datatype mismatch
		saveNotificationDetailsForOutletApproval(ltMastUsers, data, notification);

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
	
	@Transactional
	private void saveNotificationDetailsForOutletApproval(LtMastUsers ltMastUsers,JSONObject data,JSONObject notificationTitle) {
		NotificationDetails notification = new NotificationDetails();
		notification.setReadFlag("N");
		notification.setUserId(ltMastUsers.getUserId());  // Pass PENDINGAPPROVAL status user id
		notification.setTokenId(ltMastUsers.getToken());   // Token Pass
		notification.setNotificationTitle(notificationTitle.toString());
		notification.setNotificationBody(data.toJSONString());
		notification.setDistributorId(ltMastUsers.getDistributorId());
		notification.setSendDate(new Date());
		notificationDetailsRepository.save(notification);
	}
	
}
