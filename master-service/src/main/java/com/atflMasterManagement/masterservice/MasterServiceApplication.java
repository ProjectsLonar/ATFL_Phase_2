package com.atflMasterManagement.masterservice;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = { "com.atflMasterManagement.*" })
@PropertySource({ "classpath:persistence.properties" })
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
@Configuration
public class MasterServiceApplication extends SpringBootServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterServiceApplication.class);

	@Autowired
	private Environment env;

	public static Map<Long, Map<String, String>> configMap = new HashMap<Long, Map<String, String>>();

	public static Map<Integer, String> stateMap = new HashMap<Integer, String>();

	public static Map<Integer, String> categoryMap = new HashMap<Integer, String>();

	public static Map<Integer, Map<Integer, String>> subCategoryMap = new HashMap<Integer, Map<Integer, String>>();

	public static void main(String[] args) {
		SpringApplication.run(MasterServiceApplication.class, args);
	}

	@LoadBalanced
	@Bean("datasource")
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driver"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}

	@LoadBalanced
	@Bean("resttemplate")
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@LoadBalanced
	@Bean
	@DependsOn({ "datasource", "resttemplate" })
	public Map getAllState() {
		stateMap.put(1, "Assam");
		stateMap.put(2, "Bihar");
		stateMap.put(3, "Goa");
		stateMap.put(4, "Gujarat");
		stateMap.put(5, "Haryana");
		stateMap.put(6, "Jharkhand");
		stateMap.put(7, "Karnataka");
		stateMap.put(8, "Kerala");
		stateMap.put(9, "Maharashtra");
		stateMap.put(10, "Manipur");
		stateMap.put(11, "Nagaland");
		stateMap.put(12, "Punjab");
		stateMap.put(13, "Rajasthan");
		stateMap.put(14, "Sikkim");
		stateMap.put(15, "Tamil Nadu");
		System.out.println("stateMap" + stateMap);

		return stateMap;
	}

	@LoadBalanced
	@Bean
	@DependsOn({ "datasource", "resttemplate" })
	public Map getAllCategories() {
		categoryMap.put(1, "Breakfast Cereals");
		categoryMap.put(2, "Chocolate Confectionery");
		categoryMap.put(3, "Edible Oils");
		categoryMap.put(4, "Ready to Cook");
		categoryMap.put(5, "Ready to Eat Snacks");
		categoryMap.put(6, "Spreads");

		return categoryMap;
	}

	@LoadBalanced
	@Bean
	@DependsOn({ "datasource", "resttemplate" })
	public Map getAllSubCategoriesAgainstCatId() {

		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "Fresh Vegetables");
		map.put(2, "Fresh Fruits");
		map.put(3, "Herbs & Seasonings");
		subCategoryMap.put(1, map);
		Map<Integer, String> map2 = new HashMap<Integer, String>();
		map2.put(1, "Atta Flours & Sooji");
		map2.put(2, "Salt Sugar & Jaggery");
		map2.put(3, "Edible Oils & Ghee");
		subCategoryMap.put(2, map2);

		Map<Integer, String> map3 = new HashMap<Integer, String>();
		map3.put(1, "Dairy");
		map3.put(2, "Breads & Buns");
		map3.put(3, "Cookies, Rusk & Khari");
		subCategoryMap.put(3, map3);

		Map<Integer, String> map4 = new HashMap<Integer, String>();
		map4.put(1, "Biscuits & Cookies");
		map4.put(2, "Chocolates & Candies");

		subCategoryMap.put(4, map4);
		System.out.println("subCategoryMap" + subCategoryMap);
		return subCategoryMap;

	}

}
