
package com.lonar.folderDeletionSchedular.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "LT_MAST_SYS_VARIABLES")
@XmlRootElement
public class LtMastSysVariables extends BaseClass implements Serializable 
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "VARIABLE_ID")
	private Long variableId;
	
	@Column(name = "VARIABLE_NAME")
	private String variableName;
	
	@Column(name = "SYSTEM_VALUE")
	private String systemValue;
	
	@Column(name = "org_id")
	private Long orgId;

	public Long getVariableId() {
		return variableId;
	}

	public void setVariableId(Long variableId) {
		this.variableId = variableId;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getSystemValue() {
		return systemValue;
	}

	public void setSystemValue(String systemValue) {
		this.systemValue = systemValue;
	}

	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "LtMastSysVariables [variableId=" + variableId + ", variableName=" + variableName + ", systemValue="
				+ systemValue + ", orgId=" + orgId + "]";
	}
	

}
