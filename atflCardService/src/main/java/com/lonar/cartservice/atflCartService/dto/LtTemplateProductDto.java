package com.lonar.cartservice.atflCartService.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LtTemplateProductDto {
	Long templateLineId;
	Long templateHeaderId;
	String salesType;
	String productId;
	String productName;
	String productDescription;
	Long ptrPrice;
	Long quantity;
	Long availableQuantity;
	String creationDate;
	String createdBy;
	String lastUpdatedDate;
	String lastUpdatedBy;
	String lastUpdatedLogin;
}
