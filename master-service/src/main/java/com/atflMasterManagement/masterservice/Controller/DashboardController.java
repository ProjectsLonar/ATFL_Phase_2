package com.atflMasterManagement.masterservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atflMasterManagement.masterservice.common.BusinessException;
import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.servcies.DashboardService;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController implements CodeMaster {

	@Autowired
	DashboardService dashboardService;

	@RequestMapping(value = "/statusWiseOrdersCount/{orgId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> statusWiseOrdersCount(@PathVariable("orgId") String orgId,
			@PathVariable("userId") String userId) throws ServiceException {
		try {
			return new ResponseEntity<Status>(dashboardService.statusWiseOrdersCount(orgId, userId), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/categoryRevenueDistribution/{orgId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> categoryRevenueDistribution(@PathVariable("orgId") String orgId,
			@PathVariable("userId") String userId) throws ServiceException {
		try {
			return new ResponseEntity<Status>(dashboardService.categoryRevenueDistributionv2(orgId, userId),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/dailySales/{orgId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> dailySales(@PathVariable("orgId") String orgId, @PathVariable("userId") String userId)
			throws ServiceException {
		try {
			return new ResponseEntity<Status>(dashboardService.dailySalesV2(orgId, userId), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/dailySalesExcel/{orgId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> dailySalesExcel(@PathVariable("orgId") String orgId, @PathVariable("userId") String userId)
			throws ServiceException {
		try {
			return new ResponseEntity<Status>(dashboardService.dailySalesExcel(orgId, userId), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/monthlySales/{orgId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> monthlySales(@PathVariable("orgId") String orgId, @PathVariable("userId") String userId)
			throws ServiceException {
		try {
			return new ResponseEntity<Status>(dashboardService.monthlySalesV2(orgId, userId), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

}
