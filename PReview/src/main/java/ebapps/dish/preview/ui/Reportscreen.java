package ebapps.dish.preview.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.configuration.Reportdata;
import ebapps.dish.preview.database.DbFunctions;
import javax.swing.JButton;

public class Reportscreen extends JFrame {

	
	private JComboBox<String> jcbPrGroup;
	private JComboBox<String> jcbPrStatus;
	private JComboBox<String> jcbPrCond;
	private JComboBox<String> jcbCreated;
	private JComboBox<String> jcbDefType;
	private JComboBox<String> jcbDefSev;
	private JComboBox<String> jcbDefStatus;
	private JComboBox<String> jcbDefCond;
	private JComboBox<String> jcbOutput;
	private JComboBox<String> jcbOutType;
	
	private DbFunctions dbFunc;
	
	private JPanel contentPane;
	private JTextField jtxtOutFile;

	/**
	 * Create the frame.
	 */
	public Reportscreen(DbFunctions _dbFunc) {
		
		dbFunc = _dbFunc;
		
		setTitle("Reports");
		setBounds(100, 100, 865, 631);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("Review Analysis Tool");
		label.setFont(new Font("Papyrus", Font.BOLD, 30));
		
		JLabel lblReports = new JLabel("Reports");
		
		JLabel lblPeerReviewStatus = new JLabel("Peer Review Status");
		
		jcbPrStatus = new JComboBox<String>();
		jcbPrStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reportdata.setsReviewStatus(jcbPrStatus.getSelectedItem().toString());
			}
		});
		
		JLabel lblPeerReviewCondition = new JLabel("Peer Review Condition");
		
		jcbPrCond = new JComboBox<String>();
		jcbPrCond.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reportdata.setsReviewCond(jcbPrCond.getSelectedItem().toString());
			}
		});
		
		JLabel lblAssignedGroup = new JLabel("Group");
		
        jcbPrGroup = new JComboBox<String>();
        jcbPrGroup.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Reportdata.setsReviewGroup(jcbPrGroup.getSelectedItem().toString());
        	}
        });
		
		JLabel lblCreated = new JLabel("Created");
		
		jcbCreated = new JComboBox<String>();
		jcbCreated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reportdata.setsDateParam(jcbCreated.getSelectedItem().toString());
			}
		});
		
		JLabel lblDefectType = new JLabel("Defect Type");
		
		jcbDefType = new JComboBox<String>();
		jcbDefType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reportdata.setsDefectType(jcbDefType.getSelectedItem().toString());
			}
		});
		
		JLabel lblDefectSeverity = new JLabel("Defect Severity");
		
		jcbDefSev = new JComboBox<String>();
		jcbDefSev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reportdata.setsDefectSev(jcbDefSev.getSelectedItem().toString());
			}
		});
		
		JLabel lblDefectStatus = new JLabel("Defect Status");
		
		jcbDefStatus = new JComboBox<String>();
		jcbDefStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reportdata.setsDefectStatus(jcbDefStatus.getSelectedItem().toString());
			}
		});
		
		JLabel lblDefectCondition = new JLabel("Defect Condition");
		
		jcbDefCond = new JComboBox<String>();
		jcbDefCond.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reportdata.setsDefectCond(jcbDefCond.getSelectedItem().toString());
			}
		});
		
		JLabel lblQueryParameters = new JLabel("Query Parameters");
		
		JLabel lblOutput = new JLabel("Output");
		JLabel lblType = new JLabel("Type");
		
		jtxtOutFile = new JTextField();
		jtxtOutFile.setEditable(false);
		jtxtOutFile.setBackground(Color.LIGHT_GRAY);
		jtxtOutFile.setColumns(10);
		
		jcbOutput = new JComboBox<String>();
		jcbOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jcbOutput.getSelectedItem().toString().equals(Constants.S_OUTPUT_REQUIRES_FILENAME))
				{
					jtxtOutFile.setEnabled(true);
				}
				else
				{
					jtxtOutFile.setText("");
					jtxtOutFile.setEnabled(false);
				}
			}
		});
			
		jcbOutType = new JComboBox<String>();
		
		JLabel lblFilename = new JLabel("Filename");
		
		JButton btnRunReport = new JButton("Run Report");
		btnRunReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ResultSet rsData = dbFunc.getReport(getReportType());
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(327)
									.addComponent(lblReports, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(28)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblPeerReviewStatus)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblPeerReviewCondition, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblAssignedGroup, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addPreferredGap(ComponentPlacement.RELATED)
													.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(btnRunReport)
														.addComponent(lblCreated))
													.addGap(27)))
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addPreferredGap(ComponentPlacement.RELATED)
													.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(jcbPrCond, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
														.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
															.addComponent(jcbPrStatus, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
															.addComponent(jcbPrGroup, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))))
												.addGroup(gl_contentPane.createSequentialGroup()
													.addGap(4)
													.addComponent(jcbCreated, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))))
									.addPreferredGap(ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblDefectSeverity)
												.addComponent(lblDefectType)
												.addComponent(lblDefectStatus, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
											.addGap(32))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(lblDefectCondition, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
											.addGap(18)))
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(jcbDefType, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
												.addComponent(jcbDefStatus, 0, 91, Short.MAX_VALUE)
												.addComponent(jcbDefSev, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(ComponentPlacement.RELATED))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(jcbDefCond, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)))))
							.addGap(40)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblType)
								.addComponent(lblOutput)
								.addComponent(lblFilename))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(jtxtOutFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(jcbOutput, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
								.addComponent(jcbOutType, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(210)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(241)
							.addComponent(lblQueryParameters)))
					.addContainerGap(105, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblReports)
					.addGap(18)
					.addComponent(lblQueryParameters)
					.addGap(17)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(jcbPrStatus, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPeerReviewStatus, Alignment.LEADING))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAssignedGroup)
								.addComponent(jcbPrGroup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPeerReviewCondition)
								.addComponent(jcbPrCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDefectType)
								.addComponent(jcbDefType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblOutput)
								.addComponent(jcbOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDefectStatus)
								.addComponent(jcbDefStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(jtxtOutFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblFilename))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDefectSeverity)
								.addComponent(jcbDefSev, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblType)
								.addComponent(jcbOutType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(19)
							.addComponent(lblDefectCondition))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(16)
							.addComponent(jcbDefCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(19)
							.addComponent(lblCreated))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(16)
							.addComponent(jcbCreated, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(29)
					.addComponent(btnRunReport)
					.addContainerGap(277, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		loadLists();
		
	}

	private void loadLists()
	{
		ResultSet rsData = dbFunc.getListValues(Constants.S_DEFECT_STATUS_LIST);
		jcbDefStatus.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbDefStatus.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
			  jcbDefStatus.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbStatus");
			e.printStackTrace();
		}
		
		rsData = dbFunc.getListValues(Constants.S_DEFECT_CONDITION_LIST);
		jcbDefCond.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbDefCond.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
				jcbDefCond.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbCond");
			e.printStackTrace();
		}
		
		rsData = dbFunc.getListValues(Constants.S_DEFECT_SEVERITY_LIST);
		jcbDefSev.addItem(String.valueOf(Constants.I_DEFAULT_LIST_VALUE));
		jcbDefSev.setSelectedItem(Constants.I_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
				jcbDefSev.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbDefSev");
			e.printStackTrace();
		}
		
		rsData = dbFunc.getListValues(Constants.S_DEFECT_TYPE_LIST);
		jcbDefType.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbDefType.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
				jcbDefType.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbDefType");
			e.printStackTrace();
		}
		
		jcbPrStatus.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbPrStatus.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		rsData = dbFunc.getListValues(Constants.S_PEER_REVIEW_STATUS_LIST);
		 try {
			while (rsData.next())
			 {
				 jcbPrStatus.addItem(rsData.getString(2));
			 }
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbRevStatus");
			e.printStackTrace();
		}
		 
		 jcbPrCond.addItem(Constants.S_DEFAULT_LIST_VALUE);
		 jcbPrCond.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		rsData = dbFunc.getListValues(Constants.S_PEER_REVIEW_CONDITION_LIST);
		 try {
			while (rsData.next())
			 {
				 jcbPrCond.addItem(rsData.getString(2));
			 }
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbRevCond");
			e.printStackTrace();
		}
		 
		 jcbPrGroup.addItem(Constants.S_DEFAULT_LIST_VALUE);
		 jcbPrGroup.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		 rsData = dbFunc.getGroups();
		 try {
				while (rsData.next())
				 {
					jcbPrGroup.addItem(rsData.getString(1));
				 }

			} catch (SQLException e) {
				System.out.println("DEBUG->loadLists, SQLException, jcbRevCond");
				e.printStackTrace();
			}

		 jcbOutput.addItem(Constants.S_DEFAULT_LIST_VALUE);
		 jcbOutput.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		 rsData = dbFunc.getListValues(Constants.S_REPORT_OUTPUT);
		 try {
				while (rsData.next())
				 {
					jcbOutput.addItem(rsData.getString(2));
				 }

			} catch (SQLException e) {
				System.out.println("DEBUG->loadLists, SQLException, jcbOutput");
				e.printStackTrace();
			}
		 
		 jcbOutType.addItem(Constants.S_DEFAULT_LIST_VALUE);
		 jcbOutType.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		 rsData = dbFunc.getListValues(Constants.S_REPORT_OUTPUT_TYPE);
		 try {
				while (rsData.next())
				 {
					jcbOutType.addItem(rsData.getString(2));
				 }

			} catch (SQLException e) {
				System.out.println("DEBUG->loadLists, SQLException, jcbOutType");
				e.printStackTrace();
			}
		 
		 
		 jcbCreated.addItem(Constants.S_DEFAULT_LIST_VALUE);
		 jcbCreated.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		 rsData = dbFunc.getListValues(Constants.S_REPORT_CREATED_PARAM);
		 try {
				while (rsData.next())
				 {
					jcbCreated.addItem(rsData.getString(2));
				 }

			} catch (SQLException e) {
				System.out.println("DEBUG->loadLists, SQLException, jcbCreated");
				e.printStackTrace();
			}
	}
	
	private String getReportType()
	{
		String sReturn = "";
		
		// Check Peer Review filters 
		if (jcbPrStatus.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE) &&
			jcbPrGroup.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE) &&
			jcbPrCond.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE) &&
			jcbCreated.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
		{
			sReturn = Constants.S_PR_REPORT;
		}
		
		// Check defect filters, if peer review return is set then change to both if defect filters are set
		if (jcbDefType.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE) &&
			jcbDefStatus.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE) &&
			jcbDefCond.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE) &&
			jcbDefSev.getSelectedItem().toString().equals(Integer.toString(Constants.I_DEFAULT_LIST_VALUE)))
			{
			  if (sReturn.equals(""))
			  {
			  sReturn = Constants.S_DEFECT_REPORT;
			  }
			  else
			  {
				  sReturn = Constants.S_PR_DEFECT_REPORT;
			  }
			}
		
		return sReturn;
	}
}
