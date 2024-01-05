package com.lonar.cartservice.atflCartService.dao;

import java.rmi.ServerException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtTemplateHeaders;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;



@Repository
@PropertySource(value = "classpath:queries/cartMasterQueries.properties", ignoreResourceNotFound = true)
@Transactional(propagation=Propagation.MANDATORY)
public class LtTemplateDaoImpl implements LtTemplateDao,CodeMaster{
	@Autowired
	private Environment env;

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	

	@Override
	public LtTemplateHeaders getTemplateAgainstDistributor(String distributorId,Long templateHeaderId)throws ServerException{
	String query = env.getProperty("getTemplateAgainstDistributor");
	try {
		/*
		 * List<LtTemplateHeaders> templateHeader = jdbcTemplate.query(query, new
		 * Object[] { distributorId, templateHeaderId }, new
		 * BeanPropertyRowMapper<LtTemplateHeaders>(LtTemplateHeaders.class));
		 */
		
		LtTemplateHeaders templateHeader= jdbcTemplate.queryForObject(query, new Object[] {distributorId, templateHeaderId }, LtTemplateHeaders.class);
	System.out.println("templateHeader"+templateHeader);
	if (templateHeader !=null) {
		return templateHeader;
	}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return null;
	}

	@Override
	public List<LtTemplateLines> getProductDetailsAgainstheaderId(Long templateHeaderId)throws ServerException{
		String query = env.getProperty("getProductDetailsAgainstheaderId");
		List<LtTemplateLines> templateproductlist = jdbcTemplate.query(query,
				new Object[] {templateHeaderId},
				new BeanPropertyRowMapper<LtTemplateLines>(LtTemplateLines.class));
		if (!templateproductlist.isEmpty()) {
			return templateproductlist;
		}
		return null;
	}
}
