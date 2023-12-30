package com.users.usersmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_configuration")
@JsonInclude(Include.NON_NULL)
public class LtConfigurartion {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_CONFIGURATION_S")
	@SequenceGenerator(name = "LT_CONFIGURATION_S", sequenceName = "LT_CONFIGURATION_S", allocationSize = 1)
	@Column(name = "configuration_id")
	private String configurationId;

	@Column(name = "key")
	String key;

	@Column(name = "value")
	String value;

	public String getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
