package com.users.usersmanagement.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_fileupload")
@JsonInclude(Include.NON_NULL)
public class LtFileUpload {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_FILEUPLOAD_S")
	@SequenceGenerator(name = "LT_FILEUPLOAD_S", sequenceName = "LT_FILEUPLOAD_S", allocationSize = 1)
	@Column(name = "fileupload_id")
	private Long fileuploadId;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "org_id")
	private String orgId;

	@Column(name = "image_type")
	private String imageType;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "image_data")
	private String imageData;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "STATUS")
	private String status;
	
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

	public Long getFileuploadId() {
		return fileuploadId;
	}

	public void setFileuploadId(Long fileuploadId) {
		this.fileuploadId = fileuploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "LtFileUpload [fileuploadId=" + fileuploadId + ", fileName=" + fileName + ", orgId=" + orgId
				+ ", imageType=" + imageType + ", imageName=" + imageName + ", imageData=" + imageData + ", startDate="
				+ startDate + ", endDate=" + endDate + ", status=" + status + ", createdBy=" + createdBy
				+ ", creationDate=" + creationDate + ", lastUpdateLogin=" + lastUpdateLogin + ", lastUpdatedBy="
				+ lastUpdatedBy + ", lastUpdateDate=" + lastUpdateDate + "]";
	}

	
	
}
