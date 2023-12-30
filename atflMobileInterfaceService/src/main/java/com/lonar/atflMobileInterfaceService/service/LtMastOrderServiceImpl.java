package com.lonar.atflMobileInterfaceService.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.atflMobileInterfaceService.dao.LtMastOrderDao;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtMastOrder;
import com.lonar.atflMobileInterfaceService.model.LtSoHeaders;
import com.lonar.atflMobileInterfaceService.model.LtSoLines;
import com.lonar.atflMobileInterfaceService.model.OrderOutDto;
import com.lonar.atflMobileInterfaceService.repository.LtSoHeadersRepository;
import com.lonar.atflMobileInterfaceService.repository.LtSoLinesRepository;
import com.lonar.atflMobileInterfaceService.thread.InitiateThread;

@Service
public class LtMastOrderServiceImpl implements LtMastOrderService {

	@Autowired
	LtMastOrderDao ltMastOrderDao;

	@Autowired
	LtSoHeadersRepository ltSoHeadersRepository;

	@Autowired
	LtSoLinesRepository ltSoLinesRepository;

	@Autowired
	LtJobeService ltJobeService;

	@Override
	public List<LtMastOrder> getAllInprocessOrder() throws Exception {
		return ltMastOrderDao.getAllInprocessOrder();
	}

	@Override
	public boolean updateOrderStatus(String orderNumber) {
		return ltMastOrderDao.updateOrderStatus(orderNumber);
	}

	@Override
	public LtJobLogs readOrderCSVFile(LtJobLogs ltJobLogs) throws Exception {

		String filePath = InitiateThread.configMap.get("Export_Dest_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader().withTrim());

		List<OrderOutDto> orderList = new ArrayList<OrderOutDto>();

		for (CSVRecord record : parser) {
			OrderOutDto orderOutDto = new OrderOutDto();
			orderOutDto.setOrderNum(record.get("ORDERNUM").trim().toString());
			orderOutDto.setOutletCode(record.get("OUTLETCODE").trim().toString());
			orderOutDto.setProductCode(record.get("PRODUCTCODE").trim().toString());
			orderOutDto.seteImStatus(record.get("EIMSTATUS").trim().toString());
			orderOutDto.setStatus(record.get("STATUS").trim().toString());
			orderList.add(orderOutDto);
		}

		parser.close();
		System.out.println("List Size :: " + orderList.size());

		// Create CSV File Here

		// boolean fileCreate = CreateCSVFile.createOrderCSVFile(orderList, ltJobLogs);

		// Line status here change
		for (OrderOutDto orderOutDto : orderList) {
			LtSoLines ltSoLines = ltMastOrderDao.getLineByOrdernumProductCode(orderOutDto.getOrderNum(),
					orderOutDto.getProductCode());
			if (ltSoLines != null) {

				if (orderOutDto.getStatus().equalsIgnoreCase("Success")) {
					ltSoLines.setStatus("PROCESSED");
					ltSoLines.setEimstatus(orderOutDto.geteImStatus().toString());
				} else {
					ltSoLines.setStatus("ERROR");
					ltSoLines.setEimstatus(orderOutDto.geteImStatus().toString());
				}
				ltSoLinesRepository.save(ltSoLines);
			}

		}
		for (OrderOutDto orderOutDto : orderList) {

			List<LtSoHeaders> orderListFailed = ltMastOrderDao.getFailedorderList(orderOutDto.getOrderNum());
			if (orderListFailed == null) {
				LtSoHeaders ltSoHeaders = ltMastOrderDao.getHeaderByOrderNumber(orderOutDto.getOrderNum());
				if (ltSoHeaders != null) {
					ltSoHeaders.setStatus("PROCESSED");
					ltSoHeadersRepository.save(ltSoHeaders);
				}
			} else {
				LtSoHeaders ltSoHeaders = ltMastOrderDao.getHeaderByOrderNumber(orderOutDto.getOrderNum());
				if (ltSoHeaders != null) {
					ltSoHeaders.setStatus("ERROR");
					ltSoHeadersRepository.save(ltSoHeaders);
				}

			}

		}

		ltJobLogs.setSuccessRecord(Long.valueOf(orderList.size()));
		ltJobLogs.setTotalRecord(Long.valueOf(orderList.size()));
		ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);

		return ltJobLogs;
	}

	@Override
	public List<LtSoLines> checkDoubleOrderEntry(LtMastOrder ltMastOrder) throws Exception {
		return ltMastOrderDao.checkDoubleOrderEntry(ltMastOrder);
	}

	@Override
	public LtSoLines delete(LtSoLines ltSoLines) throws Exception {
		ltSoLinesRepository.deleteById(ltSoLines.getLineId());
		if (ltSoLinesRepository.existsById(ltSoLines.getLineId())) {
			return ltSoLinesRepository.findById(ltSoLines.getLineId()).get();
		} else
			return null;
	}

	@Override
	public void deleteDuplicateOrderLine() throws Exception {
		// ltMastOrderDao.deleteDuplicateOrderLine();
		try {
			ltMastOrderDao.deleteDuplicateOrderLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
