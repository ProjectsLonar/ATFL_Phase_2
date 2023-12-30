package com.atflMasterManagement.masterservice.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastAboutUs;
import com.atflMasterManagement.masterservice.repository.LtMastAboutUsRepository;

@Repository
public class LtMastAboutUsDaoImpl implements LtMastAboutUsDao {
	private static final Logger logger = LoggerFactory.getLogger(LtMastAboutUsDaoImpl.class);
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	private LtMastAboutUsRepository ltMastAboutUsRepository;

	@Override
	public LtMastAboutUs saveAboutUS(LtMastAboutUs ltMastAboutUs) throws ServiceException {
		logger.info("in save AboutUs");
		return ltMastAboutUsRepository.save(ltMastAboutUs);
	}

	@Override
	public List<LtMastAboutUs> getAllAboutUs() throws ServiceException {

		return (List<LtMastAboutUs>) ltMastAboutUsRepository.findAll();

	}

}
