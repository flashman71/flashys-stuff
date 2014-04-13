package org.spottedplaid.ui;

/**
 * This software has NO WARRANTY.  It is available ÄS-IS, use at your own risk.
 * 
 * @author gary
 * @version 1.0
 * 
 * Introscreen.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import org.spottedplaid.database.SQliteOps;
import java.awt.Font;

/* ***************************************************************
Class:    Introscreen
Purpose:  Initial screen, allows user to select the keystore and database filenames
          Authenticates password and opens database
***************************************************************  */
public class Introscreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField jtxtPass;
	private JTextField jtxtKstore;
	private JTextField jtxtDbfile;
	private Crypto l_Crypto = new Crypto();
	private SQliteOps l_Sqliteops = null;
	private Mainframe mainframe = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Introscreen frame = new Introscreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Introscreen() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		JLabel lblPassCode = new JLabel("Pass code");
		
		jtxtPass = new JPasswordField();
		
		jtxtPass.setColumns(10);
		
		/// Button - Keystore Filename
		JButton btnKstore = new JButton("Keystore File");
		btnKstore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfcKstore = new JFileChooser();
                  int iRet = jfcKstore.showOpenDialog(Introscreen.this);
                  if (iRet==JFileChooser.APPROVE_OPTION)
                  {
                	  File fileKstore = jfcKstore.getSelectedFile();
                	  jtxtKstore.setText(fileKstore.toString());
                  }
			}
		});
		
		/// Button - Database filename
		JButton btnDatabaseFile = new JButton("Database File");
		btnDatabaseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfcKstore = new JFileChooser();
                int iRet = jfcKstore.showOpenDialog(Introscreen.this);
                if (iRet==JFileChooser.APPROVE_OPTION)
                {
              	  File fileKstore = jfcKstore.getSelectedFile();
              	  jtxtDbfile.setText(fileKstore.toString());
                }
			}
		});
		
		/// Button - Ok - Authenticates password/passphrase for keystore
		final JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("DEBUG->Using Kstore [" + jtxtKstore.getText().toString() + "], Dbfile [" + jtxtDbfile.getText().toString() +"]");
				if (l_Crypto.verifyKey(jtxtKstore.getText().toString(), jtxtPass.getText().toString(),Pwdtypes.S_METHOD).equals("Success"))
					{
					   try {
						     l_Sqliteops = new SQliteOps(jtxtDbfile.getText().toString());
						     mainframe = new Mainframe(l_Sqliteops, l_Crypto);
						     mainframe.setVisible(true);
						     l_Crypto.setKeyfile(jtxtKstore.getText().toString());  // Set the keyfile name
						     jtxtDbfile.setText("");
						     jtxtPass.setText("");
						     jtxtKstore.setText("");
						     btnOk.setEnabled(false);
					   }
					   catch (Exception e)
					   {
						   JOptionPane.showMessageDialog(null,"Exception with database file [" + e.getMessage() + "]");
						   e.printStackTrace();
					   }
					   
					}
				else
				{
					JOptionPane.showMessageDialog(null, "Password Saver: Password is invalid or keystore is corrupt");
					jtxtPass.setText("");
				}
			}
		});
		btnOk.setEnabled(false);
		
		JButton btnCancel = new JButton("Close");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 System.out.println("DEBUG->Cancel, closing application");
				 System.exit(0);
			}
		});
		
		jtxtKstore = new JTextField();
		jtxtKstore.setEditable(false);
		jtxtKstore.setColumns(10);
		jtxtKstore.setText(Pwdtypes.getKstore());
		
		jtxtDbfile = new JTextField();
		jtxtDbfile.setEditable(false);
		jtxtDbfile.setColumns(10);
		jtxtDbfile.setText(Pwdtypes.getDbfile());
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Introscreen.class.getResource("/org/spottedplaid/ui/PasswdSaver.jpg")));
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(Introscreen.class.getResource("/org/spottedplaid/ui/Copyright.jpg")));
		
		JLabel lblVersion = new JLabel(Pwdtypes.S_VERSION);
		lblVersion.setFont(new Font("Square721 BT", Font.BOLD, 9));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(135)
					.addComponent(btnOk)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel)
					.addContainerGap(189, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(64)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(70, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(32)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnDatabaseFile)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(jtxtDbfile, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnKstore)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(jtxtKstore, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(9)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 365, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblPassCode)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(jtxtPass, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)))))
					.addGap(28))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(172)
					.addComponent(lblVersion)
					.addContainerGap(216, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblVersion)
					.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassCode)
						.addComponent(jtxtPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnKstore)
						.addComponent(jtxtKstore, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDatabaseFile)
						.addComponent(jtxtDbfile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(46)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOk)
						.addComponent(btnCancel))
					.addGap(48))
		);
		
		/// Listener for Password/passphrase - activates Ok button 
		jtxtPass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
			  if (jtxtPass.getText().toString().length()>0)
			  {
				  btnOk.setEnabled(true);
			  }
			  else
			  {
				  btnOk.setEnabled(false);
			  }
			}
		});
		
		contentPane.setLayout(gl_contentPane);
	}
}
