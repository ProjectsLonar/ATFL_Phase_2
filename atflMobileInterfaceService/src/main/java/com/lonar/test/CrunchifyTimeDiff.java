package com.lonar.test;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
 
/**
 * @author Crunchify.com
 * 
 */
 
public class CrunchifyTimeDiff {
 
	public static void main(String[] args) {
		try {
			String actualTime="10:00 AM";
 
			String format = "MM/dd/yyyy hh:mm a";
			String dateFormat = "MM/dd/yyyy";
			
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat);
 
			Date dateObj1 = new Date();
			Date dateObj2 = sdf.parse(sdfDate.format(dateObj1) + " " + actualTime);
			System.out.println(dateObj1);
			System.out.println(dateObj2 + "\n");
			
			DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");
 
			// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
			long diff = dateObj2.getTime() - dateObj1.getTime();
 
			int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
			System.out.println("difference between days: " + diffDays);
 
			int diffhours = (int) (diff / (60 * 60 * 1000));
			System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));
 
			int diffmin = (int) (diff / (60 * 1000));
			System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));
 
			int diffsec = (int) (diff / (1000));
			System.out.println(diffsec);
			System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));
 
			System.out.println("difference between milliseconds: " + crunchifyFormatter.format(diff));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}