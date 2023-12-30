package com.lonar.atflMobileInterfaceService.service;

import com.lonar.atflMobileInterfaceService.model.Status;

public interface LtMastCommonMessageService {
	public Status getCodeAndMessage(Integer code);

	public String getCommonMessage(String message);

}
