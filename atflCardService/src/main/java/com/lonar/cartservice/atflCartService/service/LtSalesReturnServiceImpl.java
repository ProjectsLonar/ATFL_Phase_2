package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.cartservice.atflCartService.dao.LtSalesreturnDao;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.SoLineDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;
import com.lonar.cartservice.atflCartService.model.Status;

@Service
public class LtSalesReturnServiceImpl implements LtSalesReturnService,CodeMaster {

	@Autowired
	LtSalesreturnDao  ltSalesreturnDao;
	
	@Override
	public Status saveSalesReturn(LtSalesReturnDto ltSalesReturnDto) throws ServerException{
		
		return null;
	}
	
	@Override
	public Status getStatusForSalesReturn()throws ServerException{
		try {
		Status status = new Status();
		List<LtSalesReturnStatus> list = ltSalesreturnDao.getStatusForSalesReturn();
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Status getAvailabilityForSalesReturn() throws ServerException {
		try {
		Status status = new Status();
		List<LtSalesReturnAvailability> list = ltSalesreturnDao.getAvailabilityForSalesReturn();
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Status getLocationForSalesReturn(String distributorCode) throws ServerException {
		try {
		Status status = new Status();
		List<LtSalesReturnAvailability> list = ltSalesreturnDao.getLocationForSalesReturn(distributorCode);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public Status getSalesReturn(RequestDto requestDto) throws ServerException{
		try {
		Status status = new Status();
		
		List<Long> IdsList = ltSalesreturnDao.getSalesReturnHeader(requestDto);
		Long recordCount = ltSalesreturnDao.getSalesReturnRecordCount(requestDto);
		
		status.setTotalCount(recordCount);
		status.setRecordCount(recordCount);
		if(IdsList.isEmpty()) {
			status.setCode(RECORD_NOT_FOUND);
			status.setData("Record not found"); 
			return status;
		}
		
		List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
		
		
		List<ResponseDto> list = ltSalesreturnDao.getSalesReturn(IdsList);
		
		Map<Long, LtSalesReturnDto> salesReturnHeaderDtoMap = new LinkedHashMap<Long, LtSalesReturnDto>();
		Map<Long, List<LtSalesReturnLines>> salesReturnLineDtoMap = new LinkedHashMap<Long, List<LtSalesReturnLines>>();
		
		for (Iterator iterator = responseDtoList.iterator(); iterator.hasNext();) {
			ResponseDto responseDto = (ResponseDto) iterator.next();

			LtSalesReturnLines salesReturnLineDto = new LtSalesReturnLines();

			if (responseDto.getSalesReturnLineId() != null) {
				salesReturnLineDto.setSalesReturnLineId(responseDto.getSalesReturnLineId());;
			}
			if (responseDto.getSalesReturnHeaderId() != null) {
				salesReturnLineDto.setSalesReturnHeaderId(responseDto.getSalesReturnHeaderId());;
			}
			if (responseDto.getProductId() != null) {
				salesReturnLineDto.setProductId(responseDto.getProductId());
			}
			if (responseDto.getShippedQuantity() != null) {
				salesReturnLineDto.setShippedQuantity(responseDto.getShippedQuantity());
			}
			if (responseDto.getReturnQuantity() != null) {
				salesReturnLineDto.setReturnQuantity(responseDto.getReturnQuantity());
			}
			if (responseDto.getAvailability() != null) {
				salesReturnLineDto.setAvailability(responseDto.getAvailability());
			}
			if (responseDto.getLocation() != null) {
				salesReturnLineDto.setLocation(responseDto.getLocation());
			}
			
			if (salesReturnLineDtoMap.get(responseDto.getSalesReturnHeaderId()) != null) {
				// already exost
				List<LtSalesReturnLines> soLineDtoList = salesReturnLineDtoMap.get(responseDto.getSalesReturnHeaderId());
				if(salesReturnLineDto.getSalesReturnLineId() != null) {
					soLineDtoList.add(salesReturnLineDto);
				}
				salesReturnLineDtoMap.put(responseDto.getSalesReturnHeaderId(), soLineDtoList);
			} else {
				// add data into soheader map
				LtSalesReturnDto soHeaderDto = new LtSalesReturnDto();
				soHeaderDto.setSalesReturnHeaderId(responseDto.getSalesReturnHeaderId());
				soHeaderDto.setSalesReturnNumber(responseDto.getSalesReturnNumber());
				soHeaderDto.setInvoiceNumber(responseDto.getInvoiceNumber());
				soHeaderDto.setOutletId(responseDto.getOutletId());
				soHeaderDto.setReturnReason(responseDto.getReturnReason());
				soHeaderDto.setReturnStatus(responseDto.getReturnStatus());
				
				salesReturnHeaderDtoMap.put(responseDto.getHeaderId(), soHeaderDto);

				List<LtSalesReturnLines> soLineDtoList = new ArrayList<LtSalesReturnLines>();
				
				if (salesReturnLineDto.getSalesReturnLineId() != null) {
					soLineDtoList.add(salesReturnLineDto);
				}
				salesReturnLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);	 
			}

		}
		
		LtSalesReturnDetailsDto orderDetailsDto = new LtSalesReturnDetailsDto();

		List<LtSalesReturnDto> soHeaderDtoList = new ArrayList<LtSalesReturnDto>();

		for (Map.Entry<Long, LtSalesReturnDto> entry : salesReturnHeaderDtoMap.entrySet()) {
			LtSalesReturnDto soHeaderDto = entry.getValue();

			List<LtSalesReturnLines> soLineDtoList = salesReturnLineDtoMap.get(entry.getKey());
			soHeaderDto.setLtSalesReturnLines(soLineDtoList);

			soHeaderDtoList.add(soHeaderDto);
		}
		orderDetailsDto.setLtSalesReturnDto(soHeaderDtoList);
		
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(orderDetailsDto);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Status getInvoices(RequestDto requestDto) throws ServerException{
		return null;
	}
}
