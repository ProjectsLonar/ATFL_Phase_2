package com.atflMasterManagement.masterservice.servcies;

import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;
 

@Service
public class PrivacyPolicyServiceImpl implements PrivacyPolicyService,CodeMaster{

	@Autowired
	private LtMastCommonMessageService ltMastCommonMessageService;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Override
	public Status readPrivacyPolicy() throws ServiceException {

		JSONParser parser = new JSONParser();
		Status status = new Status();
		try {

			final Resource fileResource = resourceLoader.getResource("classpath:privacyPolicy.json");
			
			Object obj = parser.parse(new FileReader(fileResource.getFile()));
 
			JSONObject jsonObject = (JSONObject) obj;
 
			JSONArray companyList = (JSONArray) jsonObject.get("Data");
			
			status= ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(companyList);
			return status;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		status= ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	@Override
	public Status termsAndConditions(Long orgId) throws ServiceException {
		Status status = new Status();
		Document htmlFile = null;
		try {
			final Resource fileResource = resourceLoader.getResource("classpath:termsAndConditions.html");
			String privacyPolicyStr = null;
			//htmlFile = Jsoup.parse(new File("/resources/templates/privacypolicy.html"), "ISO-8859-1");
			htmlFile = Jsoup.parse(new File(fileResource.getURI()), "ISO-8859-1");
			privacyPolicyStr = htmlFile.toString();
			status= ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(privacyPolicyStr);
			return status;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		status= ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;	// TODO Auto-generated method stub
	}

	@Override
	public Status privacypolicy(Long orgId) throws ServiceException {
		Status status = new Status();
		Document htmlFile = null;
		try {
			final Resource fileResource = resourceLoader.getResource("classpath:privacyPolicy.html");
			String privacyPolicyStr = null;
			//htmlFile = Jsoup.parse(new File("/resources/templates/privacypolicy.html"), "ISO-8859-1");
			htmlFile = Jsoup.parse(new File(fileResource.getURI()), "ISO-8859-1");
			privacyPolicyStr = htmlFile.toString();
			status= ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(privacyPolicyStr);
			return status;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		status= ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;	// TODO Auto-generated method stub
	}

}
