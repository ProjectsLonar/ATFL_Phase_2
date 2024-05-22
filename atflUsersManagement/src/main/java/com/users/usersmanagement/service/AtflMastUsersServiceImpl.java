package com.users.usersmanagement.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.DateTimeClass;
import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.common.Validation;
import com.users.usersmanagement.controller.WebController;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastEmployeeDao;
import com.users.usersmanagement.dao.LtMastSmsTokenDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtConfigurartion;
import com.users.usersmanagement.model.LtMastEmployees;
import com.users.usersmanagement.model.LtMastLogins;
import com.users.usersmanagement.model.LtMastSmsToken;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.LtUserAddressDetails;
import com.users.usersmanagement.model.LtUserPersonalDetails;
import com.users.usersmanagement.model.MobileSupportedVersionRequestDto;
import com.users.usersmanagement.model.MobileSupportedVersionResponseDto;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.model.UserDto;
import com.users.usersmanagement.repository.LtMastLoginsRepository;
import com.users.usersmanagement.repository.LtMastUsersRepository;

@Service
@PropertySource(value = "classpath:queries/messages.properties", ignoreResourceNotFound = true)
public class AtflMastUsersServiceImpl implements AtflMastUsersService, CodeMaster {

	private static final Logger logger = LoggerFactory.getLogger(AtflMastUsersServiceImpl.class);

	@Autowired
	private AtflMastUsersDao ltMastUsersDao;

	@Autowired
	private LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	private LtMastLoginsRepository ltMastLoginsRepository;

	@Autowired
	private LtMastSmsTokenDao ltMastSmsTokenDao;

	@Autowired
	private LtMastSmsTokenService ltMastSmsTokenService;

	@Autowired
	LtMastUsersRepository ltMastUsersRepository;

	
	@Autowired
	LtMastEmployeeDao ltMastEmployeeDao;
	

	@Autowired
	WebController webController;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private Environment env;

	private LtMastLogins generateAndSendOtp(LtMastUsers ltMastUser) throws IOException, ServiceException {
		// we have to implememt OTP logic
		Status status = new Status();
		// String otp = generateOTP(4);
		
		// String otp = "" + getRandomNumberInRange(1000, 9999);
		String otp = "" + "1234";
		 
		/* String otp;
		 if(ltMastUser.getMobileNumber().equalsIgnoreCase("8857885605")) {
			 otp = "" + "1234";
		 }else {
			otp = "" + getRandomNumberInRange(1000, 9999);  //uncommen for acual oTP
			//otp = "" + "1234";
		 }
*/
		
		System.out.println("In generate & send Otp for UserId = "+ ltMastUser.getUserId());
		System.out.println("In generate for user === "+ltMastUser);
		LtMastLogins ltMastLogin = ltMastUsersDao.getLoginDetailsByUserId(ltMastUser.getUserId());

		System.out.println("ltMastLogin in generate method"+ ltMastLogin);
		LtMastLogins ltMastLogins = new LtMastLogins();

		if (ltMastLogin != null) {
			if (ltMastLogin.getTokenId() != null) {
				ltMastLogins.setTokenId(ltMastLogin.getTokenId());
			}
		}
		ltMastLogins.setUserId(ltMastUser.getUserId());

		ltMastLogins.setOtp(Long.parseLong(otp));

		if (ltMastUser.getStatus().equals(INPROCESS) || ltMastUser.getStatus().equals(INACTIVE)) {
			ltMastLogins.setStatus(INPROCESS);
		} else {
			ltMastLogins.setStatus("SUCCESS");
		}

		ltMastLogins.setLoginDate(Validation.getCurrentDateTime());
		ltMastLogins.setDevice(null);
		ltMastLogins.setIpAddress(null);
		//ltMastLogins.setLoginId((long) 9809);
		System.out.println("ltMastLogin before save"+ltMastLogins);
		ltMastLogins = ltMastLoginsRepository.save(ltMastLogins);
		System.out.println("ltMastLogin after save");
		if (ltMastLogins.getLoginId() != null) {
			if (ltMastUser.getMobileNumber() != null) {
				status = sendMessage(ltMastLogins, ltMastUser.getMobileNumber());
			}

			if (status.getCode() == SUCCESS)
				return ltMastLogins;
			else
				return null;
		} else
			return null;
	}

