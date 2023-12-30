package com.lonar.atflMobileInterfaceService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_emp_position")
@JsonInclude(Include.NON_NULL)
public class LtMastEmployeesPosition extends BaseClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_position_id")
	Long empPositionId;

	@Column(name = "employee_id")
	Long employeeId;

	@Column(name = "position_id")
	Long positionId;
	
	@Column(name = "row_number")
	String rowNumber;
	
	@Column(name = "parent_position")
	String parentPosition;

	public Long getEmpPositionId() {
		return empPositionId;
	}

	public void setEmpPositionId(Long empPositionId) {
		this.empPositionId = empPositionId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public String getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(String parentPosition) {
		this.parentPosition = parentPosition;
	}
	
}
