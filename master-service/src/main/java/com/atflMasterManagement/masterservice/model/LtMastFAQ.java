package com.atflMasterManagement.masterservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_faq")
@JsonInclude(Include.NON_NULL)
public class LtMastFAQ extends BaseClass{
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE)
	@Column(name = "faq_id")
	String faqId;
	
	@Column(name="org_id")
	String orgId;
	
	@Column(name="faq")
	String faq;
	
	@Column(name="faq_ans")
	String faqAns;
	
	@Column(name="status")
	String status;
	
	@Column(name="sequence_no")
	Long sequenceNo;

	public String getFaqId() {
		return faqId;
	}

	public void setFaqId(String faqId) {
		this.faqId = faqId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getFaq() {
		return faq;
	}

	public void setFaq(String faq) {
		this.faq = faq;
	}

	public String getFaqAns() {
		return faqAns;
	}

	public void setFaqAns(String faqAns) {
		this.faqAns = faqAns;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Override
	public String toString() {
		return "LtMastFAQ [faqId=" + faqId + ", orgId=" + orgId + ", faq=" + faq + ", faqAns=" + faqAns + ", status="
				+ status + ", sequenceNo=" + sequenceNo + "]";
	}
	
	
}
