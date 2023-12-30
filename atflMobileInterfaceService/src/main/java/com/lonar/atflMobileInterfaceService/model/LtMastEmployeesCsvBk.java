package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lt_mast_employees_csv_bk")
public class LtMastEmployeesCsvBk{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Column(name = "position_code")
	private String position_code;

	@Column(name = "employee_code")
	private String employee_code;
	
	@Column(name = "login")
	private String login;

	@Column(name = "first_name")
	private String first_name;
	
	@Column(name = "last_name")
	private String last_name;
	
	@Column(name = "job_title")
	private String job_title;
	
	@Column(name = "employee_type")
	private String employee_type;
	
	@Column(name = "primary_mobile")
	private String primary_mobile;
	
	@Column(name = "email")
	private String email;

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

	public String getPosition_code() {
		return position_code;
	}

	public void setPosition_code(String position_code) {
		this.position_code = position_code;
	}

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getJob_title() {
		return job_title;
	}

	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	public String getEmployee_type() {
		return employee_type;
	}

	public void setEmployee_type(String employee_type) {
		this.employee_type = employee_type;
	}

	public String getPrimary_mobile() {
		return primary_mobile;
	}

	public void setPrimary_mobile(String primary_mobile) {
		this.primary_mobile = primary_mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
