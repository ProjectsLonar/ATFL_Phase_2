//package com.lonar.cartservice.atflCartService.dao;
//
//import java.util.List;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import com.lonar.cartservice.atflCartService.common.ServiceException;
//import com.lonar.cartservice.atflCartService.model.LtMastEmail;
//import com.lonar.cartservice.atflCartService.repository.LtMastEmailRepository;
//
//public class LtMastEmailDaoImpl implements LtMastEmailDao{
//
//	private JdbcTemplate jdbcTemplate;
//	
////	@Autowired
////	LtMastEmailRepository ltMastEmailRepository;
//
//
//	@Autowired
//	private Environment env;
//	
//	@Autowired
//	public void setDataSource(DataSource dataSource) {
//		this.jdbcTemplate = new JdbcTemplate(dataSource);
//	}
//
//	private JdbcTemplate getJdbcTemplate() {
//		return jdbcTemplate;
//	}
//	
//	
//	
////	@Override
////	public List<LtMastEmail> saveall(List<LtMastEmail> ltMastEmailToken) throws ServiceException {
////		return (List<LtMastEmail>) ltMastEmailRepository.saveAll(ltMastEmailToken);
////	}
//
//}
//
