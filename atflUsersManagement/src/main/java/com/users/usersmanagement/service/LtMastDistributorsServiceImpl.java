package com.users.usersmanagement.service;

import java.rmi.ServerException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastDistributorsDao;
import com.users.usersmanagement.dao.LtMastOrganisationsDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastDistributors;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.Status;

@Service
@PropertySource(value = "classpath:queries/messages.properties", ignoreResourceNotFound = true)
public class LtMastDistributorsServiceImpl implements LtMastDistributorsService, CodeMaster {

	@Autowired
	LtMastDistributorsDao ltMastDistributorsDao;

	@Autowired
	LtMastOrganisationsDao ltMastOrganisationsDao;

	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	AtflMastUsersDao ltMastUsersDao;

	@Autowired
	AtflMastUsersService ltMastUsersService;

	@Autowired
	private Environment env;

	@Override
	public Status verifyDistributor(String distributorCrmCode, String positionCode, String userCode, Long userId)
			throws ServiceException {

		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		if (ltMastUsers != null) {
			LtMastDistributors ltMastDistributors = ltMastDistributorsDao.verifyDistributors(distributorCrmCode,
					positionCode, userCode);
			if (ltMastDistributors != null) {

				if (ltMastDistributors.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastDistributors.getEmpStatus().equalsIgnoreCase(ACTIVE)) {

						if (ltMastDistributors.getPositionStatus().equalsIgnoreCase(ACTIVE)) {

							ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());
							
							ltMastUsers.setLastUpdateDate(new Date());

							//ltMastUsers.setOrgId(Long.parseLong(ltMastDistributors.getOrgId()));
							ltMastUsers.setOrgId(ltMastDistributors.getOrgId());

							ltMastUsers.setDistributorId(ltMastDistributors.getDistributorId());

							ltMastUsers.setUserType(RoleMaster.DISTRIBUTOR);
							
							ltMastUsers.setIsFirstLogin("N");
							

							ltMastUsers.setEmployeeCode(ltMastDistributors.getEmployeeCode());

							//LtMastDistributors ltDistributor = ltMastDistributorsDao.getLtDistributorsById(ltMastDistributors.getDistributorId());

							if (ltMastDistributors.getPrimaryMobile() != null) {
								
								if (ltMastDistributors.getPrimaryMobile().length() >= 10) {

									String mobileNumber = ltMastDistributors.getPrimaryMobile()
											.substring(ltMastDistributors.getPrimaryMobile().length() - 10);

									if (mobileNumber.equalsIgnoreCase(ltMastUsers.getMobileNumber())) {
										ltMastDistributors.setDistributorOperator("NO");
									} else {
										ltMastDistributors.setDistributorOperator("YES");
									}
								} else {
									ltMastDistributors.setDistributorOperator("YES");
								}
							} else {
								ltMastDistributors.setDistributorOperator("YES");
							}

							//ltMastUsers.setPositionId(Long.parseLong(ltMastDistributors.getPositionId()));
							ltMastUsers.setPositionId(ltMastDistributors.getPositionId());
							ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

							if (ltMastUsers != null) {
								status.setCode(SUCCESS);
								status.setMessage(env.getProperty("lonar.users.distributorverified"));
								
								ltMastDistributors.setDistributorCode(ltMastDistributors.getDistributorCrmCode());
								
								status.setData(ltMastDistributors);
								return status;

							}

						} else {
							status.setCode(FAIL);
							status.setMessage(
									ltMastCommonMessageService.getCommonMessage("lonar.users.position.noactive"));
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.distributor.noactive"));
					return status;
				}
			}
		}
		status.setCode(FAIL);
		status.setMessage(env.getProperty("lonar.users.distributornotverified"));
		status.setData(null);
		return status;
	}
	
	@Override
	public Status verifyDistributorV1(String distributorCrmCode,String distributorName,String proprietorName,Long userId)
			throws ServiceException {

		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		if (ltMastUsers != null) {
			LtMastDistributors ltMastDistributors = ltMastDistributorsDao.verifyDistributorsV1(distributorCrmCode,
					distributorName, proprietorName);
			if (ltMastDistributors != null) {

				if (ltMastDistributors.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastDistributors.getEmpStatus().equalsIgnoreCase(ACTIVE)) {

						if (ltMastDistributors.getPositionStatus().equalsIgnoreCase(ACTIVE)) {

							ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());
							
							ltMastUsers.setLastUpdateDate(new Date());

							//ltMastUsers.setOrgId(Long.parseLong(ltMastDistributors.getOrgId()));
							ltMastUsers.setOrgId(ltMastDistributors.getOrgId());

							ltMastUsers.setDistributorId(ltMastDistributors.getDistributorId());

							ltMastUsers.setUserType(RoleMaster.DISTRIBUTOR);
							
							ltMastUsers.setIsFirstLogin("N");

							ltMastUsers.setEmployeeCode(ltMastDistributors.getEmployeeCode());

							//LtMastDistributors ltDistributor = ltMastDistributorsDao.getLtDistributorsById(ltMastDistributors.getDistributorId());

							if (ltMastDistributors.getPrimaryMobile() != null) {
								
								if (ltMastDistributors.getPrimaryMobile().length() >= 10) {

									String mobileNumber = ltMastDistributors.getPrimaryMobile()
											.substring(ltMastDistributors.getPrimaryMobile().length() - 10);

									if (mobileNumber.equalsIgnoreCase(ltMastUsers.getMobileNumber())) {
										ltMastDistributors.setDistributorOperator("NO");
									} else {
										ltMastDistributors.setDistributorOperator("YES");
									}
								} else {
									ltMastDistributors.setDistributorOperator("YES");
								}
							} else {
								ltMastDistributors.setDistributorOperator("YES");
							}

							//ltMastUsers.setPositionId(Long.parseLong(ltMastDistributors.getPositionId()));
							ltMastUsers.setPositionId(ltMastDistributors.getPositionId());
							ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

							if (ltMastUsers != null) {
								status.setCode(SUCCESS);
								status.setMessage(env.getProperty("lonar.users.distributorverified"));
								
								ltMastDistributors.setDistributorCode(ltMastDistributors.getDistributorCrmCode());
								//ltMastDistributors.setDistributorCode(ltMastDistributors.getDistributorCode());
								status.setData(ltMastDistributors);
								return status;

							}

						} else {
							status.setCode(FAIL);
							status.setMessage(
									ltMastCommonMessageService.getCommonMessage("lonar.users.position.noactive"));
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.distributor.noactive"));
					return status;
				}
			}
		}
		status.setCode(FAIL);
		status.setMessage(env.getProperty("lonar.users.distributornotverified"));
		status.setData(null);
		return status;
	}

	
	@Override
	public Status getAllDistributorAgainstAreahead(RequestDto requestDto)throws ServerException, ServiceException{
		Status status = new Status();
		try {
		List<LtMastDistributors> distributorList = ltMastDistributorsDao.getAllDistributorAgainstAreahead(requestDto);
		if (distributorList != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(distributorList);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;	
	}
	
	@Override
	public Status getAllNotification(RequestDto requestDto) throws ServerException, ServiceException{
		Status status = new Status();
		try {
			List<NotificationDetails> notificationList  = ltMastDistributorsDao.getAllNotification(requestDto);
			if(notificationList != null) {
				status.setCode(SUCCESS);
				status.setMessage("RECORD FOUND SUCCESSFULLY");
				status.setData(notificationList);
			}else {
				status.setCode(FAIL);
				status.setMessage("RECORD NOT FOUND");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
}
