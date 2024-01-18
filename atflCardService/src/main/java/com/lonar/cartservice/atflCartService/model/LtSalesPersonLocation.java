package com.lonar.cartservice.atflCartService.model;

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
@Table(name = "LT_SALESPERSON_LOCATION")
@JsonInclude(Include.NON_NULL)
public class LtSalesPersonLocation extends BaseClass{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LT_SALESPERSON_LOCATION_S")
	@SequenceGenerator(name = "LT_SALESPERSON_LOCATION_S", sequenceName = "LT_SALESPERSON_LOCATION_S", allocationSize = 1)
	@Column(name = "SALES_PERSON_LOCATION_ID")
	Long salesPersonLocationId;
	
	@Column (name = "BEAT_ID")
	Long beatId;
	
	@Column(name = "OUTLET_ID")
	String outletId;
	
	@Column (name = "ORDER_NUMBER")
	String orderName;
	
	@Column (name = "ADDRESS")
	String address;
	
	@Column (name = "latitude")
	String latitude;
	
	@Column (name = "longitude")
	String longitude;
	
	@Transient
	Long userId;

	public Long getSalesPersonLocationId() {
		return salesPersonLocationId;
	}

	public void setSalesPersonLocationId(Long salesPersonLocationId) {
		this.salesPersonLocationId = salesPersonLocationId;
	}

	public Long getBeatId() {
		return beatId;
	}

	public void setBeatId(Long beatId) {
		this.beatId = beatId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "LtSalesPersonLocation [salesPersonLocationId=" + salesPersonLocationId + ", beatId=" + beatId
				+ ", outletId=" + outletId + ", orderName=" + orderName + ", address=" + address + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
	
	
	
}
