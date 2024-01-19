package com.lonar.cartservice.atflCartService.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.model.LtMastSysVariables;

@Repository
public interface LtMastSysVariablesDao {
	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;
}
