package com.lonar.sms.atflSmsService.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.lonar.sms.atflSmsService.dao.LtMastUsersDao;
import com.lonar.sms.atflSmsService.model.LtMastOrganisations;
import com.lonar.sms.atflSmsService.model.LtMastUsers;
import com.lonar.sms.atflSmsService.repository.LtMastNotificationTokenRepository;
import com.lonar.sms.atflSmsService.service.LtMastOrganisationService;
import com.lonar.sms.atflSmsService.service.LtMastSmsTokenService;

@Component
@EnableScheduling
@PropertySource({ "classpath:persistence.properties" })
public class ScheduledTasksDaily {

	@Autowired
	private Environment env;

	@Autowired
	LtMastOrganisationService ltMastOrganisationService;

	@Autowired
	LtMastSmsTokenService ltMastSmsTokenService;

	@Autowired
	LtMastUsersDao ltMastUsersDao;

	@Autowired
	LtMastNotificationTokenRepository ltMastNotificationTokenRepository;

	@Scheduled(cron = "0 0-5 1 * * ?")
	public void reportCurrentTime() throws ServiceException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		checkMessageBalance();
	}

	private void checkMessageBalance() {
		try {
			List<LtMastOrganisations> ltMastOrganisationsList = ltMastOrganisationService.getAllOrganisations();
			if (!ltMastOrganisationsList.isEmpty()) {
				for (LtMastOrganisations ltMastOrganisations : ltMastOrganisationsList) {
					
					//Status status = ltMastSmsTokenService.getSmsBalance(ltMastOrganisations.getOrgId());
					LtMastUsers ltMastUsers = new LtMastUsers();
					ltMastUsers.setOrgId(ltMastOrganisations.getOrgId());
					//saveSupplierNotification(ltMastUsers,status.getMessage());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*public void saveSupplierNotification(LtMastUsers ltMastUser, String notificationBody) throws ServiceException {
		List<LtMastUsers> users = ltMastUsersDao.getUserByType("SUPPLIER", ltMastUser.getSupplierId());
		if (!users.isEmpty()) {
			LtMastLogins ltMastLogins = ltMastUsersDao.getLoginDetailsByUserId(users.get(0).getUserId());
			LtMastNotificationToken ltMastNotificationToken = new LtMastNotificationToken();
			ltMastNotificationToken.setSupplierId(ltMastUser.getSupplierId());
			ltMastNotificationToken.setTransactionId(new Date().getTime());
			ltMastNotificationToken.setUserId(users.get(0).getUserId());
			ltMastNotificationToken.setNotificationTitle("A2Z");
			ltMastNotificationToken.setNotificationBody(notificationBody);
			ltMastNotificationToken.setNotificationStatus("SENDING");
			ltMastNotificationToken.setSendDate(new Date());
			ltMastNotificationToken.setTokenId(ltMastLogins.getTokenId());
			ltMastNotificationTokenRepository.save(ltMastNotificationToken);
			if (ltMastNotificationToken.getNotificationId() != null) {
				System.out.println("ltMastNotificationToken " + ltMastNotificationToken);
				callNotificationService(ltMastNotificationToken.getSupplierId(),
						ltMastNotificationToken.getTransactionId());
			}
		}
	}*/

	public void callNotificationService(Long supplierId, Long transId) {

		try {
			System.out.println("env " + env);
			final String uri = env.getProperty("notification_url");
			URL url = new URL(null, uri + supplierId + "" + "/" + transId);
			System.out.println("url " + url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			print_content(con);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void print_content(HttpURLConnection con) {
		if (con != null) {
			try {
				System.out.println("****** Content of the URL ********");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				while ((input = br.readLine()) != null) {
					System.out.println(input);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}