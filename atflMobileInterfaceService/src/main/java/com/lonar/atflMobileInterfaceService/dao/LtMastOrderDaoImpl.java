package com.lonar.atflMobileInterfaceService.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.atflMobileInterfaceService.model.LtMastOrder;
import com.lonar.atflMobileInterfaceService.model.LtSoHeaders;
import com.lonar.atflMobileInterfaceService.model.LtSoLines;

@Repository
@PropertySource(value = "classpath:queries/orderQueries.properties", ignoreResourceNotFound = true)
public class LtMastOrderDaoImpl implements LtMastOrderDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<LtMastOrder> getAllInprocessOrder() throws Exception {
		String query = env.getProperty("getAllApprovedOrder");
		List<LtMastOrder> list = jdbcTemplate.query(query, new BeanPropertyRowMapper<LtMastOrder>(LtMastOrder.class));
		if (!list.isEmpty())
			return list;
		else
			return null;
	}

	@Override
	public boolean updateOrderStatus(String orderNumber) {
		String query = env.getProperty("udateOrderStatus");
		boolean flag = false;
		int i = jdbcTemplate.update(query, orderNumber);
		if (0 < i) {
			flag = true;
		}
		
		return flag;
	}

	@Override
	public LtSoLines getLineByOrdernumProductCode(String orderNum, String productCode) {
		String query = env.getProperty("getLineByOrdernumProductCode");
		List<LtSoLines> list = jdbcTemplate.query(query,new Object[] { orderNum,productCode}, new BeanPropertyRowMapper<LtSoLines>(LtSoLines.class));
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}

	@Override
	public List<LtSoHeaders> getFailedorderList(String orderNum) {
		String query = env.getProperty("getFaliedOrderList");
		List<LtSoHeaders> list = jdbcTemplate.query(query,new Object[] { orderNum }, new BeanPropertyRowMapper<LtSoHeaders>(LtSoHeaders.class));
		if (!list.isEmpty())
			return list;
		else
			return null;
	}

	@Override
	public LtSoHeaders getHeaderByOrderNumber(String orderNum) {
		String query = env.getProperty("getHeaderByOrderNumber");
		List<LtSoHeaders> list = jdbcTemplate.query(query,new Object[] { orderNum }, new BeanPropertyRowMapper<LtSoHeaders>(LtSoHeaders.class));
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}
	@Override
	public List<LtSoLines> checkDoubleOrderEntry(LtMastOrder ltMastOrder) throws Exception {
		String query = env.getProperty("checkDuobleOrderEntry");
		List<LtSoLines> list = jdbcTemplate.query(query,new Object[] { ltMastOrder.getOrderNumber(),ltMastOrder.getProductCode(), Integer.parseInt(ltMastOrder.getQuantity().toString()) }, new BeanPropertyRowMapper<LtSoLines>(LtSoLines.class));
		if (!list.isEmpty())
			return list;
		else
			return null;
	}
	
	@Override
	@Transactional
	public void deleteDuplicateOrderLine() throws Exception {
		String query = "delete from lt_so_lines where line_id in (\r\n" + 
				"select lsl.line_id from\r\n" + 
				"lt_so_headers lsh,\r\n" + 
				"lt_so_lines lsl\r\n" + 
				"where lsh.header_id = lsl.header_id\r\n" + 
				"and Upper(lsh.status) ='PROCESSED'\r\n" + 
				"and lsl.eimstatus is null)";
		jdbcTemplate.update(query);
	}

}
