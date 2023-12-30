package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_price_lists_csv_bk")
public class LtMastPriceListsCsvBk{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
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

	@Column(name = "price_list")
	private String price_list;

	@Column(name = "price_list_desc")
	private String price_list_desc;

	@Column(name = "currency")
	private String currency;

	@Column(name = "product_code")
	private String product_code;

	@Column(name = "list_price")
	private String list_price;
	
	@Column(name = "start_date")
	private String start_date;
	
	@Column(name = "end_date")
	private String end_date;
	
	@Column(name = "ptr_price")
	private String ptr_price;

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

	public String getPrice_list() {
		return price_list;
	}

	public void setPrice_list(String price_list) {
		this.price_list = price_list;
	}

	public String getPrice_list_desc() {
		return price_list_desc;
	}

	public void setPrice_list_desc(String price_list_desc) {
		this.price_list_desc = price_list_desc;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getList_price() {
		return list_price;
	}

	public void setList_price(String list_price) {
		this.list_price = list_price;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getPtr_price() {
		return ptr_price;
	}

	public void setPtr_price(String ptr_price) {
		this.ptr_price = ptr_price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
