package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lt_mast_distributors_csv_bk")
public class LtMastDistributorsCsvBk {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "sr_no")
	private Long sr_no;
	
	@Column(name = "creation_date")
	private Date creation_date;
	
	@Column(name = "mobile_status")
	private String mobile_status;
	
	@Column(name = "mobile_remarks")
	private String mobile_remarks;
	
	@Column(name = "mobile_insert_update")
	private String mobile_insert_update;
	
	@Column(name = "distributor_code")
	private String distributor_code;
	
	@Column(name = "distributor_type")
	private String distributor_type;
	
	@Column(name = "dis_code")
	private String dis_code;
	
	@Column(name = "distributor_name")
	private String distributor_name;
	
	@Column(name = "proprietor_name")
	private String proprietor_name;
	
	@Column(name = "address_1")
	private String address_1;
	
	@Column(name = "address_2")
	private String address_2;
	
	@Column(name = "address_3")
	private String address_3;
	
	@Column(name = "address_4")
	private String address_4;
	
	@Column(name = "landmark")
	private String landmark;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "pin_code")
	private String pin_code;
	
	@Column(name = "region")
	private String region;
	
	@Column(name = "area")
	private String area;
	
	@Column(name = "territory")
	private String territory;
	
	@Column(name = "distributor_gstn")
	private String distributor_gstn;
	
	@Column(name = "distributor_pan")
	private String distributor_pan;
	
	@Column(name = "licence_no")
	private String licence_no; 
	
	@Column(name = "position_code")
	private String position_code;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "status")
	private String status;

	public Long getSr_no() {
		return sr_no;
	}

	public void setSr_no(Long sr_no) {
		this.sr_no = sr_no;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public String getMobile_status() {
		return mobile_status;
	}

	public void setMobile_status(String mobile_status) {
		this.mobile_status = mobile_status;
	}

	public String getMobile_remarks() {
		return mobile_remarks;
	}

	public void setMobile_remarks(String mobile_remarks) {
		this.mobile_remarks = mobile_remarks;
	}

	public String getMobile_insert_update() {
		return mobile_insert_update;
	}

	public void setMobile_insert_update(String mobile_insert_update) {
		this.mobile_insert_update = mobile_insert_update;
	}

	public String getDistributor_code() {
		return distributor_code;
	}

	public void setDistributor_code(String distributor_code) {
		this.distributor_code = distributor_code;
	}

	public String getDistributor_type() {
		return distributor_type;
	}

	public void setDistributor_type(String distributor_type) {
		this.distributor_type = distributor_type;
	}

	public String getDis_code() {
		return dis_code;
	}

	public void setDis_code(String dis_code) {
		this.dis_code = dis_code;
	}

	public String getDistributor_name() {
		return distributor_name;
	}

	public void setDistributor_name(String distributor_name) {
		this.distributor_name = distributor_name;
	}

	public String getProprietor_name() {
		return proprietor_name;
	}

	public void setProprietor_name(String proprietor_name) {
		this.proprietor_name = proprietor_name;
	}

	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}

	public String getAddress_3() {
		return address_3;
	}

	public void setAddress_3(String address_3) {
		this.address_3 = address_3;
	}

	public String getAddress_4() {
		return address_4;
	}

	public void setAddress_4(String address_4) {
		this.address_4 = address_4;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPin_code() {
		return pin_code;
	}

	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public String getDistributor_gstn() {
		return distributor_gstn;
	}

	public void setDistributor_gstn(String distributor_gstn) {
		this.distributor_gstn = distributor_gstn;
	}

	public String getDistributor_pan() {
		return distributor_pan;
	}

	public void setDistributor_pan(String distributor_pan) {
		this.distributor_pan = distributor_pan;
	}

	public String getLicence_no() {
		return licence_no;
	}

	public void setLicence_no(String licence_no) {
		this.licence_no = licence_no;
	}

	public String getPosition_code() {
		return position_code;
	}

	public void setPosition_code(String position_code) {
		this.position_code = position_code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
