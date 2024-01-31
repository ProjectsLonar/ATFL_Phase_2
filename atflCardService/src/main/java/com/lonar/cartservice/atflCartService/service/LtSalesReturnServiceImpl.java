package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.dao.LtSalesreturnDao;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsLineDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsResponseDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.repository.LtSalesRetrunLinesRepository;
import com.lonar.cartservice.atflCartService.repository.LtSalesReturnRepository;

@Service
public class LtSalesReturnServiceImpl implements LtSalesReturnService,CodeMaster {

	@Autowired
	LtSalesreturnDao  ltSalesreturnDao;
	
	@Autowired
	LtSalesReturnRepository ltSalesReturnRepository;
	
	@Autowired
	LtSalesRetrunLinesRepository ltSalesRetrunLinesRepository;
	
	@Override
	public Status saveSalesReturn(LtSalesReturnDto ltSalesReturnDto) throws ServerException{
		try {
		Status status = new Status();
		LtSalesReturnHeader ltSalesReturnHeader = new LtSalesReturnHeader();
		if(ltSalesReturnDto !=null) {
			if(ltSalesReturnDto.getSalesReturnHeaderId() !=null && ltSalesReturnDto.getSalesReturnNumber() !=null) {
				//code for update
				
				//delete sales return lines
				ltSalesreturnDao.deleteSalesReturnLinesByHeaderId(ltSalesReturnDto.getSalesReturnHeaderId());
				
				ltSalesReturnHeader.setSalesReturnHeaderId(ltSalesReturnDto.getSalesReturnHeaderId());
				ltSalesReturnHeader.setSalesReturnNumber(ltSalesReturnDto.getSalesReturnNumber());
				if(ltSalesReturnDto.getOutletName() !=null) {
					ltSalesReturnHeader.setOutletName(ltSalesReturnDto.getOutletName());
				}
				if(ltSalesReturnDto.getOutletCode() !=null) {
					ltSalesReturnHeader.setOutletCode(ltSalesReturnDto.getOutletCode());
				}
				if(ltSalesReturnDto.getOutletId() !=null) {
					ltSalesReturnHeader.setOutletId(ltSalesReturnDto.getOutletId());
				}if(ltSalesReturnDto.getInvoiceNumber() !=null) {
					ltSalesReturnHeader.setInvoiceNumber(ltSalesReturnDto.getInvoiceNumber());
				}
				
				if(ltSalesReturnDto.getReturnReason() !=null) {
					ltSalesReturnHeader.setReturnReason(ltSalesReturnDto.getReturnReason());
				}
				if(ltSalesReturnDto.getReturnStatus() !=null) {
					ltSalesReturnHeader.setReturnStatus(ltSalesReturnDto.getReturnStatus());
				}
				
				if(ltSalesReturnDto.getAddress() !=null) {
					ltSalesReturnHeader.setAddress(ltSalesReturnDto.getAddress());
				}
				if(ltSalesReturnDto.getLatitude() !=null) {
					ltSalesReturnHeader.setLatitude(ltSalesReturnDto.getLatitude());
				}
				if(ltSalesReturnDto.getLongitude() !=null) {
					ltSalesReturnHeader.setLongitude(ltSalesReturnDto.getLongitude());
				}
				ltSalesReturnHeader.setSalesReturnDate(new Date());
				//ltSalesReturnHeader.setCreatedBy(ltSalesReturnDto.getUserId());
				//ltSalesReturnHeader.setCreationDate(new Date());
				ltSalesReturnHeader.setLastUpdatedBy(ltSalesReturnDto.getUserId());
				ltSalesReturnHeader.setLastUpdateDate(new Date());
				
				ltSalesReturnHeader = updateSalesReturnHeader(ltSalesReturnHeader);
				
				//save sales return lines
				List<LtSalesReturnLines> salesReturnLines = ltSalesReturnDto.getLtSalesReturnLines();
				
				for (Iterator iterator = salesReturnLines.iterator(); iterator.hasNext();) {
					LtSalesReturnLines soLine = (LtSalesReturnLines) iterator.next();

					LtSalesReturnLines ltSoLinestosave = new LtSalesReturnLines();
					
					if(ltSalesReturnHeader.getSalesReturnHeaderId() !=null) {
						ltSoLinestosave.setSalesReturnHeaderId(ltSalesReturnHeader.getSalesReturnHeaderId());
					}
					if(soLine.getProductId() !=null) {
						ltSoLinestosave.setProductId(soLine.getProductId());
					}
					if(soLine.getShippedQuantity() !=null) {
						ltSoLinestosave.setShippedQuantity(soLine.getShippedQuantity());
					}
					if(soLine.getReturnQuantity() !=null) {
						ltSoLinestosave.setReturnQuantity(soLine.getReturnQuantity());
					}
					if(soLine.getAvailability() !=null) {
						ltSoLinestosave.setAvailability(soLine.getAvailability());
					}if(soLine.getLocation() !=null) {
						ltSoLinestosave.setLocation(soLine.getLocation());
					}if(soLine.getStatus() !=null) {
						ltSoLinestosave.setStatus(soLine.getStatus());
					}
					ltSoLinestosave = updateLines(ltSoLinestosave);
				}
				
			}else {
				//code for invoice number generation
				String salesReturnNumber = generateSalesreturnNumber();
				
				ltSalesReturnHeader.setSalesReturnNumber(salesReturnNumber);
				
				if(ltSalesReturnDto.getOutletName() !=null) {
					ltSalesReturnHeader.setOutletName(ltSalesReturnDto.getOutletName());
				}
				if(ltSalesReturnDto.getOutletCode() !=null) {
					ltSalesReturnHeader.setOutletCode(ltSalesReturnDto.getOutletCode());
				}
				if(ltSalesReturnDto.getOutletId() !=null) {
					ltSalesReturnHeader.setOutletId(ltSalesReturnDto.getOutletId());
				}if(ltSalesReturnDto.getInvoiceNumber() !=null) {
					ltSalesReturnHeader.setInvoiceNumber(ltSalesReturnDto.getInvoiceNumber());
				}
				
				if(ltSalesReturnDto.getReturnReason() !=null) {
					ltSalesReturnHeader.setReturnReason(ltSalesReturnDto.getReturnReason());
				}
				if(ltSalesReturnDto.getReturnStatus() !=null) {
					ltSalesReturnHeader.setReturnStatus(ltSalesReturnDto.getReturnStatus());
				}
				
				if(ltSalesReturnDto.getAddress() !=null) {
					ltSalesReturnHeader.setAddress(ltSalesReturnDto.getAddress());
				}
				if(ltSalesReturnDto.getLatitude() !=null) {
					ltSalesReturnHeader.setLatitude(ltSalesReturnDto.getLatitude());
				}
				if(ltSalesReturnDto.getLongitude() !=null) {
					ltSalesReturnHeader.setLongitude(ltSalesReturnDto.getLongitude());
				}
				ltSalesReturnHeader.setSalesReturnDate(new Date());
				ltSalesReturnHeader.setCreatedBy(ltSalesReturnDto.getUserId());
				ltSalesReturnHeader.setCreationDate(new Date());
				ltSalesReturnHeader.setLastUpdatedBy(ltSalesReturnDto.getUserId());
				ltSalesReturnHeader.setLastUpdateDate(new Date());
				
				ltSalesReturnHeader = updateSalesReturnHeader(ltSalesReturnHeader);
				
				List<LtSalesReturnLines> salesReturnLines = ltSalesReturnDto.getLtSalesReturnLines();
				
				for (Iterator iterator = salesReturnLines.iterator(); iterator.hasNext();) {
					LtSalesReturnLines soLine = (LtSalesReturnLines) iterator.next();

					LtSalesReturnLines ltSoLinestosave = new LtSalesReturnLines();
					
					if(ltSalesReturnHeader.getSalesReturnHeaderId() !=null) {
						ltSoLinestosave.setSalesReturnHeaderId(ltSalesReturnHeader.getSalesReturnHeaderId());
					}
					if(soLine.getProductId() !=null) {
						ltSoLinestosave.setProductId(soLine.getProductId());
					}
					if(soLine.getShippedQuantity() !=null) {
						ltSoLinestosave.setShippedQuantity(soLine.getShippedQuantity());
					}
					if(soLine.getReturnQuantity() !=null) {
						ltSoLinestosave.setReturnQuantity(soLine.getReturnQuantity());
					}
					if(soLine.getAvailability() !=null) {
						ltSoLinestosave.setAvailability(soLine.getAvailability());
					}if(soLine.getLocation() !=null) {
						ltSoLinestosave.setLocation(soLine.getLocation());
					}if(soLine.getStatus() !=null) {
						ltSoLinestosave.setStatus(soLine.getStatus());
					}
					ltSoLinestosave = updateLines(ltSoLinestosave);
				}
				
				
			}
			
			//get sales return response
			RequestDto requestDto = new RequestDto();
			requestDto.setSalesReturnHeaderId(ltSalesReturnHeader.getSalesReturnHeaderId());
			requestDto.setLimit(1);
			requestDto.setOffset(2);
			
			status = getSalesReturn(requestDto);
			if(status.getData() !=null) {
				status.setCode(SUCCESS);
				status.setMessage("Done");
				status.setData(status.getData());
				return status;
			}else {
				status.setCode(FAIL);
				status.setMessage("RECORD NOT FOUND");
				return status;
			}
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	String generateSalesreturnNumber() throws ServerException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
	    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
	    String finYear;
	    System.out.println("Financial month : " + month);
	    if (month <= 3) {
	        finYear = (String.valueOf(year- 1)).substring(2)+""+(String.valueOf(year)).substring(2);
	    } else {
	        finYear = (String.valueOf(year)).substring(2)+""+(String.valueOf(year + 1)).substring(2);
	    }
	
	   //Long sequanceNo = ltSalesreturnDao.getSequancesValue();
		String seqNoSixDigit = String.format("%06d", 123);
		return "MSR-" + finYear + "-" + seqNoSixDigit;
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
		System.out.println("requestDto"+requestDto);
		List<Long> IdsList = ltSalesreturnDao.getSalesReturnHeader(requestDto);
		Long recordCount = ltSalesreturnDao.getSalesReturnRecordCount(requestDto);
		System.out.println("IdsList"+IdsList +" "+recordCount);
		status.setTotalCount(recordCount);
		status.setRecordCount(recordCount);
		if(IdsList.isEmpty()) {
			status.setCode(RECORD_NOT_FOUND);
			status.setData("Record not found"); 
			return status;
		}
		
		List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
		
		
		 responseDtoList = ltSalesreturnDao.getSalesReturn(IdsList);
		
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
		
		if (responseDtoList != null) {
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
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private LtSalesReturnHeader updateSalesReturnHeader(LtSalesReturnHeader ltSalesreturnHeader) throws ServerException {
		LtSalesReturnHeader ltSoHeaderOp = null;
		ltSoHeaderOp = ltSalesreturnDao.updateSalesReturnHeader(ltSalesreturnHeader);
		return ltSoHeaderOp;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private LtSalesReturnLines updateLines(LtSalesReturnLines ltSalesreturnlines) throws ServerException {
		LtSalesReturnLines ltSoHeaderOp = null;
		ltSoHeaderOp = ltSalesreturnDao.updateLines(ltSalesreturnlines);
		return ltSoHeaderOp;
	}
	@Override
	public Status getInvoices(RequestDto requestDto) throws ServerException{
		
		Status status = new Status();
		List<LtInvoiceDetailsResponseDto> ltInvoiceDetailsResponseDto = new ArrayList<LtInvoiceDetailsResponseDto>();
		ltInvoiceDetailsResponseDto = ltSalesreturnDao.getInvoiceDetails(requestDto);
		
		Map<String, LtInvoiceDetailsDto> invoiceDetailsHeaderDtoMap = new LinkedHashMap<String, LtInvoiceDetailsDto>();
		Map<String, List<LtInvoiceDetailsLineDto>> invoiceDetailsLineDtoMap = new LinkedHashMap<String, List<LtInvoiceDetailsLineDto>>();
		
		for (Iterator iterator = ltInvoiceDetailsResponseDto.iterator(); iterator.hasNext();) {
			LtInvoiceDetailsResponseDto responseDto = (LtInvoiceDetailsResponseDto) iterator.next();

			LtInvoiceDetailsLineDto invoiceDetailsLineDto = new LtInvoiceDetailsLineDto();

			if (responseDto.getProductCode() != null) {
				invoiceDetailsLineDto.setProductCode(responseDto.getProductCode());
			}
			if(responseDto.getProductName() !=null) {
				invoiceDetailsLineDto.setProductName(responseDto.getProductName());
			}
			if(responseDto.getShippedQuantity() !=null) {
				invoiceDetailsLineDto.setShippedQuantity(responseDto.getShippedQuantity());
			}
			if(responseDto.getPtrPrice() !=null) {
				invoiceDetailsLineDto.setPtrPrice(responseDto.getPtrPrice());
			}if(responseDto.getListPrice() !=null) {
				invoiceDetailsLineDto.setListPrice(responseDto.getListPrice());
			}
			if(responseDto.getPtrBasePrice() !=null) {
				invoiceDetailsLineDto.setPtrBasePrice(responseDto.getPtrBasePrice());
			}
			
			if(invoiceDetailsLineDtoMap.get(responseDto.getProductCode()) !=null) {
				List<LtInvoiceDetailsLineDto> invoiceLineDtoList = invoiceDetailsLineDtoMap.get(responseDto.getProductCode());
				if(invoiceDetailsLineDto.getProductCode() != null) {
					invoiceLineDtoList.add(invoiceDetailsLineDto);
				}
				invoiceDetailsLineDtoMap.put(responseDto.getProductCode(), invoiceLineDtoList);
			}else {
				

				// add data into soheader map
				LtInvoiceDetailsDto invoiceHeaderDto = new LtInvoiceDetailsDto();
				
				invoiceHeaderDto.setDistributorName(responseDto.getDistributorName());
				invoiceHeaderDto.setDistributorCode(responseDto.getDistributorCode());
				invoiceHeaderDto.setDistributorId(responseDto.getDistributorId());
				invoiceHeaderDto.setOrderNumber(responseDto.getOrderNumber());
				invoiceHeaderDto.setOutletCode(responseDto.getOutletCode());
				invoiceHeaderDto.setOutletName(responseDto.getOutletName());
				invoiceHeaderDto.setInvoiveNumber(responseDto.getInvoiveNumber());
				invoiceHeaderDto.setInvoiceDate(responseDto.getInvoiceDate());
				invoiceHeaderDto.setLocation(responseDto.getLocation());
				invoiceHeaderDto.setTotalAmount(responseDto.getTotalAmount());
				
				invoiceDetailsHeaderDtoMap.put(responseDto.getInvoiveNumber(), invoiceHeaderDto);

				List<LtInvoiceDetailsLineDto> invoiceLineDtoList = new ArrayList<LtInvoiceDetailsLineDto>();
				
				//List<LtInvoiceDetailsLineDto> invoiceLineDtoList = invoiceDetailsLineDtoMap.get(responseDto.getProductCode());
				if(invoiceDetailsLineDto.getProductCode() != null) {
					invoiceLineDtoList.add(invoiceDetailsLineDto);
				}
				invoiceDetailsLineDtoMap.put(responseDto.getInvoiveNumber(), invoiceLineDtoList);
				
			}
		}
		
		LtInvoiceDto invoiceDetailsDto = new LtInvoiceDto();

		List<LtInvoiceDetailsDto> invoiceHeaderDtoList = new ArrayList<LtInvoiceDetailsDto>();

		for (Map.Entry<String, LtInvoiceDetailsDto> entry : invoiceDetailsHeaderDtoMap.entrySet()) {
			LtInvoiceDetailsDto invoiceHeaderDto = entry.getValue();

			List<LtInvoiceDetailsLineDto> ltInvoiceDetailsLineDto = invoiceDetailsLineDtoMap.get(entry.getKey());
			invoiceHeaderDto.setLtInvoiceDetailsLineDto(ltInvoiceDetailsLineDto);

			invoiceHeaderDtoList.add(invoiceHeaderDto);
		}
		invoiceDetailsDto.setLtInvoiceDetailsDto(invoiceHeaderDtoList);
		
		
		if (ltInvoiceDetailsResponseDto != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(invoiceDetailsDto);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
	}
}
