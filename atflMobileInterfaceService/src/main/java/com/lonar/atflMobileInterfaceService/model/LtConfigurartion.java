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
@Table(name = "lt_configuration")
@JsonInclude(Include.NON_NULL)
public class LtConfigurartion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "configuration_id")
	private Long configurationId;

	@Column(name = "key")
	String key;

	@Column(name = "value")
	String value;

	public Long getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(Long configurationId) {
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

	@Override
	public String toString() {
		return "LtConfigurartion [configurationId=" + configurationId + ", key=" + key + ", value=" + value + "]";
	}
	
}
