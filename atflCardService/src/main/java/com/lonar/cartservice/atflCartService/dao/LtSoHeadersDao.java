package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.DistributorDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtOrderLineDataGt;
import com.lonar.cartservice.atflCartService.dto.QuantityCheck;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.model.LtMastOutles;
import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtOrderCancellationReason;
import com.lonar.cartservice.atflCartService.model.LtSalesPersonLocation;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;

public interface LtSoHeadersDao {

	LtSoHeaders checkHeaderStatusIsDraft(String outletId) throws ServiceException, IOException;

	LtSoHeaders checkOrderStatus(String orderNumber) throws ServiceException, IOException;

	void deleteLineDataByHeaderId(Long heaaderId) throws ServiceException, IOException;
	
	int deleteLineDataByHeaderIdAndReturnStatus(Long headerId) throws ServiceException, IOException;

	List<ResponseDto> getAllOrderInprocess() throws ServiceException, IOException;

	 List<Long> getSoHeader(RequestDto requestDto) throws ServiceException, IOException;
	
	Long getRecordCount(RequestDto requestDto) throws ServiceException, IOException;

	List<ResponseDto> getOrderV1(List<Long> headerIdList) throws ServiceException, IOException;

	List<ResponseDto> getOrderV2(List<Long> headerIdList) throws ServiceException, IOException;
	
	Long getSequancesValue() throws ServiceException, IOException;

	String getDistributorCode(String outletId) throws ServiceException, IOException;

	List<LtMastUsers> getActiveDistUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException;

	List<LtMastUsers> getActiveSalesUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException;
	
	DistributorDetailsDto getDistributorDetailsByOutletId(String outletId) throws ServiceException, IOException;
	
	void updateDistributorSequance(String distId, Long distSequance)throws ServiceException, IOException;
	
	String getMobileNumber(Long userId)throws ServiceException, IOException;
	
	String getPositionIdByUserId(Long userId)throws ServiceException, IOException;
	
	int insertLine(String query)throws ServiceException, IOException;
	
	
	//##### For Customer

	//LtSoHeaders checkHeaderStatusIsDraftForCust(Long customerId) throws ServiceException, IOException;
	
	//List<ResponseDto> getOrderV1ForCustomer(List<Long> headerIdList) throws ServiceException, IOException;
	
	//LtOutletPin checkOutletIsAvilable(Long headerId)throws ServiceException, IOException;
	
	List<LtOrderCancellationReason> getOrderCancellationReport() throws ServiceException, IOException;
	
	List<LtMastUsers> getActiveAreaHeadeUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException;

	List<LtMastOutles> getOutletDetailsById(String outletId)throws ServiceException, IOException;
	
	LtSalesPersonLocation locationSaveOnNoOrder(LtSalesPersonLocation ltSalesPersonLocation)throws ServiceException, IOException;

    LtMastUsers getUserDetailsAgainsUserId(Long userId)throws ServiceException, IOException;

	String getDefaultPriceListAgainstOutletId(String outletId)throws ServiceException, IOException;

	String getOrderSequence()throws ServiceException, IOException;

	List<LtMastUsers> getActiveSysAdminUsersFromHeaderId(Long headerId, String orderNumber)throws ServiceException, IOException;

	String getUserTypeAgainsUserId(Long createdBy)throws ServiceException, IOException;

	SoHeaderDto getheaderByHeaderId(Long headerId)throws ServiceException, IOException;

	String getEmailBody(String subject)throws ServiceException, IOException;

	String getUserNameAgainsUserId(Long createdBy)throws ServiceException, IOException;

	List<Double> getTotalAmount(Long headerId)throws ServiceException, IOException;
	List<String> getTotalAmount1(Long headerId)throws ServiceException, IOException;

	LtSoHeaders getSiebelDataById(Long headerId)throws ServiceException, IOException;
	
	List<Long> getSoHeaderRemovingPendingOrdersFromGetOrderV2(RequestDto requestDto) throws ServiceException, IOException;
	
	//Long getRecordCountRemovingPendingOrdersFromGetOrderV2(RequestDto requestDto) throws ServiceException, IOException;
	
	List<ResponseDto> getOrderV2RemovingPendingOrdersFromGetOrderV2(List<Long> headerIdList) throws ServiceException, IOException;

	List<SoHeaderDto> getheaderByHeaderIdNew(List<Long> headerIdsList)throws ServiceException, IOException;

	int[] batchInsert(List<String> sqlQueries);

	List<QuantityCheck> quantityCheck(String distributorId, List<String> productIdList)throws ServiceException, IOException;

	String getUserNameFromSiebel(String mobileNumber)throws ServiceException, IOException;

	List<ResponseDto> getSoHeader11(RequestDto requestDto)throws ServiceException, IOException;

	List<ResponseDto> getSoHeader111(RequestDto requestDto)throws ServiceException, IOException;

	List<Long> getSoHeaderFromProcedure()throws ServiceException, IOException;

	List<ResponseDto> getOrderV2FromProcedure(List<Long> headerIdsList)throws ServiceException, IOException;

	List<LtOrderLineDataGt> getOrderV2DataFromProcedure()throws ServiceException, IOException;

	List<LtOrderLineDataGt> getDataFromProcedure()throws ServiceException, IOException;

	List<ResponseDto> getMultipleMrpForProductV1(String prodId, String distributorId)throws ServiceException, IOException;
	List<ResponseDto> getMultipleMrpForInstockProductV1(String prodId, String distributorId, String priceList)throws ServiceException, IOException;
	
	List<ResponseDto> getMultipleMrpForOutofStockProductV1(String prodId, String distributorId)throws ServiceException, IOException;
	
	String checkOrderNoInSiebel(String orderNumber)throws ServiceException, IOException;

	List<String> getPriceListAgainstHeaderId(String headerIdList)throws ServiceException, IOException;

	List<ResponseDto> getOpenOrderWithNewStatusFromSiebel(String prod, String distributorId, String outletId)throws ServiceException, IOException;
	
	List<ResponseDto> getMrpForMultipleProductV1(String prodId, String distributorId)throws ServiceException, IOException;

	List<ResponseDto> getMultiMrpAndInventQtyForProd(String ids, String distributorId)throws ServiceException, IOException;

	String getPriceListId(String priceList)throws ServiceException, IOException;

	String findDistributorIdAgainstOutletId(String outletId)throws ServiceException, IOException;

	List<LtMastUsers> getAllAreaHeadAgainstDist(String distributorId)throws ServiceException, IOException;

	List<LtMastUsers> getSystemAdministartorsDetails(String orgId)throws ServiceException, IOException;

	List<LtMastUsers> getAllSalesOfficersAgainstDist(String distributorId)throws ServiceException, IOException;

	public Long getUserIdFromMobileNo(String mobileNumber)throws ServiceException, IOException;

	String getMobileNoFromOrderNo(String orderNumber)throws ServiceException, IOException;

	List<String> getEmpCodeFromDistributorId(String distCode)throws ServiceException, IOException;

	List<String> getDistributorIdFromAreaHead(String empcode)throws ServiceException, IOException;

	List<Long> getSoHeaderRemovingPendingOrdersFromGetOrderV2ForAreaHead(RequestDto requestDto, String distIdList)throws ServiceException, IOException;
	
	List<String> getDistributorIdFromAreaHeadNew(String empcode)throws ServiceException, IOException;
	
//	String getDistIdFromOutletCode(String outletCode)throws ServiceException, IOException;	
	
	String getDistIdFromOrderNo(String orderNumber) throws ServiceException, IOException;

	

}
