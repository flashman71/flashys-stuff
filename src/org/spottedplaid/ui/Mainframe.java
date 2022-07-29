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
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.spottedplaid.config.Pwdtypes;
import org.spottedplaid.crypto.Crypto;
import org.spottedplaid.database.DbRecord;
import org.spottedplaid.database.SQliteOps;
import java.awt.Dialog.ModalExclusionType;

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

	// / Class variables
	/** The content pane. */
	private JPanel contentPane;

	/** The jtxt app. */
	private JTextField jtxtApp;

	/** The jtxt desc. */
	private JTextField jtxtDesc;

	/** The l_sqliteops. */
	private SQliteOps l_sqliteops = null;

	/** The l_crypto. */
	private Crypto l_crypto = null;

	/** The db rec. */
	private DbRecord dbRec = null;

	/** The jtab apps. */
	private JTable jtabApps;

	/** The i client id. */
	private int iClientId = 0;

	/** The btn replace. */
	JButton btnReplace = null;

	/** The btn delete. */
	JButton btnDelete = null;

	/** The btn search. */
	JButton btnSearch = null;

	/** The btn clear. */
	JButton btnClear = null;

	/** The btn edit */
	JButton btnEdit = null;

	/** Button to view credentials for URL/Application */
	JButton btnViewCredentials = null;
	
	/**
	 * Create the frame.
	 * 
	 * @param _Sqliteops
	 *            the _ sqliteops
	 * @param _Crypto
	 *            the _ crypto
	 */
	public Mainframe(SQliteOps _Sqliteops, Crypto _Crypto) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);

		l_sqliteops = _Sqliteops;
		l_crypto = _Crypto;

		setTitle("The Password Saver - Management");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 572, 481);

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
				Changepwd changePwd = new Changepwd(l_crypto, l_sqliteops);
				changePwd.setVisible(true);
			}
		});

		mnTools.add(mntmChgpwd);

		JMenu mnReports = new JMenu("Reports");
		mnTools.add(mnReports);

		JMenuItem mntmExcelReport = new JMenuItem("Excel/CSV");
		mntmExcelReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argo0) {
				DbRecord dbRecFull = new DbRecord();
				dbRecFull.setType(Pwdtypes.S_ALL_TYPE);
				ArrayList<String> arrFullData = l_sqliteops
						.getRecords(dbRecFull);
				String[] sRecord = new String[6];

				// / Cycle through the data and generate the CSV format for
				// Excel
				if (arrFullData != null && arrFullData.size() > 0) {
					try {
						String sFilename = "Passwords.csv";
						File fileExpRpt = new File(sFilename);
						String sData = "";
						int iElement = 0;

						BufferedWriter buffWriter = new BufferedWriter(
								new FileWriter(fileExpRpt));
						buffWriter
								.write("CLIENT_ID,CLIENT_NAME,CLIENT_DESCRIPTION,CRED_ID,CRED_CHALLENGE,CRED_RESPONSE");
						buffWriter
								.write("--------------------------------------------------------------");
						buffWriter.write("\n");
						for (int iCount = 0; iCount < arrFullData.size(); iCount++) {
							sData = arrFullData.get(iCount);
							StringTokenizer st = new StringTokenizer(sData, "|");
							iElement = 0;
							while (st.hasMoreTokens()) {
								sRecord[iElement] = st.nextToken();
								iElement++;
							}

							buffWriter.write(sRecord[0] + "," + sRecord[1]
									+ "," + sRecord[2] + "," + sRecord[3] + ","
									+ sRecord[4] + ","
									+ l_crypto.decrypt(sRecord[5]) + "\n");
							buffWriter.write("\n");
						}
						buffWriter.close();
					} catch (IOException ie) {
						System.out
								.println("Full Excel/CSV Report IO Exception ["
										+ ie.getMessage() + "]");
						ie.printStackTrace();
					}
				}
			}
		});

		mnReports.add(mntmExcelReport);

		JMenuItem mntmJsonReport = new JMenuItem("JSON");
		mntmJsonReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argo0) {
				DbRecord dbRecFull = new DbRecord();
				dbRecFull.setType(Pwdtypes.S_ALL_TYPE);
				ArrayList<String> arrFullData = l_sqliteops
						.getRecords(dbRecFull);
				String[] sRecord = new String[6];

				// / Cycle through the data and generate the CSV format for
				// Excel
				if (arrFullData != null && arrFullData.size() > 0) {
					try {
						String sFilename = "Passwords.json";
						File fileExpRpt = new File(sFilename);
						String sData = "";
						int iElement = 0;
						String sNewClientId = null;
						String sClientId = null;
						String sJsonData = new String();

						BufferedWriter buffWriter = new BufferedWriter(
								new FileWriter(fileExpRpt));
						for (int iCount = 0; iCount < arrFullData.size(); iCount++) {
							sData = arrFullData.get(iCount);
							StringTokenizer st = new StringTokenizer(sData, "|");
							iElement = 0;
							while (st.hasMoreTokens()) {
								sRecord[iElement] = st.nextToken();
								 if (iElement==0)
								 {
									 sClientId = sRecord[iElement];
								 }
								iElement++;
							}

							if ((sNewClientId == null)) 
							{
							  sJsonData = "{\"clients\": [{\"clientId\": \"" + sRecord[0] + "\",\n\"clientUrl\": \"" + sRecord[1] + "\",\n\"clientDesc\": \"" + sRecord[2] + "\",\n"
									    + "\"credentials\": [{\"credId\" : \"" + sRecord[3] + "\",\n\"credChallenge\": \"" + sRecord[4] + "\",\n\"credResponse\" : \"" + l_crypto.decrypt(sRecord[5]) + "\"}";
                              sNewClientId = sClientId;							
							}
							else if (sNewClientId.equals(sClientId))
							{
							  sJsonData += ", {\"credId\" : \"" + sRecord[3] + "\",\n\"credChallenge\": \"" + sRecord[4] + "\",\n\"credResponse\" : \"" + l_crypto.decrypt(sRecord[5]) + "\"}";								
							}
							else 
							{
								sJsonData += "] }";
								  buffWriter.write(sJsonData);
								  buffWriter.write("\n");
								  sNewClientId = sClientId;
								 
								sJsonData = ", {\"clientId\": \"" + sRecord[0] + "\",\n\"clientUrl\": \"" + sRecord[1] + "\",\n\"clientDesc\": \"" + sRecord[2] + "\",\n"
									    + "\"credentials\": [{\"credId\" : \"" + sRecord[3] + "\",\n\"credChallenge\": \"" + sRecord[4] + "\",\n\"credResponse\" : \"" + l_crypto.decrypt(sRecord[5]) + "\"}";
							}
							
						}
						buffWriter.write("] }");
						buffWriter.close();
					} catch (IOException ie) {
						System.out
								.println("Full JSON Report IO Exception ["
										+ ie.getMessage() + "]");
						ie.printStackTrace();
					}
				}
			}
		});

		mnReports.add(mntmJsonReport);

		JMenuItem mntmExpirationReport = new JMenuItem("Expiration Report");
		mntmExpirationReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DbRecord dbRecExp = new DbRecord();
				dbRecExp.setType(Pwdtypes.S_EXP_RPT);
				ArrayList<String> arrData = l_sqliteops.getRecords(dbRecExp);
				String[] sRecord = new String[3];
				String sData = "";
				int iElement = 0;

				// / Cycle through the data, output to text file, and open in
				// WordPad
				if (arrData != null && arrData.size() > 0) {
					try {
						String sFilename = "ExpirationReport.txt";
						File fileExpRpt = new File(sFilename);

						BufferedWriter buffWriter = new BufferedWriter(
								new FileWriter(fileExpRpt));
						buffWriter
								.write("URL/Application                Challenge            Expiration");
						buffWriter.write("\n");
						buffWriter
								.write("--------------------------------------------------------------");
						buffWriter.write("\n");
						for (int iCount = 0; iCount < arrData.size(); iCount++) {
							sData = arrData.get(iCount);
							StringTokenizer st = new StringTokenizer(sData, "|");
							iElement = 0;
							while (st.hasMoreTokens()) {
								sRecord[iElement] = st.nextToken();
								iElement++;
							}

							// / Define the padding for the output
							int iPadValue1 = 35 - sRecord[0].length();
							if (iPadValue1 < 0) {
								iPadValue1 = 2;
							}

							int iPadValue2 = 55 - (35 + sRecord[1].length());
							if (iPadValue2 < 0) {
								iPadValue2 = 2;
							}

							iPadValue1 += sRecord[1].length();
							iPadValue2 += sRecord[2].length();

							buffWriter.write(sRecord[0]
									+ StringUtils.leftPad(sRecord[1],
											iPadValue1)
									+ StringUtils.leftPad(sRecord[2],
											iPadValue2) + "\n");
							buffWriter.write("\n");
						}
						buffWriter.close();

						// / Opens WordPad on Windows systems. This could be
						// changed to use a property in order to work on a
						// linux/unix/apple system
						ProcessBuilder pb = new ProcessBuilder("write.exe",
								sFilename);
						pb.start();
					} catch (IOException ie) {
						System.out.println("Expiration Report IO Exception ["
								+ ie.getMessage() + "]");
						ie.printStackTrace();
					}

				} else {
					JOptionPane.showMessageDialog(null,
							"No expiring records found");
				}
			}
		});
		mnReports.add(mntmExpirationReport);

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

				// / Cycle through the data, output to text file, and open in
				// WordPad
				if (arrData != null) {
					for (int iCount = 0; iCount < arrData.size(); iCount++) {
						sData = arrData.get(iCount);
						StringTokenizer st = new StringTokenizer(sData, "|");
						iElement = 0;
						while (st.hasMoreTokens()) {
							sRecord[iElement] = st.nextToken();
							iElement++;
						}
						sDisplay += sRecord[2] + ":" + sRecord[1];
						sDisplay += "\n";
					}

					if (arrData.size() > 0) {
						JOptionPane.showMessageDialog(null, sDisplay, sTitle,
								JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
		});

		mnTools.add(mntmViewLogs);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblThePasswordSaver = new JLabel(
				"The Password Saver - Manage Passwords");
		lblThePasswordSaver.setFont(new Font("Arial", Font.BOLD, 16));
		lblThePasswordSaver.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblUrlapplication = new JLabel("URL/Application");

		jtxtApp = new JTextField();
		jtxtApp.setColumns(10);

		JLabel lblDescription = new JLabel("Description");

		jtxtDesc = new JTextField();
		jtxtDesc.setColumns(10);

		// / Button - Add button for clients/apps
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FormValidation.verifyAppData(jtxtApp.getText().toString(),
						jtxtDesc.getText().toString()) < 0) {
					JOptionPane.showMessageDialog(null,
							"URL/Application and Description are required");
				} else {
					dbRec = new DbRecord();
					dbRec.setType(Pwdtypes.S_CLIENT_TYPE);
					dbRec.setClientName(jtxtApp.getText().toString());
					dbRec.setClientDesc(jtxtDesc.getText().toString());
					int l_iClientId = l_sqliteops.insertRecord(dbRec);
					if (l_iClientId <= 0) {
						JOptionPane.showMessageDialog(null,
								"Insert record failed [" + dbRec.getResult()
										+ "]");
					} else {
						dbRec.setClientId(l_iClientId);
						addToTable();
					}
				}
			}
		});

		// / Buttons - Replace button for clients/apps
		btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (iClientId <= 0) {
					JOptionPane
							.showMessageDialog(null,
									"Update record warning: Please select record to continue");
					return;
				}
				dbRec = new DbRecord();
				dbRec.setType(Pwdtypes.S_CLIENT_TYPE);
				dbRec.setClientId(iClientId);
				dbRec.setClientName(jtxtApp.getText().toString());
				dbRec.setClientDesc(jtxtDesc.getText().toString());
				if (l_sqliteops.updateRecord(dbRec) < 0) {
					JOptionPane.showMessageDialog(null,
							"Update record failed [" + dbRec.getResult() + "]");
				} else {
					int iRow = jtabApps.getSelectedRow();
					jtabApps.setValueAt(jtxtApp.getText().toString(), iRow, 1);
					jtabApps.setValueAt(jtxtDesc.getText().toString(), iRow, 2);

					clearFields();
				}
			}
		});

		btnReplace.setEnabled(false);

		// / Button - Delete button for clients/apps
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iClientId <= 0) {
					JOptionPane
							.showMessageDialog(null,
									"Delete record failed: Please select a record then click Delete");
					return;
				}
				dbRec = new DbRecord();
				dbRec.setType("clients");
				dbRec.setClientId(iClientId);
				dbRec.setDelCreds(1);

				if (l_sqliteops.deleteRecord(dbRec) < 0) {
					JOptionPane.showMessageDialog(null,
							"Delete record failed [" + dbRec.getResult() + "]");
				} else {
					DefaultTableModel jtabModel = (DefaultTableModel) jtabApps
							.getModel();
					jtabModel.removeRow(jtabApps.getSelectedRow());

					clearFields();
				}
			}
		});
		btnDelete.setEnabled(false);

		// / Buttons - Search button for clients/apps
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
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		btnViewCredentials = new JButton("View Credentials");
		btnViewCredentials.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbRec = new DbRecord();
				dbRec.setClientId(iClientId);
				dbRec.setClientName(jtxtApp.getText().toString());
				dbRec.setClientDesc(jtxtDesc.getText().toString());
				Attributes attForm = new Attributes(l_sqliteops, l_crypto, dbRec);
				attForm.setVisible(true);
				//credForm = new Credentials(l_sqliteops, l_crypto, dbRec);
				//credForm.setVisible(true);
			}
		});

		btnViewCredentials.setEnabled(false);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
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
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addComponent(lblUrlapplication)
											.addComponent(lblDescription))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addComponent(jtxtApp, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
											.addComponent(jtxtDesc, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnViewCredentials)))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(36)
							.addComponent(lblThePasswordSaver)))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblThePasswordSaver)
					.addGap(48)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUrlapplication)
						.addComponent(jtxtApp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDescription)
						.addComponent(jtxtDesc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd)
						.addComponent(btnReplace)
						.addComponent(btnDelete)
						.addComponent(btnSearch)
						.addComponent(btnClear))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnViewCredentials))
					.addContainerGap(206, Short.MAX_VALUE))
		);
		// / JTable - Credentials table setup/definition - END

		jtabApps = new JTable();
		scrollPane.setViewportView(jtabApps);

		jtabApps.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtabApps.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0,
				0)));
		jtabApps.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "URL/Application", "Description" }) {
			Class[] columnTypes = new Class[] { Integer.class, String.class,
					String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		});
		jtabApps.getColumnModel().getColumn(1).setMinWidth(55);
		jtabApps.getColumnModel().getColumn(2).setMinWidth(55);
		contentPane.setLayout(gl_contentPane);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblThePasswordSaver, jtxtApp, jtxtDesc, btnAdd, btnReplace, btnDelete, btnSearch, btnClear, scrollPane, jtabApps, lblUrlapplication, lblDescription}));
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{menuBar, jtxtApp, jtxtDesc, btnAdd, btnReplace, btnDelete, btnSearch, btnClear, contentPane, mnFile, mntmExit, lblThePasswordSaver, scrollPane, jtabApps, lblUrlapplication, lblDescription}));

		// / Initial data load
		dbRec = new DbRecord();
		dbRec.setType(Pwdtypes.S_CLIENT_TYPE);
		dbRec.setClientName("");
		dbRec.setClientDesc("");
		loadTable(dbRec);

		ListSelectionModel rowSM = jtabApps.getSelectionModel();

		// Listener for client row change;
		rowSM.addListSelectionListener(new ListSelectionListener() {

			// / Fill the form values when a row is selected in the JTable
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsmData = (ListSelectionModel) e.getSource();
				if (!lsmData.isSelectionEmpty()) {
					int iRow = lsmData.getMinSelectionIndex();
					iClientId = Integer.parseInt(jtabApps.getValueAt(iRow, 0)
							.toString());
					jtxtApp.setText(jtabApps.getValueAt(iRow, 1).toString());
					jtxtDesc.setText(jtabApps.getValueAt(iRow, 2).toString());

					enableButtons();
				}
			}
		});

	}

	/**
	 * addToTable - Add new row to the JTable clients object.
	 */
	public void addToTable() {
		DefaultTableModel jtabModel = (DefaultTableModel) jtabApps.getModel();

		jtabModel.addRow(new Object[] { dbRec.getClientId(),
				dbRec.getClientName(), dbRec.getClientDesc() });
		clearFields();
	}



	/**
	 * clearFields - Clear text fields associated with clients.
	 */
	public void clearFields() {
		iClientId = 0;
		jtxtApp.setText("");
		jtxtDesc.setText("");
		disableButtons();
	}

	/**
	 * enableButtons - Enable buttons for client manipulation.
	 */
	public void enableButtons() {
		btnDelete.setEnabled(true);
		btnReplace.setEnabled(true);
		btnViewCredentials.setEnabled(true);		
	}

	/**
	 * disableButtons - Disable buttons associated with client manipulation.
	 */
	public void disableButtons() {
		btnDelete.setEnabled(false);
		btnReplace.setEnabled(false);
		btnViewCredentials.setEnabled(false);
	}

	/**
	 * loadTable - Loads client data.
	 * 
	 * @param _dbRec
	 *            - Parameter indicating type of data to load and any associated
	 *            filters
	 */
	public void loadTable(DbRecord _dbRec) {
		DefaultTableModel jtabModel = null;

		try {

			ArrayList<String> arrData = l_sqliteops.getRecords(_dbRec);

			if (_dbRec.getType().equals(Pwdtypes.S_CLIENT_TYPE)) {
				jtabApps.setModel(new DefaultTableModel(new Object[][] {},
						new String[] { "ID", "URL/Application", "Description" }));
				jtabModel = (DefaultTableModel) jtabApps.getModel();
			}

			String[] sRecord = new String[6];
			String sData = "";
			int iElement = 0;

			if (arrData != null) {
				for (int iCount = 0; iCount < arrData.size(); iCount++) {
					sData = arrData.get(iCount);
					StringTokenizer st = new StringTokenizer(sData, "|");
					iElement = 0;
					while (st.hasMoreTokens()) {
						sRecord[iElement] = st.nextToken();
						iElement++;
					}
					jtabModel.addRow(new Object[] { sRecord[0], sRecord[1],
							sRecord[2], sRecord[3], sRecord[4] });

				}
			}
		} catch (Exception e) {
			System.out.println("Exception->loadTable Exception [" + e.getMessage()	+ "]");
		}
	}
}
