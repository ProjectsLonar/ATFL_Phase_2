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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
//
@Entity
@Table(name = "lt_promotion")
@JsonInclude(Include.NON_NULL)
public class LtPromotion {
//
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_PROMOTION_S")
	@SequenceGenerator(name = "LT_PROMOTION_S", sequenceName = "LT_PROMOTION_S", allocationSize = 1)
	@Column(name = "promotion_id")
	private Long promotionId;

	@Column(name = "promotion_name")
	private String promotionName;

	@Column(name = "org_id")
	private String orgId;

	@Column(name = "image_type")
	private String imageType;

	@Column(name = "image_name")
	private String imageName;
//
	@Column(name = "image_data")
	private String imageData;

	@Column(name = "start_date")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date startDate;

	@Column(name = "end_date")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date endDate;
	
	@Column(name = "STATUS")
	private String status;

	@Column(name = "all_time_show_flag")
	private String allTimeShowFlag;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;
	
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;
//
//	
	@Column(name = "start_date_1")
	private Date startDate1;

	@Column(name = "end_date_1")
	private Date endDate1;
	
	
	public Date getStartDate1() {
		return startDate1;
	}

	public void setStartDate1(Date startDate1) {
		this.startDate1 = startDate1;
	}

	public Date getEndDate1() {
		return endDate1;
	}

	public void setEndDate1(Date endDate1) {
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
				+ ", createdBy=" + createdBy + ", creationDate=" + creationDate + ", lastUpdateLogin=" + lastUpdateLogin
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdateDate=" + lastUpdateDate + ", startDate1="
				+ startDate1 + ", endDate1=" + endDate1 + "]";
	}
	
	
}



