package com.lonar.sms.atflSmsService;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import com.lonar.sms.atflSmsService.model.LtOrganisationSMS;
import com.lonar.sms.atflSmsService.service.LtOrganisationSMSService;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = { "com.lonar.sms.*" })
@PropertySource({ "classpath:persistence.properties" })
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
@Configuration
public class AtflSmsServiceApplication extends SpringBootServletInitializer{

	
	public static Map<Long,Map<String,LtOrganisationSMS>> configSMSMap = new HashMap<Long,Map<String,LtOrganisationSMS>>();
	
	@Autowired
	private Environment env;
	
	@Lazy
	@Autowired
	private LtOrganisationSMSService ltSupplierSMSService; 
	
	public static void main(String[] args) {
		SpringApplication.run(AtflSmsServiceApplication.class, args);
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
	
	/*@LoadBalanced
	@Bean
	@DependsOn({ "datasource", "resttemplate" })
	public void getAllConfiguration() {
		try {
			List<LtOrganisationSMS> ltOrganisationSMSList = ltSupplierSMSService.getAll();
			
			for (Iterator iterator = ltOrganisationSMSList.iterator(); iterator.hasNext();) {
				
				LtOrganisationSMS ltOrganisationSMS = (LtOrganisationSMS) iterator.next();
				
				if(configSMSMap.get(ltOrganisationSMS.getOrgId())!=null) {
					
					Map<String,LtOrganisationSMS> ltOrganisationSMSMap=configSMSMap.get(ltOrganisationSMS.getOrgId());
					
					ltOrganisationSMSMap.put(ltOrganisationSMS.getTemplateType(), ltOrganisationSMS);
					
					configSMSMap.put(ltOrganisationSMS.getOrgId(), ltOrganisationSMSMap);
					
				}else {
					
					Map<String,LtOrganisationSMS> ltOrganisationSMSMap=new HashMap<String,LtOrganisationSMS>();
					
					ltOrganisationSMSMap.put(ltOrganisationSMS.getTemplateType(), ltOrganisationSMS);
					
					configSMSMap.put(ltOrganisationSMS.getOrgId(), ltOrganisationSMSMap);
				}
				
			}
					
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("configSMSMap:---"+configSMSMap.toString());
		
	}*/
}
