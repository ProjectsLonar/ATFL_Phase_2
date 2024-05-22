package com.lonar.cartservice.atflCartService.controller;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.NotificationDetails;
import com.lonar.cartservice.atflCartService.repository.NotificationDetailsRepository;
import com.lonar.cartservice.atflCartService.service.AndroidPushNotificationsService;



//@RestController
@Component
public class WebController {

	//private final String TOPIC = "JavaSampleApproach";
	
	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepository;

	//@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send(LtMastUsers ltMastUsers, LtSoHeaders ltSoHeader
			,String outletCode, String outletName ) throws JSONException {
		
		String orderType = "Instock";
		String orderNo = "";
		if(ltSoHeader.getInStockFlag()!= null ) {
			if(ltSoHeader.getInStockFlag().equalsIgnoreCase("Y")) {
				 orderType= "Instock";
			}else {
				 orderType= "Out of stock";
			}
		}
		
		if(ltSoHeader.getOrderNumber()!= null) {
			orderNo = ltSoHeader.getOrderNumber();
		}
		
		JSONObject body = new JSONObject();
		//body.put("to", "/topics/" + TOPIC);
		body.put("to", ltMastUsers.getTokenData());
		body.put("collapse_key", "type_a");
		//body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "Pending approval");
		notification.put("body", outletName +"( "+outletCode +")"+" has placed an "+orderType +" order "+orderNo+" and is pending for approval.");
		
		JSONObject data = new JSONObject();
		data.put("objectType", "order");
		data.put("outletId", ltSoHeader.getOutletId());
		data.put("outletName", outletName);
		data.put("outletCode", outletCode);
		data.put("orderNumber", ltSoHeader.getOrderNumber());
		data.put("click_action", "FLUTTER_NOTIFICATION_CLICK");

		body.put("notification", notification);
		body.put("data", data);
		
		saveNotificationDetails(ltMastUsers, data, notification);
		
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

	
public ResponseEntity<String> sendSalesReturnNotification(LtSalesReturnHeader ltSalesReturnHeader, LtMastUsers ltMastUsers) throws JSONException {
	System.out.println("Hi I'm in sales return notification");
		JSONObject body = new JSONObject();
		//body.put("to", "/topics/" + TOPIC);
		body.put("to", ltMastUsers.getTokenData());
		body.put("collapse_key", "type_a");
		//body.put("priority", "high");

		String outletName = "";
		String salesReturnNo = "";
		if(ltSalesReturnHeader.getOutletName() != null) {
			outletName = ltSalesReturnHeader.getOutletName();
		}
		
		if(ltSalesReturnHeader.getSalesReturnNumber() != null) {
			salesReturnNo = ltSalesReturnHeader.getSalesReturnNumber(); 
		}
		JSONObject notification = new JSONObject();
		notification.put("title", "Pending approval");
		notification.put("body", ""+outletName+"  has created a salesr return order "+salesReturnNo + " and is pending for approval." );
		
		JSONObject data = new JSONObject();
		data.put("objectType", "retailer");
		//data.put("userId",  ltSalesReturnHeader.getUserId()); 
		data.put("outletName",  ltSalesReturnHeader.getOutletName());
		//data.put("outletType",  ltSalesReturnHeader.getOutletType());
		data.put("outletId",  ltSalesReturnHeader.getOutletId());
		data.put("click_action",  "FLUTTER_NOTIFICATION_CLICK");  
		
		body.put("notification", notification);
		body.put("data", data);
		
		//commented beacuse of outletid datatype mismatch
		saveNotificationDetailsForSalesReturn(ltMastUsers, data, notification);

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
	private void saveNotificationDetailsForSalesReturn(LtMastUsers ltMastUsers,JSONObject data,JSONObject notificationTitle) {
		NotificationDetails notification = new NotificationDetails();
		notification.setReadFlag("N");
		notification.setUserId(ltMastUsers.getUserId());  // Pass PENDINGAPPROVAL status user id
		notification.setTokenId(ltMastUsers.getTokenData());   // Token Pass
		notification.setNotificationTitle(notificationTitle.toString());
		notification.setNotificationBody(data.toString());    //(data.toJSONString());
		notification.setDistributorId(ltMastUsers.getDistributorId());
		notification.setSendDate(new Date());
		notificationDetailsRepository.save(notification);
	}

	
	@Transactional
	private void saveNotificationDetails(LtMastUsers ltMastUsers, JSONObject data, JSONObject  notification) {
		NotificationDetails notifictn  = new NotificationDetails();
		notifictn.setReadFlag("N");
		notifictn.setUserId(ltMastUsers.getUserId()); 
		notifictn.setTokenId(ltMastUsers.getTokenData()); 
		notifictn.setDistributorId(ltMastUsers.getDistributorId());
		notifictn.setNotificationBody(data.toString());
		notifictn.setNotificationTitle(notification.toString());
		notifictn.setSendDate(new Date());
		notificationDetailsRepository.save(notifictn);
	}
}
