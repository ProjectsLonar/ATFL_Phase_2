package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dao.LtMastAboutUsDao;
import com.atflMasterManagement.masterservice.dao.LtMastFAQDao;
import com.atflMasterManagement.masterservice.dto.AboutUsAndFaqDetailsDto;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastAboutUs;
import com.atflMasterManagement.masterservice.model.LtMastFAQ;
import com.atflMasterManagement.masterservice.model.Status;

@Service
public class LtMastFAQServiceImpl implements LtMastFAQService, CodeMaster {
	private static final Logger logger = LoggerFactory.getLogger(LtMastFAQServiceImpl.class);

	@Autowired
	private LtMastFAQDao ltMastFAQDao;

	@Autowired
	private LtMastAboutUsDao ltMastAboutUsDao;

/*	@Override  comment on 19-June-2024 for api time calculation 
	public Status post(LtMastFAQ ltMastFAQ) throws ServiceException, IOException {
		Status status = new Status();

		ltMastFAQ.setCreatedBy(ltMastFAQ.getLastUpdateLogin());
		ltMastFAQ.setLastUpdateDate(new Date());
		ltMastFAQ.setCreationDate(new Date());
		ltMastFAQ.setLastUpdatedBy(ltMastFAQ.getLastUpdateLogin());

		ltMastFAQ.setStatus(ACTIVE);

		LtMastFAQ ltMastFaq = ltMastFAQDao.saveFAQ(ltMastFAQ);
		if (ltMastFaq.getFaqId() != null) {
			status.setCode(SUCCESS);
			status.setMessage("SUCCESS");
			status.setData(ltMastFaq);

		} else {
			status.setCode(FAIL);
			status.setMessage("FAIL");
		}
		logger.info(status.getMessage());
		return status;
	}
*/	
	@Override
	public Status post(LtMastFAQ ltMastFAQ) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		System.out.println("In method post = "+LocalDateTime.now());
		Map<String,String> timeDifference = new HashMap<>();
		long inQuerysaveFAQ = 0;
		long outQuerysaveFAQ = 0;
		Status status = new Status();
 
		ltMastFAQ.setCreatedBy(ltMastFAQ.getLastUpdateLogin());
		ltMastFAQ.setLastUpdateDate(new Date());
		ltMastFAQ.setCreationDate(new Date());
		ltMastFAQ.setLastUpdatedBy(ltMastFAQ.getLastUpdateLogin());
 
		ltMastFAQ.setStatus(ACTIVE);
		inQuerysaveFAQ = System.currentTimeMillis();
		LtMastFAQ ltMastFaq = ltMastFAQDao.saveFAQ(ltMastFAQ);
		outQuerysaveFAQ = System.currentTimeMillis();
		if (ltMastFaq.getFaqId() != null) {
			status.setCode(SUCCESS);
			status.setMessage("SUCCESS");
			status.setData(ltMastFaq);
 
		} else {
			status.setCode(FAIL);
			status.setMessage("FAIL");
		}
		logger.info(status.getMessage());
		timeDifference.put("QuerysaveFAQ", timeDiff(inQuerysaveFAQ,outQuerysaveFAQ));
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method post at "+LocalDateTime.now());
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
	
/*	@Override comment on 19-June-2024 for api time calculation
	public Status getAll(Long orgId, Long userId) throws ServiceException, IOException {
		Status status = new Status();

		AboutUsAndFaqDetailsDto aboutUsFaqDto = new AboutUsAndFaqDetailsDto();

		List<LtMastFAQ> list = ltMastFAQDao.getAllFAQ(orgId, userId);

		if (!list.isEmpty()) {
			aboutUsFaqDto.setLtMastFAQlist(list);
		}

		List<LtMastAboutUs> aboutUsList = ltMastAboutUsDao.getAllAboutUs();

		if (!aboutUsList.isEmpty()) {
			aboutUsFaqDto.setLtMastAboutUs(aboutUsList.get(0));
		}

		if (list.isEmpty()) {
			status.setCode(FAIL);
			status.setMessage("Records not found");
			return status;
		}
		status.setCode(SUCCESS);
		status.setMessage("Records found");
		status.setData(aboutUsFaqDto);

		return status;
	}
*/
	
	@Override
	public Status getAll(Long orgId, Long userId) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		System.out.println("In method getAll = "+LocalDateTime.now());
		Map<String,String> timeDifference = new HashMap<>();
		long inQuerygetAllFAQ = 0;
		long outQuerygetAllFAQ = 0;
		long inQuerygetAllAboutUs = 0;
		long outQuerygetAllAboutUs = 0;
		Status status = new Status();
 
		AboutUsAndFaqDetailsDto aboutUsFaqDto = new AboutUsAndFaqDetailsDto();
 
		inQuerygetAllFAQ = System.currentTimeMillis();
		List<LtMastFAQ> list = ltMastFAQDao.getAllFAQ(orgId, userId);
		outQuerygetAllFAQ = System.currentTimeMillis();
		if (!list.isEmpty()) {
			aboutUsFaqDto.setLtMastFAQlist(list);
		}
		inQuerygetAllAboutUs = System.currentTimeMillis();
		List<LtMastAboutUs> aboutUsList = ltMastAboutUsDao.getAllAboutUs();
		outQuerygetAllAboutUs = System.currentTimeMillis();
 
		if (!aboutUsList.isEmpty()) {
			aboutUsFaqDto.setLtMastAboutUs(aboutUsList.get(0));
		}
 
		if (list.isEmpty()) {
			status.setCode(FAIL);
			status.setMessage("Records not found");
			return status;
		}
		status.setCode(SUCCESS);
		status.setMessage("Records found");
		status.setData(aboutUsFaqDto);
		timeDifference.put("QuerygetAllFAQ", timeDiff(inQuerygetAllFAQ,outQuerygetAllFAQ));
		timeDifference.put("QuerygetAllAboutUs", timeDiff(inQuerygetAllAboutUs,outQuerygetAllAboutUs));
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getAll at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
}
