package com.atflMasterManagement.masterservice.servcies;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;


@Service
public  class LtMastCommonMessageServiceImpl implements LtMastCommonMessageService,CodeMaster
{

	private  Map<Integer,String> messages;
	
	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;
	
	@Transactional
	@Override
	public Status getCodeAndMessage(Integer code) 
	{
		Status status=new Status();
		try{
		status.setCode(code);
		//status.setMessage(ResourceServerConfiguration.messages.get(code));
		if(status.getMessage()==null)
		{
			if(code==UPDATE_SUCCESSFULLY) {
				status.setCode(UPDATE_SUCCESSFULLY);
				status.setMessage("Record Updated Successfully");
			}else if(code==UPDATE_FAIL) {
				status.setCode(UPDATE_FAIL);
				status.setMessage("Record Update Failed");
			}else if(code==DELETE_FAIL) {
				status.setCode(DELETE_FAIL);
				status.setMessage("Record Delete Failed");
			}else if(code==DELETE_SUCCESSFULLY) {
				status.setCode(DELETE_SUCCESSFULLY);
				status.setMessage("Record Deleted Successfully");
			}else if(code==INSERT_SUCCESSFULLY) {
				status.setCode(INSERT_SUCCESSFULLY);
				status.setMessage("Record Inserted Successfully");
			}else if(code==INSERT_FAIL) {
				status.setCode(INSERT_FAIL);
				status.setMessage("Record Insert Failed");
			}else if (code == RECORD_FOUND) {
				status.setCode(RECORD_FOUND);
				 status.setMessage("Record Found Successfully");
			} else if (code == RECORD_NOT_FOUND) {
				status.setCode(RECORD_NOT_FOUND);
				 status.setMessage("Record Not Found");
			}
		}
		}catch(Exception e)
		{
			status.setCode(EXCEPTION);
			status.setMessage("Error in finding message! The action was unsuccessful");
			
		}
		return status;
	}
}
