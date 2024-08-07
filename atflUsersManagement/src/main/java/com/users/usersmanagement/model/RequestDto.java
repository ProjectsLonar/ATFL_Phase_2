package com.users.usersmanagement.model;

public class RequestDto {

	private String orgId;
	private String distributorId;
	private String outletId;
	private int limit;
	private int offset;
	private String searchField;
	private String outletName;
	private String salesPersonId;
	private Long userId;
	private String primaryMobile;
	
	private String beatName;
	
	private int notificationId;
	
	//Added by Nikhil
	private String userType;
	private String status;
	private String userName;
	
	
	
	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPrimaryMobile() {
		return primaryMobile;
	}

	public void setPrimaryMobile(String primaryMobile) {
		this.primaryMobile = primaryMobile;
	}
	
	public String getBeatName() {
		return beatName;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}

	@Override
	public String toString() {
		return "RequestDto [orgId=" + orgId + ", distributorId=" + distributorId + ", outletId=" + outletId + ", limit="
				+ limit + ", offset=" + offset + ", searchField=" + searchField + ", outletName=" + outletName
				+ ", salesPersonId=" + salesPersonId + ", userId=" + userId + ", primaryMobile=" + primaryMobile
				+ ", beatName=" + beatName + ", notificationId=" + notificationId + ", userType=" + userType
				+ ", status=" + status + ", userName=" + userName + "]";
	}

	
	
}
