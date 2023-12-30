package com.lonar.folderDeletionSchedular.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class UtilsMaster {
	public static Date getCurrentDateTime() {
		 Instant instant = Instant.now();
		 ZonedDateTime jpTime = instant.atZone(ZoneId.of("Asia/Calcutta"));
	
		 try {
			 Calendar calendar = new GregorianCalendar();
			 calendar.set(jpTime.getYear(), jpTime.getMonthValue()-1, jpTime.getDayOfMonth(), jpTime.getHour(), jpTime.getMinute(), jpTime.getSecond());
			 return calendar.getTime();
		 } catch (Exception e1) {
			e1.printStackTrace();
		 }
		 return new Date();
	}
	public static Date convertDate(Date date) {
		Instant instant = date.toInstant();
		ZonedDateTime jpTime = instant.atZone(ZoneId.of("Asia/Calcutta"));
		
		try {
		Calendar calendar = new GregorianCalendar();
		calendar.set(jpTime.getYear(), jpTime.getMonthValue()-1, jpTime.getDayOfMonth(), jpTime.getHour()+5, jpTime.getMinute()+30, jpTime.getSecond());
		
		return calendar.getTime();
		} catch (Exception e1) {
		e1.printStackTrace();
		}
		return new Date();
		}
	 public static Date getUtcDate(Date getDate) throws ParseException{
			//String input = getDeliveryDate.toString();
		    TimeZone timeZone = TimeZone.getTimeZone("Asia/Calcutta");
		    SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd");
		    formatted.setTimeZone(timeZone);
		    GregorianCalendar cal = new GregorianCalendar(timeZone);
		    cal.setTime(getDate);
		   // cal.set(getDeliveryDate.getYear(),getDeliveryDate.getMonth(),getDeliveryDate.getDay(),getDeliveryDate.getHours(),getDeliveryDate.getMinutes(),getDeliveryDate.getSeconds());
		  //  cal.setTime(formatted.parse(input));
		    return cal.getTime(); 
		    }
	
}
