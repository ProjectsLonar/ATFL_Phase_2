package com.users.usersmanagement.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastOrganisationsDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastOrganisations;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.Status;

@Service
public class LtMastOrganisationsServiceImpl implements LtMastOrganisationsService, CodeMaster {

	@Autowired
	LtMastOrganisationsDao ltMastOrganisationsDao;

	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	AtflMastUsersDao ltMastUsersDao;

	@Autowired
	AtflMastUsersService ltMastUsersService;

	@Override
	public Status verifyOrganisation(String userCode, String orgCode, String userId) throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

			LtMastOrganisations ltMastOrganisations = ltMastOrganisationsDao.verifyOrganisation(userCode, orgCode);

			if (ltMastOrganisations != null) {

				if (ltMastOrganisations.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastOrganisations.getEmployeeStatus().equalsIgnoreCase(ACTIVE)) {

						ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());
						ltMastUsers.setLastUpdateDate(new Date());
						ltMastUsers.setOrgId(ltMastOrganisations.getOrgId());
						ltMastUsers.setUserType(RoleMaster.ADMIN);
						ltMastUsers.setEmployeeCode(ltMastOrganisations.getEmployeeCode());
						ltMastUsers.setPositionId(ltMastOrganisations.getPositionId());
						ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);
						if (ltMastUsers != null) {
							status.setCode(SUCCESS);
							status.setMessage("VERFIED");
							status.setData(ltMastOrganisations);
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.org.noactive"));
					return status;
				}
			}

		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.organisationnotverified"));
		return status;
	}

	@Override
	public Status verifyOrganisationV2(String userCode, String orgCode, String userId) throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

			LtMastOrganisations ltMastOrganisations = ltMastOrganisationsDao.verifyOrganisationV2(userCode, orgCode);

			if (ltMastOrganisations != null) {

				if (ltMastOrganisations.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastOrganisations.getEmployeeStatus().equalsIgnoreCase(ACTIVE)) {

						ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());
						ltMastUsers.setLastUpdateDate(new Date());
						ltMastUsers.setOrgId(ltMastOrganisations.getOrgId());
						ltMastUsers.setUserType(RoleMaster.ADMIN);
						ltMastUsers.setEmployeeCode(ltMastOrganisations.getEmployeeCode());
						ltMastUsers.setPositionId(ltMastOrganisations.getPositionId());
						ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);
						if (ltMastUsers != null) {
							status.setCode(SUCCESS);
							status.setMessage("VERFIED");
							status.setData(ltMastOrganisations);
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.org.noactive"));
					return status;
				}
			}

		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.organisationnotverified"));
		return status;
	}

}
