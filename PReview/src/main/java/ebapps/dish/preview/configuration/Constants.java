package ebapps.dish.preview.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @author gary
 *  Constants used by other classes.  This includes display messages, property file name, and default values.
 *  ANY values displayed to the user should be set here.
 */
public class Constants {

	public final static String S_APP_VERSION = "Application Version: 0.9.5";
    public final static String S_DB_VERSION  = "Database Revision: 1.7";
    public final static String S_BUILD_DATE  = "09/24/2014 09:45:00 MST";
	
	public final static String S_PROPERTIES_FILE = "../../cfg/PeerReview.properties";
	
	public final static String S_LOGIN_FAILURE = "Username or Password is invalid, or database is unavailable";
	public final static String S_CONNECTION_FAILURE = "Database connection failed.  Please check the database and server.";
	
	public final static String S_PROPERTIES_FILE_NOT_FOUND = "Properties file not found.  Program will exit";
	public final static String S_PROPERTIES_FILE_IO_ERROR = "Error reading properties file.  Program will exit";
	
	public final static String S_DB_HOST_OR_NAME_MISSING = "DB_HOST and DB_NAME are required in the properties file.  Program will exit";
	
	public final static String S_DEFAULT_VALUE_MESSAGE = "Please select a value for ";
	public final static String S_INVALID_CREATE_STATUS = "For new Review only New and Assigned are valid status values";
	public final static String S_INVALID_GROUP = "A valid group must be selected for Assigned status";
	public final static String S_INVALID_ARTIFACT_ID = "Artifact Number must be numeric";
	
	public final static String S_INVALID_USER_LOGIN = "Login is a required field";
	public final static String S_INVALID_USER_FULLNAME = "Full name of the user is required";
	public final static String S_INVALID_USER_EMAIL = "Email address is required";
	public final static String S_INVALID_USER_PASSWORD = "Password is required";
	
	public final static String S_INVALID_TRANSITION = "This transition is not permitted";
	
	public final static String S_NO_REVIEW_SELECTED = "No review selected.  Please highlight a row in the table then click Process Review";
	public final static String S_NO_DATA_RETURNED = "No records found for query";
	
	public final static String S_NO_DEFECT_DESCRIPTION = "Defect Description is required";
	
	public final static String S_INVALID_FILTER_VALUE = "Filter value must be populated";
	public final static String S_DEFECT_NOTE_INSERT_FAILED = "Unable to add notes to defect";
	
	public final static String S_OPEN_DEFECTS = "Review cannot be closed, there are open defects";
	
	public final static String S_ADMIN_USER = "pr_admin";
	
	public final static String S_OBSERVE_ONLY_PRIV = "Observer";
	
	public final static String S_DEFAULT_LIST_VALUE = "Select";
	public final static String S_DEFAULT_REV_CONDITION = "None";
	
	public final static int I_DEFAULT_LIST_VALUE = 0;
	
	public final static String S_REVIEW_INSERTED = "Add Review Successful.  ID: ";
	public final static String S_DEFECT_INSERTED = "Created Defect ID: ";
	
	public final static String S_TRANS_REQUIRES_GROUP = "Assigned";
	
	public final static String S_QUERY_BY_ID = "Review ID";
	public final static String S_QUERY_BY_GROUP = "Group Name";
	public final static String S_QUERY_BY_ASG_GROUP = "Assigned Groups";
	
	public final static String S_PEER_REVIEW_STATUS_LIST = "peer_review_status";
	public final static String S_PEER_REVIEW_ARTIFACT_TYPE_LIST = "artifact_types";
	public final static String S_PEER_REVIEW_CONDITION_LIST = "peer_review_condition";
	
	public final static String S_DEFECT_STATUS_LIST = "defect_status";
	public final static String S_DEFECT_CONDITION_LIST = "defect_condition";
	public final static String S_DEFECT_SEVERITY_LIST = "defect_severity";
	public final static String S_DEFECT_TYPE_LIST = "defect_type";
	
	public final static String S_REPORT_OUTPUT = "report_output";
	public final static String S_REPORT_OUTPUT_TYPE = "report_output_type";
	public final static String S_REPORT_CREATED_PARAM = "report_date_param";
	
	public final static String S_OUTPUT_REQUIRES_FILENAME = "File";
		
	public final static String S_PEER_REVIEW_SUCCESS_CONDITION = "Pass";
	public final static String S_DEFECT_OPEN_VALUE = "Open";
	public final static String S_DEFECT_CLOSED_VALUE = "Closed";
	public final static String S_DEFECT_INVALID_STATUS = "Defect cannot be closed, Status is invalid";
	public final static String[] ARR_DEFECT_OPEN_STATUS_VALUES = {"New","Researching", "In Dev"};
	public final static String[] ARR_REVIEW_CREATOR_ALLOWED_STATUS = {"New", "Failed"};
	
	public final static String S_HISTORY_BASE = "---------- BEGIN NOTE -------------------";
	public final static String S_HISTORY_END =  "------------ END NOTE--------------------";
	
	public final static String S_AVAILABLE_USERS_TYPE = "Available";
	public final static String S_ASSIGNED_USERS_TYPE = "Assigned";
	public final static String S_ALL_USERS_TYPE = "All";
	public final static String S_GET_USER_BY_LOGIN = "USER_BY_LOGIN";
	
	public final static String S_PROC_SUCCESS = "SUCCESS";
	public final static String S_PROC_EXCEPTION = "EXCEPTION";
	public final static String S_GROUP_ADD_FAILED_MESSAGE = "Unable to add user to specified group.  Please see the log for more details";
	public final static String S_GROUP_DELETE_FAILED_MESSAGE = "Unable to remove user from specified group.  Please see the log for more details";
	public final static String S_MISSING_PASSWORD = "Cannot change status.  Password is missing";
	public final static String S_ENABLE_USER_FAILED = "Enable user failed.  Please see log for more details";
	public final static String S_DISABLE_USER_FAILED = "Disable user failed.  Please see log for more details";
	public final static String S_ENABLE_USER_SUCCESS = "Enabled User";
	public final static String S_DISABLE_USER_SUCCESS = "Disabled User";
	
	public final static String S_MISSING_DEFECT_NOTE = "Note text is required to add a defect note";
	public final static String S_MISSING_DEFECT = "Defect must be selected before a note can be added";
	
	public final static String S_PR_REPORT = "Peer Review Report";
	public final static String S_DEFECT_REPORT = "Defect Report";
	public final static String S_PR_DEFECT_REPORT = "Peer Review/Defect Report";
	
	public final static String S_YES = "Y";
	
}
