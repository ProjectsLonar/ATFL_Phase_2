package com.lonar.cartservice.atflCartService.dto;

import java.util.List;

public class SalesReturnApproval {

	
	private List<ResponseDto> ResponseDto;

	public List<ResponseDto> getResponseDto() {
		return ResponseDto;
	}

	public void setResponseDto(List<ResponseDto> responseDto) {
		ResponseDto = responseDto;
	}

	@Override
	public String toString() {
		return "SalesReturnApproval [ResponseDto=" + ResponseDto + "]";
	}
	
	
}
