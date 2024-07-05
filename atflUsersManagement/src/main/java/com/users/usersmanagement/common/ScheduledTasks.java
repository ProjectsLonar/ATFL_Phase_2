package com.users.usersmanagement.common;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.users.usersmanagement.service.LtMastDistributorsService;


//ScheduledTasks to delete notification after 72 hours
@Component
public class ScheduledTasks {

	@Autowired
    private LtMastDistributorsService ltMastDistributorsService;

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void deleteNotificationAfter72Hours() {
    	try {
			try {
				ltMastDistributorsService.deleteNotificationAfter72Hours();
			} catch (ServerException e) {
				e.printStackTrace();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    }
}
