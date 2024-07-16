package com.eureka.auth.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.sql.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eureka.auth.model.LtMastLogins;
import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;
import com.eureka.auth.model.UserData;
import com.eureka.auth.model.UserLoginDto;
import com.eureka.auth.repository.LtMastUsersRepository;

import java.net.URI;
import java.net.*;

import java.io.IOException;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class AtflMastUsersDaoImpl implements AtflMastUsersDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	LtMastUsersRepository ltMastUsersRepository;

	
	public static <T> List<T> consumeApi(String query, Object[] body,Class<T> clazz) throws IOException, InterruptedException {
        List<T> result = new ArrayList<>();
//		List<LtMastUsers> ltMastUsers = new ArrayList<LtMastUsers>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());

        // Convert the Object[] to JSON string
        String jsonBody = objectMapper.writeValueAsString(body);

        // Build the URI
        String uri = "http://10.245.4.74/OrderApi/ExecuteQueryWithParams?query=" + encodedQuery;
        //String uri = "http://174.138.187.142:8085/OrderApi/ExecuteQueryWithParams?query=" + encodedQuery;
        
        // Create HttpPost request
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
            System.out.println("After response body = "+ LocalDateTime.now());
        
            //List<LtMastUsers> usersArray = objectMapper.readValue(responseBody, LtMastUsers(LtMastUsers.class));
            //ltMastUsers = objectMapper.readValue(responseBody, new TypeReference<List<LtMastUsers>>() {});
            
            result = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            System.out.println("Result = " + result);
            
            //System.out.println("LtMastUsers = " + ltMastUsers);
       }
//		return ltMastUsers;
        return result;
    }
	
	
	
	//This is original method commented by Rohan for optimization on 3 June 2024
	@Override
	public LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber) throws ServiceException {
		String query = env.getProperty("getLtMastUsersByMobileNumber");
//		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { mobileNumber.trim() },
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
//		System.out.println("list"+list);
		System.out.println("Time for mobile query = "+ LocalDateTime.now());
		try {
			List<LtMastUsers> list = consumeApi(query, new Object[] { mobileNumber.trim() },LtMastUsers.class);
			System.out.println("Time after mobile query = "+ LocalDateTime.now());
			System.out.println("list1 is = "+list);
			if (list.isEmpty())
				return null;
			else
				return list.get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	//This is optimized method created by Rohan for optimization on 3 June 2024
	@Override
	public UserLoginDto getLtMastUsersByMobileNumber1(String mobileNumber) throws ServiceException {
		String query = env.getProperty("getLtMastUsersByMobileNumber");
		List<UserLoginDto> list = jdbcTemplate.query(query, new Object[] { mobileNumber.trim() },
				new BeanPropertyRowMapper<UserLoginDto>(UserLoginDto.class));
		System.out.println("list"+list);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastLogins getLoginDetailsByUserId(Long userId) throws ServiceException {
		String query = env.getProperty("getLoginDetailsByUserId");
//		List<LtMastLogins> list = jdbcTemplate.query(query, new Object[] { userId },
//				new BeanPropertyRowMapper<LtMastLogins>(LtMastLogins.class));
		
		List<LtMastLogins> list = new ArrayList<LtMastLogins>();
		try {
			list = consumeApi(query, new Object[] { userId },LtMastLogins.class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
		
	}

	@Override
	public LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException {
		return ltMastUsersRepository.save(ltMastUser);
	}

	@Override
	public LtVersion getVersionAuthAPI(LtVersion ltVersion) throws ServiceException {
		String query = "select * from lt_version where service_name = '" + ltVersion.getService_name() + "'";
		List<LtVersion> ltVersionsList = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtVersion>(LtVersion.class));
		Map<String, LtVersion> apiVersionMap = new LinkedHashMap<String, LtVersion>();

		if (ltVersionsList != null) {
			return ltVersionsList.get(0);
		}

		return null;
	}

}
