package com.lonar.cartservice.atflCartService.dto;

import java.util.List;

public class OrderDetailsDto {
	private List<SoHeaderDto> soHeaderDto;

	public List<SoHeaderDto> getSoHeaderDto() {
		return soHeaderDto;
	}

	public void setSoHeaderDto(List<SoHeaderDto> soHeaderDto) {
		this.soHeaderDto = soHeaderDto;
	}

}
