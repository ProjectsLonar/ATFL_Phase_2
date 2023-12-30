package com.atflMasterManagement.masterservice.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastTestProduct;
import com.atflMasterManagement.masterservice.repository.LtMastTestProductRepository;

@Repository
@PropertySource(value = "classpath:queries/ltMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastTestProductDaoImpl implements LtMastTestProductDao {

	@Autowired
	LtMastTestProductRepository ltMastTestProductRepository;

	@Autowired
	private Environment env;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public LtMastTestProduct saveProduct(LtMastTestProduct ltMastTestProduct) throws ServiceException {
		return ltMastTestProductRepository.save(ltMastTestProduct);
	}

	@Override
	public LtMastTestProduct deleteByProductId(Long productId) throws ServiceException {
		LtMastTestProduct ltMastTestProduct = null;
		ltMastTestProductRepository.deleteById(productId);
		if (ltMastTestProductRepository.existsById(productId)) {
			ltMastTestProduct = ltMastTestProductRepository.findById(productId).get();
		}
		return ltMastTestProduct;
	}

	@Override
	public LtMastTestProduct getById(Long productId) throws ServiceException {
		Optional<LtMastTestProduct> ltMastTestProduct = ltMastTestProductRepository.findById(productId);
		if (ltMastTestProduct.isPresent()) {
			return ltMastTestProduct.get();
		}
		return null;

	}

	@Override
	public List<LtMastTestProduct> getAllProducts(Long limit, Long offset) throws ServiceException {
//		List<LtMastTestProduct> ltMastTestProductList = new ArrayList<LtMastTestProduct>();
//		ltMastTestProductRepository.findAll().forEach(list -> ltMastTestProductList.add(list));

		String query = env.getProperty("getAllTestProduct");
		if (limit == 0) {
			limit = Long.parseLong(env.getProperty("limit_value"));
		}
		List<LtMastTestProduct> ltMastTestProductList = jdbcTemplate.query(query, new Object[] { limit, offset },
				new BeanPropertyRowMapper<LtMastTestProduct>(LtMastTestProduct.class));
		if (!ltMastTestProductList.isEmpty()) {
			return ltMastTestProductList;
		}

		return null;
	}
}
