package com.atflMasterManagement.masterservice.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
	
	public static String getDateAndTimeFromUTCDate(String dateStr) {
	      DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

	        Date date;
			try {
				//date = utcFormat.parse("2020-10-20T18:30:00.000Z");
				date = utcFormat.parse(dateStr);
		        DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		        pstFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Calcutta")));
		        String formatedDate = pstFormat.format(date);
		        return formatedDate;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	 public static void main(String[] argv) throws ParseException {
		 System.out.println("DD :: "+getDateAndTimeFromUTCDate("2020-10-15T18:30:00.000Z"));
	 }
	
	
	
}

