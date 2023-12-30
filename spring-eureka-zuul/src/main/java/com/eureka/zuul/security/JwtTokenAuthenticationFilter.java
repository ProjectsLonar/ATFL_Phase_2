package com.eureka.zuul.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eureka.zuul.common.Configuration;
import com.eureka.zuul.common.Status;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIdentityReference(alwaysAsId=true)
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;

	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			String header = request.getHeader(jwtConfig.getHeader());
			
			//check version of API Nikhi Ambhore 21-Oct-2020
			String XAPIVersion = request.getHeader(jwtConfig.getXAPIVersion());
			boolean versionflag = CheckVersionAPI.checkAPIVersion(request, XAPIVersion);
			
			if(!versionflag) {
				
//				System.out.println("API CHECK FLAG ====>"+versionflag);
//				SecurityContextHolder.clearContext();
//				
//				response.sendError(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED, "HTTP Version not supported");
//		        String json = String.format("{\"message\": \"HTTP Version not supported\"}");
//		        response.setStatus(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED);
//		        response.setContentType("application/json");
//		        response.setCharacterEncoding("UTF-8");
//		        response.getWriter().write(json);   
//		        chain.doFilter(request, response);
				Status status = new Status();
				status.setCode(505);
				status.setMessage("Current version of HTTP service is nor supported.Please update your application version or contact to administrator");
				status.setData(null);
				Gson gson = new Gson();
				String statusStr = gson.toJson(status);

				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(statusStr);
				out.flush();
				
			}else {
				// 2. validate the header and check the prefix
				if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
					chain.doFilter(request, response); // If not valid, go to the next filter.
					return;
				}
				// 3. Get the token
				System.out.println("API CHECK FLAG ELSE====>"+versionflag);
				String token = header.replace(jwtConfig.getPrefix(), "");

				try { // exceptions might be thrown in creating the claims if for example the token is
						// expired

					// 4. Validate the token
					Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token)
							.getBody();

					// check inactive user number is avilable in map
					//Map<String, LtMastUsers> inactiveUserMap = Configuration.inactiveUserMap;
					String mobileNo = (String) claims.get("sub");
					if (Configuration.inactiveUserMap.get(mobileNo) != null) {
						SecurityContextHolder.clearContext();
					} else {

						String username = claims.getSubject();
						if (username != null) {
							@SuppressWarnings("unchecked")
							List<String> authorities = (List<String>) claims.get("authorities");

							UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username,
									null,
									authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

							// 6. Authenticate the user
							// Now, user is authenticated
							SecurityContextHolder.getContext().setAuthentication(auth);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					SecurityContextHolder.clearContext();
				}
				chain.doFilter(request, response);
			}
			//chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}