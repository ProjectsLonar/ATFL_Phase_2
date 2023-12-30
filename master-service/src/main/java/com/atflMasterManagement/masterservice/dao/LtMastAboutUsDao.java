package com.atflMasterManagement.masterservice.dao;


import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastAboutUs;
import java.util.List;



public interface LtMastAboutUsDao {

	public LtMastAboutUs saveAboutUS(LtMastAboutUs ltMastAboutUs)throws ServiceException;

	public List<LtMastAboutUs> getAllAboutUs()throws ServiceException;

}
