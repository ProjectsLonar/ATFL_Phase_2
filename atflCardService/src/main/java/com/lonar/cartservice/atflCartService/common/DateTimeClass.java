package com.lonar.cartservice.atflCartService.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeClass {

	public static Date addDays(Date date, int day) {
		try {
			Calendar calender = Calendar.getInstance();
			calender.setTime(date);
			calender.add(Calendar.DAY_OF_YEAR, day);
			return calender.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

