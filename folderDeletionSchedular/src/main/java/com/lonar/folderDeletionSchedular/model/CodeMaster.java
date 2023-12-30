package com.lonar.folderDeletionSchedular.model;

public interface CodeMaster {
	
	public static final int DATA_ALREADY_EXISTS = 1001;
	
	public static final int INSERT_SUCCESSFULLY = 1002; // addsucessfully
	public static final int INSERT_FAIL = 1003;
	
	public static final int UPDATE_SUCCESSFULLY = 1004; // updatesucessfully
	public static final int UPDATE_FAIL = 1005;
	
	public static final int DELETE_SUCCESSFULLY = 1006; // noresult
	public static final int DELETE_FAIL = 1007; // invalidid
	
	public static final int ENTITY_CANNOT_DELETE = 1008; // cannotdelete
	
	public static final int INTERNAL_SERVER_ERROR = 1009; // internalservererror
	
	public static final int LINK_EXPIRED = 1010; // login.linkexpired
	
	public static final int INPUT_IS_EMPTY = 1011; // inputempty
	
	public static final int ENTITY_NOT_FOUND = 1012; // cannotdelete
	
	public static final int FILE_UPLOADED_SUCESSFULLY = 1013; 
	
	public static final int FILE_UPLOAD_FAIL = 1014; 
	
	public static final int NO_DIRECTIVE_EXISTS = 1015; 
	
	public static final int SUBMIT_FOR_APPROVAL = 1016; 
	
	public static final int RECORD_FOUND = 1017;
	
	public static final int RECORD_NOT_FOUND = 1018;
	
	
	public static final int EXCEPTION = 1100; // Exception
	
	public static final int SUCCESS = 1101; // Exception
	
	public static final int FAIL = 1102;
	
	public static final String ACTIVE="ACTIVE";
	
	public static final String INACTIVE="INACTIVE";
	
	public static final String INPROCESS="INPROCESS";
	
	public static final String SUCCESS_LOGIN="SUCCESS";
	
	public static final String PENDING = "PENDING";
	
	public static final String ADMIN = "ADMIN";
	
}
