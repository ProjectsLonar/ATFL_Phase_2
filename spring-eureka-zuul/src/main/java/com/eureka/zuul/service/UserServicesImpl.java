package com.eureka.zuul.service;

import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eureka.zuul.common.CodeMaster;
import com.eureka.zuul.common.Configuration;
import com.eureka.zuul.common.LtMastUsers;
import com.eureka.zuul.common.LtVersion;
import com.eureka.zuul.common.Status;
import com.eureka.zuul.dao.UserDao;

@Service
public class UserServicesImpl implements UserServices, CodeMaster {

	@Autowired
	UserDao userDao;

	@Override
	public Status getInactiveUsers() throws ServiceException {

		Status status = new Status();
		List<LtMastUsers> userList = userDao.getAllInactiveUsers();
		if (!userList.isEmpty()) {
			Configuration.inactiveUserMap.clear();
			for (LtMastUsers ltMastUsers : userList) {
				Configuration.inactiveUserMap.put(ltMastUsers.getMobileNumber(), ltMastUsers);
			}
			status.setCode(RECORD_FOUND);
			status.setData(Configuration.inactiveUserMap);
			System.out.println("INACTIVE USER MAP SIZE :: " + Configuration.inactiveUserMap.size());
		} else {
			Configuration.inactiveUserMap.clear();
			status.setMessage("Record Not Found");
			status.setCode(RECORD_NOT_FOUND);
		}
		return status;
	}

	@Override
	public Map<String, LtVersion> getAllAPIVersion() throws ServiceException {
		return userDao.getAllAPIVersion();
	}

}
