package com.eureka.zuul.common;

import java.util.Date;

public class LtVersion {

	private Long srNo;
	private String service_name;
	private String current_version;
	private String supported_version;
	private String call_services_name;
	private Date CREATION_DATE;
	private Date LAST_UPDATE_DATE;
	private String status;

	public Long getSrNo() {
		return srNo;
	}

	public void setSrNo(Long srNo) {
		this.srNo = srNo;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public String getCurrent_version() {
		return current_version;
	}

	public void setCurrent_version(String current_version) {
		this.current_version = current_version;
	}

	public String getSupported_version() {
		return supported_version;
	}

	public void setSupported_version(String supported_version) {
		this.supported_version = supported_version;
	}

	public String getCall_services_name() {
		return call_services_name;
	}

	public void setCall_services_name(String call_services_name) {
		this.call_services_name = call_services_name;
	}

	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(Date cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}

	public Date getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}

	public void setLAST_UPDATE_DATE(Date lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
