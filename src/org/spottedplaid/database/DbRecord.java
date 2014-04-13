package org.spottedplaid.database;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * @version 1.0
 * 
 * DbRecord.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

/* ***************************************************************
Class:    DbRecord
Purpose:  Record holder for database inserts/updates
***************************************************************  */
public class DbRecord {


	/// Class variables
	private String sType         = null;
	private String sClientName   = null;
	private String sClientDesc   = null;
	private String sChallenge    = null;
	private String sResponse     = null;
	private int iClientId        = -1;
	private int iCredId          = -1;
	private String sResult       = null;
	private int iDelCreds        = 0;
	private String sTrack        = "";
	private String sDateModified = null;
	private String sLog          = null;
	
	/// Get/Set Methods
	public String getType() {
		return sType;
	}
	public void setType(String sType) {
		this.sType = sType;
	}
	public String getClientName() {
		return sClientName;
	}
	public void setClientName(String sClientName) {
		this.sClientName = sClientName;
	}
	public String getClientDesc() {
		return sClientDesc;
	}
	public void setClientDesc(String sClientDesc) {
		this.sClientDesc = sClientDesc;
	}
	public String getChallenge() {
		return sChallenge;
	}
	public void setChallenge(String sChallenge) {
		this.sChallenge = sChallenge;
	}
	public String getResponse() {
		return sResponse;
	}
	public void setResponse(String sResponse) {
		this.sResponse = sResponse;
	}
	public int getClientId() {
		return iClientId;
	}
	public void setClientId(int iClientId) {
		this.iClientId = iClientId;
	}
	public int getCredId() {
		return iCredId;
	}
	public void setCredId(int iCredId) {
		this.iCredId = iCredId;
	}
	public String getResult() {
		return sResult;
	}
	public void setResult(String _sResult) {
		this.sResult = _sResult;
	}
	
	public int getDelCreds()
	{
		return iDelCreds;
	}
	
	public void setDelCreds(int _iDelCreds)
	{
		this.iDelCreds = _iDelCreds; 
	}
	
	public void setTrack(String _sTrack)
	{
		this.sTrack = _sTrack;
	}
	
	public String getTrack()
	{
		return this.sTrack;
	}
	
	public void setModifyDate(String _sModDate)
	{
	    this.sDateModified = _sModDate;
	}
	
	public String getModifyDate()
	{
		return this.sDateModified;
	}
	
	public void setLog(String _sLog)
	{
		this.sLog = _sLog;
	}
	
	public String getLog()
	{
		return this.sLog;
	}
	
}
