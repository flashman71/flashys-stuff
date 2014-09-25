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
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.database.DbFunctions;

public class SubmitFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtIdNumber;
	private JComboBox<String> jcbStatus;
	private JComboBox<String> jcbArtifactTypes;
	private JComboBox<String> jcbGroups;
    private DbFunctions dbFunc = null;


	/**
	 * Create the frame.
	 */
	public SubmitFrame(DbFunctions _dbFunc) {
		setTitle("Submit Review");
		dbFunc = _dbFunc;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 541, 322);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("Review Analysis Tool");
		label.setFont(new Font("Papyrus", Font.BOLD, 30));
		
		JLabel lblSubmitReview = new JLabel("Submit Review");
		
		txtIdNumber = new JTextField();
		txtIdNumber.setColumns(10);
		
		JLabel lblNumber = new JLabel("Number");
		
		JLabel lblStatus = new JLabel("Status");
		
		jcbStatus = new JComboBox<String>();
		
		JLabel lblAssignTo = new JLabel("Assign to");
		
		jcbGroups = new JComboBox<String>();
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isFormValid())
				{
					long lReviewId = dbFunc.addReview(jcbArtifactTypes.getSelectedItem().toString(), Integer.parseInt(txtIdNumber.getText().toString()), jcbStatus.getSelectedItem().toString(), jcbGroups.getSelectedItem().toString());
					if (lReviewId > 0)
					{
						JOptionPane.showMessageDialog(null, Constants.S_REVIEW_INSERTED + lReviewId);
					    jcbArtifactTypes.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
					    jcbStatus.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
					    jcbGroups.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
					    txtIdNumber.setText("");
					    
					}
				}
			}
		});
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		JLabel lblArtifactType = new JLabel("Artifact Type");
		
		jcbArtifactTypes = new JComboBox<String>();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(17)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblArtifactType)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(jcbArtifactTypes, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblStatus)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jcbStatus, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNumber)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(txtIdNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblAssignTo)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jcbGroups, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(203)
							.addComponent(lblSubmitReview))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(78)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(191)
							.addComponent(btnSave)
							.addGap(18)
							.addComponent(btnExit)))
					.addContainerGap(115, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSubmitReview)
					.addGap(36)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblArtifactType)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNumber)
							.addComponent(jcbArtifactTypes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtIdNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStatus)
						.addComponent(jcbStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAssignTo)
						.addComponent(jcbGroups, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnExit))
					.addContainerGap(69, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		loadFormLists();
	}
	
	private void loadFormLists()
	{
		jcbStatus.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbStatus.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		
		jcbArtifactTypes.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbArtifactTypes.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		
		jcbGroups.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbGroups.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		
		ResultSet rsData = dbFunc.getListValues(Constants.S_PEER_REVIEW_STATUS_LIST);
		 try {
			while (rsData.next())
			 {
				 jcbStatus.addItem(rsData.getString(2));
			 }
		} catch (SQLException e) {
			System.out.println("DEBUG->loadFormLists(1) SQLException");
			e.printStackTrace();
		}
		 
		rsData = dbFunc.getListValues(Constants.S_PEER_REVIEW_ARTIFACT_TYPE_LIST);
		 try {
				while (rsData.next())
				 {
					jcbArtifactTypes.addItem(rsData.getString(2));
				 }
			} catch (SQLException e) {
				System.out.println("DEBUG->loadFormLists(2) SQLException");
				e.printStackTrace();
			}
		 
		rsData = dbFunc.getGroups();
		try {
			while (rsData.next())
			 {
				jcbGroups.addItem(rsData.getString(1));
			 }
		} catch (SQLException e) {
			System.out.println("DEBUG->loadFormLists(3) SQLException");
			e.printStackTrace();
		}
	}
	
	private boolean isFormValid()
	{
		if (jcbStatus.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
		{
			JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + " Status");
			 return false;
		}
		
		if (jcbArtifactTypes.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
		{
			JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + " Artifact Type");
			 return false;
		} 
		
		if (!jcbStatus.getSelectedItem().toString().equals("New") && (!jcbStatus.getSelectedItem().toString().equals("Assigned")))
		{
			JOptionPane.showMessageDialog(null, Constants.S_INVALID_CREATE_STATUS);
			 return false;
		}
		
		if (jcbStatus.getSelectedItem().toString().equals("Assigned") && (jcbGroups.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE)))
		{
			JOptionPane.showMessageDialog(null, Constants.S_INVALID_GROUP);
			 return false;
		}
		
		if (!txtIdNumber.getText().toString().matches(("^[-+]?\\d+(\\.\\d+)?$")))
		{
			JOptionPane.showMessageDialog(null, Constants.S_INVALID_ARTIFACT_ID);
			 return false;
		}
	
		return true;
		
	}
}
