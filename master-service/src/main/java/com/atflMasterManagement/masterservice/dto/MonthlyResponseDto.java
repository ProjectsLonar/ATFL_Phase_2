package com.atflMasterManagement.masterservice.dto;

public class MonthlyResponseDto {
	private String monthNo;
	private String month;
	private String sale = "0";
	private String totalEff = "0";
	private String dbc = "0";
	private String tls = "0";

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public String getMonthNo() {
		return monthNo;
	}

	public void setMonthNo(String monthNo) {
		this.monthNo = monthNo;
	}

	public String getTotalEff() {
		return totalEff;
	}

	public void setTotalEff(String totalEff) {
		this.totalEff = totalEff;
	}

	public String getDbc() {
		return dbc;
	}

	public void setDbc(String dbc) {
		this.dbc = dbc;
	}

	public String getTls() {
		return tls;
	}

	public void setTls(String tls) {
		this.tls = tls;
	}

}
