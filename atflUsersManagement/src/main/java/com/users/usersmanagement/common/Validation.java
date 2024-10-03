package com.users.usersmanagement.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Validation {
	public static boolean validatePhoneNumber(String phoneNo) {
		if (phoneNo.matches("^[1-9]\\d{9}$"))
		return true;
		return false;
	}
	
	public static boolean validateOTP(Long otp) {
		String otps = String.valueOf(otp);
		if (otps.matches("[0-9]{4}")) return true;
		return false;
	}
	
	public static Date getCurrentDateTime() {
		Instant instant = Instant.now();
		ZonedDateTime jpTime = instant.atZone(ZoneId.of("Asia/Calcutta"));
		try {
			Calendar calendar = new GregorianCalendar();
			calendar.set(jpTime.getYear(), jpTime.getMonthValue() - 1, jpTime.getDayOfMonth(), jpTime.getHour(),
					jpTime.getMinute(), jpTime.getSecond());
			return calendar.getTime();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new Date();
	}
}
