package com.lonar.cartservice.atflCartService.model;

public class LtSalesReturnStatus {
private String  statusCode;
private String status;
public String getStatusCode() {
	return statusCode;
}
public void setStatusCode(String statusCode) {
	this.statusCode = statusCode;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
@Override
public String toString() {
	return "LtSalesReturnStatus [statusCode=" + statusCode + ", status=" + status + "]";
}



}
