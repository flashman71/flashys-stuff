package ebapps.dish.preview.database;

import ebapps.dish.preview.configuration.Values;

public class DbConstants {

	public final static String S_DB_NAME = "team_peer_review";
	public final static String S_SECURITY_QUERY = "select * from vw_security where login = ?";
	public final static String S_GEN_LIST_QUERY = "select * from vw_gen_lists where list_title = ?";
	public final static String S_GROUP_QUERY = "select group_name from user_groups";
	
	public final static String S_ADD_USER = "insert into users(login,email_addr,full_name,security_group,active,updated_by,create_date,update_date) values(?,?,?,?,?,?,Now(),Now())";
	public final static String S_ADD_GROUP = "insert into user_groups(group_name) values(?)";
    public final static String S_ADD_REVIEW = "insert into peer_review(artifact_type,artifact_id,status,num_iterations,review_group,created_by,updated_by,create_date,update_date) values(?,?,?,?,?,?,?,Now(),Now()"
    		+ ")";
    public final static String S_UPD_REVIEW = "update peer_review set status = ?, review_condition = ?, updated_by = ?, review_group = ?, update_date = Now() where id = ?";
    
    public final static String S_ADD_PEER_REVIEW_NOTE = "insert into peer_review_notes(peer_review_id,user_id,review_note,create_date) values(?,?,?,Now())";
    public final static String S_ADD_PEER_REVIEW_DEFECT = "insert into peer_review_defects(peer_review_id,defect_type,defect_description,severity,status,defect_condition,created_by,update_by,create_date,update_date) values(?,?,?,?,?,?,?,?,Now(),Now())";
    public final static String S_ADD_PEER_REVIEW_DEFECT_NOTE = "insert into peer_review_defect_notes(defect_id,defect_note,created_by,create_date) values (?,?,?,Now())";
    public final static String S_UPD_PEER_REVIEW_DEFECT = "update peer_review_defects set defect_type = ?, severity = ?, status = ?, defect_condition = ?, update_by = ?, update_date = Now() where id = ?";
    
    public final static String S_UPD_USER_DATA = "update users set email_addr = ?, full_name = ?, security_group = ?, active = ?, updated_by = ?, update_date = Now() where id = ?";
    public final static String S_RESET_USER_PASSWORD = "set password for ?@? = PASSWORD(?)";
    public final static String S_ADD_USER_TO_GROUPS = "{call ADD_USER_TO_GROUPS(?,?,?)}";
    public final static String S_REMOVE_USER_FROM_GROUPS = "{call REMOVE_USER_FROM_GROUPS(?,?,?)}";
    
    public final static String S_GET_ALL_USERS = "select login from users";
    public final static String S_GET_USER_BY_LOGIN = "select id,email_addr,full_name,security_group,active from users where login = ?";
    public final static String S_GET_AVAILABLE_USERS = "select LOGIN from users where active = ?";
    public final static String S_GET_ASSIGNED_USERS = "SELECT LOGIN FROM vw_asg_users where group_name = ? and login is not null";
    public final static String S_GET_USER_ASSIGNED_TO_GROUP = "SELECT LOGIN FROM vw_asg_users where login = ? and group_name = ?";
    public final static String S_GET_AVAILABLE_USERS2 = "select u.login from users u where u.id not in (select user_id from vw_groups1 where  group_name = ?) and u.login is not null";
    
    public final static String S_GET_PEER_REVIEW_HISTORY = "select login,full_name,review_note,create_date from vw_peer_review_history where peer_review_id = ?";
    public final static String S_GET_DEFECT_NOTES = "select login,defect_note,create_date from vw_defect_notes where defect_id = ?";

    public final static String S_VALID_TRANSITIONS = "select transition_to from vw_transitions where list_title = ? and transition_from = ? and transition_to = ?";
    
    public final static String S_GET_REVIEWS_BY_ID = "select pr_id,artifact_type,artifact_id,status,review_condition,review_group,num_iterations,creator from vw_review_by_id where pr_id = ?";
    public final static String S_GET_REVIEWS_BY_GROUP = "select pr_id,artifact_type,artifact_id,status,review_condition,review_group,num_iterations,creator from vw_review_groups where review_group = ?";
    public final static String S_GET_REVIEWS_BY_ASG_GROUP = "select pr_id,artifact_type,artifact_id,status,review_condition,review_group,num_iterations,creator from vw_asg_groups where user_id = " + Values.getId(); 
    
    public final static String S_GET_ASSOCIATED_DEFECTS = "select id,defect_type,defect_description,severity,status,defect_condition from peer_review_defects where peer_review_id = ?";
    public final static String S_GET_OPEN_DEFECTS = "select * from peer_review_defects where peer_review_id = ? and defect_condition = 'Open'";
    
    public final static String S_GET_REVIEW_DATA = "select * from vw_all_review_data";
    
    public final static String S_REVIEW_REPORT = "select * from vw_review_rpt";
    public final static String S_DEFECT_REPORT = "select * from vw_defect_report";
    
    
}
