package com.lonar.cartservice.atflCartService.common;

public class Validation {
	public static boolean validatePhoneNumber(String phoneNo) {
		if (phoneNo.matches("[0-9]{10}"))
		return true;
		return false;
	}
	
	public static boolean validateOTP(Long otp) {
		String otps = String.valueOf(otp);
		if (otps.matches("[0-9]{4}")) return true;
		return false;
	}
}
