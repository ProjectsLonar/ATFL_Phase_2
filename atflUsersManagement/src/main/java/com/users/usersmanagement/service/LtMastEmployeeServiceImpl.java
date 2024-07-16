package com.users.usersmanagement.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastEmployeeDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastEmployees;
import com.users.usersmanagement.model.LtMastPositions;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.model.RequestDto;

@Service
public class LtMastEmployeeServiceImpl implements LtMastEmployeeService, CodeMaster {

	@Autowired
	LtMastEmployeeDao ltMastEmployeeDao;

	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	AtflMastUsersDao ltMastUsersDao;

	@Autowired
	AtflMastUsersService ltMastUsersService;

	@Override
	public Status verifyEmployee(String employeeCode, String distributorCrmCode, String positionCode, Long userId)
			throws ServiceException {
		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		if (ltMastUsers != null) {

			LtMastEmployees ltMastEmployees = ltMastEmployeeDao.verifyEmployee(employeeCode, distributorCrmCode,
					positionCode);

			if (ltMastEmployees != null) {

				if (ltMastEmployees.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastEmployees.getDistributorStatus().equalsIgnoreCase(ACTIVE)) {

						if (ltMastEmployees.getPositionStatus().equalsIgnoreCase(ACTIVE)) {

							ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());

							ltMastUsers.setLastUpdateDate(new Date());

							ltMastUsers.setOrgId(ltMastEmployees.getOrgId());

							ltMastUsers.setDistributorId(ltMastEmployees.getDistributorId());

							ltMastUsers.setPositionId(ltMastEmployees.getPositionId());

							ltMastUsers.setUserType(RoleMaster.SALES);

							ltMastUsers.setIsFirstLogin("N");
							
							ltMastUsers.setEmployeeCode(ltMastEmployees.getEmployeeCode());

							ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

							if (ltMastUsers != null) {
								status.setCode(SUCCESS);
								status.setMessage("VERFIED");
								ltMastEmployees.setDistributorCode(ltMastEmployees.getDistributorCrmCode());
								status.setData(ltMastEmployees);
								return status;

							}
						} else {
							status.setCode(FAIL);
							status.setMessage(
									ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(
								ltMastCommonMessageService.getCommonMessage("lonar.users.distributor.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
					return status;
				}
			}
		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.salenotverified"));
		status.setData(null);
		return status;
	}

	@Override
	public Status getSalesPersonsForDistributorV1(RequestDto requestDto) throws ServiceException {
		Status status = new Status();
		
		List<LtMastPositions> list = ltMastEmployeeDao.getSalesPersonsForDistributorV1(requestDto);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);

		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		return status;
	}

	@Override
	public Status verifySalesOfficer(String primaryMobile, String emailId, String positionCode, Long userId)
			throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

				LtMastEmployees ltMastEmployees = ltMastEmployeeDao.verifySalesOfficer(primaryMobile, emailId,
						positionCode);

				if (ltMastEmployees != null) {

					ltMastUsers.setStatus(ACTIVE);

					ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());

					ltMastUsers.setLastUpdateDate(new Date());

					ltMastUsers.setOrgId(ltMastEmployees.getOrgId());

					ltMastUsers.setDistributorId(ltMastEmployees.getDistributorId());

					ltMastUsers.setPositionId(ltMastEmployees.getPositionId());

					ltMastUsers.setUserType(RoleMaster.SALESOFFICER);

					ltMastUsers.setEmployeeCode(ltMastEmployees.getEmployeeCode());

					ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

					if (ltMastUsers != null) {
						status.setCode(SUCCESS);
						status.setMessage("VERFIED");
						ltMastEmployees.setDistributorCode(ltMastEmployees.getDistributorCrmCode());
						ltMastEmployees.setMobileNumber(ltMastUsers.getMobileNumber());
						status.setData(ltMastEmployees);
						return status;

					}

			}
		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.salenotverified"));
		status.setData(null);
		return status;
	}

	@Override
	public Status verifyAreaHead(String primaryMobile, String emailId, String positionCode, Long userId)
			throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

			LtMastEmployees ltMastEmployees = ltMastEmployeeDao.verifyAreaHead(primaryMobile, emailId, positionCode);

			if (ltMastEmployees != null) {

				ltMastUsers.setStatus(ACTIVE);

				ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());

				ltMastUsers.setLastUpdateDate(new Date());

				ltMastUsers.setOrgId(ltMastEmployees.getOrgId());

				ltMastUsers.setDistributorId(ltMastEmployees.getDistributorId());

				ltMastUsers.setPositionId(ltMastEmployees.getPositionId());

				ltMastUsers.setUserType(RoleMaster.AREAHEAD);
				
				ltMastUsers.setIsFirstLogin("N");

				ltMastUsers.setEmployeeCode(ltMastEmployees.getEmployeeCode());

				ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

				if (ltMastUsers != null) {
					status.setCode(SUCCESS);
					status.setMessage("VERFIED");
					ltMastEmployees.setDistributorCode(ltMastEmployees.getDistributorCrmCode());
					ltMastEmployees.setMobileNumber(ltMastUsers.getMobileNumber());
					status.setData(ltMastEmployees);
					return status;

				}

			}
		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.salenotverified"));
		status.setData(null);
		return status;
	}
	
	
	@Override
	public Status verifyAreaHeadV1(String employeeCode, Long userId,String userName)
			throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

			LtMastEmployees ltMastEmployees = ltMastEmployeeDao.verifyAreaHeadV1(employeeCode);

			if (ltMastEmployees != null) {

				ltMastUsers.setStatus(ACTIVE);

				ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());

				ltMastUsers.setLastUpdateDate(new Date());

				ltMastUsers.setOrgId(ltMastEmployees.getOrgId());

				ltMastUsers.setDistributorId(ltMastEmployees.getDistributorId());

				ltMastUsers.setPositionId(ltMastEmployees.getPositionId());

				ltMastUsers.setUserType(RoleMaster.AREAHEAD);
				
				ltMastUsers.setIsFirstLogin("N");

				ltMastUsers.setEmployeeCode(ltMastEmployees.getEmployeeCode());

				ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

				if (ltMastUsers != null) {
					status.setCode(SUCCESS);
					status.setMessage("VERFIED");
					ltMastEmployees.setDistributorCode(ltMastEmployees.getDistributorCrmCode());
					ltMastEmployees.setMobileNumber(ltMastUsers.getMobileNumber());
					status.setData(ltMastEmployees);
					return status;

				}

			}
		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.salenotverified"));
		status.setData(null);
		return status;
	}
	
	
	@Override
	public Status verifySalesOfficerV1(String employeeCode,Long userId,String userName)
			throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

				LtMastEmployees ltMastEmployees = ltMastEmployeeDao.verifySalesOfficerV1(employeeCode);

				if (ltMastEmployees != null) {

					ltMastUsers.setStatus(ACTIVE);

					ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());

					ltMastUsers.setLastUpdateDate(new Date());

					ltMastUsers.setOrgId(ltMastEmployees.getOrgId());

					ltMastUsers.setDistributorId(ltMastEmployees.getDistributorId());

					ltMastUsers.setPositionId(ltMastEmployees.getPositionId());

					ltMastUsers.setUserType(RoleMaster.SALESOFFICER);
					
					ltMastUsers.setIsFirstLogin("N");

					ltMastUsers.setEmployeeCode(ltMastEmployees.getEmployeeCode());

					ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

					if (ltMastUsers != null) {
						status.setCode(SUCCESS);
						status.setMessage("VERFIED");
						ltMastEmployees.setDistributorCode(ltMastEmployees.getDistributorCrmCode());
						ltMastEmployees.setMobileNumber(ltMastUsers.getMobileNumber());
						status.setData(ltMastEmployees);
						return status;

					}

			}
		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.salenotverified"));
		status.setData(null);
		return status;
	}
	
	@Override
	public Status verifySystemAdministrator(String employeeCode, Long userId, String userName)throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

			LtMastEmployees ltMastEmployees = ltMastEmployeeDao.verifySystemAdministrator(employeeCode);

			if (ltMastEmployees != null) {

				ltMastUsers.setStatus(ACTIVE);

				ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());

				ltMastUsers.setLastUpdateDate(new Date());

				ltMastUsers.setOrgId(ltMastEmployees.getOrgId());

				ltMastUsers.setDistributorId(ltMastEmployees.getDistributorId());

				ltMastUsers.setPositionId(ltMastEmployees.getPositionId());

				ltMastUsers.setUserType(RoleMaster.SYSTEMADMINISTRATOR);
				
				ltMastUsers.setIsFirstLogin("N");

				ltMastUsers.setEmployeeCode(ltMastEmployees.getEmployeeCode());

				ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

				if (ltMastUsers != null) {
					status.setCode(SUCCESS);
					status.setMessage("VERFIED");
					ltMastEmployees.setDistributorCode(ltMastEmployees.getDistributorCrmCode());
					ltMastEmployees.setMobileNumber(ltMastUsers.getMobileNumber());
					status.setData(ltMastEmployees);
					return status;

				}

			}
		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.salenotverified"));
		status.setData(null);
		return status;
	}
	


}
