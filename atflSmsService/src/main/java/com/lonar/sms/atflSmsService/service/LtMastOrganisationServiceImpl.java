package com.lonar.sms.atflSmsService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.dao.LtMastOrganisationDao;
import com.lonar.sms.atflSmsService.model.CodeMaster;
import com.lonar.sms.atflSmsService.model.LtMastOrganisations;
import com.lonar.sms.atflSmsService.model.Status;

@Service
public class LtMastOrganisationServiceImpl implements LtMastOrganisationService,CodeMaster{

	@Autowired
	LtMastOrganisationDao ltMastOrganisationDao;
	
	@Override
	public LtMastOrganisations getLtMastOrgById(String orgId) throws ServiceException {
		return ltMastOrganisationDao.getLtMastOrganisationById(orgId);
	}
	@Override
	public List<LtMastOrganisations> getAllOrganisations() throws ServiceException {
		return ltMastOrganisationDao.getAllOrganisation();
	}
	@Override
	public Status getByOrgCode(String orgCode) throws ServiceException {
		Status status = new Status();
		List<LtMastOrganisations> ltMastSuppliersList = ltMastOrganisationDao.ltMastOrganisationDao(orgCode);
		if(!ltMastSuppliersList.isEmpty()) {
			status.setCode(SUCCESS);
			status.setMessage(ltMastSuppliersList.get(0).getOrganisationName());
			status.setData(ltMastSuppliersList.get(0).getOrgId());
			return status;
		}else {
			status.setCode(FAIL);
			status.setMessage("Invalid Supplier Code");
			return status;
		}
	}
	/*@Override
	public LtMastOrganisations getPaymentDetailsById(Long supplierId) throws ServiceException {
		// TODO Auto-generated method stub
		return ltMastSuppliersDao.getPaymentDetailsById(supplierId);
	}*/

}
