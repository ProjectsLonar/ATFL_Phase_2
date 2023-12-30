package com.lonar.sms.atflSmsService.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.dao.LtMastSmsTokenDao;
import com.lonar.sms.atflSmsService.dao.LtMastUsersDao;
import com.lonar.sms.atflSmsService.dao.LtOrganisationSMSDAO;
import com.lonar.sms.atflSmsService.model.CodeMaster;
import com.lonar.sms.atflSmsService.model.LtMastSmsToken;
import com.lonar.sms.atflSmsService.model.LtOrganisationSMS;
import com.lonar.sms.atflSmsService.model.Status;

@Service
public class LtMastSmsTokenServiceImpl implements LtMastSmsTokenService, CodeMaster {

	@Autowired
	LtMastSmsTokenDao ltMastSmsTokenDao;
	
	@Autowired
	LtMastUsersDao ltMastUsersDao;
	
	@Autowired
	LtOrganisationSMSDAO ltOrganisationSMSDAO;

	@Override
	public Status sendSms(String userId, Long transId) throws ServiceException, IOException {
		Status status = new Status();
		List<LtMastSmsToken> ltMastSmsTokenList = ltMastSmsTokenDao.getBySmsId(userId, transId);

		for (Iterator iterator = ltMastSmsTokenList.iterator(); iterator.hasNext();) {
			LtMastSmsToken ltMastSmsToken = (LtMastSmsToken) iterator.next();
			status = sendMessage(ltMastSmsToken);
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

		
		LtOrganisationSMS ltOrganisationSMS=ltOrganisationSMSDAO.getOrgSmsbyOrgId();
		
		if (ltOrganisationSMS != null) {

			// String mainUrl = ltOrganisationSMS.getSmsUrl().toString();
			String mainUrl = ltOrganisationSMS.getSmsUrl().toString();
			mainUrl = mainUrl.replace("#sendOtp#", ltMastSmsToken.getSmsObject());
			mainUrl = mainUrl.replace("#mobileNumber#", ltMastSmsToken.getSendTo().toString());
			StringBuilder sbPostData = new StringBuilder(mainUrl);
			mainUrl = sbPostData.toString();
			try {
				// prepare connection
				/*myURL = new URL(mainUrl);
				myURLConnection = myURL.openConnection();
				myURLConnection.connect();
				reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
				// reading response
				String response;
				while ((response = reader.readLine()) != null)
				System.out.println(response);
				reader.close();
				status.setCode(SUCCESS);*/
				
				///////
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
	public Status getSmsBalance(String supplierId) throws ServiceException, IOException {
		Status status  =new Status();
		/*StringBuffer GET_URL = new StringBuffer(ltOrganisationSMS.getSmsUrl() + "username=" + ltOrganisationSMS.getUserName()
				+ "&api_key=" + ltOrganisationSMS.getApiKey() + "&check_credit=1");*/
		String GET_URL="";
		URL url = new URL(GET_URL.toString());
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		String message = print_content(con);
		if(message!=null) {
			status.setCode(SUCCESS);
			status.setMessage(message);
		}else {
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
	

}
