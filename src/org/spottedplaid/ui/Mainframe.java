package org.spottedplaid.ui;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * 
 * Mainframe.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import org.apache.commons.lang.StringUtils;

import org.spottedplaid.config.Pwdtypes;
import org.spottedplaid.crypto.Crypto;
import org.spottedplaid.database.DbRecord;
import org.spottedplaid.database.SQliteOps;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;

import org.spottedplaid.ui.Changepwd;

import javax.swing.JComboBox;

/* ***************************************************************
Class:    Mainframe
Purpose:  Methods to display and manipulate database values
***************************************************************  */
/**
 * The Class Mainframe.
 */
public class Mainframe extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/// Class variables 
	/** The content pane. */
	private JPanel contentPane;
	
	/** The jtxt app. */
	private JTextField jtxtApp;
	
	/** The jtxt desc. */
	private JTextField jtxtDesc;
	
	/** The l_sqliteops. */
	private SQliteOps l_sqliteops = null;
	
	/** The l_crypto. */
	private Crypto    l_crypto    = null;
    
    /** The db rec. */
    private DbRecord dbRec = null;
    
    /** The jtab apps. */
    private JTable jtabApps;
    
    /** The i client id. */
    private int iClientId = 0;
    
    /** The i cred id. */
    private int iCredId   = 0;
    
    /** The btn replace. */
    JButton btnReplace = null;
	
	/** The btn delete. */
	JButton btnDelete = null;
	
	/** The btn search. */
	JButton btnSearch = null;
	
	/** The btn clear. */
	JButton btnClear = null;
	
	/** The btn cred add. */
	JButton btnCredAdd = null;
	
	/** The btn cred replace. */
	JButton btnCredReplace = null;
	
	/** The btn cred delete. */
	JButton btnCredDelete = null;
	
	/** The btn cred clear. */
	JButton btnCredClear = null;
	
	/** The btn edit  */
	JButton btnEdit = null;
	
	/** The btn show assoc. */
	JButton btnShowAssoc = null;
	
	/** The chk del assoc. */
	JCheckBox chkDelAssoc = new JCheckBox("Delete Associated Challenge/Responses");
	
	/** The jtxt chlng. */
	private JTextField jtxtChlng;
	
	/** The jtxt rsp. */
	private JTextField jtxtRsp;
	
	/** The jtab creds. */
	private JTable jtabCreds;
	
	/** Track Updates combo box */
	private JComboBox<String> jcbTrack = new JComboBox<String>();
	
	/** Date modify variable */
	private String sDateModified = null;
	
	/** Holder variable for Challenge */
	private String sChallenge = null;
	
	/** Holder variable for Response */
	private String sResponse = null;
	
	/**
	 * Create the frame.
	 *
	 * @param _Sqliteops the _ sqliteops
	 * @param _Crypto the _ crypto
	 */
	public Mainframe(SQliteOps _Sqliteops, Crypto _Crypto) {
		
		l_sqliteops = _Sqliteops;
		l_crypto    = _Crypto;
		
		setTitle("The Password Saver - Management");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 982, 656);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		mnFile.add(mntmExit);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmChgpwd = new JMenuItem("Change Passphrase");
		mntmChgpwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Changepwd changePwd = new Changepwd(l_crypto,l_sqliteops);
				changePwd.setVisible(true);
			}
		});
		
        mnTools.add(mntmChgpwd);
        
        JMenuItem mntmExpirationReport = new JMenuItem("Expiration Report");
        mntmExpirationReport.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		DbRecord dbRecExp = new DbRecord();
        		dbRecExp.setType(Pwdtypes.S_EXP_RPT);
        		ArrayList<String> arrData = l_sqliteops.getRecords(dbRecExp);
        		String[] sRecord = new String[3];
            	String sData = "";
            	int iElement = 0;

            	/// Cycle through the data, output to text file, and open in WordPad
        	     if (arrData != null && arrData.size() > 0)
        	     {
        	    	 try {
        	    	 String sFilename = "ExpirationReport.txt";
        	    	 File fileExpRpt = new File (sFilename);

        	    	 BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fileExpRpt));
        	    	 buffWriter.write("URL/Application                Challenge            Expiration");
        	    	 buffWriter.write("\n");
        	    	 buffWriter.write("--------------------------------------------------------------");
        	    	 buffWriter.write("\n");
        		   for (int iCount=0; iCount < arrData.size();iCount++)
            	   {
                       sData = arrData.get(iCount);
                       System.out.println("DEBUG->sData [" + sData + "]");
                        StringTokenizer st = new StringTokenizer(sData,"|");
                        iElement = 0;
                         while (st.hasMoreTokens())
                         {
                        	 sRecord[iElement] =  st.nextToken();
                        	 iElement++;
                         }

                         /// Define the padding for the output
                         int iPadValue1 = 35 - sRecord[0].length();
                         if (iPadValue1 < 0)
                         {
                        	 iPadValue1 = 2;
                         }
                         
                         int iPadValue2 = 55 - (35 + sRecord[1].length());
                         if (iPadValue2 < 0)
                         {
                        	 iPadValue2 = 2;
                         }
                         
                         iPadValue1 += sRecord[1].length();
                         iPadValue2 += sRecord[2].length();
                         
                         buffWriter.write(sRecord[0] + StringUtils.leftPad(sRecord[1],iPadValue1) + StringUtils.leftPad(sRecord[2],iPadValue2) + "\n");
                         buffWriter.write("\n");
        			}
        		   buffWriter.close();
        		   
        		   /// Opens WordPad on Windows systems.  This could be changed to use a property in order to work on a linux/unix/apple system
        		   ProcessBuilder pb = new ProcessBuilder("write.exe", sFilename);
        		   pb.start();
        	    	 }
        	    	 catch (IOException ie)
        	    	 {
        	    		System.out.println("Expiration Report IO Exception [" + ie.getMessage() + "]");
        	    		ie.printStackTrace();
        	    	 }       	    	 
        	    	 
        	     }
    	    	 else
    	    	 {
    	    		 JOptionPane.showMessageDialog(null,"No expiring records found");
    	    	 }
        	}
        });
        mnTools.add(mntmExpirationReport);
        
        JMenuItem mntmViewLogs = new JMenuItem("View Logs");
        mntmViewLogs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		DbRecord dbRecLogs = new DbRecord();
        		dbRecLogs.setType(Pwdtypes.S_LOG_TYPE);
        		ArrayList<String> arrData = l_sqliteops.getRecords(dbRecLogs);
        		String[] sRecord = new String[3];
            	String sData = "";
            	String sTitle = "Display Data Changes";
            	String sDisplay = "Date                  Log Message";
            	sDisplay += "\n";
            	int iElement = 0;

            	/// Cycle through the data, output to text file, and open in WordPad
        	     if (arrData != null)
        	     {
        		   for (int iCount=0; iCount < arrData.size();iCount++)
            	   {
                       sData = arrData.get(iCount);
                       System.out.println("DEBUG->sData [" + sData + "]");
                        StringTokenizer st = new StringTokenizer(sData,"|");
                        iElement = 0;
                         while (st.hasMoreTokens())
                         {
                        	 sRecord[iElement] =  st.nextToken();
                        	 iElement++;
                         }
                         sDisplay += sRecord[2] + ":" + sRecord[1];
                         sDisplay += "\n";
            	   }
        		   
        		   if (arrData.size() >0)
        		   {
        		     JOptionPane.showMessageDialog(null,sDisplay,sTitle,JOptionPane.INFORMATION_MESSAGE);
        		   }
        		   
        	     }
        	}
        });
        
        mnTools.add(mntmViewLogs);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblThePasswordSaver = new JLabel("The Password Saver - Manage Passwords");
		lblThePasswordSaver.setFont(new Font("Arial", Font.BOLD, 16));
		lblThePasswordSaver.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblUrlapplication = new JLabel("URL/Application");
		
		jtxtApp = new JTextField();
		jtxtApp.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description");
		
		jtxtDesc = new JTextField();
		jtxtDesc.setColumns(10);
		
		/// Button - Add button for clients/apps
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FormValidation.verifyAppData(jtxtApp.getText().toString(), jtxtDesc.getText().toString())<0)
				{
					JOptionPane.showMessageDialog(null,"URL/Application and Description are required");
				}
				else
				{
					dbRec = new DbRecord();
					dbRec.setType(Pwdtypes.S_CLIENT_TYPE);
					dbRec.setClientName(jtxtApp.getText().toString());
					dbRec.setClientDesc(jtxtDesc.getText().toString());
					int l_iClientId = l_sqliteops.insertRecord(dbRec);
					if (l_iClientId <= 0)
					{
						JOptionPane.showMessageDialog(null,"Insert record failed [" + dbRec.getResult() + "]");
					}
					else
					{
						dbRec.setClientId(l_iClientId);
						addToTable();
					}
				}
			}
		});
		
		/// Buttons - Replace button for clients/apps
		btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			  if (iClientId <= 0)
			  {
				  JOptionPane.showMessageDialog(null,"Update record warning: Please select record to continue");
				   return;
			  }
				dbRec = new DbRecord();
		        dbRec.setType(Pwdtypes.S_CLIENT_TYPE);
		        dbRec.setClientId(iClientId);
			    dbRec.setClientName(jtxtApp.getText().toString());
			    dbRec.setClientDesc(jtxtDesc.getText().toString());
			    if (l_sqliteops.updateRecord(dbRec)<0)
			    {
			    	JOptionPane.showMessageDialog(null,"Update record failed [" + dbRec.getResult() + "]");
			    }
			    else
			    {
			    	 int iRow = jtabApps.getSelectedRow();
	                 jtabApps.setValueAt(jtxtApp.getText().toString(), iRow, 1);
	                 jtabApps.setValueAt(jtxtDesc.getText().toString(), iRow, 2);

			    	clearFields();
			    }
			}
		});
		
		btnReplace.setEnabled(false);
		
		/// Button - Delete button for clients/apps
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iClientId <= 0)
				{
					JOptionPane.showMessageDialog(null,"Delete record failed: Please select a record then click Delete");
					 return;
				}
				dbRec = new DbRecord();
				dbRec.setType("clients");
				dbRec.setClientId(iClientId);
				dbRec.setDelCreds(0);
				
				if (chkDelAssoc.isSelected())
				{
					dbRec.setDelCreds(1);
				}
				
				 if (l_sqliteops.deleteRecord(dbRec)<0)
				 {
					 JOptionPane.showMessageDialog(null,"Delete record failed [" + dbRec.getResult() + "]");
				 }
				 else
				 {
					 DefaultTableModel jtabModel = (DefaultTableModel) jtabApps.getModel();
			          jtabModel.removeRow(jtabApps.getSelectedRow());
			           if (chkDelAssoc.isSelected())
			           {
			        	   DefaultTableModel model = (DefaultTableModel) jtabCreds.getModel();
			        	    model.setRowCount(0);
			           }
				          clearFields();
				 }
			}
		});
		btnDelete.setEnabled(false);
		
		/// Buttons - Search button for clients/apps
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbRec = new DbRecord();
				dbRec.setType(Pwdtypes.S_CLIENT_TYPE);
				dbRec.setClientName(jtxtApp.getText().toString());
				dbRec.setClientDesc(jtxtDesc.getText().toString());
				 loadTable(dbRec);
			}
		});
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                    clearFields();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		/// Begin section for credentials challenges/responses - text fields and buttons
		JLabel lblChallenge = new JLabel("Challenge");
		
		JLabel lblResponse = new JLabel("Response");
		
		jtxtChlng = new JTextField();
		jtxtChlng.setColumns(10);
		
		jtxtRsp = new JTextField();
		jtxtRsp.setColumns(10);
		
		/// Buttons - Add button for credentials
		btnCredAdd = new JButton("Add");
		btnCredAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FormValidation.verifyCredData(jtxtChlng.getText().toString(),jtxtRsp.getText().toString())<0)
				{
					JOptionPane.showMessageDialog(null,"Challenge and Response are required");
				}
				else
				{
					dbRec = new DbRecord();
					dbRec.setType(Pwdtypes.S_CREDS_TYPE);
					dbRec.setClientId(iClientId);
					dbRec.setChallenge(jtxtChlng.getText().toString());
					dbRec.setResponse(l_crypto.encrypt(jtxtRsp.getText().toString()));
					dbRec.setTrack(jcbTrack.getSelectedItem().toString());
					
					/// Set the modify date if the track days are > 0
					if (!jcbTrack.getSelectedItem().toString().equals("0"))
					{
						Calendar calNow = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						int iDaysToAdd = Integer.parseInt(jcbTrack.getSelectedItem().toString());
						
						
					       calNow.add(Calendar.DATE,iDaysToAdd);
					       String sValue = sdf.format(calNow.getTime());
					        dbRec.setModifyDate(sValue);
					}
					
					int l_iClientId = l_sqliteops.insertRecord(dbRec);
					if (l_iClientId <= 0)
					{
						JOptionPane.showMessageDialog(null,"Insert record failed [" + dbRec.getResult() + "]");
					}
					else
					{
						dbRec.setCredId(l_iClientId);
						addToCredsTable();
					}
				}
			}
		});
		
		/// Button - Replace button for credentials
		btnCredReplace = new JButton("Replace");
		btnCredReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DbRecord dbRecLog = new DbRecord();
				int iDaysToAdd = 0;
				Calendar calNow = Calendar.getInstance();
			    SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
				
			    String sCurDate = sdf1.format(calNow.getTime());
			    String sValue = sDateModified; 
				String sLogMsg = "";
				StringBuilder sbLogMsg = new StringBuilder(sLogMsg);
				
				if (dbRec.getType().equals(Pwdtypes.S_CREDS_TYPE) && (dbRec.getCredId() > 0))
				{
					dbRec.setClientId(iClientId);
					dbRec.setCredId(iCredId);
					dbRec.setChallenge(jtxtChlng.getText().toString());
					dbRec.setResponse(l_crypto.encrypt(jtxtRsp.getText().toString()));
					dbRec.setTrack(jcbTrack.getSelectedItem().toString());
					
					/** Check for changes and insert log if necessary */
					if (!sChallenge.equals(jtxtChlng.getText()))
					{
				      sbLogMsg.append("Application [" + jtxtApp.getText() + "], Challenge modified, old [" + sChallenge + "], new [" + jtxtChlng.getText() + "]");	   	
					}
	                
					if (!sResponse.equals(jtxtRsp.getText()))
	                {
						if (sbLogMsg.toString().length() > 0)
						{
							sbLogMsg.append(",");
						}
						else 
						{
							sbLogMsg.append("Application [" + jtxtApp.getText() + "],");
						}
						sbLogMsg.append("Response modified, old [" + sResponse + "]");
	                }

					if (sbLogMsg.toString().length() > 0)
					{
						dbRecLog.setType(Pwdtypes.S_LOG_TYPE);
						dbRecLog.setLog(sbLogMsg.toString());
						dbRecLog.setModifyDate(sCurDate);
						if (l_sqliteops.insertRecord(dbRecLog)<0)
						{
							JOptionPane.showMessageDialog(null,"Insert log record failed [" + dbRecLog.getResult() + "]");
						}
					}
	                   
					 if (!jcbTrack.getSelectedItem().toString().equals("0"))
					 {
				       iDaysToAdd = Integer.parseInt(jcbTrack.getSelectedItem().toString());
				
				
				       calNow.add(Calendar.DATE,iDaysToAdd);
				       sValue = sdf1.format(calNow.getTime());
				      
				       System.out.println("DEBUG->Date (sValue) [" + sValue + "]");
				        dbRec.setModifyDate(sValue);
					 }
					/// Update the record
					if (l_sqliteops.updateRecord(dbRec)<0)
					{
						JOptionPane.showMessageDialog(null,"Update record failed [" + dbRec.getResult() + "]");
					}
					else
					{
						int iRow = jtabCreds.getSelectedRow();
		                 jtabCreds.setValueAt(jtxtChlng.getText().toString(), iRow, 1);
		                 jtabCreds.setValueAt(l_crypto.encrypt(jtxtRsp.getText().toString()), iRow, 2);
                         jtabCreds.setValueAt(jcbTrack.getSelectedItem().toString(),iRow,3);
                         jtabCreds.setValueAt(sValue,iRow,4);

                         
                         jtabCreds.setValueAt(sValue,iRow,4);
				    	clearCredsFields();
				        enableCredsButtons();
					}
				}
			}
		});
		
		/// Button - Delete button for credentials
		btnCredDelete = new JButton("Delete");
		btnCredDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbRec.setType(Pwdtypes.S_CREDS_TYPE);
				dbRec.setCredId(iCredId);
				dbRec.setChallenge(jtxtChlng.getText().toString());
				dbRec.setResponse(jtxtRsp.getText().toString());
				if (l_sqliteops.deleteRecord(dbRec)<0)
				 {
					 JOptionPane.showMessageDialog(null,"Delete credential record failed [" + dbRec.getResult() + "]");
				 }
				 else
				 {
					 DefaultTableModel jtabModel = (DefaultTableModel) jtabCreds.getModel();
			          jtabModel.removeRow(jtabCreds.getSelectedRow());
			          clearCredsFields();
			          enableCredsButtons();
				 }
			}
		});
		
		/// Button - Clear button for credentials
		btnCredClear = new JButton("Clear");
		btnCredClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearCredsFields();
				enableCredsButtons();
			}
		});
				
		/// End section for credentials challenges/responses - text fields and buttons
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		btnShowAssoc = new JButton("Display Associated Challenges/Responses in new window");
		
		/// Display the challenges/responses associated to the application in a popup window. 
		/// This is to make it easier to view when all of the values are needed
		btnShowAssoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sTitle = "Credentials for: " + jtxtApp.getText();
				String sDisplay = "";
				
				sDisplay += "\n";
				DefaultTableModel jTmpModel = (DefaultTableModel) jtabCreds.getModel();
		     for (int i=0; i< jTmpModel.getRowCount();i++)
		     {
		    	 sDisplay += "Q. " + jTmpModel.getValueAt(i,1).toString() + "  A. " + l_crypto.decrypt(jTmpModel.getValueAt(i,2).toString()) + "\n";
		     }
		     
		     JOptionPane.showMessageDialog(null,sDisplay,sTitle,JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JLabel lblTrackUpdates = new JLabel("Exp Days");
		
		/// Values for expiration days are hardcoded, may want to move to a table for metadata
		jcbTrack.addItem("0");
		jcbTrack.addItem("30");
		jcbTrack.addItem("45");
		jcbTrack.addItem("60");
		jcbTrack.addItem("90");
		jcbTrack.addItem("180");
		jcbTrack.addItem("365");
		jcbTrack.setSelectedItem("0");
		
	    btnEdit = new JButton("Edit");
	    btnEdit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		jtxtChlng.setEnabled(true);
	    		jtxtRsp.setEnabled(true);
	    		jcbTrack.setEnabled(true);
	    		btnCredReplace.setEnabled(true);
	    		btnCredAdd.setEnabled(true);
	    	}
	    });
		btnEdit.setEnabled(false);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(207)
							.addComponent(lblThePasswordSaver))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(23)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnAdd)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnReplace)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnDelete)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSearch)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnClear))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblUrlapplication)
												.addComponent(lblDescription))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(jtxtApp, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
												.addComponent(jtxtDesc, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)))
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnShowAssoc)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addComponent(lblResponse)
												.addComponent(lblChallenge))
											.addGap(18)
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addComponent(jtxtRsp, 272, 272, 272)
													.addGap(26)
													.addComponent(lblTrackUpdates)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(jcbTrack, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
												.addComponent(jtxtChlng, GroupLayout.PREFERRED_SIZE, 440, GroupLayout.PREFERRED_SIZE)))
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
											.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(btnCredAdd, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnCredReplace)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnCredDelete, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(btnCredClear)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnEdit))
											.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(36)
							.addComponent(chkDelAssoc)))
					.addContainerGap(57, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblThePasswordSaver)
					.addGap(45)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUrlapplication)
								.addComponent(jtxtApp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblChallenge))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDescription)
								.addComponent(jtxtDesc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblResponse)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(jtxtChlng, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(jtxtRsp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTrackUpdates)
								.addComponent(jcbTrack, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd)
						.addComponent(btnReplace)
						.addComponent(btnDelete)
						.addComponent(btnSearch)
						.addComponent(btnClear)
						.addComponent(btnCredAdd)
						.addComponent(btnCredReplace)
						.addComponent(btnCredDelete)
						.addComponent(btnCredClear)
						.addComponent(btnEdit))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(120)
							.addComponent(btnShowAssoc)))
					.addGap(18)
					.addComponent(chkDelAssoc)
					.addContainerGap(170, Short.MAX_VALUE))
		);
		
		/// JTable - Credentials table setup/definition - BEGIN
		jtabCreds = new JTable();
		jtabCreds.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Challenge", "Response", "Exp Days", "Expiration Date"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		jtabCreds.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		scrollPane_1.setViewportView(jtabCreds);
		/// JTable - Credentials table setup/definition - END
		
		jtabApps = new JTable();
		scrollPane.setViewportView(jtabApps);
		
		jtabApps.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtabApps.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		jtabApps.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "URL/Application", "Description"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		});
		jtabApps.getColumnModel().getColumn(1).setMinWidth(55);
		jtabApps.getColumnModel().getColumn(2).setMinWidth(55);
		contentPane.setLayout(gl_contentPane);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{jtxtChlng, lblThePasswordSaver, jtxtApp, jtxtDesc, btnAdd, btnReplace, btnDelete, btnSearch, btnClear, jtxtRsp, btnCredAdd, btnCredReplace, btnCredDelete, btnCredClear, scrollPane, jtabApps, lblUrlapplication, lblDescription, chkDelAssoc, lblChallenge, lblResponse, scrollPane_1, jtabCreds}));
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{menuBar, jtxtApp, jtxtDesc, btnAdd, btnReplace, btnDelete, btnSearch, btnClear, jtxtChlng, jtxtRsp, btnCredAdd, btnCredReplace, btnCredDelete, btnCredClear, contentPane, mnFile, mntmExit, lblThePasswordSaver, scrollPane, jtabApps, lblUrlapplication, lblDescription, chkDelAssoc, lblChallenge, lblResponse, scrollPane_1, jtabCreds}));
		
		  /// Initial data load
		       dbRec = new DbRecord();
		       dbRec.setType(Pwdtypes.S_CLIENT_TYPE);
		       dbRec.setClientName("");
		       dbRec.setClientDesc("");
           loadTable(dbRec);
           disableCredsButtons();

             ListSelectionModel rowSM = jtabApps.getSelectionModel();

    //Listener for client row change;
    rowSM.addListSelectionListener(new ListSelectionListener() {

        /// Fill the form values when a row is selected in the JTable
		@Override
		public void valueChanged(ListSelectionEvent e) {
		      ListSelectionModel lsmData = (ListSelectionModel)e.getSource();
               if (!lsmData.isSelectionEmpty())
               {
                   int iRow = lsmData.getMinSelectionIndex();
                   iClientId = Integer.parseInt(jtabApps.getValueAt(iRow, 0).toString());
                   jtxtApp.setText(jtabApps.getValueAt(iRow,1).toString());
                   jtxtDesc.setText(jtabApps.getValueAt(iRow, 2).toString());            

                     dbRec.setType(Pwdtypes.S_CREDS_TYPE);
                     dbRec.setClientId(iClientId);
                      loadTable(dbRec);
                   enableButtons();
                   clearCredsFields();
                   enableCredsButtons();
                 }
            }
    });

    ListSelectionModel rowCred = jtabCreds.getSelectionModel();

    //Listener for credential row change;
    rowCred.addListSelectionListener(new ListSelectionListener() {

        /// Fill the form values when a row is selected in the JTable
		@Override
		public void valueChanged(ListSelectionEvent e) {
		      ListSelectionModel lsmData = (ListSelectionModel)e.getSource();
               if (!lsmData.isSelectionEmpty())
               {
                   int iRow = lsmData.getMinSelectionIndex();
                   iCredId = Integer.parseInt(jtabCreds.getValueAt(iRow, 0).toString());
                   jtxtChlng.setText(jtabCreds.getValueAt(iRow,1).toString());
                   jtxtRsp.setText(l_crypto.decrypt(jtabCreds.getValueAt(iRow, 2).toString()));            
                   jcbTrack.setSelectedItem(jtabCreds.getValueAt(iRow, 3).toString());
                   if (null==jtabCreds.getValueAt(iRow, 4))
                   {
                	   sDateModified = "";
                   }
                   else
                   {
                   sDateModified = jtabCreds.getValueAt(iRow, 4).toString();
                   }
                   sChallenge = jtxtChlng.getText();
                   sResponse  = jtxtRsp.getText();
                     dbRec.setType(Pwdtypes.S_CREDS_TYPE);
                     dbRec.setCredId(iClientId);
                     jtxtChlng.setEnabled(false);
                     jtxtRsp.setEnabled(false);
                     jcbTrack.setEnabled(false);
                      btnEdit.setEnabled(true);
                      btnCredDelete.setEnabled(true);
                      btnCredClear.setEnabled(true);
                 }
            }
    });
    
    
	}
	
	
	
	/**
	 * addToTable - Add new row to the JTable clients object.
	 */     
    public void addToTable()
    {
       DefaultTableModel jtabModel = (DefaultTableModel) jtabApps.getModel();

       jtabModel.addRow(new Object[]{dbRec.getClientId(),dbRec.getClientName(),dbRec.getClientDesc()});
       clearFields();
    }
    
    /**
     * addToCredsTable - Add new row to the JTable credentials object.
     */
    public void addToCredsTable()
    {
    	 DefaultTableModel jtabModel = (DefaultTableModel) jtabCreds.getModel();

         jtabModel.addRow(new Object[]{dbRec.getCredId(),dbRec.getChallenge(),dbRec.getResponse(),dbRec.getTrack(),dbRec.getModifyDate()});
         clearCredsFields();
         enableCredsButtons();
    }
    
    /**
     * clearFields - Clear text fields associated with clients.
     */
    public void clearFields()
    {
		iClientId = 0;
		jtxtApp.setText("");
		jtxtDesc.setText("");
		chkDelAssoc.setSelected(false);
		  clearCredsFields();
		  disableButtons();		  
    }
    
    /**
     * clearCredsFields - Clear text fields associated with credentials.
     */
    public void clearCredsFields()
    {
    	jtxtChlng.setText("");
        jtxtChlng.setEnabled(true);
    	jtxtRsp.setText("");
    	jtxtRsp.setEnabled(true);
    	btnEdit.setEnabled(false);
    	jcbTrack.setSelectedItem("0");
    	jcbTrack.setEnabled(true);
    	disableCredsButtons();
    }

    /**
     * enableButtons - Enable buttons for client manipulation.
     */
    public void enableButtons()
    {
      btnDelete.setEnabled(true);
      btnReplace.setEnabled(true);
    }
    
    /**
     * disableButtons - Disable buttons associated with client manipulation.
     */
    public void disableButtons()
    {
      btnDelete.setEnabled(false);
      btnReplace.setEnabled(false);
    }
    
    /**
     * enableCredsButtons - Enable buttons for credentials manipulation.
     */
    public void enableCredsButtons()
    {
    	btnCredAdd.setEnabled(true);
    	btnCredReplace.setEnabled(true);
    	btnCredClear.setEnabled(true);
    	btnShowAssoc.setEnabled(true);
    }
    
    /**
     * disableCredsButtons - Disable buttons associated with credentials manipulation.
     */
    public void disableCredsButtons()
    {
    	btnCredAdd.setEnabled(false);
    	btnCredReplace.setEnabled(false);
    	btnCredDelete.setEnabled(false);
    	btnCredClear.setEnabled(false);
    	btnShowAssoc.setEnabled(false);
    }
    
    /**
     * loadTable - Loads client data.
     *
     * @param _dbRec - Parameter indicating type of data to load and any associated filters
     */
    public void loadTable(DbRecord _dbRec)
    {
    	DefaultTableModel jtabModel = null;
    	
    	try {

    	ArrayList<String> arrData = l_sqliteops.getRecords(_dbRec);
    	
    	if (_dbRec.getType().equals(Pwdtypes.S_CLIENT_TYPE))
    	{
    	jtabApps.setModel(new DefaultTableModel(
    			new Object[][] {
    			},
    			new String[] {
    				"ID", "URL/Application", "Description"
    			}
    		));
    	jtabModel = (DefaultTableModel) jtabApps.getModel();
    	}
    	else if (_dbRec.getType().equals(Pwdtypes.S_CREDS_TYPE))
    	{
    		jtabCreds.setModel(new DefaultTableModel(
        			new Object[][] {
        			},
        			new String[] {
        				"ID", "Challenge", "Response","Exp Days","Expiration Date"
        			}
        		));
        jtabModel = (DefaultTableModel) jtabCreds.getModel();	
    	}
    	
    	String[] sRecord = new String[6];
    	String sData = "";
    	int iElement = 0;

	     if (arrData != null)
	     {
	    	 System.out.println("DEBUG->size [" + arrData.size() + "]");
		   for (int iCount=0; iCount < arrData.size();iCount++)
    	   {
               sData = arrData.get(iCount);
                StringTokenizer st = new StringTokenizer(sData,"|");
                iElement = 0;
                System.out.println("DEBUG->Tokencount [" + st.countTokens() + "]");
                 while (st.hasMoreTokens())
                 {
                	 sRecord[iElement] =  st.nextToken();
                	 System.out.println("DEBUG->Element [" + sRecord[iElement] + "]");
                	 iElement++;
                 }
				jtabModel.addRow(new Object[] {sRecord[0],sRecord[1],sRecord[2],sRecord[3],sRecord[4]});
				
			}
	     }
    	}
    	catch (Exception e)
    	{
    		System.out.println("DEBUG->loadTable Exception [" + e.getMessage()  + "]");
    		e.printStackTrace();
    	}
    }
}
