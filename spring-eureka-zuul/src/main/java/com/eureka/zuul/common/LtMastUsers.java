package com.eureka.zuul.common;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_MAST_USERS")
@JsonInclude(Include.NON_NULL)
public class LtMastUsers{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_MAST_USERS_S")
	@SequenceGenerator(name = "LT_MAST_USERS_S", sequenceName = "LT_MAST_USERS_S", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "ORG_ID")
	private Long orgId;

	@Column(name = "DISTRIBUTOR_ID")
	private Long distributorId;

	@Column(name = "OUTLET_ID")
	private Long outletId;

	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;

	@Column(name = "USER_TYPE")
	private String userType;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	@Column(name = "DESIGNATION")
	private String Designation;
	
	@Column(name = "POSITION_ID")
	private Long PositionId;
	
	@Column(name = "ADDRESS")
	private String Address;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ALTERNATE_NO")
	private String alternateNo;
	
	@Column(name = "LATITUDE")
	private String latitude;
	
	@Column(name = "LONGITUDE")
	private String longitude;
	
	@Column(name = "image_type")
	private String imageType;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "image_data")
	private byte[] imageData;
	
	@Column(name = "ADDRESS_DETAILS")
	private String addressDetails;
	
	@Column(name = "RECENT_SEARCH_ID")
	private Long recentSerachId;
	
	@Transient
	String distributorCode;
	
	@Transient
	String distributorName;
	
	@Transient
	String distributorAddress;
	
	@Transient
	Long employeeId;
	
	@Transient
	String empName;
	
	@Transient
	String empCode;
	
	@Transient
	String organisationCode;
	
	@Transient
	String organisationName;
	
	@Transient
	String outletName;
	
	@Transient
	String outletCode;
	
	@Transient
	String outletAddress;
	
	@Transient
	String orgStatus;
	
	@Transient
	String position;
	
	@Column(name = "STATUS")
	private String status;
	
	/*@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;*/
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "LAST_UPDATE_LOGIN")
	private Long lastUpdateLogin;
	
	@Column(name = "LAST_UPDATED_BY")
	private Long lastUpdatedBy;
	
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Transient
	private String stDate;
	
	@Transient
	private String enDate;
	
	@Transient
	private Integer start;

	@Transient
	private Integer length;
	
	@Transient
	private Integer columnNo;
	
	@Transient
	private String sort;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getLastUpdateLogin() {
		return lastUpdateLogin;
	}

	public void setLastUpdateLogin(Long lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getStDate() {
		return stDate;
	}

	public void setStDate(String stDate) {
		this.stDate = stDate;
	}

	public String getEnDate() {
		return enDate;
	}

	public void setEnDate(String enDate) {
		this.enDate = enDate;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Integer columnNo) {
		this.columnNo = columnNo;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDesignation() {
		return Designation;
	}

	public void setDesignation(String designation) {
		Designation = designation;
	}

	public Long getPositionId() {
		return PositionId;
	}

	public void setPositionId(Long positionId) {
		PositionId = positionId;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlternateNo() {
		return alternateNo;
	}

	public void setAlternateNo(String alternateNo) {
		this.alternateNo = alternateNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}

	public Long getRecentSerachId() {
		return recentSerachId;
	}

	public void setRecentSerachId(Long recentSerachId) {
		this.recentSerachId = recentSerachId;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getDistributorAddress() {
		return distributorAddress;
	}

	public void setDistributorAddress(String distributorAddress) {
		this.distributorAddress = distributorAddress;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getOrganisationCode() {
		return organisationCode;
	}

	public void setOrganisationCode(String organisationCode) {
		this.organisationCode = organisationCode;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "LtMastUsers [userId=" + userId + ", orgId=" + orgId + ", distributorId=" + distributorId + ", outletId="
				+ outletId + ", mobileNumber=" + mobileNumber + ", userType=" + userType + ", userName=" + userName
				+ ", employeeCode=" + employeeCode + ", Designation=" + Designation + ", PositionId=" + PositionId
				+ ", Address=" + Address + ", email=" + email + ", alternateNo=" + alternateNo + ", latitude="
				+ latitude + ", longitude=" + longitude + ", imageType=" + imageType + ", imageName=" + imageName
				+ ", imageData=" + Arrays.toString(imageData) + ", addressDetails=" + addressDetails
				+ ", recentSerachId=" + recentSerachId + ", distributorCode=" + distributorCode + ", distributorName="
				+ distributorName + ", distributorAddress=" + distributorAddress + ", employeeId=" + employeeId
				+ ", empName=" + empName + ", empCode=" + empCode + ", organisationCode=" + organisationCode
				+ ", organisationName=" + organisationName + ", outletName=" + outletName + ", outletCode=" + outletCode
				+ ", outletAddress=" + outletAddress + ", orgStatus=" + orgStatus + ", position=" + position
				+ ", status=" + status + ", createdBy=" + createdBy + ", creationDate=" + creationDate
				+ ", lastUpdateLogin=" + lastUpdateLogin + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdateDate="
				+ lastUpdateDate + ", stDate=" + stDate + ", enDate=" + enDate + ", start=" + start + ", length="
				+ length + ", columnNo=" + columnNo + ", sort=" + sort + "]";
	}
	
	
	
}
