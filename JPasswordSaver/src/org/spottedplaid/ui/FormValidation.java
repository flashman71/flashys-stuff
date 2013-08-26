package org.spottedplaid.ui;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * 
 * FormValidation.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

/* ***************************************************************
Class:    FormValidation
Purpose:  Methods used for validating form values
***************************************************************  */
public class FormValidation {

	/**
	 * verifyAppData - Verifies data for application has been entered
	 * @param sName
	 * @param sDesc
	 * @return - 0 for success, < 0 for failure
	 */
	public static int verifyAppData(String sName, String sDesc)
	{
		if (sName==null||sName.length() < 1)
		{
			return -1;
		}
		
		if (sDesc==null||sDesc.length() < 1)
		{
			return -2;
		}
		
		return 0;
	}
	
	/**
	 * verifyCredData - Verifies data for credentials record has been entered
	 * @param _sChallenge
	 * @param _sResponse
	 * @return - 0 for success, < 0 for failure
	 */
	public static int verifyCredData(String _sChallenge, String _sResponse)
	{
		if (_sChallenge==null||_sChallenge.length() < 1)
		{
			return -1;
		}
		
		if (_sResponse==null||_sResponse.length() < 1)
		{
			return -2;
		}
		
		return 0;
	}
}
