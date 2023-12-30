package com.atflMasterManagement.masterservice.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

public class Test {

	public static void main_Test(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		LocalDate today = LocalDate.now();
		int year = today.getYear();
		int month = today.getMonthValue();
		int day = today.getDayOfMonth();
		System.out.println(day);
		
		/*
		 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); String
		 * dateInString = "2020-07-20";
		 * System.out.println(formatter.format(dateInString));
		 */
		
		
		for (int i = 5; i >= 0; i--) {
	        YearMonth date = YearMonth.now().minusMonths(i);
	        String monthName = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
	        System.out.println("@@@@@  "+date.getMonth().getValue());
	        System.out.println(monthName + " " + date.getYear());
	    }
		
		
		System.out.println("month!!!! "+month);
		
		    int date = 1;
		
		    int val = 11;
		       System.out.println("Integer: "+val);
		       String formattedStr = String.format("%02d", val);
		       System.out.println("With leading zeros = " + formattedStr);

		    Calendar cal = Calendar.getInstance();
		    cal.clear();
		    cal.set(Calendar.YEAR, year);
		    cal.set(Calendar.MONTH, month);
		    cal.set(Calendar.DATE, date);

		    java.util.Date utilDate = cal.getTime();
		    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			
			  System.out.println(formatter.format(utilDate));
		    System.out.println(utilDate);
	}

}
