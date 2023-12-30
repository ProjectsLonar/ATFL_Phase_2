package com.lonar.folderDeletionSchedular.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Validation {

	public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.trim().isEmpty())
			return false;
		return true;
	}

	public static boolean isStrNullOrEmpty(String str) {
		// System.out.println("Str===>" + str);
		if (isNullOrEmpty(str)) {
			return true;
		}
		return false;
	}

	public static String isCreatePipeSeparator(List<String> strList) {

		StringBuilder sb = new StringBuilder();
		int count = 0;

		if (!strList.isEmpty()) {
			for (String string : strList) {
				sb = sb.append(string);
				if (count == strList.size() - 1) {
					return sb.toString();
				}
				sb = sb.append("|");
				count++;
			}
		}

		return sb.toString();

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
