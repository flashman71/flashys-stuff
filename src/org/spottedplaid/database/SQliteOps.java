package org.spottedplaid.database;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * @version 1.0
 * 
 * SQliteOps.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.spottedplaid.config.Pwdtypes;
import org.spottedplaid.database.constants.DbConstant;

/* ***************************************************************
Class:    SQliteOps
Purpose:  Methods used for database actions
          including - database connection, initialization, insert, update, delete, lookup
***************************************************************  */
public class SQliteOps {
    
	/// Class variables
	private Connection dbConn  = null;
	private String sDbFilename = null;
	private File fileDb        = null;
	private int iNumRecords    = 0;
	

	/**
	 *  Constructor
	 *    Takes a filename as the input and checks to see if it exists
	 *    Will attempt to create if it does not exist
	 */
	public SQliteOps(String _sDbFilename)
	{
	   try {
		Class.forName("org.sqlite.JDBC");
		sDbFilename = _sDbFilename;
		fileDb = new File(sDbFilename);
	   if (!fileDb.exists())
	   {
		   initDb();
	   }
	   else
	   {
			/// Check versions and update if necessary
			if (dbVersionsMatch() < 0)
			{
				updateVersion();
			}

	   }
	} catch (ClassNotFoundException e) {
		System.out.println("EXCEPTION->SQliteOps ClassNotFoundException [" + e.getMessage() + "]");
	}
	}
	   
	/** 
	 *  initDb
	 *     Initializes database, executed if database file does not exist
	 */  
	public void initDb()
	{
				
	          	connectToDb();	
	          	Statement stmtInit;
				try {
					  stmtInit = dbConn.createStatement();
			          stmtInit.executeUpdate(DbConstant.S_CLIENT_TABLE);		  
		              stmtInit.executeUpdate(DbConstant.S_CREDS_TABLE);
		              stmtInit.executeUpdate(DbConstant.S_CLIENT_IDX1);
		              stmtInit.executeUpdate(DbConstant.S_VIEW1);
		              stmtInit.executeUpdate(DbConstant.S_LOG_TABLE);
  	                  stmtInit.executeUpdate(DbConstant.S_APP_DB_TABLE);		               
   		              stmtInit.executeUpdate(DbConstant.S_VIEW2);		               
		   
		   /// Update version control table
		   String sUpd = "insert into app_db(db_rev,app_rev) values(\"" + DbConstant.S_DB_REV + "\", \"" + DbConstant.S_APP_REV + "\")";
			           stmtInit.execute(sUpd);   
			           
				} catch (SQLException e) {
			        System.out.println("initDb SQLException [" + e.getMessage() + "]");
					e.printStackTrace();
				}
	}

	/**
	 *  connectToDb
	 *    Establish database connection if object is not set
	 */
	public void connectToDb()
	{
		String sConn = "jdbc:sqlite:" + sDbFilename;
		
		try {
		if (dbConn==null || dbConn.isClosed())
		{
		
			dbConn = DriverManager.getConnection(sConn);
		}
		} catch (SQLException e) {
			System.out.println("EXCEPTION->connectToDb SQLException [" + e.getMessage() + "]");
		}
		
	}
	
	/**
	 *  closeDb
	 *    Close database connection if set
	 */
	public void closeDb()
	{
	  if (dbConn!=null)
	  { 
       System.out.println("closeDb - Closing database");
		 try {
			   dbConn.close();
			   dbConn = null;
		  	 } catch (SQLException e) {
				System.out.println("closeDb SQLException [" + e.getMessage() + "]");
				e.printStackTrace();
			}
		
	  }
	}
	
