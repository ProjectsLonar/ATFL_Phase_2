package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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

	@Override
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

	@Override
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

}
