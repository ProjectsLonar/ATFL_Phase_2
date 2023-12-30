package com.lonar.cloud.eurekaservice;

import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ATFLEurekaServerApplication extends SpringBootServletInitializer{

	 private static final Logger logger = LoggerFactory.getLogger(ATFLEurekaServerApplication.class);
	   
	   public static void main(String[] args) {
	      logger.info("this is a info message");
	      logger.warn("this is a warn message");
	      logger.error("this is a error message");
		SpringApplication.run(ATFLEurekaServerApplication.class, args);
	}

}
