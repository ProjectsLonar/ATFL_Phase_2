package com.eureka.auth.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eureka.auth.dao.AtflMastUsersDao;
import com.eureka.auth.model.CodeMaster;
import com.eureka.auth.model.LtMastLogins;
import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;

@Service // It has to be annotated with @Service.
public class UserDetailsServiceImpl implements UserDetailsService, CodeMaster {

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private AtflMastUsersDao ltMastUsersDao;

	public static Map<String, LtMastUsers> ltMastUsersMap = new HashMap<String, LtMastUsers>();
	public static Map<String, LtVersion> LtVersionsMap = new HashMap<String, LtVersion>();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			System.out.println("in loadUserByUsername method = "+new Date()+"\n");
			System.out.println("above getLtMastUsersByMobileNumber query call = "+  System.currentTimeMillis());
			LtMastUsers ltMastUser = ltMastUsersDao.getLtMastUsersByMobileNumber(username);
			System.out.println("below getLtMastUsersByMobileNumber query call = "+  System.currentTimeMillis());
			System.out.println("ltMastUser = "+ltMastUser+"\n");
			LtMastUsers ltMastUserToken = ltMastUsersMap.get(username);
			String userOTP = ltMastUserToken.getMobileNumber();
			System.out.println("userOTP = "+userOTP);
			ltMastUser.setTokenData(ltMastUserToken.getTokenData());
			System.out.println("47. = "+ltMastUser+"\n");
			ltMastUsersMap.put(username, ltMastUser);
			System.out.println("49. = "+ltMastUsersMap+"\n");
			// LtMastLogins ltMastLogins =new LtMastLogins();
			
			
			if (ltMastUser.getStatus().equals("INACTIVE")) {
				System.out.println("in get status = inactive");
				// System.out.println("User is Inactive");
				// throw new UsernameNotFoundException("Username: " + username + " is
				// inactive");

			}
			
			System.out.println("60. = "+new Date()+"\n");
			System.out.println("above getLoginDetailsByUserId query call = "+  System.currentTimeMillis());
			LtMastLogins ltMastLogin = ltMastUsersDao.getLoginDetailsByUserId(ltMastUser.getUserId());
			System.out.println("below getLoginDetailsByUserId query call = "+  System.currentTimeMillis());
			
			System.out.println("62. = "+new Date()+"\n");
			String otp = "" + ltMastLogin.getOtp();

			System.out.println("otp===>" + otp);

			CharSequence charSeqOTP = otp;
			final List<String> roles = new ArrayList<String>();

			if (otp.equals(userOTP)) {
				// otp matched ...save token into token
				if (ltMastUser.getStatus().equals("INPROCESS")) {
					try {
						LtMastUsers ltMastUserInprocess = new LtMastUsers();
						ltMastUserInprocess.setUserId(ltMastUser.getUserId());
						ltMastUserInprocess.setMobileNumber(ltMastUser.getMobileNumber());
						ltMastUserInprocess.setStatus(ltMastUser.getStatus());
						ltMastUserInprocess.setCreatedBy(ltMastUser.getCreatedBy());
						ltMastUserInprocess.setCreationDate(ltMastUser.getCreationDate());
						ltMastUserInprocess.setLastUpdatedBy(ltMastUser.getLastUpdatedBy());
						ltMastUserInprocess.setLastUpdateLogin(ltMastUser.getLastUpdateLogin());
						ltMastUserInprocess.setLastUpdateDate(new Date());
						ltMastUserInprocess.setTokenData(ltMastUser.getTokenData());

						
						// set null nikhil changes
						ltMastUserInprocess.setOrgId(null);
						ltMastUserInprocess.setDistributorId("0");
						ltMastUserInprocess.setOutletId(null);
						ltMastUserInprocess.setUserType("");
						ltMastUserInprocess.setEmployeeCode("");
						ltMastUserInprocess.setUserName("");
						ltMastUserInprocess.setDesignation(null);
						ltMastUserInprocess.setEmail(null);
						ltMastUserInprocess.setAddress(null);
						ltMastUserInprocess.setAlternateNo(null);
						ltMastUserInprocess.setImageType(null);
						ltMastUserInprocess.setImageData(null);
						ltMastUserInprocess.setImageName(null);
						ltMastUserInprocess.setLatitude(null);
						ltMastUserInprocess.setLongitude(null);
						ltMastUserInprocess.setAddressDetails(null);
						ltMastUserInprocess.setRecentSerachId("0");
						ltMastUserInprocess.setPositionId(null);

						// end nikhil changes

						ltMastUsersDao.saveLtMastUsers(ltMastUserInprocess);
					} catch (Exception e) {
						e.printStackTrace();
					}
					roles.add("PREVERIFIED");
				} else {
					System.out.println("hi in else");
					roles.add(ltMastUser.getUserType());
					ltMastUsersDao.saveLtMastUsers(ltMastUser);
				}
			}

		final List<AppUser> users = Arrays.asList(new AppUser(ltMastUser.getUserId().toString(), ltMastUser.getMobileNumber(),
					encoder.encode(charSeqOTP), roles));

			for (AppUser appUser : users) {
				if (appUser.getUsername().equals(username)) {

					// Remember that Spring needs roles to be in this format: "ROLE_" + userRole
					// (i.e. "ROLE_ADMIN")
					// So, we need to set it to that format, so we can verify and compare roles
					// (i.e. hasRole("ADMIN")).
					List<GrantedAuthority> grantedAuthorities = AuthorityUtils
							.commaSeparatedStringToAuthorityList("ROLE_" + appUser.getRoles().get(0));


					// The "User" class is provided by Spring and represents a model class for user
					// to be returned by UserDetailsService
					// And used by auth manager to verify and check user authentication.
					return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
				}
		} 
		} catch (Exception e) {
			throw new UsernameNotFoundException("Username: " + username + " not found");
		}
		System.out.println("exit loadUserByUsername method = "+new Date());
		return null;

		// If user not found. Throw this exception.

	}

	// A (temporary) class represent the user saved in the database.
	private static class AppUser {
		private String id;
		private String username, password;
		private List<String> roles = new ArrayList<String>();

		public AppUser(String id, String username, String password, List<String> roles) {
			super();
			this.id = id;
			this.username = username;
			this.password = password;
			this.roles = roles;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

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

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

	}
}