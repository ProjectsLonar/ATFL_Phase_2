package com.eureka.zuul;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import com.eureka.zuul.common.LtVersion;
import com.eureka.zuul.service.UserServices;

@SpringBootApplication
@EnableEurekaClient 	// It acts as a eureka client
@EnableZuulProxy		// Enable Zuul
@ComponentScan(basePackages = { "com.eureka.zuul.*" })
@PropertySource({ "classpath:persistence.properties" })
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
@Configuration
public class SpringZuulApplication extends SpringBootServletInitializer{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringZuulApplication.class);
	
	public static Map<String, LtVersion> apiversionMap = new LinkedHashMap<String, LtVersion>();	

	public static void main(String[] args) {
		SpringApplication.run(SpringZuulApplication.class, args);
	}
	
	@Autowired
	private Environment env;
	
	@Lazy
	@Autowired
	UserServices userServices;
	
	@LoadBalanced
	 @Bean("resttemplate")
	   public RestTemplate getRestTemplate() {
	      return new RestTemplate();
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
	@Bean("apiversion")
	@DependsOn({ "datasource", "resttemplate" })
	public Map<String,LtVersion> getAllAPIVersion() throws Exception {
		try {
			apiversionMap.putAll(userServices.getAllAPIVersion());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apiversionMap;
	}
}