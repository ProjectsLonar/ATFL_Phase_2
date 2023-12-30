package com.users.usersmanagement;

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

import com.users.usersmanagement.service.LtMastSysVariablesService;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = { "com.users.usersmanagement.*" })
@PropertySource({ "classpath:persistence.properties" })
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
@Configuration
public class UsersManagementApplication extends SpringBootServletInitializer{

	@Autowired
	private Environment env;
	
	@Lazy
	@Autowired
	LtMastSysVariablesService ltMastSysVariablesService;
	
	public static Map<Long,Map<String,String>> configMap = new HashMap<Long,Map<String,String>>();
	
	public static void main(String[] args) {
		   SpringApplication.run(new Class[] { UsersManagementApplication.class, }, args);
	}
	
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
	
	
	
	/* @LoadBalanced
	 @Bean
	 @DependsOn({"datasource","resttemplate"})
	    public Map getAllConfiguration() {
			try
			{
				List<LtMastSysVariables> ltMastSysVariablesList = ltMastSysVariablesService.loadAllConfiguration();
				System.out.println("ltMastSysVariablesList "+ltMastSysVariablesList);
				 Iterator<LtMastSysVariables> itr=ltMastSysVariablesList.iterator();
					while(itr.hasNext())
					{
						LtMastSysVariables ltMastSysVariables=itr.next();
						Set<Long> set = configMap.keySet();
						if(set.contains(ltMastSysVariables.getSupplierId())) {
							Map<String,String> myMap = configMap.get(ltMastSysVariables.getSupplierId());
							Set<String> variableNameSet = myMap.keySet();
							if(!variableNameSet.contains(ltMastSysVariables.getVariableName())) {
								myMap.put(ltMastSysVariables.getVariableName(), ltMastSysVariables.getSystemValue());
								configMap.put(ltMastSysVariables.getSupplierId(), myMap);
							}
						}else {
							Map<String,String> myMap = new HashMap<String,String>();
							myMap.put(ltMastSysVariables.getVariableName(), ltMastSysVariables.getSystemValue());
							configMap.put(ltMastSysVariables.getSupplierId(), myMap);							
						}
					}
					System.out.println("configMap "+configMap);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	        return configMap;
	    }*/
}
