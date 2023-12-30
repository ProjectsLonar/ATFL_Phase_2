package com.eureka.auth.service;

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

	
	@Override
	public ResponceEntity login(String mobileNumber,String otp, HttpServletResponse response) throws ServiceException {
		ResponceEntity entity = new ResponceEntity();
		Status status = new Status();
		LtMastUsers ltMastUser = ltMastUsersDao.getLtMastUsersByMobileNumber(mobileNumber);
		LtMastLogins ltMastLogins =new LtMastLogins();
		if (ltMastUser.getStatus().equals("INACTIVE")) {
			entity.setCode(FAIL);
			entity.setMessage("User is Inactive");
			return entity;
		}
		LtMastLogins ltMastLogin = ltMastUsersDao.getLoginDetailsByUserId(ltMastUser.getUserId());

		if (ltMastLogin != null) {

			if (ltMastLogin.getOtp().equals(otp)) {

				ltMastLogin.setStatus(SUCCESS_LOGIN);
				ltMastLogin.setLoginDate(new Date());
				ltMastLogins = ltMastLoginsRepository.save(ltMastLogin);
				status.setCode(SUCCESS);
				status.setMessage("Login Success");
				status.setData(ltMastLogin.getStatus());
				// LtMastUsers mastUsers = ltMastUsersDao.getUserById(ltMastLogin.getUserId());
				entity.setCode(SUCCESS);
				entity.setRole(ltMastUser.getUserType());
				entity.setUserId(ltMastLogin.getUserId());
				entity.setStatus(ltMastUser.getStatus());
				entity.setUserName(ltMastUser.getUserName());
				entity.setLastLoginId(ltMastLogins.getLoginId());

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
			entity.setRole(ltMastUser.getUserType());
			entity.setUserId(ltMastUser.getUserId());
			entity.setUserName(ltMastUser.getUserName());
		}

		if (status.getCode() == SUCCESS) {
			System.out.println("login success");
		}

		return entity;
	}


	@Override
	public LtVersion getVersionAuthAPI(LtVersion ltVersion) {
		return ltMastUsersDao.getVersionAuthAPI(ltVersion);
	}

	
}
