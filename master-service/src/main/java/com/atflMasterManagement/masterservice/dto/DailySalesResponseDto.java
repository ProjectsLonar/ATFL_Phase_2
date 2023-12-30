package com.atflMasterManagement.masterservice.dto;

public class DailySalesResponseDto {
	private String date;
	private String sale = "0";
	private String dbc = "0";
	private String tls = "0";

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
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
