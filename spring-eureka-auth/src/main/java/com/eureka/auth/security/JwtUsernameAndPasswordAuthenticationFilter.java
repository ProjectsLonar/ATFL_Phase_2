package com.eureka.auth.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.eureka.auth.model.CodeMaster;
import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;
import com.eureka.auth.model.Status;
import com.eureka.auth.response.JwtResponse;
import com.eureka.auth.service.AtflMastUsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter
		implements CodeMaster {

	// We use auth manager to validate the user credentials
	private AuthenticationManager authManager;

	private final JwtConfig jwtConfig;

	AtflMastUsersService atflMastUsersService;

	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig,
			AtflMastUsersService atflMastUsersService) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;

		this.atflMastUsersService = atflMastUsersService;
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		logger.info("Authentication failed");
		Status status = new Status();
		status.setCode(FAIL);
		status.setMessage("Username /OTP is invalid");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		Gson gson = new Gson();
		String jwtResponseStr = gson.toJson(status);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(jwtResponseStr);
		out.flush();

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {

			// 1. Get credentials from request
			UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
			LtMastUsers ltMastUser = new LtMastUsers();
			ltMastUser.setTokenData(creds.getToken());
			ltMastUser.setMobileNumber(creds.getPassword()); // temp set otp as mobilenumber
			UserDetailsServiceImpl.ltMastUsersMap.put(creds.getUsername(), ltMastUser);
			// manager
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), Collections.emptyList());
			return authManager.authenticate(authToken);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String userName = auth.getName();
		Status status = new Status();
		String XAPIVersion = request.getHeader("X-API-Version");
		boolean versionFlag = false;
		try {

			/// Check Auth Version Nikhil Ambhore 22-Oct-2020
			if (XAPIVersion != null) {

				boolean versionFlagAuth = checkAPIVersionAuth(XAPIVersion);
				if (!versionFlagAuth) {
					versionFlag = versionFlagAuth;
					status.setCode(505);
					status.setMessage("Current version of HTTP service is nor supported.Please update your application version or contact to administrator");
					status.setData(null);
				} else {
					versionFlag = versionFlagAuth;
				}

			} else {
				status.setCode(505);
				status.setMessage("Current version of HTTP service is nor supported.Please update your application version or contact to administrator");
				status.setData(null);
			}

			/// End Check Auth Version Nikhil Ambhore 22-Oct-2020

			if (versionFlag) {

				JwtResponse jwtResponse = new JwtResponse();
				LtMastUsers ltMastUser = UserDetailsServiceImpl.ltMastUsersMap.get(userName);
				UserDetailsServiceImpl.ltMastUsersMap.remove(userName);
				if (ltMastUser.getStatus().toUpperCase().equals("INACTIVE")) {
					status.setCode(FAIL);
					status.setMessage("User is inactive");
					status.setData(null);
				} else if (ltMastUser.getStatus().toUpperCase().equals("ACTIVE")
						|| ltMastUser.getStatus().toUpperCase().equals("INPROCESS")
						|| ltMastUser.getStatus().toUpperCase().equals("PENDING_APPROVAL")) {
					System.out.println("User is " + ltMastUser.getStatus());
					status.setCode(SUCCESS);
					status.setMessage("User status is " + ltMastUser.getStatus());
					System.out.println("auth.getName()  ============ " + auth.getName());
					Long now = System.currentTimeMillis();
					String token = Jwts.builder().setSubject(auth.getName()) // Change to mobile no for Active user
																				// check
							.claim("mobileNumber", auth.getName())
							.claim("authorities",
									auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
											.collect(Collectors.toList()))
							.setIssuedAt(new Date(now))
							.setExpiration(new Date(now + (jwtConfig.getExpiration() * 1000))) // in
																								// milliseconds
							.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes()).compact();

					// Add token to header
					response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

					response.setStatus(HttpStatus.OK.value());

					List<String> roles = new ArrayList<String>();
					roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
							.collect(Collectors.toList());

					jwtResponse = new JwtResponse(token, "Bearer", auth.getName(), roles, ltMastUser.getStatus(),
							ltMastUser.getUserId(), ltMastUser.getUserType(), ltMastUser.getEmployeeCode(),
							ltMastUser.getOrgId(), ltMastUser.getUserName(), ltMastUser.getDistributorId(),
							ltMastUser.getOutletId(), ltMastUser.getDesignation(), ltMastUser.getPositionId(),
							ltMastUser.getTokenData(),ltMastUser.getOutletName(),ltMastUser.getDistributorCode(),
							ltMastUser.getDistributorName(),ltMastUser.getProprietorName(),ltMastUser.getPosition(),
							ltMastUser.getCreationDate(),ltMastUser.getLastUpdateDate(),ltMastUser.getIsFirstLogin());

					status.setData(jwtResponse);

				} else {
					System.out.println("Unknown Status of User");
					status.setCode(FAIL);
					status.setMessage("User unknown status is " + ltMastUser.getStatus());
				}

			}
			Gson gson = new Gson();
			String statusStr = gson.toJson(status);

			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(statusStr);
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean checkAPIVersionAuth(String ltversionauth) {
		LtVersion ltVersionRequest = new LtVersion();
		ltVersionRequest.setService_name("auth/login");
		LtVersion ltVersion = atflMastUsersService.getVersionAuthAPI(ltVersionRequest);
		if (ltVersion != null) {
			List<String> versionList = Arrays.asList(ltVersion.getSupported_version().split(",", -1));
			if (versionList.contains(ltversionauth)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	// A (temporary) class just to represent the user credentials
	private static class UserCredentials {
		private String username, password, token;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

	}
}