package com.lonar.folderDeletionSchedular.model;

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
@Table(name = "lt_job_logs")
@JsonInclude(Include.NON_NULL)
public class LtJobeLogs {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	
	@Column(name = "logs_id")
	private Long logsId;
	
	@Column(name = "import_export_id")
	private Long importExportId;
	
	@Column(name = "job_type")
	String jobType;
	
	@Column(name = "logs_status")
	String logsStatus;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "total_record")
	int totalRecord;
	
	@Column(name = "success_record")
	int successRecord;
	
	@Column(name = "failed_record")
	int failedRecord;

	public Long getLogsId() {
		return logsId;
	}

	public void setLogsId(Long logsId) {
		this.logsId = logsId;
	}

	public Long getImportExportId() {
		return importExportId;
	}

	public void setImportExportId(Long importExportId) {
		this.importExportId = importExportId;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getLogsStatus() {
		return logsStatus;
	}

	public void setLogsStatus(String logsStatus) {
		this.logsStatus = logsStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getSuccessRecord() {
		return successRecord;
	}

	public void setSuccessRecord(int successRecord) {
		this.successRecord = successRecord;
	}

	public int getFailedRecord() {
		return failedRecord;
	}

	public void setFailedRecord(int failedRecord) {
		this.failedRecord = failedRecord;
	}
	
}
