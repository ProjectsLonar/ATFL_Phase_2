 package com.users.usersmanagement.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastSmsTokenDao;
import com.users.usersmanagement.dao.LtOrganisationSMSDAO;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastSmsToken;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.LtOrganisationSMS;
import com.users.usersmanagement.model.Status;

@Service
public class LtMastSmsTokenServiceImpl implements LtMastSmsTokenService, CodeMaster {

	@Autowired
	LtMastSmsTokenDao ltMastSmsTokenDao;

	@Autowired
	AtflMastUsersDao ltMastUsersDao;

	@Autowired
	LtOrganisationSMSDAO ltOrganisationSMSDAO;

	@Override
	public Status sendSms(Long userId, String transId) throws ServiceException, IOException {
		Status status = new Status();
		List<LtMastSmsToken> ltMastSmsTokenList = ltMastSmsTokenDao.getBySmsId(userId, transId);

		for (Iterator iterator = ltMastSmsTokenList.iterator(); iterator.hasNext();) {
			LtMastSmsToken ltMastSmsToken = (LtMastSmsToken) iterator.next();

			if (ltMastSmsToken.getSmsType().equalsIgnoreCase("OTP")) {
				status = sendMessage(ltMastSmsToken);
			} else if (ltMastSmsToken.getSmsType().equalsIgnoreCase("STATUS CHANGE")) {
				status = sendStatusChangeMessage(ltMastSmsToken);
			}

			if (status.getCode() == SUCCESS) {
				ltMastSmsTokenDao.updateStatus("SEND", userId, transId);
			} else {
				ltMastSmsTokenDao.updateStatus("FAIL TO SEND", userId, transId);
			}
		}

		return status;
	}

	private Status sendMessage(LtMastSmsToken ltMastSmsToken) throws IOException, ServiceException {
		Status status = new Status();

		LtOrganisationSMS ltOrganisationSMS = ltOrganisationSMSDAO.getsmsTemplateByType("OTP");

		if (ltOrganisationSMS != null) {

			// String mainUrl = ltOrganisationSMS.getSmsUrl().toString();
			String mainUrl = ltOrganisationSMS.getSmsUrl().toString();
			mainUrl = mainUrl.replace("#sendOtp#", ltMastSmsToken.getSmsObject());
			mainUrl = mainUrl.replace("#mobileNumber#", ltMastSmsToken.getSendTo().toString());
			StringBuilder sbPostData = new StringBuilder(mainUrl);
			mainUrl = sbPostData.toString();
			try {
				URL obj = new URL(mainUrl.toString());
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					status.setCode(SUCCESS);
				} else {
					status.setCode(FAIL);
					System.out.println("GET request not worked");
				}
			} catch (IOException e) {
				status.setCode(FAIL);
				System.out.println("GET request not worked");
				e.printStackTrace();
			}
		} else {
			status.setCode(FAIL);
			System.out.println("GET request not worked");
		}
		return status;
	}


	@Override
	public Status getSmsBalance(Long supplierId) throws ServiceException, IOException {
		Status status = new Status();
		/*
		 * StringBuffer GET_URL = new StringBuffer(ltOrganisationSMS.getSmsUrl() +
		 * "username=" + ltOrganisationSMS.getUserName() + "&api_key=" +
		 * ltOrganisationSMS.getApiKey() + "&check_credit=1");
		 */
		String GET_URL = "";
		URL url = new URL(GET_URL.toString());
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		String message = print_content(con);
		if (message != null) {
			status.setCode(SUCCESS);
			status.setMessage(message);
		} else {
			status.setCode(FAIL);
		}
		return status;
	}

	private static String print_content(HttpsURLConnection con) {
		if (con != null) {
			try {
				System.out.println("****** Content of the URL ********");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				while ((input = br.readLine()) != null) {
					System.out.println(input);
					return input;
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Status sendStatusChangeMessage(LtMastSmsToken ltMastSmsToken)
			throws IOException, ServiceException {

		Status status = new Status();

		LtOrganisationSMS ltOrganisationSMS = ltOrganisationSMSDAO.getsmsTemplateByType("STATUS CHANGE");

		if (ltOrganisationSMS != null) {

			LtMastUsers ltMastUser = ltMastUsersDao.getUserById(ltMastSmsToken.getUserId());

			String smsUrl = ltOrganisationSMS.getSmsUrl().toString();

			URL url = new URL(smsUrl.toString());

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json; utf-8");
			
			con.setDoOutput(true);

			Map<String,String> obj=new HashMap<String,String>();

			obj.put("mobiles", ltMastSmsToken.getSendTo().toString());
			obj.put("authkey", ltOrganisationSMS.getAuthkey().toString());
			obj.put("template_id", ltOrganisationSMS.getTemplateId().toString());
			obj.put("user", ltMastUser.getUserName());
			obj.put("COMPANY_NAME", ltOrganisationSMS.getCompanyName());
			obj.put("status", ltMastUser.getStatus());

			if (ltMastUser.getStatus().equalsIgnoreCase(ACTIVE)) {
				obj.put("activity", "Start");
			} else if (ltMastUser.getStatus().equalsIgnoreCase(INACTIVE)) {
				obj.put("activity", "Stop");
			} else if (ltMastUser.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
				obj.put("activity", "Pending");
			}
			String jsonInputString =JSONValue.toJSONString(obj);
			
			System.out.println("jsonInputString====>"+jsonInputString);

			try (OutputStream os = con.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				
				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					status.setCode(SUCCESS);
				} else {
					status.setCode(FAIL);
					System.out.println("GET request not worked");
				}
				System.out.println(response.toString());
			}catch (Exception e) {
				// TODO: handle exception
				status.setCode(FAIL);
				System.out.println("GET request not worked");
			}

			
			
		} else {
			status.setCode(FAIL);
			System.out.println("GET request not worked");
		}
		return status;
	}

}
