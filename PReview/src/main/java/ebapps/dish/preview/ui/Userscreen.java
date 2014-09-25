package ebapps.dish.preview.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.configuration.Userdata;
import ebapps.dish.preview.database.DbFunctions;
import ebapps.dish.preview.util.Tools;

import java.awt.Color;

public class Userscreen extends JFrame {

	private JPanel contentPane;
	private JTextField txtLogin;
	private JTextField txtFullName;
	private JTextField txtEmail;
	private JPasswordField txtPass;
    private JComboBox<String> jcbPrivs;
    private JComboBox<String> jcbStatus;
    private JComboBox<String> jcbCurrUsers;
    private DbFunctions dbFunc;
    private Userdata userData = new Userdata();

    private String sCurrStatus = "";
    private String sCurrPrivs = "";
    private String sUser = "";
    private String sPass = "";
	
	/**
	 * Create the frame.
	 */
	public Userscreen(DbFunctions _dbFunc) {
		setTitle("User Maintenance");
		
		dbFunc = _dbFunc;
		setBounds(100, 100, 517, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("Review Analysis Tool");
		label.setFont(new Font("Dialog", Font.BOLD, 30));
		
		JLabel lblUserMaintenance = new JLabel("User Maintenance");
		
		JLabel lblLogin = new JLabel("Login");
		
		txtLogin = new JTextField();
		txtLogin.setForeground(Color.BLACK);
		txtLogin.setBackground(Color.LIGHT_GRAY);
		txtLogin.setColumns(10);
		
		JLabel lblFullName = new JLabel("Full Name");
		
		txtFullName = new JTextField();
		txtFullName.setColumns(10);
		
		JLabel lblEmailAddress = new JLabel("Email Address");
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		
		JLabel lblPrivileges = new JLabel("Privileges");
		
		jcbPrivs = new JComboBox<String>();
		
		JLabel lblActive = new JLabel("Active");
		
		jcbStatus = new JComboBox<String>();
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isFormValid("Create"))
				{
					char[] cPass = txtPass.getPassword();
					String sPass = new String(cPass);
					
					Userdata uData = new Userdata();
					uData.setsEmail(txtEmail.getText().toString());
					uData.setsFullname(txtFullName.getText().toString());
					uData.setsPrivs(jcbPrivs.getSelectedItem().toString());
					uData.setsStatus(jcbStatus.getSelectedItem().toString());
					uData.setsUsername(txtLogin.getText().toString());
					uData.setsPassword(sPass);
					  if (dbFunc.addUser(uData))
					  {
						  System.out.println("User added");
						  jcbCurrUsers.addItem(uData.getsUsername());
						  uData = null;
						  txtEmail.setText("");
						  txtFullName.setText("");
						  txtLogin.setText("");
						  txtPass.setText("");
						  jcbPrivs.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
						  jcbStatus.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
					  }
				}
			}
		});
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		JLabel lblPassword = new JLabel("Password");
		
		txtPass = new JPasswordField();
		
		JLabel lblCurrentUsers = new JLabel("Current Users");
		
		jcbCurrUsers = new JComboBox<String>();
		jcbCurrUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    sUser = jcbCurrUsers.getSelectedItem().toString();
				if (!sUser.equals(Constants.S_DEFAULT_LIST_VALUE))
				{
				  fillForm(sUser);	
				}
			}
		});
		
		JButton btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isFormValid("Replace"))
				{
					userData.setsEmail(txtEmail.getText().toString());
					userData.setsFullname(txtFullName.getText().toString());
					userData.setsPrivs(jcbPrivs.getSelectedItem().toString());
					userData.setsStatus(jcbStatus.getSelectedItem().toString());
					
					dbFunc.updateUserData(userData);
					if (jcbStatus.getSelectedItem().toString().equals(Constants.S_YES) && isValidStatusChange()==0)
					{
						if (dbFunc.resetUserPassword(txtLogin.getText().toString(), sPass) <0)
						{
							JOptionPane.showMessageDialog(null, Constants.S_ENABLE_USER_FAILED);
						}
						else
						{
							JOptionPane.showMessageDialog(null, Constants.S_ENABLE_USER_SUCCESS + " - " + txtLogin.getText().toString());
						}
					}
					else if (canBeDisabled()==0)
					{
						if (dbFunc.resetUserPassword(txtLogin.getText().toString(), Tools.generateCode(8)) < 0)
						{
							JOptionPane.showMessageDialog(null, Constants.S_DISABLE_USER_FAILED);
						}
						else
						{
							JOptionPane.showMessageDialog(null, Constants.S_DISABLE_USER_SUCCESS + " - " + txtLogin.getText().toString());
						}
					}
					

				}
			}
		});
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearForm();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(75)
									.addComponent(label))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(183)
									.addComponent(lblUserMaintenance))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(10)
									.addComponent(lblCurrentUsers)
									.addGap(4)
									.addComponent(jcbCurrUsers, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(10)
									.addComponent(lblPassword)
									.addGap(25)
									.addComponent(txtPass, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(10)
									.addComponent(lblEmailAddress)
									.addGap(5)
									.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(10)
									.addComponent(lblPrivileges)
									.addGap(26)
									.addComponent(jcbPrivs, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblActive)
									.addGap(4)
									.addComponent(jcbStatus, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(10)
									.addComponent(lblLogin)
									.addGap(46)
									.addComponent(txtLogin, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(lblFullName)
									.addGap(4)
									.addComponent(txtFullName, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)))
							.addGap(41))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(165)
							.addComponent(btnCreate)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnReplace)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnClear)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExit)
							.addGap(62))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(label)
					.addGap(6)
					.addComponent(lblUserMaintenance)
					.addGap(19)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblCurrentUsers))
						.addComponent(jcbCurrUsers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblLogin))
						.addComponent(txtLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblFullName))
						.addComponent(txtFullName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblPassword))
						.addComponent(txtPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblEmailAddress))
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblPrivileges))
						.addComponent(jcbPrivs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblActive))
						.addComponent(jcbStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(55)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCreate)
						.addComponent(btnReplace)
						.addComponent(btnExit)
						.addComponent(btnClear))
					.addContainerGap(30, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		loadScreen();
		loadUsers();
	}
	
	private void loadScreen()
	{
		ResultSet rsData = dbFunc.getListValues("security_group");
		jcbPrivs.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbPrivs.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		
		jcbStatus.addItem("Y");
		jcbStatus.addItem("N");
		jcbStatus.setSelectedItem("Y");
		
		try {
			while (rsData.next())
			{
				jcbPrivs.addItem(rsData.getString(2));
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadScreen, SQLException");
			e.printStackTrace();
		}
	}
	
	private void loadUsers()
	{
	  ResultSet rsUsers = dbFunc.getAvailableUsers(Constants.S_ALL_USERS_TYPE, "", "");
	  jcbCurrUsers.addItem(Constants.S_DEFAULT_LIST_VALUE);
	  jcbCurrUsers.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
	  
	    try {
	    	  while (rsUsers.next())
	    	  {
	    		  String sUser = rsUsers.getString(1);
	    		  if (sUser.length()>1)
	    		  {
	    		    jcbCurrUsers.addItem(sUser);
	    		  }
	    	  }
	    }
	    catch (SQLException e)
	    {
	    	System.out.println("DEBUG->loadUsers, SQLException");
	    	e.printStackTrace();
	    }
	  
	}
	
	private boolean isFormValid(String _sType)
	{
		if (null==txtLogin.getText() || txtLogin.getText().length()==0)
		{
			JOptionPane.showMessageDialog(null, Constants.S_INVALID_USER_LOGIN);
			 return false;
		}
		
		if (null==txtFullName.getText() || txtFullName.getText().length()==0)
		{
			JOptionPane.showMessageDialog(null, Constants.S_INVALID_USER_FULLNAME);
			 return false;
		}
	
		if (null==txtEmail.getText() || txtEmail.getText().length()==0)
		{
			JOptionPane.showMessageDialog(null, Constants.S_INVALID_USER_EMAIL);
			 return false;
		}
		
		if (jcbPrivs.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
		{
			JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + " user privileges");
			 return false;
		}
		
		if (_sType.equals("Create"))
		{
		if (null==txtPass.getPassword() || txtPass.getPassword().toString().length()==0)
		{
			JOptionPane.showMessageDialog(null, Constants.S_INVALID_USER_PASSWORD);
			 return false;
		}
		}
		
		return true;
	}
	
	private void fillForm(String _sLogin)
	{
		ResultSet rsUser = dbFunc.getUserByLogin(_sLogin);
		try {
			while (rsUser.next())
			{
                userData.setiId(rsUser.getInt(1));
				sCurrPrivs = rsUser.getString(4);
				sCurrStatus = rsUser.getString(5);
				
				txtEmail.setText(rsUser.getString(2));
				txtLogin.setText(sUser);
				txtFullName.setText(rsUser.getString(3));
				jcbPrivs.setSelectedItem(sCurrPrivs);
				jcbStatus.setSelectedItem(sCurrStatus);
				txtPass.setText("");
				
				userData.setsEmail(txtEmail.getText().toString());
				userData.setsFullname(txtFullName.getText().toString());
				userData.setsPrivs(jcbPrivs.getSelectedItem().toString());
				userData.setsStatus(jcbStatus.getSelectedItem().toString());
				userData.setsUsername(txtLogin.getText().toString());
				txtLogin.setEnabled(false);
				
			}
		}
		catch (SQLException e)
		{
			System.out.println("DEBUG->fillForm, SQLException");
			e.printStackTrace();
		}
	}
	
	private void clearForm()
	{
		txtEmail.setText("");
		txtFullName.setText("");
		jcbPrivs.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbStatus.setSelectedItem("Y");
		txtLogin.setText("");
		jcbCurrUsers.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		txtPass.setText("");
		
        userData.resetFields();
        
		sCurrPrivs = "";
		sCurrStatus = "";
	}
	
	private int isValidStatusChange()
	{

		char[] cPass = txtPass.getPassword();
		sPass = new String(cPass);
		
		if (!sCurrStatus.equals(jcbStatus.getSelectedItem().toString()) && sPass.length()==0)
		{
			JOptionPane.showMessageDialog(null, Constants.S_MISSING_PASSWORD);
			 return -1;
		}
		else
		{
		     return 0;
		}
	}
	
	private int canBeDisabled()
	{
		if (sCurrStatus.equals(Constants.S_YES) && (!jcbStatus.getSelectedItem().toString().equals(Constants.S_YES)))
		{
		  return 0;	
		}
		return -1;
	}
}
