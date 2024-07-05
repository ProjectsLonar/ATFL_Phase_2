package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
/*  comment on 19-June-2024 for api time calculation
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
*/
	
	@Override
	public Status getCategory(RequestDto requestDto) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		System.out.println("In method getCategory = "+LocalDateTime.now());
		Map<String,String> timeDifference = new HashMap<>();
		long inQuerygetCategory = 0;
		long outQuerygetCategory = 0;
		long inQuerygetProdCatCount = 0;
		long outQuerygetProdCatCount = 0;
		Status status = new Status();
		try {
		inQuerygetCategory = System.currentTimeMillis();
		List<LtMastProductCat> ltMastProdCat = ltMastProdCatDao.getCategory(requestDto);
		outQuerygetCategory = System.currentTimeMillis();
		inQuerygetProdCatCount = System.currentTimeMillis();
		Long prodCatCount = ltMastProdCatDao.getProdCatCount(requestDto);
		outQuerygetProdCatCount = System.currentTimeMillis();
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
		timeDifference.put("QuerygetCategory", timeDiff(inQuerygetCategory,outQuerygetCategory));
		timeDifference.put("QuerygetProdCatCount", timeDiff(inQuerygetProdCatCount,outQuerygetProdCatCount));
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getCategory at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
	
	public String timeDiff(long startTime,long endTime) {
		// Step 4: Calculate the time difference in milliseconds
        long durationInMillis = endTime - startTime;
 
        // Step 5: Convert the duration into a human-readable format
        long seconds = durationInMillis / 1000;
        long milliseconds = durationInMillis % 1000;
 
        String formattedDuration = String.format(
            "%d seconds, %d milliseconds",
            seconds, milliseconds
        );
		return formattedDuration;
	}
}
