package org.spottedplaid.config;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * 
 * Pwdtypes.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

/* ***************************************************************
Class:    Pwdtypes
Purpose:  Constants defined for use with other classes in the Password Saver program
***************************************************************  */
public class Pwdtypes {

	/// Application Version
	public final static String S_VERSION = "Version 3.4";
	
	/// Encryption method
	public final static String S_METHOD = "Blowfish";
	
	/// Constant for client table
	public final static String S_CLIENT_TYPE = "clients";
	
	/// Constant for credentials table
	public final static String S_CREDS_TYPE = "creds";
	
	/// Constant to indicate all records
	public final static String S_ALL_TYPE = "all_clients_creds";
	
	/// Constant for log table
	public final static String S_LOG_TYPE = "logs";

	/// Constant for expiration report 
	public final static String S_EXP_RPT = "expiring_creds";
	
	/// Constant for deleting creds based on client
	public final static String S_DEL_CREDS = "creds_del";
	
	/// Constant for creds updates
	public final static String S_CREDS_UPD = "creds_upd";
	
	/// Constant for default keystore filename
	private static String sKeystore = "keystore.kps";
	
	/// Constant for default database filename
	private static String sDbfilename = "jpwdsaver.dbx";
	
	/// Constant for full report
	private static String S_REPORT_FULLNAME = "";
	/**
	 * setKstore - Setter method for Keystore, not used at this time
	 * @param _sKstore
	 */
	public static void setKstore(String _sKstore)
	{
		sKeystore = _sKstore;
	}
	
	
	/**
	 * getKstore - Getter method for Keystore, not used at this time
	 * @return
	 */
	public static String getKstore()
	{
		return sKeystore; 
	}
	
	/**
	 * setDbfile - Setter method for database filename string
	 * @param _sDbfile
	 */
	public static void setDbfile(String _sDbfile)
	{
		sDbfilename = _sDbfile;
	}
	
	/**
	 * getDbfile - Getter method for database filename string
	 * @return
	 */
	public static String getDbfile()
	{
		return sDbfilename;
	}
}
