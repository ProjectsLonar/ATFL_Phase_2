package com.users.usersmanagement.model;

import javax.persistence.Column;
import javax.persistence.Transient;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OutletDto {

	private String outletId;
	private String orgId;
	private String outletCode;
	private String outletType;
	private String outletName;
	private String distributorId;
	private String proprietorName;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String landmark;
	private String country;
	private String state;
	private String city;
	private String pin_code;
	private String region;
	private String area;
	private String territory;
	private String outletGstn;
	private String outletPan;
	private String licenceNo;
	private String positionsId;
	private String phone;
	private String email;
	private String primaryMobile;
	private String distributorCode;
	private String distributorCrmCode;
	private String distributorStatus;
	private String distributorName;
	private String employeeId;
	private String empName;
	private String employeeCode;
	private String position;
	private String outletAddress;
    
	private String beatNo;
	private String outletChannel;

}
