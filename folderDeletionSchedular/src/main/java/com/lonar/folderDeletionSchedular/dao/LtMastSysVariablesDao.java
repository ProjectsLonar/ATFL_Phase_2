package com.lonar.folderDeletionSchedular.dao;

import java.util.List;

import com.lonar.folderDeletionSchedular.model.LtMastSysVariables;
import com.lonar.folderDeletionSchedular.model.ServiceException;

public interface LtMastSysVariablesDao {

	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;

}
