package com.lonar.cartservice.atflCartService.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LtSalesReturnAvailability {

	private String availabilityCode;
	private String availability;
	private String location;
	public String getAvailabilityCode() {
		return availabilityCode;
	}
	public void setAvailabilityCode(String availabilityCode) {
		this.availabilityCode = availabilityCode;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "LtSalesReturnAvailability [availabilityCode=" + availabilityCode + ", availability=" + availability
				+ "]";
	}
	
}
