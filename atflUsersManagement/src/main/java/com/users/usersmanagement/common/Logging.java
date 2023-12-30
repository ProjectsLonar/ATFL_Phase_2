package com.users.usersmanagement.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Logging {

	static Logger logger = LoggerFactory.getLogger(Logging.class);

	public static void setRequestLog(Object obj, String methodName, String Url, String methodType) throws Exception {
		String beutifulJson = "";
		if (obj != null) {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(obj);
			beutifulJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		}
		logger.info("===========================Request begin================================================");
		logger.info("URI         :" + Url);
		logger.info("Method      :" + methodName);
		logger.info("Method Type :" + methodType);
		logger.info("Request body:" + beutifulJson);
		logger.info("==========================Request end=====================================================");
	}

	public static void setResponceLog(Object obj, String methodName, String Url, String methodType) throws Exception {

		String beutifulJson = "";
		if (obj != null) {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(obj);
			beutifulJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		}
		logger.info("===========================Responce begin================================================");
		logger.info("URI         :" + Url);
		logger.info("Method      :" + methodName);
		logger.info("Method Type :" + methodType);
		logger.info("Responce body:" + beutifulJson);
		logger.info("==========================Responce end=====================================================");
	}

}