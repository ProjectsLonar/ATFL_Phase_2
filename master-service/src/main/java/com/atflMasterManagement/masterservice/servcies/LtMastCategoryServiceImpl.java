package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dao.LtMastProdCatDao;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastProductCat;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.model.Status;

@Service
public class LtMastCategoryServiceImpl implements LtMastCategoryService, CodeMaster {

	@Autowired
	private LtMastProdCatDao ltMastProdCatDao;

	@Override
	public Status getCategory(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		try {
		List<LtMastProductCat> ltMastProdCat = ltMastProdCatDao.getCategory(requestDto);
		Long prodCatCount = ltMastProdCatDao.getProdCatCount(requestDto);
		status.setTotalCount(prodCatCount);
		status.setRecordCount(prodCatCount);
		if (ltMastProdCat != null) {
			status.setCode(SUCCESS);
			status.setMessage("Success");
			status.setData(ltMastProdCat);
		} else {
			status.setCode(FAIL);
			status.setMessage("FAIL");
			status.setData(ltMastProdCat);
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
