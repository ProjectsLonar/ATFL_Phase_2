package com.lonar.cartservice.atflCartService.controller;

import java.io.IOException;
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

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dao.LtSoHeadersDao;
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
	
	@Autowired
	LtSoHeadersDao ltSoHeadersDao;

	Long userId = 0L;
	//String distributorId;
	
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
		//body.put("to", ltMastUsers.getTokenData());  original code comment on 30-Sep-2024 bcz venkat query in use
		body.put("to", ltMastUsers.getMobileNumber());
		body.put("collapse_key", "type_a");
		//body.put("priority", "high");

		
		try {
			userId = ltSoHeadersDao.getUserIdFromMobileNo(ltMastUsers.getMobileNumber());
//			distributorId= ltSoHeadersDao.getDistIdFromOutletCode(outletCode);
			System.out.println("Notification userId == " +userId);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject notification = new JSONObject();
		notification.put("title", "Pending approval");
		notification.put("body", outletName +"( "+outletCode +")"+" has placed an "+orderType +" order "+orderNo+" and is pending for approval.");
		
		JSONObject data = new JSONObject();
		data.put("objectType", "order");
		data.put("userId", userId);
		data.put("outletId", ltSoHeader.getOutletId());
		data.put("outletName", outletName);
		data.put("outletCode", outletCode);
		data.put("orderNumber", ltSoHeader.getOrderNumber());
		data.put("click_action", "FLUTTER_NOTIFICATION_CLICK");

		body.put("notification", notification);
		body.put("data", data);
		
		CompletableFuture<String> pushNotification = null;
		try {
		saveNotificationDetails(ltMastUsers, data, notification,ltSoHeader);
		
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		
		 pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}System.out.println("Hi THis is notification catch block...+ pushNotification" + pushNotification);

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}

	
public ResponseEntity<String> sendSalesReturnNotification(LtSalesReturnHeader ltSalesReturnHeader, LtMastUsers ltMastUsers) throws JSONException {
	System.out.println("Hi I'm in sales return notification");
		JSONObject body = new JSONObject();
		//body.put("to", "/topics/" + TOPIC);
		//body.put("to", ltMastUsers.getTokenData()); comment on 14-Oct-2024 bcz venkat query in use
		body.put("collapse_key", "type_a");
		//body.put("priority", "high");

		String outletName = "";
		String salesReturnNo = "";
		if(ltSalesReturnHeader.getOutletName() != null) {
			outletName = ltSalesReturnHeader.getOutletName();
		}
		
		try {
			userId = ltSoHeadersDao.getUserIdFromMobileNo(ltMastUsers.getMobileNumber());
//			distributorId= ltSoHeadersDao.getDistIdFromOutletCode(outletCode);
			System.out.println("Notification userId == " +userId);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		data.put("userId", userId);
		data.put("outletName",  ltSalesReturnHeader.getOutletName());
		//data.put("outletType",  ltSalesReturnHeader.getOutletType());
		data.put("outletId",  ltSalesReturnHeader.getOutletId());
		data.put("click_action",  "FLUTTER_NOTIFICATION_CLICK");  
		
		body.put("notification", notification);
		body.put("data", data);
		
		CompletableFuture<String> pushNotification = null;
		try {
		//commented beacuse of outletid datatype mismatch
		saveNotificationDetailsForSalesReturn(ltMastUsers, data, notification,ltSalesReturnHeader);

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
	private void saveNotificationDetailsForSalesReturn(LtMastUsers ltMastUsers,JSONObject data,JSONObject notificationTitle,LtSalesReturnHeader ltSalesReturnHeader ) {
		NotificationDetails notification = new NotificationDetails();
		notification.setReadFlag("N");
		notification.setUserId(userId);
//		notification.setUserId(ltMastUsers.getUserId());  // Pass PENDINGAPPROVAL status user id
		notification.setTokenId(ltMastUsers.getTokenData());   // Token Pass
		notification.setNotificationTitle(notificationTitle.toString());
		notification.setNotificationBody(data.toString());    //(data.toJSONString());
//		notification.setDistributorId(ltMastUsers.getDistributorId());
		notification.setDistributorId(ltSalesReturnHeader.getDistributorId());	
		notification.setSendDate(new Date());
		notificationDetailsRepository.save(notification);
	}

	
	@Transactional
	private void saveNotificationDetails(LtMastUsers ltMastUsers, JSONObject data, JSONObject  notification, LtSoHeaders ltSoHeader) {
		NotificationDetails notifictn  = new NotificationDetails();
		notifictn.setReadFlag("N");
//		notifictn.setUserId(ltMastUsers.getUserId()); // original code comment on 30-sep-2024 bcz venkat query not have userid
		notifictn.setUserId(userId);
		notifictn.setTokenId(ltMastUsers.getTokenData()); 
//		notifictn.setDistributorId(ltMastUsers.getDistributorId()); this is original code comment on 13-Oct-24
		notifictn.setDistributorId(ltSoHeader.getDistributorId());
		notifictn.setNotificationBody(data.toString());
		notifictn.setNotificationTitle(notification.toString());
		notifictn.setSendDate(new Date());
		notificationDetailsRepository.save(notifictn);
	}
}
