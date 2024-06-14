package com.eureka.auth.service;

import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.service.spi.ServiceException;
import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.eureka.auth.dao.AtflMastUsersDao;
import com.eureka.auth.model.CodeMaster;
import com.eureka.auth.model.LtMastLogins;
import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;
import com.eureka.auth.model.ResponceEntity;
import com.eureka.auth.model.Status;
import com.eureka.auth.model.UserLoginDto;
import com.eureka.auth.repository.LtMastLoginsRepository;



@Service
@PropertySource(value = "classpath:queries/messages.properties", ignoreResourceNotFound = true)
public class AtflMastUsersServiceImpl implements AtflMastUsersService, CodeMaster {
	
	private static final Logger logger = LoggerFactory.getLogger( AtflMastUsersServiceImpl.class);
	
	@Autowired
	private AtflMastUsersDao ltMastUsersDao;
	
	@Autowired
	private LtMastLoginsRepository ltMastLoginsRepository;
	
	@Override
	public LtMastUsers getLtMastUsers(String username) {
		return ltMastUsersDao.getLtMastUsersByMobileNumber(username);
	}

	//This is original method commented by Rohan on 3 June 2024 for optimization
//	@Override
//	public ResponceEntity login(String mobileNumber,String otp, HttpServletResponse response) throws ServiceException {
//		ResponceEntity entity = new ResponceEntity();
//		Status status = new Status();
//		System.out.println("in login service "+LocalDateTime.now());
//		System.out.println("Database call 1 at "+LocalDateTime.now());
//		LtMastUsers ltMastUser = ltMastUsersDao.getLtMastUsersByMobileNumber(mobileNumber);
//		System.out.println("Database call exit 1 at "+LocalDateTime.now());
//		System.out.println("ltMastUser = "+ltMastUser);
//		LtMastLogins ltMastLogins =new LtMastLogins();
//		System.out.println("ltMastUser.getStatus() = "+ltMastUser.getStatus());
//		if (ltMastUser.getStatus().equals("INACTIVE")) {
//			entity.setCode(FAIL);
//			entity.setMessage("User is Inactive");
//			return entity;
//		}
//		System.out.println("Above ltMastLogin = "+new Date());
//		System.out.println("Database call 2 at "+LocalDateTime.now());
//		//change query of first call according to second call
//		LtMastLogins ltMastLogin = ltMastUsersDao.getLoginDetailsByUserId(ltMastUser.getUserId());//avoid 63 tag
//		System.out.println("Database call exit 2 at "+LocalDateTime.now());
//		System.out.println("Below ltMastLogin = "+new Date());
//		
//		System.out.println("ltMastLogin = "+ltMastLogin);
//		if (ltMastLogin != null) {
//			System.out.println("in ltMastLogin not null");
//			if (ltMastLogin.getOtp().equals(otp)) {
//				System.out.println("in otp equals");
//				ltMastLogin.setStatus(SUCCESS_LOGIN);
//				ltMastLogin.setLoginDate(new Date());
//				ltMastLogins = ltMastLoginsRepository.save(ltMastLogin);
//				status.setCode(SUCCESS);
//				status.setMessage("Login Success");
//				status.setData(ltMastLogin.getStatus());
//				// LtMastUsers mastUsers = ltMastUsersDao.getUserById(ltMastLogin.getUserId());
//				entity.setCode(SUCCESS);
//				entity.setRole(ltMastUser.getUserType());
//				entity.setUserId(ltMastLogin.getUserId());
//				entity.setStatus(ltMastUser.getStatus());
//				entity.setUserName(ltMastUser.getUserName());
//				entity.setLastLoginId(ltMastLogins.getLoginId());
//
//			} else {
//				entity.setCode(FAIL);
//				entity.setMessage("Please Enter Valid OTP");
//			}
//		} else {
//			status.setCode(SUCCESS);
//			status.setMessage("Login Success");
//			status.setData("SUCCESS");
//			entity.setCode(SUCCESS);
//			// LtMastUsers mastUsers = ltMastUsersDao.getUserById(ltMastUser.getUserId());
//			entity.setCode(SUCCESS);
//			entity.setRole(ltMastUser.getUserType());
//			entity.setUserId(ltMastUser.getUserId());
//			entity.setUserName(ltMastUser.getUserName());
//		}
//
//		if (status.getCode() == SUCCESS) {
//			System.out.println("login success");
//		}
//		System.out.println("End of login service = "+new Date());
//		return entity;
//	}
	
	
	@Override
	public ResponceEntity login(String mobileNumber,String otp, HttpServletResponse response) throws ServiceException {
		ResponceEntity entity = new ResponceEntity();
		Status status = new Status();
		System.out.println("in login service "+LocalDateTime.now());
		System.out.println("Database call 1 at "+LocalDateTime.now());
		UserLoginDto userLoginDto = ltMastUsersDao.getLtMastUsersByMobileNumber1(mobileNumber);
		System.out.println("Database call exit 1 at "+LocalDateTime.now());
		System.out.println("userLoginDto = "+userLoginDto);
		LtMastLogins ltMastLogins =new LtMastLogins();
		System.out.println("ltMastUser.getStatus() = "+userLoginDto.getStatus());
		if (userLoginDto.getStatus().equals("INACTIVE")) {
			entity.setCode(FAIL);
			entity.setMessage("User is Inactive");
			return entity;
		}
		System.out.println("Above ltMastLogin = "+new Date());
		System.out.println("Database call 2 at "+LocalDateTime.now());
		//change query of first call according to second call
//		LtMastLogins ltMastLogin = ltMastUsersDao.getLoginDetailsByUserId(ltMastUser.getUserId());//avoid 63 tag
		System.out.println("Database call exit 2 at "+LocalDateTime.now());
		System.out.println("Below ltMastLogin = "+new Date());
		
//		LtMastLogins ltMastLogin = new LtMastLogins();
	    
		ltMastLogins.setLoginId(userLoginDto.getLoginId());
		ltMastLogins.setLoginDate(userLoginDto.getLoginDate());
		ltMastLogins.setIpAddress(userLoginDto.getIpAddress());
		ltMastLogins.setDevice(userLoginDto.getDevice());
		ltMastLogins.setOtp(userLoginDto.getOtp());
		ltMastLogins.setStatus(userLoginDto.getStatus());
		ltMastLogins.setTokenId(userLoginDto.getTokenId());
		
		System.out.println("userLoginDto = "+userLoginDto);
		if (userLoginDto != null) {
			System.out.println("in ltMastLogin not null");
			if (userLoginDto.getOtp().equals(otp)) {
				System.out.println("in otp equals");
				userLoginDto.setStatus(SUCCESS_LOGIN);
				userLoginDto.setLoginDate(new Date());
				ltMastLogins = ltMastLoginsRepository.save(ltMastLogins);
				status.setCode(SUCCESS);
				status.setMessage("Login Success");
				status.setData(userLoginDto.getStatus());
				// LtMastUsers mastUsers = ltMastUsersDao.getUserById(ltMastLogin.getUserId());
				entity.setCode(SUCCESS);
				entity.setRole(userLoginDto.getUserType());
				entity.setUserId(userLoginDto.getUserId());
				entity.setStatus(userLoginDto.getStatus());
				entity.setUserName(userLoginDto.getUserName());
				entity.setLastLoginId(userLoginDto.getLoginId());

			} else {
				entity.setCode(FAIL);
				entity.setMessage("Please Enter Valid OTP");
			}
		} else {
			status.setCode(SUCCESS);
			status.setMessage("Login Success");
			status.setData("SUCCESS");
			entity.setCode(SUCCESS);
			// LtMastUsers mastUsers = ltMastUsersDao.getUserById(ltMastUser.getUserId());
			entity.setCode(SUCCESS);
			entity.setRole(userLoginDto.getUserType());
			entity.setUserId(userLoginDto.getUserId());
			entity.setUserName(userLoginDto.getUserName());
		}

		if (status.getCode() == SUCCESS) {
			System.out.println("login success");
		}
		System.out.println("End of login service = "+new Date());
		return entity;
	}


	@Override
	public LtVersion getVersionAuthAPI(LtVersion ltVersion) {
		return ltMastUsersDao.getVersionAuthAPI(ltVersion);
	}

	
}
