package com.lonar.cartservice.atflCartService.dto;

import java.util.List;

public class LtInvoiceDto {

	private List<LtInvoiceDetailsDto> ltInvoiceDetailsDto;

	public List<LtInvoiceDetailsDto> getLtInvoiceDetailsDto() {
		return ltInvoiceDetailsDto;
	}

	public void setLtInvoiceDetailsDto(List<LtInvoiceDetailsDto> ltInvoiceDetailsDto) {
		this.ltInvoiceDetailsDto = ltInvoiceDetailsDto;
	}
	
}
