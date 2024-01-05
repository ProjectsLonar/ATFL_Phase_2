package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.util.List;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.DistributorDetailsDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtOrderCancellationReason;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;

public interface LtSoHeadersDao {

	LtSoHeaders checkHeaderStatusIsDraft(String outletId) throws ServiceException, IOException;

	LtSoHeaders checkOrderStatus(String orderNumber) throws ServiceException, IOException;

	void deleteLineDataByHeaderId(Long heaaderId) throws ServiceException, IOException;
	
	int deleteLineDataByHeaderIdAndReturnStatus(Long headerId) throws ServiceException, IOException;

	List<ResponseDto> getAllOrderInprocess() throws ServiceException, IOException;

	List<String> getSoHeader(RequestDto requestDto) throws ServiceException, IOException;
	
	Long getRecordCount(RequestDto requestDto) throws ServiceException, IOException;

	List<ResponseDto> getOrderV1(List<String> headerIdList) throws ServiceException, IOException;

	List<ResponseDto> getOrderV2(List<String> headerIdList) throws ServiceException, IOException;
	
	Long getSequancesValue() throws ServiceException, IOException;

	String getDistributorCode(String outletId) throws ServiceException, IOException;

	List<LtMastUsers> getActiveDistUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException;

	List<LtMastUsers> getActiveSalesUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException;
	
	DistributorDetailsDto getDistributorDetailsByOutletId(String outletId) throws ServiceException, IOException;
	
	void updateDistributorSequance(String distId, Long distSequance)throws ServiceException, IOException;
	
	String getMobileNumber(String userId)throws ServiceException, IOException;
	
	String getPositionIdByUserId(String userId)throws ServiceException, IOException;
	
	int insertLine(String query)throws ServiceException, IOException;
	
	
	//##### For Customer

	//LtSoHeaders checkHeaderStatusIsDraftForCust(Long customerId) throws ServiceException, IOException;
	
	//List<ResponseDto> getOrderV1ForCustomer(List<Long> headerIdList) throws ServiceException, IOException;
	
	//LtOutletPin checkOutletIsAvilable(Long headerId)throws ServiceException, IOException;
	
	List<LtOrderCancellationReason> getOrderCancellationReport() throws ServiceException, IOException;
	
}
