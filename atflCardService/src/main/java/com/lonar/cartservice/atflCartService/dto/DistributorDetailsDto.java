package com.lonar.cartservice.atflCartService.dto;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class DistributorDetailsDto {

	private String distributorId;
	@Transient
	private String distributorSequance = "1";
//	private Long distributorSequance = 1L;

	private String distributorCrmCode;

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributorSequance() {
		return distributorSequance;
	}

	public void setDistributorSequance(String distributorSequance) {
		this.distributorSequance = distributorSequance;
	}

	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}

}
