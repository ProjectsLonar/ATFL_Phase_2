package com.users.usersmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_outlet_positions")
@JsonInclude(Include.NON_NULL)
public class LtMastOutletPosition extends BaseClass {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "outlet_position_id")
	Long outletPositionId;
	
	@Column(name = "outlet_id")
	Long outletId;
	
	@Column(name = "position_id")
	Long positionId;
	
	@Column(name = "is_primary")
	char isPrimary;

	public Long getOutletPositionId() {
		return outletPositionId;
	}

	public void setOutletPositionId(Long outletPositionId) {
		this.outletPositionId = outletPositionId;
	}

	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}



	public char getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(char isPrimary) {
		this.isPrimary = isPrimary;
	}

	@Override
	public String toString() {
		return "LtMastOutletPosition [outletPositionId=" + outletPositionId + ", outletId=" + outletId + ", positionId="
				+ positionId + ", isPrimary=" + isPrimary + "]";
	}


}
