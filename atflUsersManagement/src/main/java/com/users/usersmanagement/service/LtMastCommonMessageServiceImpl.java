package com.users.usersmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.Status;

@Service
@PropertySource(value = "classpath:queries/messages.properties", ignoreResourceNotFound = true)
public class LtMastCommonMessageServiceImpl implements LtMastCommonMessageService, CodeMaster {


	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	private Environment env;

	@Transactional
	@Override
	public Status getCodeAndMessage(Integer code) {
		Status status = new Status();
		try {
			status.setCode(code);
			if (status.getMessage() == null) {
				if (code == UPDATE_SUCCESSFULLY) {
					status.setCode(UPDATE_SUCCESSFULLY);
					status.setMessage(env.getProperty("lonar.users.recordupdated"));
					// status.setMessage("Record Updated Successfully");
				} else if (code == UPDATE_FAIL) {
					status.setCode(UPDATE_FAIL);
					status.setMessage(env.getProperty("lonar.users.recordupdatedfailed"));
					// status.setMessage("Record Update Failed");
				} else if (code == DELETE_FAIL) {
					status.setCode(DELETE_FAIL);
					status.setMessage(env.getProperty("lonar.users.recorddeletedfailed"));
					// status.setMessage("Record Delete Failed");
				} else if (code == DELETE_SUCCESSFULLY) {
					status.setCode(DELETE_SUCCESSFULLY);
					status.setMessage(env.getProperty("lonar.users.recorddeleted"));
					// status.setMessage("Record Deleted Successfully");
				} else if (code == INSERT_SUCCESSFULLY) {
					status.setCode(INSERT_SUCCESSFULLY);
					status.setMessage(env.getProperty("lonar.users.recordsave"));
					// status.setMessage("Record Inserted Successfully");
				} else if (code == INSERT_FAIL) {
					status.setCode(INSERT_FAIL);
					status.setMessage(env.getProperty("lonar.users.recordsavefailed"));
					// status.setMessage("Record Insert Failed");
				} else if (code == RECORD_FOUND) {
					status.setCode(RECORD_FOUND);
					status.setMessage(env.getProperty("lonar.users.recordfound"));
					// status.setMessage("Record Found Successfully");
				} else if (code == RECORD_NOT_FOUND) {
					status.setCode(RECORD_NOT_FOUND);
					status.setMessage(env.getProperty("lonar.users.recordnotfound"));
					// status.setMessage("Record Not Found");
				}
			}
		} catch (Exception e) {
			status.setCode(EXCEPTION);
			status.setMessage("Error in finding message! The action was unsuccessful");

		}
		return status;
	}

	@Override
	public String getCommonMessage(String message) {
		return env.getProperty(message);
	}
}
