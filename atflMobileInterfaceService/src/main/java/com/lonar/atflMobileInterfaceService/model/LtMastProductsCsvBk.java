package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lt_mast_products_csv_bk")
public class LtMastProductsCsvBk {
	@Id
	@Basic(optional = false)
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

	@Column(name = "product_type")
	private String product_type;

	@Column(name = "category")
	private String category;

	@Column(name = "subcategory")
	private String subcategory;

	@Column(name = "product_code")
	private String product_code;

	@Column(name = "product_name")
	private String product_name;

	@Column(name = "product_desc")
	private String product_desc;

	@Column(name = "primary_uom")
	private String primary_uom;

	@Column(name = "secondary_uom")
	private String secondary_uom;

	@Column(name = "secondary_uom_value")
	private String secondary_uom_value;

	@Column(name = "units_per_case")
	private String units_per_case;

	@Column(name = "segment")
	private String segment;

	@Column(name = "brand")
	private String brand;

	@Column(name = "subbrand")
	private String subbrand;

	@Column(name = "style")
	private String style;

	@Column(name = "flavor")
	private String flavor;

	@Column(name = "case_pack")
	private String case_pack;

	@Column(name = "hsn_code")
	private String hsn_code;

	@Column(name = "orderable")
	private String orderable;

	@Column(name = "product_image")
	private String product_image;

	@Column(name = "thumbnail_image")
	private String thumbnail_image;
	
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

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_desc() {
		return product_desc;
	}

	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}

	public String getPrimary_uom() {
		return primary_uom;
	}

	public void setPrimary_uom(String primary_uom) {
		this.primary_uom = primary_uom;
	}

	public String getSecondary_uom() {
		return secondary_uom;
	}

	public void setSecondary_uom(String secondary_uom) {
		this.secondary_uom = secondary_uom;
	}

	public String getSecondary_uom_value() {
		return secondary_uom_value;
	}

	public void setSecondary_uom_value(String secondary_uom_value) {
		this.secondary_uom_value = secondary_uom_value;
	}

	public String getUnits_per_case() {
		return units_per_case;
	}

	public void setUnits_per_case(String units_per_case) {
		this.units_per_case = units_per_case;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSubbrand() {
		return subbrand;
	}

	public void setSubbrand(String subbrand) {
		this.subbrand = subbrand;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	public String getCase_pack() {
		return case_pack;
	}

	public void setCase_pack(String case_pack) {
		this.case_pack = case_pack;
	}

	public String getHsn_code() {
		return hsn_code;
	}

	public void setHsn_code(String hsn_code) {
		this.hsn_code = hsn_code;
	}

	public String getOrderable() {
		return orderable;
	}

	public void setOrderable(String orderable) {
		this.orderable = orderable;
	}

	public String getProduct_image() {
		return product_image;
	}

	public void setProduct_image(String product_image) {
		this.product_image = product_image;
	}

	public String getThumbnail_image() {
		return thumbnail_image;
	}

	public void setThumbnail_image(String thumbnail_image) {
		this.thumbnail_image = thumbnail_image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
