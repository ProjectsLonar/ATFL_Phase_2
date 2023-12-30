package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lt_mast_inventory_csv_bk")
public class LtMastInventoryCsvBk {
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

	@Column(name = "inventory_code")
	private String inventory_code;

	@Column(name = "inventory_name")
	private String inventory_name;

	@Column(name = "dist_code")
	private  String dist_code;

	@Column(name = "inventory_status")
	private String inventory_status;

	@Column(name = "product_rid")
	private String product_rid;
	
	@Column(name = "prod_code")
	private String prod_code;
	
	@Column(name = "quantity")
	private String quantity;

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

	public String getInventory_code() {
		return inventory_code;
	}

	public void setInventory_code(String inventory_code) {
		this.inventory_code = inventory_code;
	}

	public String getInventory_name() {
		return inventory_name;
	}

	public void setInventory_name(String inventory_name) {
		this.inventory_name = inventory_name;
	}

	public String getDist_code() {
		return dist_code;
	}

	public void setDist_code(String dist_code) {
		this.dist_code = dist_code;
	}

	public String getInventory_status() {
		return inventory_status;
	}

	public void setInventory_status(String inventory_status) {
		this.inventory_status = inventory_status;
	}

	public String getProduct_rid() {
		return product_rid;
	}

	public void setProduct_rid(String product_rid) {
		this.product_rid = product_rid;
	}

	public String getProd_code() {
		return prod_code;
	}

	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	

}
