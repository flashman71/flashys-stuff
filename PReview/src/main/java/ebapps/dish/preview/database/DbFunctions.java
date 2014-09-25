package ebapps.dish.preview.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.JOptionPane;

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.configuration.Defectdata;
import ebapps.dish.preview.configuration.Reportdata;
import ebapps.dish.preview.configuration.Userdata;
import ebapps.dish.preview.configuration.Values;

public class DbFunctions {
	
	
	private Connection dbConn = null;
	private PreparedStatement psSelect = null;
	private ResultSet rsData = null;
	
	public Boolean bConnectToDb()
	{
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			dbConn = DriverManager.getConnection("jdbc:mysql://" + Values.getsDbHost() + ":3306/" + Values.getsDbName(), Values.getsUsername(), Values.getsPassword());
			setUserPrivileges();
			 return true;
		} catch (SQLException e) {
            System.out.println("DEBUG->SQL Exception connecting to db");
            e.printStackTrace();
			JOptionPane.showMessageDialog(null, Constants.S_LOGIN_FAILURE);
			 return false;
		} catch (InstantiationException e) {
			System.out.println("DEBUG->Instantiation Exception connecting to db");
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			System.out.println("DEBUG->Illegal Exception connecting to db");
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			System.out.println("DEBUG->Class Not Found Exception connecting to db");
			e.printStackTrace();
			return false;
		}
	}
	
	public void disconnectFromDb()
	{
		try {
			dbConn.close();
		} catch (SQLException e) {
			System.out.println("DEBUG->disconnectFromDb SQL Exception");
			e.printStackTrace();
		}
	}

	public void setUserPrivileges()
	{
		if (Values.getsUsername().equals(Constants.S_ADMIN_USER))
		{
			Values.setsFullname("Administrator");
			Values.setsPrivs("Admin");
			Values.setId(1);
		}
		else
		{

		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_SECURITY_QUERY);
			psSelect.setString(1, Values.getsUsername());
			rsData = psSelect.executeQuery();
			while (rsData.next())
			{
				Values.setsFullname(rsData.getString(2));
				Values.setsPrivs(rsData.getString(3));
				Values.setId(rsData.getInt(4));
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->SQL Exception executing setUserPrivileges");
			e.printStackTrace();
		}
		}
	}
	
	public ResultSet getListValues(String _sListName)
	{
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_GEN_LIST_QUERY);
			psSelect.setString(1, _sListName);
			rsData = psSelect.executeQuery();
		} catch (SQLException e) {
			System.out.println("DEBUG->getListValues SQLException");
			e.printStackTrace();
			  return null;
		}
		
		return rsData;
	}
	
	public ResultSet getGroups()
	{
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_GROUP_QUERY);
			rsData = psSelect.executeQuery();
		} catch (SQLException e) {
			System.out.println("DEBUG->getGroups SQLException");
			e.printStackTrace();
			 return null;
		}
		
		return rsData;
	}
	
	public boolean addUser(Userdata _uData)
	{
		String sCreateUser = "grant all privileges on " + DbConstants.S_DB_NAME + ".* to '" + _uData.getsUsername() + "'@'localhost'" 
	                       + " identified by '" + _uData.getsPassword() + "'";

		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_ADD_USER);
			psSelect.setString(1,_uData.getsUsername());
			psSelect.setString(2,_uData.getsEmail());
			psSelect.setString(3,_uData.getsFullname());
			psSelect.setString(4,_uData.getsPrivs());
			psSelect.setString(5,_uData.getsStatus());
			psSelect.setString(6,Values.getsUsername());
			
			psSelect.execute();
			psSelect.execute(sCreateUser);
			  return true;
		} catch (SQLException e) {
			System.out.println("DEBUG->addUser, SQLException");
			e.printStackTrace();
			 return false;
		}
	}
	
	public long addGroup(String _sGroupName)
	{
		try {
			 psSelect = dbConn.prepareStatement(DbConstants.S_ADD_GROUP,Statement.RETURN_GENERATED_KEYS);
			 psSelect.setString(1, _sGroupName);
			 psSelect.execute();
			 rsData = psSelect.getGeneratedKeys();
			 if (rsData.next())
			 {
				 return rsData.getLong(1);
			 }
			 
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->addGroup, SQLException");
			e.printStackTrace(); 
			  return -2;
		}
		
		return -1;
	}
	
	public long addReview(String _sType, int _iId, String _sStatus, String _sGroup)
	{
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_ADD_REVIEW,Statement.RETURN_GENERATED_KEYS);
			psSelect.setString(1, _sType);
			psSelect.setInt(2, _iId);
			psSelect.setString(3, _sStatus);
			psSelect.setInt(4, 0);
			psSelect.setString(5, _sGroup);
			psSelect.setInt(6, Values.getId());
			psSelect.setInt(7, Values.getId());
            psSelect.execute();
            rsData = psSelect.getGeneratedKeys();
             while (rsData.next())
             {
            	 return rsData.getLong(1);
             }
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->addReview, SQLException");
			e.printStackTrace();
			  return -2;
		}
		return -1;
	}
	
	public long addReviewNote(int _iPrid, int _iUserid, String _sNote)
	{
		try {
			 psSelect = dbConn.prepareStatement(DbConstants.S_ADD_PEER_REVIEW_NOTE,Statement.RETURN_GENERATED_KEYS);
			 psSelect.setInt(1, _iPrid);
			 psSelect.setInt(2, _iUserid);
			 psSelect.setString(3, _sNote);
			 psSelect.execute();
			 rsData = psSelect.getGeneratedKeys();
			  while (rsData.next())
			  {
				  return rsData.getLong(1);
			  }
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->addReviewNote, SQLException");
			 e.printStackTrace();
			  return -2;
		}	
		
	  return -1;
	}
	
	public long addReviewDefect(Defectdata _defectData)
	{
		try {
			 psSelect = dbConn.prepareStatement(DbConstants.S_ADD_PEER_REVIEW_DEFECT,Statement.RETURN_GENERATED_KEYS);
			 psSelect.setInt(1, _defectData.getiPeerReviewId());
			 psSelect.setString(2, _defectData.getsDefectType());
			 psSelect.setString(3, _defectData.getsDefectDesc());
			 psSelect.setInt(4, _defectData.getiSeverity());
			 psSelect.setString(5, _defectData.getsStatus());
			 psSelect.setString(6, _defectData.getsCond());
			 psSelect.setInt(7, Values.getId());
			 psSelect.setInt(8, Values.getId());
			 psSelect.execute();
			 rsData = psSelect.getGeneratedKeys();
			  while (rsData.next())
			  {
				  return rsData.getLong(1);
			  }
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->addReviewDefect, SQLException");
			 e.printStackTrace();
			  return -2;
		}

		return -1;
	}
	
	public long addDefectNotes(int _iPrId, String _sNote)
	{
		try {
			 psSelect = dbConn.prepareStatement(DbConstants.S_ADD_PEER_REVIEW_DEFECT_NOTE,Statement.RETURN_GENERATED_KEYS);
			 psSelect.setInt(1, _iPrId);
			 psSelect.setString(2, _sNote);
			 psSelect.setInt(3, Values.getId());
			 psSelect.execute();
			 rsData = psSelect.getGeneratedKeys();
			  while (rsData.next())
			  {
				  return rsData.getLong(1);
			  }
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->addDefectNotes, SQLException");
			 e.printStackTrace();
			  return -2; 
		}
		return -1;
	}
	
	public String addUserToGroups(String _sUsername, String _sGroupname)
	{
		try {
			  CallableStatement csProc = dbConn.prepareCall(DbConstants.S_ADD_USER_TO_GROUPS);
			  csProc.setString(1, _sUsername);
			  csProc.setString(2, _sGroupname);
			  csProc.registerOutParameter(3, Types.VARCHAR);
			  csProc.execute();
			    return csProc.getString(3);
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->addUserToGroups, SQLException");
			e.printStackTrace();
			  return Constants.S_PROC_EXCEPTION;
		}
	}
	
	public String removeUserFromGroups(String _sUsername, String _sGroupname)
	{
		try {
			  CallableStatement csProc = dbConn.prepareCall(DbConstants.S_REMOVE_USER_FROM_GROUPS);
			  csProc.setString(1, _sUsername);
			  csProc.setString(2, _sGroupname);
			  csProc.registerOutParameter(3, Types.VARCHAR);
			  csProc.execute();
			    return csProc.getString(3);
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->addUserToGroups, SQLException");
			e.printStackTrace();
			  return Constants.S_PROC_EXCEPTION;
		}
	}
	
	public void updateReview(int _iId, String _sStatus, String _sCond, String _sGroup)
	{
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_UPD_REVIEW);
			psSelect.setString(1, _sStatus);
			psSelect.setString(2, _sCond);
			psSelect.setInt(3, Values.getId());
			psSelect.setString(4, _sGroup);
			psSelect.setInt(5, _iId);
			psSelect.executeUpdate();
			 psSelect.close();
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->updateReview, SQLException");
			e.printStackTrace();
		}
	}
	
	public void updateDefectNotes(Defectdata _defectData)
	{
		System.out.println("DEBUG->updateDefectNotes");
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_UPD_PEER_REVIEW_DEFECT);
			psSelect.setString(1, _defectData.getsDefectType());
			psSelect.setInt(2, _defectData.getiSeverity());
			psSelect.setString(3, _defectData.getsStatus());
			psSelect.setString(4, _defectData.getsCond());
			psSelect.setInt(5,Values.getId());
			psSelect.setInt(6, _defectData.getiDefectId());
			psSelect.executeUpdate();
			 psSelect.close();
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->updateDefectNotes, SQLException");
			e.printStackTrace();
		}
		
	}
	
	public void updateUserData(Userdata _uData)
	{
	   System.out.println("DEBUG->updateUserData");
	   try {
		    psSelect = dbConn.prepareStatement(DbConstants.S_UPD_USER_DATA);
		    psSelect.setString(1, _uData.getsEmail());
		    psSelect.setString(2, _uData.getsFullname());
		    psSelect.setString(3, _uData.getsPrivs());
		    psSelect.setString(4, _uData.getsStatus());
		    psSelect.setInt(5, Values.getId());
		    psSelect.setInt(6, _uData.getiId());
		     psSelect.executeUpdate();
		      psSelect.close();
	   }
	   catch (SQLException e)
	   {
		   System.out.println("DEBUG->updateUserData, SQLException");
		   e.printStackTrace();
	   }
	}
	
	public ResultSet getAvailableUsers(String _sType, String _sGroupName, String _sActive)
	{
		try {
			
			if (_sType.equals(Constants.S_ALL_USERS_TYPE))
			{
				psSelect = dbConn.prepareStatement(DbConstants.S_GET_ALL_USERS);
			}
			else if (_sType.equals(Constants.S_AVAILABLE_USERS_TYPE))
			{
				if (_sGroupName.length() > 0) {
					psSelect = dbConn.prepareStatement(DbConstants.S_GET_AVAILABLE_USERS2);
					psSelect.setString(1, _sGroupName);
				} else {
					psSelect = dbConn.prepareStatement(DbConstants.S_GET_AVAILABLE_USERS);
					psSelect.setString(1, _sActive);
				}

			} else {
				if (_sGroupName.length() > 0) {
					psSelect = dbConn.prepareStatement(DbConstants.S_GET_ASSIGNED_USERS);
					psSelect.setString(1, _sGroupName);
				} else {
					return null;
				}
			}
			return psSelect.executeQuery();

		} catch (SQLException e) {
			System.out.println("DEBUG->getAvailableUsers, SQLException");
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getUserByLogin(String _sLogin)
	{
	  try {
		   psSelect = dbConn.prepareStatement(DbConstants.S_GET_USER_BY_LOGIN);
		   psSelect.setString(1, _sLogin);
		    return psSelect.executeQuery();
	  }
	  catch (SQLException e)
	  {
		  System.out.println("DEBUG->getUserByLogin, SQLException");
		  e.printStackTrace();
		   return null;
	  }
	}
	
	public ResultSet getReviews(String _sFilterType, String _sInput)
	{
			try {
				  if (_sFilterType.equals(Constants.S_QUERY_BY_ID))
				  {
				psSelect = dbConn.prepareStatement(DbConstants.S_GET_REVIEWS_BY_ID);
				psSelect.setInt(1, Integer.parseInt(_sInput));
				  }
				  if (_sFilterType.equals(Constants.S_QUERY_BY_GROUP))
				  {
				psSelect = dbConn.prepareStatement(DbConstants.S_GET_REVIEWS_BY_GROUP);
				psSelect.setString(1, _sInput);
				  }
				  if (_sFilterType.equals(Constants.S_QUERY_BY_ASG_GROUP))
				  {
				psSelect = dbConn.prepareStatement(DbConstants.S_GET_REVIEWS_BY_ASG_GROUP);
				  }
				  
				  return psSelect.executeQuery();
			} catch (SQLException e) {
				System.out.println("DEBUG->getReviews SQLException");
				e.printStackTrace();
				 return null;
			}
	}
	
	public boolean hasOpenDefects(int _iPeerReviewId)
	{
		boolean bReturn = false;
		
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_GET_OPEN_DEFECTS);
			psSelect.setInt(1, _iPeerReviewId);
			rsData = psSelect.executeQuery();
			 while (rsData.next())
			 {
				 bReturn = true;
			 }
		} catch (SQLException e) {
			System.out.println("DEBUG->areDefectsOpen, SQLException");
			e.printStackTrace();
		}
		return bReturn;
	}
	
	public ResultSet getPeerReviewHistory(int _iPrID)
	{
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_GET_PEER_REVIEW_HISTORY);
			psSelect.setInt(1, _iPrID);
			 return psSelect.executeQuery();
		} catch (SQLException e) {
			System.out.println("DEBUG->getPeerReviewHistory SQLException");
			e.printStackTrace();
			 return null;
		}
	}
	
	public ResultSet getAssociatedDefects(int _iPrID)
	{
		System.out.println("DEBUG->getAssociatedDefects, id [" + _iPrID + "]");
		try {
			  psSelect = dbConn.prepareStatement(DbConstants.S_GET_ASSOCIATED_DEFECTS);
			  psSelect.setInt(1, _iPrID);
			   return psSelect.executeQuery();
		}
	    catch (SQLException e)
	    {
	    	System.out.println("DEBUG->getAssociatedDefects, SQLException");
	    	 e.printStackTrace();
	    	  return null;
	    }
	}
	
	public ResultSet getAllReviewData()
	{
		try {
			  psSelect = dbConn.prepareStatement(DbConstants.S_GET_REVIEW_DATA);
			   return psSelect.executeQuery();
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->getAllReviewData, SQLException");
			e.printStackTrace();
			  return null;
		}
	}
	
	public ResultSet getDefectNotes(int _iDefId)
	{
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_GET_DEFECT_NOTES);
			psSelect.setInt(1, _iDefId);
			 return psSelect.executeQuery();
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->getDefectNotes, SQLException");
			e.printStackTrace();
			 return null;
		}
	}
	
	public int isTransitionValid(String _sType, String _sFrom, String _sTo)
	{
		try {
			  psSelect = dbConn.prepareStatement(DbConstants.S_VALID_TRANSITIONS);
			  psSelect.setString(1, _sType);
			  psSelect.setString(2, _sFrom);
			  psSelect.setString(3, _sTo);
			   rsData = psSelect.executeQuery();
			   while (rsData.next())
			   {
				   return 1;
			   }
		}
		catch (SQLException e)
		{
		  System.out.println("DEBUG->isTransitionValid, SQLException");
		  e.printStackTrace();
		    return -2;
		}
		
		return -1;
	}
	
	public int userBelongsToGroup(String _sUsername, String _sGroup)
	{
		try {
			  psSelect = dbConn.prepareStatement(DbConstants.S_GET_USER_ASSIGNED_TO_GROUP);
			  psSelect.setString(1, _sUsername);
			  psSelect.setString(2, _sGroup);
			   rsData = psSelect.executeQuery();
			    while (rsData.next())
			    {
			    	return 1;
			    }
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->userBelongsToGroup, SQLException");
			e.printStackTrace();
			 return -2;
		}
		return -1;
	}
	
	public int resetUserPassword(String _sUser, String _sPassword)
	{
		try {
			psSelect = dbConn.prepareStatement(DbConstants.S_RESET_USER_PASSWORD);
			psSelect.setString(1, _sUser);
			psSelect.setString(2,Values.getsDbHost());
			psSelect.setString(3, _sPassword);
			 psSelect.execute();
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->resetUserPassword, SQLException");
			e.printStackTrace();
			  return -1;
		}
		return 0;
	}
	
	public ResultSet getReport(String _sReport)
	{
		return null;
	}
}
