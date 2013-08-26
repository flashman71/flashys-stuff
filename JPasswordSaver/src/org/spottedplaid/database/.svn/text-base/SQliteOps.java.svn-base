package org.spottedplaid.database;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
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

import org.spottedplaid.config.Pwdtypes;;

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
	
	/// Constants
	///   clients table
	private final String S_CLIENT_TABLE = "CREATE TABLE `clients`" 
			                      + "( id INTEGER PRIMARY KEY AUTOINCREMENT," 
		                          + "client_name VARCHAR(255) NOT NULL,"
		                          + "description VARCHAR(500) NOT NULL )";
	
	///   Index on client name
	private final String S_CLIENT_IDX1 = "CREATE INDEX IDX_CLIENT_CLIENTNAME ON clients(client_name)";
	
	///   creds table
	private final String S_CREDS_TABLE = "CREATE TABLE `creds` "
		                         + "( id INTEGER PRIMARY KEY AUTOINCREMENT,"
		                         + "challenge TEXT(1000) NOT NULL,"
		                         + "response TEXT(1000) NOT NULL,"
		                         + "client_id INT)";
	
	private final String S_KEY_TABLE = "CREATE TABLE `keys` (id INTEGER PRIMARY KEY AUTOINCREMENT, key BLOB, active int)";
	
	private final String S_LOG_TABLE = "CREATE TABLE `logs` (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT(2000), date_changed TEXT(40))";
	
	///   view for clients and creds
	private final String S_VIEW1 = "CREATE VIEW all_clients_creds AS "
			               + "SELECT CLIENTS.ID,CLIENTS.CLIENT_NAME,CLIENTS.DESCRIPTION,"
			               + "CREDS.ID, CREDS.CHALLENGE, CREDS.RESPONSE "
			               + "FROM  CREDS LEFT OUTER JOIN CLIENTS ON CREDS.CLIENT_ID = CLIENTS.ID";	
	
	
	       
	       
	
	/// Constructor
	///  Takes a filename as the input and checks to see if it exists
	///  Will attempt to create if it does not exist
	public SQliteOps(String _sDbFilename)
	{
	   try {
		Class.forName("org.sqlite.JDBC");
		sDbFilename = _sDbFilename;
		fileDb = new File(sDbFilename);
	   if (!fileDb.exists())
	   {
		   System.out.println("DEBUG->File [" + sDbFilename + "] does not exist...creating database");
		   initDb();
	   }
	} catch (ClassNotFoundException e) {
		System.out.println("DEBUG->SQliteOps ClassNotFoundException [" + e.getMessage() + "]");
		e.printStackTrace();
	}
	}
	   
	/// initDb
	///   Initializes database, executed if database file does not exist
	public void initDb()
	{
		
   System.out.println("DEBUG->initDb - Creating database [" + this.sDbFilename + "]");			
	          	connectToDb();	
	          	Statement stmtInit;
				try {
					  stmtInit = dbConn.createStatement();
		   System.out.println("DEBUG->initDb, Creating table [" + S_CLIENT_TABLE + "]");
			           stmtInit.executeUpdate(S_CLIENT_TABLE);		  
		   System.out.println("DEBUG->initDb, Creating table [" + S_CREDS_TABLE + "]");			  
					   stmtInit.executeUpdate(S_CREDS_TABLE);
		   System.out.println("DEBUG->initDb, Creating index [" + S_CLIENT_IDX1 + "]");			  
		               stmtInit.executeUpdate(S_CLIENT_IDX1);
		   System.out.println("DEBUG->initDb, Creating view [" + S_VIEW1 + "]");
		               stmtInit.executeUpdate(S_VIEW1);
		   System.out.println("DEBUG->initDb, Creating table [" + S_KEY_TABLE + "]");
		               stmtInit.executeUpdate(S_KEY_TABLE);
		   System.out.println("DEBUG->initDb, Creating table [" + S_LOG_TABLE + "]");
		               stmtInit.executeUpdate(S_LOG_TABLE);		               
				} catch (SQLException e) {
			        System.out.println("initDb SQLException [" + e.getMessage() + "]");
					e.printStackTrace();
				}
	}

	/// connectToDb
	///  Establish database connection if object is not set
	public void connectToDb()
	{
		System.out.println("DEBUG->connectToDb, Trying filename [" + sDbFilename + "]");
		String sConn = "jdbc:sqlite:" + sDbFilename;
		
		try {
		if (dbConn==null || dbConn.isClosed())
		{
		
			dbConn = DriverManager.getConnection(sConn);
		}
		} catch (SQLException e) {
			System.out.println("DEBUG->connectToDb SQLException [" + e.getMessage() + "]");
		}
		
	}
	
	/// closeDb
	///  Close database connection if set
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
		System.out.println("DEBUG->getKey, Retrieving key from database");
		try {
			  stmtKey = dbConn.createStatement();
			  stmtKey.execute(sGetkey);
			  ResultSet rs = stmtKey.getResultSet();
			   while (rs.next())
			   {
				   blReturn = rs.getBlob("key");
				    System.out.println("DEBUG->getKey, return key [" + blReturn.toString() + "]");
			   }
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->getKey, SQLException [" + e.getMessage() + "]");
			e.printStackTrace();
			return null;
		}
		finally 
		{
			closeDb();
		}
		
		return blReturn;
	}
	
	/**
	 * saveKey - Method to insert key value to database table - not implemented
	 * @param _blKey
	 */
	public void saveKey(Blob _blKey)
	{
	  connectToDb();
	    String sSave = "insert into keys(key,active) values(" + _blKey + ",1)";
		Statement stmtSave;
		try {
			 stmtSave = dbConn.createStatement();
			 stmtSave.execute(sSave);
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->saveKey, SQLException [" + e.getMessage() + "]");
		}
		finally 
		{
		  closeDb();
		}
	}
	
	/// insertRecord
	///   @param - DbRecord Object
	///   @return - int value
	///               0 - completed, did not throw exception, but did not create record
	///              -1 - Exception encountered
	///              >0 - completed, records inserted.
	public int insertRecord(DbRecord dbRecord)
	{

		
		String sInsert = null; 
	    Statement stmtIns = null;
	    int iReturn = 0;
	    
	    
	    System.out.println("DEBUG->insertRecord, Type [" + dbRecord.getType() + "]");
		if (dbRecord.getType().equals("clients"))
		{
		  sInsert = "insert into clients(client_name,description) values(\"" + dbRecord.getClientName() + "\",\"" + dbRecord.getClientDesc() + "\")";      		   
		}
		else if (dbRecord.getType().equals("creds"))
		{
 		  sInsert = "insert into creds(challenge,response,client_id) values(\"" + dbRecord.getChallenge() + "\",\"" + dbRecord.getResponse() + "\"," + dbRecord.getClientId() + ")";
		}
		else 
		{
			dbRecord.setResult("insertRecord, unknown type [" + dbRecord.getType() + "]");
			System.out.println("insertRecord, unknown type [" + dbRecord.getType() + "]");
			  return -1;
		}
		
		try {
			System.out.println("DEBUG->insertRecord, insert statement [" + sInsert + "]");
			System.out.println("DEBUG->insertRecord, calling connectToDb");
			connectToDb();
			stmtIns = dbConn.createStatement();
			System.out.println("DEBUG->insertRecord, dbConn [" + dbConn.getCatalog() + "]");
			stmtIns.execute(sInsert);
			ResultSet rs = stmtIns.getGeneratedKeys();
			 while (rs.next())
			 {
				 iReturn = rs.getInt("last_insert_rowid()");
				 System.out.println("DEBUG->insertRecord, found rs [" + iReturn + "]");
				 return iReturn; //rs.getInt("last_insert_rowid()");
				  
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		System.out.println("DEBUG->insertRecord, returning [" + iReturn + "]");
		return iReturn;
	}

	/// updateRecord
	///  @param - DbRecord Object
	///  @return - int value
	///             0 - successful update
	///            -1 - Unknown type
	///            -2 - Exception executing update sql
	public int updateRecord(DbRecord dbRec)
	{
	  connectToDb();
	  Statement stmtUpd = null;
	  String sUpd = null;
	  
	  if (dbRec.getType().equals("clients"))
	  {
   	    sUpd = "update clients set client_name = \"" + dbRec.getClientName() + "\", description = \"" + dbRec.getClientDesc() + "\" where id = " + dbRec.getClientId();
		  
	  }
	  else if (dbRec.getType().equals("creds"))
	  {
		sUpd = "update creds set challenge = \"" + dbRec.getChallenge() + "\", response = \"" + dbRec.getResponse() + "\" where id = " + dbRec.getCredId() + " and client_id = " + dbRec.getClientId();		  
	  }
	  else
	  {
		  dbRec.setResult("updateRecord, unknown type [" + dbRec.getType() + "]");
		  System.out.println("DEBUG->updateRecord, unknown type [" + dbRec.getType() + "]");
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
	
	/// deleteRecord
	///  @param - DbRecord
	///  @return - int
	///              0 = success
	///             -1 = failed
	public int deleteRecord(DbRecord dbRec)
	{
		Statement stmtDel = null;
		String    sDel    = null;
		connectToDb();
		
		if (dbRec.getType().equals("clients"))
		{
			DbRecord dbRecTemp = new DbRecord();
			dbRecTemp.setType(Pwdtypes.S_CREDS_TYPE);
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
		else
		{
			dbRec.setResult("updateRecord, unknown type [" + dbRec.getType() + "]");
			 return -1;
		}
		
		try {
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
	 * @param dbRec - Defines record type, and parameters for query
	 * @return - ArrayList<String> of retrieved records
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
		else if (dbRec.getType().equals(Pwdtypes.S_CREDS_TYPE))
		{
			sQry = "select * from " + dbRec.getType() + " where client_id = " + dbRec.getClientId();
		}
		else if (dbRec.getType().equals(Pwdtypes.S_LOG_TYPE))
		{
			sQry = "select * from " + dbRec.getType();
		}
		
		try {
			  stmtData = dbConn.createStatement();
			  stmtData.execute(sQry);
			   rs = stmtData.getResultSet();
			   while (rs.next())
			   {
			     sData = rs.getInt(1) + "|" + rs.getString(2) + "|" +  rs.getString(3);
				   arrList.add(sData);
                     System.out.println("DEBUG->getRecords, sData [" + sData + "]");
			   }
			   
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->getRecords SQLException [" + e.getMessage() + "], sql statement [" + sQry + "]");
		}
		finally
		{
			closeDb();
		}		
		
		return arrList;
	}
	
	/**
	 * getNumRecords - Return the number of records returned from last query - not implemented
	 * @return
	 */
	public int getNumRecords()
	{
		return this.iNumRecords;
	}
	
}
