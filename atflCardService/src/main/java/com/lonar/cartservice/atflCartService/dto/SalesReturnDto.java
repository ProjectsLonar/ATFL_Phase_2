package com.lonar.cartservice.atflCartService.dto;

import java.util.List;

public class SalesReturnDto {

	private List<LtSalesReturnHeaderDto> ltSalesReturnHeaderDto;

	
	
	public List<LtSalesReturnHeaderDto> getLtSalesReturnHeaderDto() {
		return ltSalesReturnHeaderDto;
	}

	public void setLtSalesReturnHeaderDto(List<LtSalesReturnHeaderDto> ltSalesReturnHeaderDto) {
		this.ltSalesReturnHeaderDto = ltSalesReturnHeaderDto;
	}

	
	@Override
	public String toString() {
		return "SalesReturnDto [ltSalesReturnHeaderDto=" + ltSalesReturnHeaderDto + "]";
	}
	
	
	
}
