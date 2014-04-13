package org.spottedplaid.ui;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * @version 1.0
 * 
 * Changepwd.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import org.spottedplaid.config.Pwdtypes;
import org.spottedplaid.crypto.Crypto;
import org.spottedplaid.database.DbRecord;
import org.spottedplaid.database.SQliteOps;

public class Changepwd extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2287624629768601436L;
	
	/** Main frame */
	private JPanel contentPane;
	
	/** Crypto variable */
	private Crypto lCrypto = null;
	
	/** SQliteOps variable */
    private SQliteOps lSQliteOps = null;
    
    /** New passphrase variable */
    private JTextField jtxtNewpwd;

	/**
	 * Create the frame.
	 *   @param Crypto object
	 *   @param SQliteOps object
	 */
	public Changepwd(Crypto _lCrypto, SQliteOps _sqliteOps) {
		setTitle("Change Passphrase");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		lCrypto = _lCrypto;
		lSQliteOps = _sqliteOps;
		
		JLabel lblChangePassphrase = new JLabel("Change Passphrase");
		lblChangePassphrase.setFont(new Font("Square721 BT", Font.BOLD, 18));
		
		JLabel lblNewPassphrase = new JLabel("New passphrase");
		
		jtxtNewpwd = new JPasswordField();
		
		/// "Update" button and listener does the work.  This will verify that a passphrase has been defined, get the existing records and update with the new key.
		///  The application will then close so it can be reopened using the new passphrase.
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sNewfile = "";
				String sNewpwd = jtxtNewpwd.getText();
				Crypto newCrypto = new Crypto();
				
				if (sNewpwd.length()< 1)
				{
					JOptionPane.showMessageDialog(null, "Passphrase cannot be blank");
					
				}
				else
				{
					String sCurrentFile = lCrypto.getKeyfile();
					if (sCurrentFile.length() < 1)
					{
						JOptionPane.showMessageDialog(null, "Current keyfile is blank");
					}
					else
					{			
		        		  Changepwd.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));	
				        sNewfile = "tmp_" + sCurrentFile;	
				        if (newCrypto.verifyKey(sNewfile, sNewpwd,Pwdtypes.S_METHOD).equals("Success"))
				        {
				        	DbRecord dbPrevrec = new DbRecord();
				        	dbPrevrec.setType(Pwdtypes.S_CREDS_TYPE);
				        	dbPrevrec.setClientId(-1);
				
				        	ArrayList<String> arrData = lSQliteOps.getRecords(dbPrevrec);
				        	  if (arrData != null)
				        	  {
				        		  String[] sRecord = new String[6];
				        		  String sData = null;
				        		  int iElement = 0;
				        		  DbRecord dbNew = new DbRecord();
				        	  
				        		  /// Loop through the existing set of records, decrypt with old key and encrypt with new
				        		  for (int iCount=0; iCount < arrData.size();iCount++)
				           	   {
				                      sData = arrData.get(iCount);
				                       StringTokenizer st = new StringTokenizer(sData,"|");
				                       iElement = 0;
				                        while (st.hasMoreTokens())
				                        {
				                       	 sRecord[iElement] =  st.nextToken();
				                       	 iElement++;
			
				                        }
				                        String sDecvalue = lCrypto.decrypt(sRecord[2]);
				                        dbNew.setType(Pwdtypes.S_CREDS_UPD);
				                        dbNew.setClientId(Integer.parseInt(sRecord[0]));
				                        dbNew.setResponse(newCrypto.encrypt(sDecvalue).toString());
				                        dbNew.setTrack(sRecord[3]);
				                        dbNew.setModifyDate(sRecord[4]);
				                        lSQliteOps.updateRecord(dbNew);
				                        
				        	  }
                                /// Rename the new key file and delete the old file
				        		  File fileOrig = new File(sCurrentFile);
				        		  File fileNew = new File(sNewfile);
				        		  fileOrig.delete();
				        		  fileNew.renameTo(fileOrig);
				        		  Changepwd.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				        		  JOptionPane.showMessageDialog(null, "Passphrase changed.  Records updated: " + arrData.size() + ".  The application will now close, please login with the new passphrase");
				        		  System.exit(0);
				        	  }
				        }
					}
				}
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        setVisible(false);
			}
		});
		
		jtxtNewpwd = new JPasswordField();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(128, Short.MAX_VALUE)
					.addComponent(lblChangePassphrase)
					.addGap(107))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(162, Short.MAX_VALUE)
					.addComponent(btnUpdate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel)
					.addGap(124))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(23)
					.addComponent(lblNewPassphrase)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jtxtNewpwd, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(26, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblChangePassphrase)
					.addGap(56)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewPassphrase)
						.addComponent(jtxtNewpwd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(52)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnUpdate)
						.addComponent(btnCancel))
					.addContainerGap(78, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
