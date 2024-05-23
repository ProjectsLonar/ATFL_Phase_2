package com.lonar.cartservice.atflCartService.dto;

import java.util.List;

public class LtSoHeaderDto {

	
	private List<SoHeaderDto> SoHeaderDto;

	
	public List<SoHeaderDto> getSoHeaderDto() {
		return SoHeaderDto;
	}

	public void setSoHeaderDto(List<SoHeaderDto> soHeaderDto) {
		SoHeaderDto = soHeaderDto;
	}

	
	@Override
	public String toString() {
		return "LtSoHeaderDto [SoHeaderDto=" + SoHeaderDto + "]";
	}

	
}
