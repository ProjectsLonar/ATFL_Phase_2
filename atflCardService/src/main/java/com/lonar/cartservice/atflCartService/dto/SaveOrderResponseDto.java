package com.lonar.cartservice.atflCartService.dto;

import java.util.List;

import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.LtSoLines;

public class SaveOrderResponseDto {
	private LtSoHeaders ltSoHeaders;
	private List<LtSoLines> ltSoLinesList;

	public LtSoHeaders getLtSoHeaders() {
		return ltSoHeaders;
	}

	public void setLtSoHeaders(LtSoHeaders ltSoHeaders) {
		this.ltSoHeaders = ltSoHeaders;
	}

	public List<LtSoLines> getLtSoLinesList() {
		return ltSoLinesList;
	}

	public void setLtSoLinesList(List<LtSoLines> ltSoLinesList) {
		this.ltSoLinesList = ltSoLinesList;
	}

}
