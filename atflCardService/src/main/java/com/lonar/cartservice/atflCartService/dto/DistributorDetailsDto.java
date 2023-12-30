package com.lonar.cartservice.atflCartService.dto;

public class DistributorDetailsDto {

	private String distributorId;
	private Long distributorSequance = 1L;
	private String distributorCrmCode;

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public Long getDistributorSequance() {
		return distributorSequance;
	}

	public void setDistributorSequance(Long distributorSequance) {
		this.distributorSequance = distributorSequance;
	}

	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}

}