	/**
	 * getKey - Method to get the key value from a database table - not implemented
	 * @return
	 */
	public Blob getKey()
	{
		connectToDb();
		String    sGetkey = "select key from keys wheren active = 1";
		Blob    blReturn = null;
		Statement stmtKey = null;
		try {
			  stmtKey = dbConn.createStatement();
			  stmtKey.execute(sGetkey);
			  ResultSet rs = stmtKey.getResultSet();
			   while (rs.next())
			   {
				   blReturn = rs.getBlob("key");
			   }
		}
		catch (SQLException e)
		{
			System.out.println("EXCEPTION->getKey, SQLException [" + e.getMessage() + "]");
			return null;
		}
		finally 
		{
			closeDb();
		}
		
		return blReturn;
	}
	
	
	/** 
	 * insertRecord
	 *   @param - DbRecord Object
	 *   @return - int value
	 *               0 - completed, did not throw exception, but did not create record
	 *              -1 - Exception encountered
	 *              >0 - completed, records inserted.
	 * 
	 */
	public int insertRecord(DbRecord dbRecord)
	{

		String sInsert = null; 
	    Statement stmtIns = null;
	    int iReturn = 0;
	    
		if (dbRecord.getType().equals(Pwdtypes.S_CLIENT_TYPE))
		{
		  sInsert = "insert into clients(client_name,description) values(\"" + dbRecord.getClientName() + "\",\"" + dbRecord.getClientDesc() + "\")";      		   
		}
		else if (dbRecord.getType().equals(Pwdtypes.S_CREDS_TYPE))
		{
 		  sInsert = "insert into creds(challenge,response,track_updates,modify_date,client_id) values(\"" + dbRecord.getChallenge() + "\",\"" + dbRecord.getResponse() + "\",\"" + dbRecord.getTrack() + "\",\"" + dbRecord.getModifyDate() + "\"," + dbRecord.getClientId() + ")";
		}
		else if (dbRecord.getType().equals(Pwdtypes.S_LOG_TYPE))
		{
		  sInsert = "insert into logs(message, date_changed) values (\"" + dbRecord.getLog() + "\",\"" + dbRecord.getModifyDate() + "\")";
		}
		else 
		{
			dbRecord.setResult("insertRecord, unknown type [" + dbRecord.getType() + "]");
			System.out.println("insertRecord, unknown type [" + dbRecord.getType() + "]");
			  return -1;
		}
		
		try {
			connectToDb();
			stmtIns = dbConn.createStatement();

			stmtIns.execute(sInsert);
			ResultSet rs = stmtIns.getGeneratedKeys();
			 while (rs.next())
			 {
				 iReturn = rs.getInt("last_insert_rowid()");
				 return iReturn; 
				  
			 }
		} catch (SQLException e) {
			dbRecord.setResult("insertRecord, SQLException getting keys [" + e.getMessage() + "]");
			System.out.println("insertRecord, SQLException getting keys [" + e.getMessage() + "]");
			e.printStackTrace();
		}
		catch (Exception e) {
			try {
				System.out.println("insertRecord, Exception [" + e.getMessage() + "], stmt [" + stmtIns.toString() + "], dbConn [" + dbConn.getCatalog() + "]");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return iReturn;
	}

	/** 
	 * updateRecord
	 *  @param - DbRecord Object
	 *  @return - int value
	 *             0 - successful update
	 *            -1 - Unknown type
	 *            -2 - Exception executing update sql
	 * 
	 */
	public int updateRecord(DbRecord dbRec)
	{
	  connectToDb();
	  Statement stmtUpd = null;
	  String sUpd = null;

	  if (dbRec.getType().equals(Pwdtypes.S_CLIENT_TYPE))
	  {
   	    sUpd = "update clients set client_name = \"" + dbRec.getClientName() + "\", description = \"" + dbRec.getClientDesc() + "\" where id = " + dbRec.getClientId();
		  
	  }
	  else if (dbRec.getType().equals(Pwdtypes.S_CREDS_TYPE))
	  {
		sUpd = "update creds set challenge = \"" + dbRec.getChallenge() + "\", response = \"" + dbRec.getResponse() + "\", track_updates = \"" + dbRec.getTrack() + "\", modify_date = \"" + dbRec.getModifyDate() + "\" where id = " + dbRec.getCredId() + " and client_id = " + dbRec.getClientId();		  
	  }
	  else if (dbRec.getType().equals(Pwdtypes.S_CREDS_UPD))
	  {
		sUpd = "update creds set response = \"" + dbRec.getResponse() + "\" , track_updates = \"" + dbRec.getTrack() + "\", modify_date = \"" + dbRec.getModifyDate() + "\" where id = " + dbRec.getClientId();
	  }
	  else
	  {
		  dbRec.setResult("updateRecord, unknown type [" + dbRec.getType() + "]");
		  return -1;
	  }
	  
	  try {
		stmtUpd = dbConn.createStatement();
		stmtUpd.execute(sUpd);
		  return 0;
	} catch (SQLException e) {
		dbRec.setResult("updateRecord, SQLException [" + e.getMessage() + "]");
		e.printStackTrace();
		 return -2;
	}
	  
	}
	
	/** 
	 * deleteRecord
	 *  @param - DbRecord
	 *  @return - int
	 *              0 = success
	 *             -1 = failed
	 * 
	 */
	public int deleteRecord(DbRecord dbRec)
	{
		Statement stmtDel = null;
		String    sDel    = null;

		
		if (dbRec.getType().equals("clients"))
		{
			DbRecord dbRecTemp = new DbRecord();
			dbRecTemp.setType(Pwdtypes.S_DEL_CREDS);
			dbRecTemp.setClientId(dbRec.getClientId());
			if (dbRec.getDelCreds() == 0)
			{
				 ArrayList<String> arrL = new ArrayList<String>();
				 arrL = getRecords(dbRecTemp);
				  if (arrL.size() > 0)
				  {
					  dbRec.setResult("deleteRecord Failed - There are associated credentials records, please delete first or check 'Delete Associated Records'");
                        return -1; 
				  }
				  
			}
			else
			{
			  deleteRecord(dbRecTemp);	
			}
			
		   	sDel = "delete from clients where id = " + dbRec.getClientId();
		}
		else if (dbRec.getType().equals("creds"))
		{
			sDel = "delete from creds where id = " + dbRec.getCredId();
		}	
		else if (dbRec.getType().equals(Pwdtypes.S_DEL_CREDS))
		{
			sDel = "delete from creds where client_id = " + dbRec.getClientId();
		}
		else
		{
			dbRec.setResult("updateRecord, unknown type [" + dbRec.getType() + "]");
			 return -1;
		}
		
		try {
			connectToDb();
		stmtDel = dbConn.createStatement();
		stmtDel.execute(sDel);
		}
		catch (SQLException e)
		{
			dbRec.setResult("deleteRecord, SQLException [" + e.getMessage() + "]");
		    return -2;
		}
		finally
		{
			closeDb();
		}
		return 0;
	}
	
	/**
	 * getRecords - Fetches records from database using type defined by DbRecord
	 *  @param dbRec - Defines record type, and parameters for query
	 *  @return - ArrayList<String> of retrieved records
	 */
	public ArrayList<String> getRecords(DbRecord dbRec)
	{
		ResultSet rs      = null;
		String    sQry    = null;
		String    sClause = " where ";
		String    sData   = "";
		Statement stmtData = null;
		ArrayList<String> arrList = new ArrayList<String>();
		
		connectToDb();
		
		if (dbRec.getType().equals(Pwdtypes.S_CLIENT_TYPE))
		{
			sQry = "select * from " + dbRec.getType();
		
			if (!dbRec.getClientName().equals("")){
				sQry += sClause + " client_name like \"%" + dbRec.getClientName() + "%\"";
				sClause = " and ";
			}
			if (!dbRec.getClientDesc().equals(""))
			{
				sQry += sClause + " description like \"%" + dbRec.getClientDesc() + "%\"";
			}
		} 
		else if (dbRec.getType().equals(Pwdtypes.S_EXP_RPT))
		{
			sQry = "select * from " + dbRec.getType();
		}
		else if (dbRec.getType().equals(Pwdtypes.S_CREDS_TYPE))
		{
			if (dbRec.getClientId() == -1)
			{
				sQry = "select * from " + dbRec.getType();
			}
			else
			{
			    sQry = "select * from " + dbRec.getType() + " where client_id = " + dbRec.getClientId();
			}
		}
		else if (dbRec.getType().equals(Pwdtypes.S_LOG_TYPE))
		{
			sQry = "select * from " + dbRec.getType() + " order by date_changed desc";
		}
		else if (dbRec.getType().equals(Pwdtypes.S_ALL_TYPE))
		{
			sQry = "select * from " + dbRec.getType() + " order by CLIENT_NAME ASC";
		}
		

		try {
			  stmtData = dbConn.createStatement();
			  stmtData.execute(sQry);
			   rs = stmtData.getResultSet();
			   while (rs.next())
			   {
		         /// S_EXP_RPT - 1st field is string, for all others 1st field is int
				   if (dbRec.getType().equals(Pwdtypes.S_EXP_RPT))
				   {
					   sData = rs.getString(1) + "|" + rs.getString(2) + "|" +  rs.getString(3);
				   }
				   /// S_ALL_TYPE will get all URLs/Applications and Credential challenge/response values
				   else if (dbRec.getType().equals(Pwdtypes.S_ALL_TYPE))
				   {
					   sData = rs.getInt(1) + "|" + rs.getString(2) + "|" +  rs.getString(3) + "|" +  rs.getString(4) + "|" +  rs.getString(5) + "|" +  rs.getString(6);  
				   }
				   else
				   {
			         sData = rs.getInt(1) + "|" + rs.getString(2) + "|" +  rs.getString(3);
				   }

				/// S_CREDS_TYPE table has 6 fields, all others 3
			     if (dbRec.getType().equals(Pwdtypes.S_CREDS_TYPE))
			     {
			    	 sData = sData + "|" + rs.getString(4) + "|" + rs.getString(5) + "|" + rs.getInt(6);
			     }
				   arrList.add(sData);
			   }
			   
		}
		catch (SQLException e)
		{
			System.out.println("EXCEPTION->getRecords SQLException [" + e.getMessage() + "], sql statement [" + sQry + "]");
		}
		finally
		{
			closeDb();
		}		
		
		return arrList;
	}
	
	/**
	 * getNumRecords - Return the number of records returned from last query - not implemented
	 *  @return
	 */
	public int getNumRecords()
	{
		return this.iNumRecords;
	}
	
	 /**
	    * dbVersionsMatch
	    *  Compares the current database version to see if an upgrade is required
	    * @return - integer value, 0 if the versions match, otherwise < 0
	    */
    public int dbVersionsMatch()
	 {
		   String sSql = "select db_rev,app_rev from app_db";
		   int iReturn = -1;
		   try {
			    connectToDb();
			    Statement stmtQry = dbConn.createStatement();
			    stmtQry.execute(sSql);
			    ResultSet rsQry = stmtQry.getResultSet();
			     while (rsQry.next())
			     {
			    	 String sDbRev = rsQry.getString(1);
			    	  if (sDbRev!=null && sDbRev.equals(DbConstant.S_DB_REV))
			    	  {
			    		  iReturn = 0;
			    	  }
			    	  else
			    	  {
			    		  iReturn = -1;
			    	  }
			     }
		   }
		   catch (SQLException se)
		   {
			   System.out.println("dbVersionsMatch SQLException [" + se.getMessage() + "]");
			   se.printStackTrace();
			   iReturn = -2;
		   }
		   return iReturn;
	   }
	   
	   /**
	    * updateVersion
	    *  Executes statements to update application to the next version
	    * @return
	    */
	   private int updateVersion()
	   {
		  try {
				connectToDb();	
	          	Statement stmtUpd;
					  stmtUpd = dbConn.createStatement();
		               stmtUpd.executeUpdate(DbConstant.S_UPD0);
		               stmtUpd.executeUpdate(DbConstant.S_UPD1);			  
					   stmtUpd.executeUpdate(DbConstant.S_CREDS_TABLE);
		               stmtUpd.executeUpdate(DbConstant.S_UPD2);		               
		               stmtUpd.executeUpdate(DbConstant.S_UPD3);		           
		               stmtUpd.executeUpdate(DbConstant.S_UPD4);

		           String sUpd = "insert into app_db(db_rev,app_rev) values(\"" + DbConstant.S_DB_REV + "\", \"" + DbConstant.S_APP_REV + "\")";
		           stmtUpd.execute(sUpd);
		  }
		  catch (SQLException se)
		  {
			  System.out.println("updateVersion SQLException [" + se.getMessage() + "]");
			   return -1;
		  }
		   
		   return 1;
	   }

}
