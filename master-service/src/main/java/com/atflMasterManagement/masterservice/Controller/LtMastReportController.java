package com.atflMasterManagement.masterservice.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.reports.ExcelReportDto;
import com.atflMasterManagement.masterservice.servcies.LtReportService;

@RestController
@RequestMapping("/reports")
public class LtMastReportController implements CodeMaster{

	@Autowired
	private LtReportService ltReportService;
	
	@RequestMapping(value = "/generateSalesPersonReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getSalesReportData(@RequestBody ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
			return new ResponseEntity<Status>(ltReportService.getSalesReportData(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/generateRegionWiseSalesReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getRegionwiseSalesReportData(@RequestBody ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
			return new ResponseEntity<Status>(ltReportService.getRegionwiseSalesReportData2(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/generateProductReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getProductReportData(@RequestBody ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
			return new ResponseEntity<Status>(ltReportService.getProductReportData2(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/generateDistributorReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getDistributorReportData(@RequestBody ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
			return new ResponseEntity<Status>(ltReportService.getDistributorReportData(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/generateOutletReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOutletReportData(@RequestBody ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
			return new ResponseEntity<Status>(ltReportService.getOutletReportData(excelReportDto), HttpStatus.OK);	
	}
	
	//-------------filter api-------------
	@RequestMapping(value = "/searchDistributor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> searchDistributor(@RequestBody ExcelReportDto excelReportDto
			) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.searchDistributor(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/searchSalesPerson", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> searchSalesPerson(@RequestBody ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.searchSalesPerson(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/getRegion/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getRegion(@PathVariable("orgId") String orgId) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.getRegion(orgId), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/getRegion/{orgId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getRegionV2(@PathVariable("orgId") String orgId, @PathVariable("userId") String userId) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.getRegionV2(orgId, userId), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/searchProduct", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> searchProduct(@RequestBody ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.searchProduct(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/searchOutlets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> searchOutlets(@RequestBody ExcelReportDto excelReportDto ) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.searchOutlets(excelReportDto), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/getOutletStatus/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOutletStatus(@PathVariable("orgId") String orgId) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.getOutletStatus(orgId), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/getCategoryDetails/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getCategoryDetails(@PathVariable("orgId") String orgId) throws ServiceException, FileNotFoundException, IOException {
		return new ResponseEntity<Status>(ltReportService.getCategoryDetails(orgId), HttpStatus.OK);	
	}
	
}
