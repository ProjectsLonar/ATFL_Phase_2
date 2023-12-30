package com.atflMasterManagement.masterservice.dto;

import java.util.List;

import com.atflMasterManagement.masterservice.model.LtMastAboutUs;
import com.atflMasterManagement.masterservice.model.LtMastFAQ;

public class AboutUsAndFaqDetailsDto {

	private LtMastAboutUs ltMastAboutUs;

	private List<LtMastFAQ> ltMastFAQlist;

	public LtMastAboutUs getLtMastAboutUs() {
		return ltMastAboutUs;
	}

	public void setLtMastAboutUs(LtMastAboutUs ltMastAboutUs) {
		this.ltMastAboutUs = ltMastAboutUs;
	}

	public List<LtMastFAQ> getLtMastFAQlist() {
		return ltMastFAQlist;
	}

	public void setLtMastFAQlist(List<LtMastFAQ> ltMastFAQlist) {
		this.ltMastFAQlist = ltMastFAQlist;
	}

}