	private Status sendMessage(LtMastLogins ltMastLogins, String mobileNumber) throws IOException, ServiceException {
		Status status = new Status();

		List<LtMastSmsToken> ltMastSmsTokenList = new ArrayList<LtMastSmsToken>();
		LtMastSmsToken ltMastSmsToken = new LtMastSmsToken();

		Long trasid = System.currentTimeMillis();
		ltMastSmsToken.setTransactionId(trasid.toString());
		ltMastSmsToken.setSmsType("OTP");
		ltMastSmsToken.setSmsObject(ltMastLogins.getOtp().toString());
		ltMastSmsToken.setSendTo(Long.parseLong(mobileNumber));
		ltMastLogins.setMobile(mobileNumber);
		//ltMastSmsToken.setSendDate(Validation.getCurrentDateTime());
		ltMastSmsToken.setSmsStatus("SENDING");
		ltMastSmsToken.setUserId(ltMastLogins.getUserId());
		//ltMastSmsToken.setSmsTokenId((long)9803);
		//System.out.println("hi in sms");
		ltMastSmsTokenList.add(ltMastSmsToken);

		List<LtMastSmsToken> ltMastSmsTokenListOp = ltMastSmsTokenDao.saveall(ltMastSmsTokenList);

		if (ltMastSmsTokenListOp.isEmpty()) {
			status.setMessage("Message OTP send failed");
			status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
			status.setCode(INSERT_FAIL);

		} else {
			// sendSMS(ltMastLogins);
			// mailSendServiceCall.callToSMSService(trasid, ltMastSmsToken.getUserId());
			status = ltMastSmsTokenService.sendSms(ltMastSmsToken.getUserId(), trasid.toString());
			if (status.getCode() == SUCCESS) {
				status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
				status.setCode(SUCCESS);
				status.setMessage("Message OTP send success");
			}

		}
		return status;
	}

	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
		// return 1234;
	}

	@Override
	public Status login(String mobileNumber, Long otp) throws ServiceException {
		Status status = new Status();

		if (!Validation.validatePhoneNumber(mobileNumber)) {
			status.setCode(FAIL);
			status.setMessage(env.getProperty(env.getProperty("lonar.users.mobilenoValid")));
			return status;
		}
		if (!Validation.validateOTP(otp)) {
			status.setCode(FAIL);
			status.setMessage(env.getProperty("lonar.users.InvalidOTP"));
			return status;
		}
		LtMastUsers ltMastUser = ltMastUsersDao.getLtMastUsersByMobileNumber(mobileNumber);
		if (ltMastUser.getStatus().equals("INACTIVE")) {
			status.setCode(FAIL);
			status.setMessage(env.getProperty(env.getProperty("lonar.users.loginuserinactive")));
			return status;
		}
		LtMastLogins ltMastLogin = ltMastUsersDao.getLoginDetailsByUserId(ltMastUser.getUserId());

		if (ltMastLogin != null) {

			if (ltMastLogin.getOtp().equals(otp)) {

				ltMastLogin.setStatus(SUCCESS_LOGIN);
				ltMastLogin.setLoginDate(new Date());
				ltMastLogin = ltMastLoginsRepository.save(ltMastLogin);

				// LtMastUsers mastUsers = ltMastUsersDao.getUserById(ltMastLogin.getUserId());
				status.setCode(SUCCESS);
				status.setMessage(env.getProperty("lonar.users.loginuserinactive"));
				status.setData(ltMastUser);

			} else {
				status.setCode(FAIL);
				status.setMessage(env.getProperty("lonar.users.InvalidOTP"));
			}
		} else {
			status.setCode(FAIL);
			status.setMessage(env.getProperty("lonar.users.InvalidOTP"));
		}
		return status;
	}

	private Status checkDuplicate(LtMastUsers ltMastUser) throws ServiceException {
		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getLtMastUsersByMobileNumber(ltMastUser.getMobileNumber());
		if (ltMastUsers != null) {
			if (!ltMastUsers.getUserId().equals(ltMastUser.getUserId())) {
				status.setCode(FAIL);
				status.setMessage("Mobile Number Already Exists");
				return status;
			}
		}
		status.setCode(SUCCESS);
		return status;
	}

	@Override
	public Status sendOTPToUser(String mobileNumber) throws ServiceException, IOException {

		try {
		Status status = new Status();
		LtMastUsers user = null;

		if (Validation.validatePhoneNumber(mobileNumber)) {
			
			if(mobileNumber.equalsIgnoreCase("8806962104") || mobileNumber.equalsIgnoreCase("6260011680") ||//("7013600327") || 
					mobileNumber.equalsIgnoreCase("7796551219") || mobileNumber.equalsIgnoreCase("7972458618")|| //("9178188671")|| //("9987792717") || 
					mobileNumber.equalsIgnoreCase("9775977932") || mobileNumber.equalsIgnoreCase("6370239243") ||
					mobileNumber.equalsIgnoreCase("8092383807")) {
				
			}
			else {
				LtMastUsers user123 = ltMastUsersDao.verifyUserDetailsByMobileNumbervInSiebel1(mobileNumber);
				if(user123 == null) {
					status.setCode(FAIL);
					status.setMessage(env.getProperty("lonar.users.userLoginNotsuccess"));
					return status;
				}
			}

			if (mobileNumber != null) {				
				user = ltMastUsersDao.getLtMastUsersByMobileNumber(mobileNumber);
			} else {
				status.setCode(FAIL);
				status.setMessage(env.getProperty("lonar.users.sentOTPnotsuccess"));
				return status;
			}
			System.out.println("user data is"+user);
			if (user == null) {
				LtMastUsers ltMastUser = new LtMastUsers();
                
				UserDto userDto = new UserDto();
				
				System.out.println("Input MobNo is"+mobileNumber);
				userDto= ltMastUsersDao.verifyUserDetailsByMobileNumbervInSiebel(mobileNumber);
				
				if(userDto == null) {
					status.setCode(FAIL);
					status.setMessage(env.getProperty("lonar.users.userLoginNotsuccess"));
					return status;
				}else {
				System.out.println("User Data is"+ltMastUser);
				ltMastUser.setMobileNumber(mobileNumber);
				//ltMastUser.setStatus(INPROCESS); vaibhav 15-mar-24
				ltMastUser.setStatus(userDto.getStatus());
				ltMastUser.setOrgId("1");
				ltMastUser.setOutletId(userDto.getOutletId());
				ltMastUser.setRecentSerachId(userDto.getRecentSearchId());
				ltMastUser.setDistributorId(userDto.getDistributorId());
				ltMastUser.setEmployeeCode(userDto.getEmployeeCode());
				ltMastUser.setUserName(userDto.getUserName());
				if(userDto.getLatitude().isPresent()) {
				ltMastUser.setLatitud(userDto.getLatitude());
				}
				if(userDto.getLongitude().isPresent()) {
				ltMastUser.setLongitud(userDto.getLongitude());
				}
				if(userDto.getUserType().equalsIgnoreCase("Sales Person")) {
					ltMastUser.setUserType("SALES");
					}
					if(userDto.getUserType().equalsIgnoreCase("Area Head")) {
						ltMastUser.setUserType("AREAHEAD");
					}
					if(userDto.getUserType().equalsIgnoreCase("Distributor Proprietor")) {
						ltMastUser.setUserType("DISTRIBUTOR");
					}
					if(userDto.getUserType().equalsIgnoreCase("Sales Officer")) {
						ltMastUser.setUserType("SALESOFFICER");
					}
					if(userDto.getUserType().equalsIgnoreCase("Organization User")) {
						ltMastUser.setUserType("ORGANIZATION_USER");
					}
					if(userDto.getUserType().equalsIgnoreCase("System Administrator")) {
						ltMastUser.setUserType("SYSTEMADMINISTRATOR");
					}
					if(userDto.getUserType().equalsIgnoreCase("RETAILER")) {
						ltMastUser.setUserType("RETAILER");
					}

			//	ltMastUser.setUserType(userDto.getUserType());
				ltMastUser.setAddress(userDto.getAddress());
				ltMastUser.setEmail(userDto.getEmail());
				ltMastUser.setHomephNum(userDto.getHomephNum());
			//	ltMastUser.setMobileNumber(ltMastUser.getMobileNumber());
				ltMastUser.setAsstOPhNum(userDto.getAsstPhNum());
				ltMastUser.setAddressDetails(userDto.getAddressDetails());
				ltMastUser.setPositionId(userDto.getPositionId());
				ltMastUser.setCreationDate(userDto.getCreationDate());
			//	ltMastUser.setCreationDate(Validation.getCurrentDateTime());
			//	ltMastUser.setCreatedBy(Long.parseLong(userDto.getCreatedBy()));
    			ltMastUser.setCreatedBy(-1L);
				ltMastUser.setLastUpdateDate(userDto.getLastUpdateDate());
		  //	ltMastUser.setLastUpdateDate(Validation.getCurrentDateTime());
		//		ltMastUser.setLastUpdatedBy(Long.parseLong(userDto.getLastUpdatedBy()));
				ltMastUser.setLastUpdatedBy(-1L);
				ltMastUser.setRecentSerachId(userDto.getRecentSearchId());
				ltMastUser.setLastUpdateLogin(-1L);
				ltMastUser.setIsFirstLogin("Y");
				ltMastUser.setTerritory(userDto.getTerritory());
				
/*                    phase1-old-code				
                ltMastUser.setCreatedBy(-1L);
				ltMastUser.setCreationDate(Validation.getCurrentDateTime());
				ltMastUser.setLastUpdateDate(Validation.getCurrentDateTime());
				ltMastUser.setLastUpdatedBy(-1L);
				ltMastUser.setLastUpdateLogin(-1L);
				ltMastUser.setDistributorId("0");
				ltMastUser.setUserType("");
				ltMastUser.setEmployeeCode("");
				ltMastUser.setUserName("");
*/				ltMastUser = ltMastUsersDao.saveLtMastUsers(ltMastUser);
				}
				if (ltMastUser.getUserId() != null) {
					LtMastLogins mastLogins = this.generateAndSendOtp(ltMastUser);
					if (mastLogins != null) {
						status.setCode(SUCCESS);
						status.setMessage(env.getProperty("lonar.users.sentOTPsuccess"));

					} else {
						status.setCode(FAIL);
						status.setMessage(env.getProperty("lonar.users.sentOTPnotsuccess"));
					}
				} else {
					status.setCode(FAIL);
					status.setMessage(env.getProperty("lonar.users.sentOTPnotsuccess"));
				}

			} else {

				LtMastLogins mastLogins = this.generateAndSendOtp(user);

				if (mastLogins != null) {
					status.setCode(SUCCESS);
					status.setMessage(env.getProperty("lonar.users.sentOTPsuccess"));
				} else {
					status.setCode(FAIL);
					status.setMessage(env.getProperty("lonar.users.sentOTPnotsuccess"));
				}
			}
		} else {
			status.setCode(FAIL);
			status.setMessage(env.getProperty("lonar.users.mobilenoValid"));
			status.setData(null);
		}
		return status;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Status reSendOTP(String mobileNumber, Long distributorId) throws ServiceException, IOException {
		return null;
	}

	@Override
	public Status update(LtMastUsers ltMastUser) throws ServiceException, IOException {

		Status status = new Status();
		ltMastUser.setLastUpdatedBy(ltMastUser.getLastUpdateLogin());
		ltMastUser.setLastUpdateLogin(ltMastUser.getLastUpdateLogin());
		ltMastUser.setLastUpdateDate(new Date());

		if (ltMastUser.getCreatedBy() == null) {
			ltMastUser.setCreatedBy(ltMastUser.getLastUpdateLogin());
		}

		if (ltMastUser.getCreationDate() == null) {
			ltMastUser.setCreationDate(new Date());
		}

		status = checkDuplicate(ltMastUser);

		if (status.getCode() == FAIL) {
			return status;
		}

		LtMastUsers ltMastUserdto = ltMastUsersDao.getLtMastUsersByMobileNumber(ltMastUser.getMobileNumber());

		if (ltMastUserdto != null) {

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.SALES)
					&& ltMastUser.getStatus().equalsIgnoreCase(ACTIVE)
					&& ltMastUserdto.getStatus().equalsIgnoreCase(INPROCESS)) {
				ltMastUser.setStatus(PENDINGAPPROVAL);
			}

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.SALES)
					&& ltMastUser.getStatus().equalsIgnoreCase(ACTIVE)
					&& ltMastUserdto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
				ltMastUser.setStatus(PENDINGAPPROVAL);
			}

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.DISTRIBUTOR)
					&& ltMastUser.getStatus().equalsIgnoreCase(ACTIVE)
					&& ltMastUserdto.getStatus().equalsIgnoreCase(INPROCESS)) {

				if (ltMastUser.getEmployeeCode() != null) {

					//LtMastDistributors LtMastDistributor = ltMastDistributorsDao.getLtDistributorsById(ltMastUser.getDistributorId());
					LtMastEmployees  ltMastEmployees = ltMastEmployeeDao.getEmployeeByCode(ltMastUser.getEmployeeCode());
					
					
					if (ltMastEmployees.getPrimaryMobile().length() >= 10) {

						String mobileNumber = ltMastEmployees.getPrimaryMobile()
								.substring(ltMastEmployees.getPrimaryMobile().length() - 10);

						if (mobileNumber.equalsIgnoreCase(ltMastUser.getMobileNumber())) {
							ltMastUser.setStatus(ACTIVE);
						} else {
							ltMastUser.setStatus(PENDINGAPPROVAL);
						}

					} else {
						ltMastUser.setStatus(PENDINGAPPROVAL);
					}

				}

			}
			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.DISTRIBUTOR)
					&& ltMastUser.getStatus().equalsIgnoreCase(ACTIVE)
					&& ltMastUserdto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
				ltMastUser.setStatus(ltMastUserdto.getStatus());
			}

			ltMastUser = ltMastUsersDao.saveLtMastUsers(ltMastUser);

			if (ltMastUser != null) {
				status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_SUCCESSFULLY);
				status.setData(userResponce(ltMastUser));
				return status;
			}

		}
		status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public Status getUserById(Long userId) throws ServiceException {

		Status status = new Status();
		try {
			LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
			if (ltMastUsers != null) {

				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
				LtMastUsers ltMastUsersResponce = userResponce(ltMastUsers);
				status.setData(ltMastUsersResponce);
			} else {
				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
				status.setData(null);
			}
		} catch (Exception e) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(null);
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public Status delete(Long userId) throws ServiceException {
		Status status = new Status();

		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {
			LtMastUsers ltMastUserDelete = ltMastUsersDao.delete(userId);
			if (ltMastUserDelete == null) {
				status.setCode(SUCCESS);
				status = ltMastCommonMessageService.getCodeAndMessage(DELETE_SUCCESSFULLY);
				return status;
			}

		} else {
			status.setCode(FAIL);
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			return status;
		}

		status.setCode(FAIL);
		status = ltMastCommonMessageService.getCodeAndMessage(DELETE_FAIL);
		return status;
	}

	private Status sendSMS(LtMastLogins ltMastLogins) throws IOException, ServiceException {
		Status status = new Status();
		// Prepare Url
		URLConnection myURLConnection = null;
		URL myURL = null;
		BufferedReader reader = null;

		String mainUrl = "https://api.msg91.com/api/v5/otp?authkey=326761AUOTEmpKN5e9ec8bcP1&template_id=5ea2d4a1d6fc055ee335c782&extra_param={\"OTP\":\"#sendOtp#\", \"COMPANY_NAME\":\"ATFL\"}&mobile=#mobileNumber#";
		mainUrl = mainUrl.replace("#sendOtp#", ltMastLogins.getOtp().toString());
		mainUrl = mainUrl.replace("#mobileNumber#", ltMastLogins.getMobile());

		StringBuilder sbPostData = new StringBuilder(mainUrl);
		// final string
		mainUrl = sbPostData.toString();
		try {
			// prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			// reading response
			String response;
			while ((response = reader.readLine()) != null)
				// print response
				System.out.println(response);
			// finally close connection
			reader.close();
			status.setCode(SUCCESS);
		} catch (IOException e) {
			status.setCode(FAIL);
			System.out.println("GET request not worked");
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public Status getPenddingApprovalByDistributorId(Long distributorId, Long userId)
			throws ServiceException, IOException {
		Status status = new Status();
		// List<LtMastUsers> ltMastUserList = null;
		List<LtMastUsers> ltMastUsersList = ltMastUsersDao.getPenddingApprovalByDistributorId(distributorId, userId);

		if (!ltMastUsersList.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(ltMastUsersList);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(ltMastUsersList);
		}
		return status;
	}

	@Override
	public Status changeUserStatus(Long userId, String userStatus) throws ServiceException, IOException {
		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);

		if (ltMastUsers != null) {

			if (ltMastUsers.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {

				ltMastUsers.setStatus(userStatus);
				ltMastUsers.setLastUpdatedBy(userId);
				ltMastUsers.setLastUpdateLogin(ltMastUsers.getLastUpdateLogin());
				ltMastUsers.setLastUpdateDate(new Date());

				LtMastUsers ltMastUsersDto = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

				if (ltMastUsersDto != null) {
					status = sendSMSStatusChange(ltMastUsers);
					status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_SUCCESSFULLY);
					status.setData(userResponce(ltMastUsersDto));
					return status;
				}
			} else {
				ltMastUsers.setStatus(userStatus);
				ltMastUsers.setLastUpdatedBy(userId);
				ltMastUsers.setLastUpdateLogin(ltMastUsers.getLastUpdateLogin());
				ltMastUsers.setLastUpdateDate(new Date());
				LtMastUsers ltMastUsersDto = ltMastUsersDao.saveLtMastUsers(ltMastUsers);
				if (ltMastUsersDto != null) {
					status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_SUCCESSFULLY);
					status.setData(userResponce(ltMastUsersDto));
					if (userStatus.equalsIgnoreCase(INACTIVE) || userStatus.equalsIgnoreCase(ACTIVE)) {
						Status statusObj = this.getInactiveUsers();
						System.out.println("Data===>" + statusObj);

					}
					return status;
				}
			}

			if (userStatus.equalsIgnoreCase(PENDINGAPPROVAL)) {
				sendNotification(ltMastUsers);
			}

		}

		status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_FAIL);
		status.setData(ltMastUsers);
		return status;
	}

	private void sendNotification(LtMastUsers pendingAppUsers) throws ServiceException {
		List<LtMastUsers> usersList = ltMastUsersDao.getActiveUsersDistByUserId(pendingAppUsers.getUserId());

		for (Iterator iterator = usersList.iterator(); iterator.hasNext();) {
			LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
			if (ltMastUsers.getToken() != null) {
				webController.send(ltMastUsers, pendingAppUsers);
			}
		}
	}

	private static void print_content(HttpURLConnection con) {
		if (con != null) {
			try {
				System.out.println("****** Content of the URL ********");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				while ((input = br.readLine()) != null) {
					System.out.println(input);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public LtMastUsers userResponce(LtMastUsers ltMastUsers) throws ServiceException {

		LtMastUsers ltMastUserDto = new LtMastUsers();
try {
		if (ltMastUsers != null) {

			ltMastUserDto.setUserId(ltMastUsers.getUserId());
			ltMastUserDto.setEmployeeCode(ltMastUsers.getEmployeeCode());
			ltMastUserDto.setUserType(ltMastUsers.getUserType());
			ltMastUserDto.setUserName(ltMastUsers.getUserName());
			ltMastUserDto.setStatus(ltMastUsers.getStatus());
			ltMastUserDto.setOrgId(ltMastUsers.getOrgId());
			ltMastUserDto.setDistributorId(ltMastUsers.getDistributorId());
			ltMastUserDto.setOutletId(ltMastUsers.getOutletId());
			ltMastUserDto.setMobileNumber(ltMastUsers.getMobileNumber());
			ltMastUserDto.setPositionId(ltMastUsers.getPositionId());
			ltMastUserDto.setDesignation(ltMastUsers.getDesignation());
			ltMastUserDto.setAddress(ltMastUsers.getAddress());
			ltMastUserDto.setLatitude(ltMastUsers.getLatitude());
			ltMastUserDto.setLongitude(ltMastUsers.getLongitude());
			ltMastUserDto.setEmail(ltMastUsers.getEmail());
			ltMastUserDto.setAlternateNo(ltMastUsers.getAlternateNo());
			ltMastUserDto.setRecentSerachId(ltMastUsers.getRecentSerachId());
			ltMastUserDto.setAddressDetails(ltMastUsers.getAddressDetails());

			ltMastUserDto.setImageType(ltMastUsers.getImageType());
			ltMastUserDto.setImageName(ltMastUsers.getImageName());
			ltMastUserDto.setImageData(ltMastUsers.getImageData());

			ltMastUserDto.setEmployeeId(ltMastUsers.getEmployeeId());
			ltMastUserDto.setEmpName(ltMastUsers.getEmpName());
			ltMastUserDto.setEmpCode(ltMastUsers.getEmpCode());

			ltMastUserDto.setOutletCode(ltMastUsers.getOutletCode());
			ltMastUserDto.setOutletName(ltMastUsers.getOutletName());
			ltMastUserDto.setOutletAddress(ltMastUsers.getOutletAddress());
			ltMastUserDto.setProprietorName(ltMastUsers.getProprietorName());

			ltMastUserDto.setOrganisationCode(ltMastUsers.getOrganisationCode());
			ltMastUserDto.setOrganisationName(ltMastUsers.getOrganisationName());

			ltMastUserDto.setDistributorAddress(ltMastUsers.getDistributorAddress());
			ltMastUserDto.setDistributorCode(ltMastUsers.getDistributorCrmCode());
			ltMastUserDto.setDistributorName(ltMastUsers.getDistributorName());
			ltMastUserDto.setDistributorCrmCode(ltMastUsers.getDistributorCrmCode());

			ltMastUserDto.setOrgStatus(ltMastUsers.getOrgStatus());
			ltMastUserDto.setPosition(ltMastUsers.getPosition());
			ltMastUserDto.setTerritory(ltMastUsers.getTerritory());
			ltMastUserDto.setInventoryLocationName(ltMastUsers.getInventoryLocationName());

		}
}catch(Exception e) {
	e.printStackTrace();
}

		return ltMastUserDto;
	}

	public Status sendSMSStatusChange(LtMastUsers ltMastUser) throws ServiceException, IOException {
		Status status = new Status();

		if (ltMastUser.getStatus().equalsIgnoreCase(INACTIVE) || ltMastUser.getStatus().equalsIgnoreCase(ACTIVE)) {

			LtMastSmsToken ltMastSmsToken = new LtMastSmsToken();
			List<LtMastSmsToken> ltMastSmsTokenList = new ArrayList<LtMastSmsToken>();

			Long trasid = System.currentTimeMillis();
			ltMastSmsToken.setTransactionId(trasid.toString());
			ltMastSmsToken.setSmsType("STATUS CHANGE");
			String mobileNo = "91"+ltMastUser.getMobileNumber();
			ltMastSmsToken.setSendTo(Long.parseLong(mobileNo));
			ltMastSmsToken.setSendDate(new Date());
			ltMastSmsToken.setSmsStatus("SENDING");
			ltMastSmsToken.setUserId(ltMastUser.getUserId());
			ltMastSmsTokenList.add(ltMastSmsToken);

			List<LtMastSmsToken> ltMastSmsTokenListOp = ltMastSmsTokenDao.saveall(ltMastSmsTokenList);

			if (ltMastSmsTokenListOp.isEmpty()) {
				status.setMessage("Message send failed");
				status.setCode(FAIL);

			} else {
				status = ltMastSmsTokenService.sendSms(ltMastSmsToken.getUserId(), trasid.toString());
			}

		}
		return status;

	}

	@Override
	public Status savePersonalDetails(LtUserPersonalDetails ltUserPersonalDetails) throws ServiceException {

		Status status = new Status();

		if (ltUserPersonalDetails != null) {

			LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(ltUserPersonalDetails.getUserId());

			if (ltMastUsers != null) {

				if (ltMastUsers.getMobileNumber().equalsIgnoreCase(ltUserPersonalDetails.getMobileNumber())) {

					ltMastUsers.setUserName(ltUserPersonalDetails.getUserName());
					ltMastUsers.setEmail(ltUserPersonalDetails.getEmail());
					ltMastUsers.setAlternateNo(ltUserPersonalDetails.getAlternateNo());

					ltMastUsers.setLastUpdatedBy(ltUserPersonalDetails.getUserId());
					ltMastUsers.setLastUpdateLogin(ltUserPersonalDetails.getUserId());
					ltMastUsers.setLastUpdateDate(new Date());

					ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);
					if (ltMastUsers != null) {
						status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
						status.setData(userResponce(ltMastUsers));
						return status;
					}

				} else {
					status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.mobilenoValid"));
					status.setData(userResponce(null));
					return status;
				}

			}

		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(userResponce(null));
		return status;
	}

	// @Override
	public Status saveAddressOld(LtUserAddressDetails ltUserAddressDetails) throws ServiceException {

		Status status = new Status();

		if (ltUserAddressDetails != null) {

			LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(ltUserAddressDetails.getUserId());
			if (ltMastUsers != null) {

				if (ltMastUsers.getUserName().length() > 0 && ltMastUsers.getUserName() != ""
						&& ltMastUsers.getUserName() != null) {

					ltMastUsers.setAddress(ltUserAddressDetails.getAddress());
					ltMastUsers.setLatitude(ltUserAddressDetails.getLatitude());
					ltMastUsers.setLongitude(ltUserAddressDetails.getLongitude());
					ltMastUsers.setAddressDetails(ltUserAddressDetails.getAddressDetails());

					ltMastUsers.setLastUpdatedBy(ltUserAddressDetails.getUserId());
					ltMastUsers.setLastUpdateLogin(ltUserAddressDetails.getUserId());
					ltMastUsers.setLastUpdateDate(new Date());
					ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

					if (ltMastUsers != null) {
						status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
						status.setData(userResponce(ltMastUsers));
						return status;
					}
				} else {

					status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
					status.setMessage(
							ltMastCommonMessageService.getCommonMessage("lonar.users.personaldetails.notfound"));
					status.setData(null);
					return status;
				}

			}

		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public Status saveAddress(LtUserAddressDetails ltUserAddressDetails) throws ServiceException {

		Status status = new Status();

		if (ltUserAddressDetails != null) {

			LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(ltUserAddressDetails.getUserId());
			if (ltMastUsers != null) {
				ltMastUsers.setAddress(ltUserAddressDetails.getAddress());
				ltMastUsers.setLatitude(ltUserAddressDetails.getLatitude());
				ltMastUsers.setLongitude(ltUserAddressDetails.getLongitude());
				ltMastUsers.setAddressDetails(ltUserAddressDetails.getAddressDetails());

				ltMastUsers.setLastUpdatedBy(ltUserAddressDetails.getUserId());
				ltMastUsers.setLastUpdateLogin(ltUserAddressDetails.getUserId());
				ltMastUsers.setLastUpdateDate(new Date());
				ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

				if (ltMastUsers != null) {
					status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
					status.setData(userResponce(ltMastUsers));
					return status;
				}
			}

		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public Status getPersonaldetailsById(Long userId) throws ServiceException {
		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		if (ltMastUsers != null) {
			if (ltMastUsers.getUserName() != "" && ltMastUsers.getUserName().length() > 0) {
				LtUserPersonalDetails ltUserPersonalDetails = new LtUserPersonalDetails();
				ltUserPersonalDetails.setUserName(ltMastUsers.getUserName());
				ltUserPersonalDetails.setMobileNumber(ltMastUsers.getMobileNumber());
				ltUserPersonalDetails.setAlternateNo(ltMastUsers.getAlternateNo());
				ltUserPersonalDetails.setEmail(ltMastUsers.getEmail());
				ltUserPersonalDetails.setUserId(ltMastUsers.getUserId());
				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
				status.setData(ltUserPersonalDetails);
				return status;

			}
		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	@Override
	public Status getAddressDetailsById(Long userId) throws ServiceException {
		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		if (ltMastUsers != null) {
			if (ltMastUsers.getAddress() != null && ltMastUsers.getAddress() != ""
					&& ltMastUsers.getAddress().length() > 0) {

				LtUserAddressDetails ltUserAddressDetails = new LtUserAddressDetails();
				ltUserAddressDetails.setAddress(ltMastUsers.getAddress());
				ltUserAddressDetails.setUserId(ltMastUsers.getUserId());
				ltUserAddressDetails.setLatitude(ltMastUsers.getLatitude());
				ltUserAddressDetails.setLongitude(ltMastUsers.getLongitude());
				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
				status.setData(ltUserAddressDetails);
				return status;

			} else {
				status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
				status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.addressdetails.notfound"));
				status.setData(null);
				return status;
			}

		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	@Override
	public Status saveUserOrganisationDetails(Long userId) throws ServiceException, IOException {
		Status status = new Status();

		status = getPersonaldetailsById(userId);

		if (status.getCode() == RECORD_FOUND) {

			status = getAddressDetailsById(userId);

			if (status.getCode() == RECORD_FOUND) {
				LtMastUsers ltMastUser = ltMastUsersDao.getUserById(userId);
				ltMastUser.setStatus(ACTIVE);
				status = this.update(ltMastUser);
				if (status.getCode() == UPDATE_SUCCESSFULLY) {
					status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
					status.setData(userResponce(ltMastUser));
					return status;
				}

			} else {
				status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
				status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.addressdetails.notfound"));
				status.setData(null);
				return status;
			}

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
			status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.personaldetails.notfound"));
			status.setData(null);
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public Status getAllUserByDistributorId(Long distributorId) throws ServiceException {
		Status status = new Status();

		List<LtMastUsers> ltMastUsersList = ltMastUsersDao.getAllUserByDistributorId(distributorId);
		List<LtMastUsers> userResponceList = new LinkedList<>();

		if (!ltMastUsersList.isEmpty()) {
			for (LtMastUsers ltMastUsers : ltMastUsersList) {
				ltMastUsers = userResponce(ltMastUsers);
				userResponceList.add(ltMastUsers);
			}
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(userResponceList);
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	@Override
	public Status saveRecentSearchId(Long userId, String serachId) throws ServiceException {
		Status status = new Status();

		LtMastUsers ltMastUser = ltMastUsersDao.getUserById(userId);
		System.out.println("ltMastUser"+ltMastUser);
		if (ltMastUser != null) {

			ltMastUser.setLastUpdatedBy(userId);
			ltMastUser.setLastUpdateLogin(userId);
			ltMastUser.setLastUpdateDate(DateTimeClass.getCurrentDateTime());
			System.out.println("DateTimeClass.getCurrentDateTime()");
			ltMastUser.setRecentSerachId(serachId);
			ltMastUser = ltMastUsersDao.saveLtMastUsers(ltMastUser);
			status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
			status.setData(userResponce(ltMastUser));
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public Status getAllUsersData() throws ServiceException {
		Status status = new Status();
		List<LtMastUsers> ltMastUserList = ltMastUsersDao.getAllUsersData();
		List<LtMastUsers> ltMastUserResponce = new LinkedList<LtMastUsers>();
		if (ltMastUserList != null) {

			for (LtMastUsers ltMastUsers : ltMastUserList) {
				userResponce(ltMastUsers);
				ltMastUserResponce.add(ltMastUsers);

			}
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(ltMastUserResponce);
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	@Override
	public Status getUserAllMasterDataById(Long userId) throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUser = ltMastUsersDao.getUserAllMasterDataById(userId);

		if (ltMastUser != null) {
			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.DISTRIBUTOR)
					&& ltMastUser.getStatus().equalsIgnoreCase(INPROCESS)) {

				if (ltMastUser.getDistributorId() != null) {
					//LtMastDistributors ltMastDistributors = ltMastDistributorsDao.getLtDistributorsById(ltMastUser.getDistributorId());

					if (ltMastUser.getPrimaryMobile().length() >= 10) {

						String mobileNumber = ltMastUser.getPrimaryMobile()
								.substring(ltMastUser.getPrimaryMobile().length() - 10);
						if (mobileNumber.equalsIgnoreCase(ltMastUser.getMobileNumber())) {
							ltMastUser.setStatus(ACTIVE);
							ltMastUser.setOrgStatus(ACTIVE);
						} else {
							ltMastUser.setStatus(PENDINGAPPROVAL);
							ltMastUser.setOrgStatus(PENDINGAPPROVAL);
						}
					} else {
						ltMastUser.setOrgStatus(PENDINGAPPROVAL);
					}

				}

			}

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.RETAILER)
					&& ltMastUser.getStatus().equalsIgnoreCase(INPROCESS)) {
				ltMastUser.setOrgStatus(ACTIVE);
			}
			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.SALES)
					&& ltMastUser.getStatus().equalsIgnoreCase(INPROCESS)) {
				ltMastUser.setOrgStatus(PENDINGAPPROVAL);
			}

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.ADMIN)
					&& ltMastUser.getStatus().equalsIgnoreCase(INPROCESS)) {
				ltMastUser.setOrgStatus(ACTIVE);
			}
			
			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.SALESOFFICER)) {
				ltMastUser.setOrgStatus(ltMastUser.getStatus());
			}
			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.AREAHEAD)) {
				ltMastUser.setOrgStatus(ltMastUser.getStatus());
			}

			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(userResponce(ltMastUser));
			return status;
		}

		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	@Override
	public Status getUsersList(RequestDto requestDto) throws IOException {
		Status status = new Status();
		List<LtMastUsers> list = ltMastUsersDao.getUsersList(requestDto);
		if (list != null) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(list);
		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		}
		return status;
	}

	@Override
	public Status getInactiveUsers() throws ServiceException {
		Status status = new Status();

		try {
			RestTemplate restTemplate1 = new RestTemplate();
			String callInactiveServices = env.getProperty("callInactiveServices");
			String fooResourceUrl = callInactiveServices + "user/getInactiveUsers";
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("X-API-Version", "v1.0");

			HttpEntity entity = new HttpEntity(headers);
			
			ResponseEntity<Status> response = restTemplate1.exchange(fooResourceUrl, HttpMethod.GET, entity, Status.class);
			
			//ResponseEntity<Status> response = restTemplate1.getForEntity(fooResourceUrl, Status.class);
			Status newStatusObj = response.getBody();
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(newStatusObj.getData());

		} catch (Exception e) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status uploadProfilePic(MultipartFile mFile, Long userId) throws ServiceException {
		Status status = new Status();
		String fileName = null;
		try {
			Optional<LtMastUsers> users = ltMastUsersRepository.findById(userId);
			if (users.isPresent()) {
				LtMastUsers itMastUsers = users.get();

				String fileUploadPath = env.getProperty("fileUploadPath");
				String fileDownloadPath = env.getProperty("fileDownloadPath");

				File dir = new File(fileUploadPath);
				if (!dir.exists()) {
					dir.mkdirs();
					if (!dir.isDirectory()) {
						status.setCode(NO_DIRECTIVE_EXISTS);
						status.setMessage("No Directive Exists");
						return status;
					}
				}

				fileName = mFile.getOriginalFilename();
				byte[] bytes = mFile.getBytes();

				BufferedOutputStream buffStream = new BufferedOutputStream(
						new FileOutputStream(new File(fileUploadPath + fileName)));
				buffStream.write(bytes);

				buffStream.close();

				// String imgFilePath = fileUploadPath+""+mFile.getName();
				String imgDownloadPath = fileDownloadPath + "" + fileName;

				itMastUsers.setImageName(fileName);
				itMastUsers.setImageData(imgDownloadPath);
				// itMastUsers.setImageType(mFile.get);

				itMastUsers = ltMastUsersRepository.save(itMastUsers);

				LtMastUsers profileData = new LtMastUsers();
				profileData.setUserId(itMastUsers.getUserId());
				profileData.setImageData(itMastUsers.getImageData());
				profileData.setImageName(itMastUsers.getImageName());
				profileData.setImageType(itMastUsers.getImageType());

				status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_SUCCESSFULLY);
				status.setData(profileData);

			} else {
				status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
			}

			return status;

		} catch (IOException e) {
			e.printStackTrace();
			return ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		}
	}

	@Override
	public MobileSupportedVersionResponseDto isMobileSupportedVersion(MobileSupportedVersionRequestDto requestDto) throws ServiceException {
		MobileSupportedVersionResponseDto responseDto = new MobileSupportedVersionResponseDto();
		List<LtConfigurartion> configList = ltMastUsersDao.getAllConfiguration();
		
		String latestAndroidVersion = null;
		String latestIOSVersion= null;
		
		for (Iterator iterator = configList.iterator(); iterator.hasNext();) {
			LtConfigurartion ltConfigurartion = (LtConfigurartion) iterator.next();
			
			if (ltConfigurartion.getKey().trim().equalsIgnoreCase("LatestAndroidVersion")) {
				latestAndroidVersion = ltConfigurartion.getValue();
			}
			
			if (ltConfigurartion.getKey().trim().equalsIgnoreCase("LatestIOSVersion")) {
				latestIOSVersion = ltConfigurartion.getValue();
			}
		}	
		
		boolean found = false;
		String platformString = requestDto.getPlatform().trim().toUpperCase();
		String buildString = requestDto.getBuildNumber().toString();
		if(platformString.equals("ANDROID")) {
			if (!configList.isEmpty()) {
				for (Iterator iterator = configList.iterator(); iterator.hasNext();) {
					LtConfigurartion ltConfigurartion = (LtConfigurartion) iterator.next();
					if (ltConfigurartion.getKey().trim().equalsIgnoreCase("supportedVersionAndoid")) {
					String androidSupporedVersion = ltConfigurartion.getValue();
						String[] andVersionArray = androidSupporedVersion.trim().split(",");
						System.out.println("andVersionArray :: " + andVersionArray.toString());
						List<String> list = Arrays.asList(andVersionArray);
						if(!list.contains(buildString)){
							found = true;
							responseDto.setBuildNumber(Long.parseLong(latestAndroidVersion));
							responseDto.setForceUpdate(found);
							return responseDto;
				        }
					}
				}
			}
		}else {//IOS
			if (!configList.isEmpty()) {
				for (Iterator iterator = configList.iterator(); iterator.hasNext();) {
					LtConfigurartion ltConfigurartion = (LtConfigurartion) iterator.next();
					if (ltConfigurartion.getKey().trim().equalsIgnoreCase("supportedVersionIOS")) {
					String iosSupporedVersion = ltConfigurartion.getValue();
						String[] iosVersionArray = iosSupporedVersion.trim().split(",");
						List<String> list = Arrays.asList(iosVersionArray);
						if(!list.contains(buildString)){
							found = true;
							responseDto.setBuildNumber(Long.parseLong(latestIOSVersion));
							responseDto.setForceUpdate(found);
							return responseDto;
				        }
					}
				}
			}
		}
		
		responseDto.setBuildNumber(Long.parseLong(buildString));
		responseDto.setForceUpdate(found);
		return responseDto;
		
	}

}
