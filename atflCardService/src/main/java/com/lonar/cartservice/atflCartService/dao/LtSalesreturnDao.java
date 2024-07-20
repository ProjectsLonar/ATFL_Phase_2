package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsResponseDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnHeaderDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnLineDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnResponseDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.SalesReturnApproval;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderLineDto;
import com.lonar.cartservice.atflCartService.dto.SoLineDto;
import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;
import com.lonar.cartservice.atflCartService.dto.SalesReturnResponse;

public interface LtSalesreturnDao {

	List<LtSalesReturnStatus> getStatusForSalesReturn() throws ServerException;
	
	List<LtSalesReturnAvailability> getAvailabilityForSalesReturn() throws ServerException;
	
	List<LtSalesReturnAvailability> getLocationForSalesReturn(String distributorCode) throws ServerException;
	
	String getDefaultLocationForSalesReturn(String distributorCode) throws ServerException;
	
	List<Long> getSalesReturnHeader(RequestDto requestDto)throws ServerException;
	
	Long getSalesReturnRecordCount(RequestDto requestDto)throws ServerException; 
	
	List<ResponseDto> getSalesReturn(List<Long> IdsList) throws ServerException;
	
	void deleteSalesReturnLinesByHeaderId(Long HeaderId)throws ServerException;
	
	LtSalesReturnHeader updateSalesReturnHeader(LtSalesReturnHeader ltSalesreturnHeader)throws ServerException;
	
	LtSalesReturnLines updateLines(LtSalesReturnLines ltSalesreturnlines) throws ServerException;
	
	List<LtInvoiceDetailsResponseDto> getInvoiceDetails( RequestDto requestDto) throws ServerException;
	
	String getSalesReturnSequence();

	List<SoHeaderDto> getBeatNameAgainstInvoiceNo(String invoiceNo)throws ServerException;

	List<LtSalesReturnAvailability> getLotNumber(String prodId, String inventId) throws ServerException;

	List<LtMastUsers> getAreaHeadDetails(String outletId)throws ServerException;

	LtSalesReturnHeader getSalesReturnSiebelDataById(Long salesReturnHeaderId)throws ServerException;

	List<LtSalesReturnResponseDto> getSalesReturnForPendingAprroval(List<Long> salesReturnHeaderId)throws ServerException;

	List<Long> getSalesReturnHeaderId(RequestDto requestDto)throws ServerException;

	List<LtSalesReturnLineDto> getSalesReturnLineData(Long long1, RequestDto requestDto)throws ServerException;
	
	LtSalesReturnResponseDto getSalesReturnForPendingAprroval1(Long salesReturnHeaderId, RequestDto requestDto)throws ServerException;
    
	List<String> getSalesOrderInvoiceNo(RequestDto requestDto)throws ServerException;

	List<SoHeaderDto> getSoHeaderDataFromInvoiceNo(List<String> salesReturnInvoice, RequestDto requestDto)throws ServerException;

	//List<SoLineDto> getSoLineData(Long headerId, RequestDto requestDto)throws ServerException;

	List<SoLineDto> getSoLineData(String siebelInvoicenumber, RequestDto requestDto)throws ServerException;
	
	SoHeaderLineDto getSoHeaderData(String string, RequestDto requestDto)throws ServerException;

	List<SoHeaderDto> getSoHeaderDataFromInvoiceNo12(String string, RequestDto requestDto);
	
	SoHeaderDto getSoHeaderData12(String string, RequestDto requestDto)throws ServerException;

	List<LtSalesReturnLines> getSalesReturnLineDataForApproval(Long long1, RequestDto requestDto)throws ServerException;

	ResponseDto getSalesReturnForAprroval1(Long long1, RequestDto requestDto)throws ServerException;

	String getInvoiceNumber(String orderNumber)throws ServerException;

	String getUserNameAgainsUserId(Long userId)throws ServerException;

	String getOrderNoFromInvoiceNo(String invoiceNumber)throws ServerException;

	String getDistIdFromOutletCode(String outletId)throws ServerException;

	String getProdNameFromProdId(String productId)throws ServerException;

	SoLineDto getProductId(String siebelInvoicenumber)throws ServerException;

	StringBuilder getInvoiceErrorMSg(String orderNumber)throws ServerException;

	LtSalesReturnHeaderDto getSalesReturnOrderAgainstReturnOrderNo(String returnOrderNo)throws ServerException;

	List<LtSalesReturnLineDto> getSalesReturnOrderLineData(Long salesReturnHeaderId)throws ServerException;

	List<LtMastUsers> getSalesOfficersDetails(String outletId)throws ServerException;

	List<LtMastUsers> getSysAdminDetails(String outletId)throws ServerException;

	List<SoHeaderDto> getSoHeaderDataNew(List<String> salesReturnInvoice, RequestDto requestDto)throws ServerException;

	String getUserNameFromSiebel(String mobileNumber)throws ServerException;
	
	List<ResponseDto> getSalesReturnForAprroval_Opt(List<Long> long1, RequestDto requestDto)throws ServerException;
	
	Map<Long, List<LtSalesReturnLines>> getSalesReturnLinesForApproval_Opt(List<Long> long1, RequestDto requestDto) throws ServerException;

	List<LtSalesReturnLineDto> getSalesReturnLineData(List<Long> long1, RequestDto requestDto)throws ServerException;

	List<LtMastUsers> getAllUsersForEmail(String outletId)throws ServerException;
}
