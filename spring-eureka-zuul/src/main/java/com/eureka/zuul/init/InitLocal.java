package com.eureka.zuul.init;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eureka.zuul.common.Configuration;
import com.eureka.zuul.common.LtMastUsers;
import com.eureka.zuul.dao.UserDao;

@Component
public class InitLocal {
	@Autowired
	UserDao userDao;

	@PostConstruct
	public void startInitLocal() throws IOException {
		try {
			//Get All Data
			List<LtMastUsers> ltMastUsersList= userDao.getAllInactiveUsers();
			System.out.println("file size"+ltMastUsersList.size());
		
			for (Iterator iterator = ltMastUsersList.iterator(); iterator.hasNext();) {
				LtMastUsers users = (LtMastUsers) iterator.next();
				Configuration.inactiveUserMap.put(users.getMobileNumber(), users);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
