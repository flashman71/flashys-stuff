package ebapps.dish.preview.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

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

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.configuration.Values;
import ebapps.dish.preview.database.DbFunctions;


public class Startscreen extends JFrame {

	private JPanel contentPane;
	private JTextField txtUser;
	private JPasswordField txtPass;
	private DbFunctions dbFunc = null;
	private Mainscreen mainScreen = null;
    private Properties props;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Startscreen frame = new Startscreen();
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
	public Startscreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("Review Analysis Tool");
		label.setFont(new Font("Papyrus", Font.BOLD, 30));
		
		JLabel lblLogin = new JLabel("Connect to Database");
		
		JLabel lblUsername = new JLabel("Username");
		
		JLabel lblPassword = new JLabel("Password");
		
		txtUser = new JTextField();
		txtUser.setColumns(10);
		
		txtPass = new JPasswordField();
		
		props = new Properties();
        FileInputStream fis;
		try {
			fis = new FileInputStream(Constants.S_PROPERTIES_FILE);
			props.load(fis);
	        fis.close();	        
		} catch (FileNotFoundException e1) {
	        JOptionPane.showMessageDialog(null, Constants.S_PROPERTIES_FILE_NOT_FOUND);
			e1.printStackTrace();
			 System.exit(-1);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, Constants.S_PROPERTIES_FILE_IO_ERROR);
			e1.printStackTrace();
			 System.exit(-1);
		}

    	
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char[] cPass = txtPass.getPassword();
				String sPass = new String(cPass);
				Values l_Values = new Values();
				  l_Values.setsUsername(txtUser.getText());
				  l_Values.setsPassword(sPass);
				  l_Values.setsDbHost(props.getProperty("DB_HOST"));
				  l_Values.setsDbName(props.getProperty("DB_NAME"));
				   if (null==l_Values.getsDbHost()||null==l_Values.getsDbName())
				   {
					   JOptionPane.showMessageDialog(null, Constants.S_DB_HOST_OR_NAME_MISSING);
						 System.exit(-1);
				   }
				  //l_Values.setsDbHost("localhost:3306");
				  //l_Values.setsDbName("team_peer_review");
				dbFunc = new DbFunctions();
				
				if (dbFunc.bConnectToDb())
				{
	                mainScreen = new Mainscreen(dbFunc);
	                mainScreen.setVisible(true);
	                setVisible(false);
				}
				else
				{
					JOptionPane.showMessageDialog(null, Constants.S_CONNECTION_FAILURE);
				}
			}
		});
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			  System.exit(0);
			}
			
		});
		
		JLabel lblAppVersion = new JLabel("");
		lblAppVersion.setFont(new Font("Tahoma", Font.PLAIN, 8));
		
		JLabel lblDbVersion = new JLabel("");
		lblDbVersion.setFont(new Font("Tahoma", Font.PLAIN, 8));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(57, Short.MAX_VALUE)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE)
					.addGap(45))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(85)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblPassword)
								.addComponent(lblUsername))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(txtUser)
								.addComponent(txtPass, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnLogin)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnExit))
						.addComponent(lblLogin))
					.addContainerGap(174, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(95)
					.addComponent(lblAppVersion)
					.addGap(30)
					.addComponent(lblDbVersion)
					.addContainerGap(136, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(39)
							.addComponent(lblLogin))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAppVersion)
								.addComponent(lblDbVersion))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(txtPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogin)
						.addComponent(btnExit))
					.addGap(32))
		);
		contentPane.setLayout(gl_contentPane);
		lblAppVersion.setText(Constants.S_APP_VERSION);
		lblDbVersion.setText(Constants.S_DB_VERSION);
	}
}
