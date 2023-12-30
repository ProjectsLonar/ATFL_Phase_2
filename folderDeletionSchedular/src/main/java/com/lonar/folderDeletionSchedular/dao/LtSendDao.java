package com.lonar.folderDeletionSchedular.dao;

import java.util.List;

import com.lonar.folderDeletionSchedular.model.LtMastMail;
import com.lonar.folderDeletionSchedular.model.LtMastSMS;
import com.lonar.folderDeletionSchedular.model.ServiceException;

public interface LtSendDao {

	List<LtMastSMS> getSMSDataPainding() throws ServiceException;
	
	List<LtMastMail> getMailDataPainding() throws ServiceException;
	
	LtMastSMS saveSMS(LtMastSMS ltMastSMS) throws ServiceException;
	
	LtMastMail saveMail(LtMastMail ltMastMail) throws ServiceException;

}
