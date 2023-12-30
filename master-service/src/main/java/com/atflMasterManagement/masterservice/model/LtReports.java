package com.atflMasterManagement.masterservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="LT_REPORTS")
@JsonInclude(Include.NON_NULL)
public class LtReports {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "REPORT_ID")
	private Long reportId;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "REPORT_NUMBER")
	private String reporttNumber;
	
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "FINAL_AMOUNT")
	private Double finalAmount;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getReporttNumber() {
		return reporttNumber;
	}

	public void setReporttNumber(String reporttNumber) {
		this.reporttNumber = reporttNumber;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}
	
	
}
