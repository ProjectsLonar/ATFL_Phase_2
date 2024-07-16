package com.users.usersmanagement.model;
//
import java.util.Date;
//
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
//
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
//
@Entity
@Table(name = "lt_promotion")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class LtPromotion {
//
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_PROMOTION_S")
	@SequenceGenerator(name = "LT_PROMOTION_S", sequenceName = "LT_PROMOTION_S", allocationSize = 1)
	//@JsonProperty("promotionId")
	@Column(name = "promotion_id")
	private Long promotionId;

	//@JsonProperty("promotionName")
	@Column(name = "promotion_name")
	private String promotionName;

	//@JsonProperty("orgId")
	@Column(name = "org_id")
	private String orgId;

	//@JsonProperty("imageType")
	@Column(name = "image_type")
	private String imageType;

	//@JsonProperty("imageName")
	@Column(name = "image_name")
	private String imageName;
//
	//@JsonProperty("imageData")
	@Column(name = "image_data")
	private String imageData;

	//@JsonProperty("startDate")
	@Column(name = "start_date")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date startDate;

	//@JsonProperty("endDate")
	@Column(name = "end_date")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date endDate;
	
	//@JsonProperty("status")
	@Column(name = "STATUS")
	private String status;

	//@JsonProperty("allTimeShowFlag")
	@Column(name = "all_time_show_flag")
	private String allTimeShowFlag;
	
	//@JsonProperty("createdBy")
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	//@JsonProperty("creationDate")
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	//@JsonProperty("creationDate1")
	@Column(name = "CREATION_DATE_1")
	private String creationDate1;
	
	//@JsonProperty("lastUpdateLogin")
	@Column(name = "LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;
	
	//@JsonProperty("lastUpdatedBy")
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	//@JsonProperty("lastUpdateDate")
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;
	
	//@JsonProperty("lastUpdateDate1")
	@Column(name = "LAST_UPDATE_DATE_1")
	private String lastUpdateDate1;
//
	//@JsonProperty("startDate1")
	@Column(name = "start_date_1")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private String startDate1;

	//@JsonProperty("endDate1")
	@Column(name = "end_date_1")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private String endDate1;
	
	
	
	public String getCreationDate1() {
		return creationDate1;
	}

	public void setCreationDate1(String creationDate1) {
		this.creationDate1 = creationDate1;
	}

	public String getLastUpdateDate1() {
		return lastUpdateDate1;
	}

	public void setLastUpdateDate1(String lastUpdateDate1) {
		this.lastUpdateDate1 = lastUpdateDate1;
	}

	public String getStartDate1() {
		return startDate1;
	}

	public void setStartDate1(String startDate1) {
		this.startDate1 = startDate1;
	}

	public String getEndDate1() {
		return endDate1;
	}

	public void setEndDate1(String endDate1) {
		this.endDate1 = endDate1;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
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

	public String getAllTimeShowFlag() {
		return allTimeShowFlag;
	}

	public void setAllTimeShowFlag(String allTimeShowFlag) {
		this.allTimeShowFlag = allTimeShowFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdateLogin() {
		return lastUpdateLogin;
	}

	public void setLastUpdateLogin(String lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LtPromotion [promotionId=" + promotionId + ", promotionName=" + promotionName + ", orgId=" + orgId
				+ ", imageType=" + imageType + ", imageName=" + imageName + ", imageData=" + imageData + ", startDate="
				+ startDate + ", endDate=" + endDate + ", status=" + status + ", allTimeShowFlag=" + allTimeShowFlag
				+ ", createdBy=" + createdBy + ", creationDate=" + creationDate + ", creationDate1=" + creationDate1
				+ ", lastUpdateLogin=" + lastUpdateLogin + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdateDate="
				+ lastUpdateDate + ", lastUpdateDate1=" + lastUpdateDate1 + ", startDate1=" + startDate1 + ", endDate1="
				+ endDate1 + "]";
	}

	
	
}



