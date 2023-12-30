package com.lonar.folderDeletionSchedular.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_job_import_export")
@JsonInclude(Include.NON_NULL)
public class LtJobeImportExport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "import_export_id")
	private Long importExportId;

	@Column(name = "type")
	String type;

	@Column(name = "name")
	String name;

	@Column(name = "status")
	String status;
	
	@Column(name = "sequence")
	private Long sequence;

	public Long getImportExportId() {
		return importExportId;
	}

	public void setImportExportId(Long importExportId) {
		this.importExportId = importExportId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public Long getSequence() {
		return sequence;
	}


	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	
	@Override
	public String toString() {
		return "LtJobeImportExport [importExportId=" + importExportId + ", type=" + type + ", name=" + name
				+ ", status=" + status + ", sequence=" + sequence + "]";
	}
	
}
