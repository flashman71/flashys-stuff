package org.spottedplaid.database.constants;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * @version 1.0
 * 
 * DbConstant.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

/* ***************************************************************
Class:    DbConstant
Purpose:  Constants used by database operations
***************************************************************  */
public class DbConstant {
	
	/// Database Version
	public final static String S_DB_REV = "4";
	
	/// Application Version
	public final static String S_APP_REV = "4";
	
	/// Constants
	///   clients table
	public final static String S_CLIENT_TABLE = "CREATE TABLE `clients`" 
			                      + "( id INTEGER PRIMARY KEY AUTOINCREMENT," 
		                          + "client_name VARCHAR(255) NOT NULL,"
		                          + "description VARCHAR(500) NOT NULL )";
	
	///   Index on client name
	public final static String S_CLIENT_IDX1 = "CREATE INDEX IDX_CLIENT_CLIENTNAME ON clients(client_name)";
	
	///   creds table
	public final static String S_CREDS_TABLE = "CREATE TABLE `creds` "
		                         + "( id INTEGER PRIMARY KEY AUTOINCREMENT,"
		                         + "challenge TEXT(1000) NOT NULL,"
		                         + "response TEXT(1000) NOT NULL,"
		                         + "track_updates INT,"
		                         + "modify_date TEXT(100),"
		                         + "client_id INT)";
	
	public final static String S_LOG_TABLE = "CREATE TABLE `logs` (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT(2000), date_changed TEXT(40))";
	
	///   view for clients and creds
	public final static String S_VIEW1 = "CREATE VIEW all_clients_creds AS "
			               + "SELECT CLIENTS.ID,CLIENTS.CLIENT_NAME,CLIENTS.DESCRIPTION,"
			               + "CREDS.ID, CREDS.CHALLENGE, CREDS.RESPONSE "
			               + "FROM  CREDS LEFT OUTER JOIN CLIENTS ON CREDS.CLIENT_ID = CLIENTS.ID";	
	
	/// Version control table for application/database
	public final static String S_APP_DB_TABLE = "CREATE TABLE `app_db`" 
                                              + "( id INTEGER PRIMARY KEY AUTOINCREMENT," 
                                              + "db_rev VARCHAR(10) NOT NULL,"
                                              + "app_rev VARCHAR(10) NOT NULL)";
	
	/// View  for expiring credentials
    public final static String S_VIEW2 = "CREATE VIEW expiring_creds AS SELECT CLIENTS.CLIENT_NAME,CREDS.CHALLENGE, CREDS.MODIFY_DATE "
                                       + "FROM CREDS LEFT OUTER JOIN CLIENTS ON CREDS.CLIENT_ID = CLIENTS.ID "
                                       + "WHERE CREDS.track_updates > 0"
                                       + " ORDER BY MODIFY_DATE ASC";

	/// Database updates
	public final static String S_UPD0 = S_APP_DB_TABLE;
	
	
	public final static String S_UPD1 = "ALTER TABLE `creds` rename to `creds_bkp`";
    public final static String S_UPD2 = "INSERT INTO `creds` select id,challenge,response,0,DATE('now'),client_id from creds_bkp";
    public final static String S_UPD3 = "DROP TABLE creds_bkp";
    public final static String S_UPD4 = S_VIEW2;
    
}
